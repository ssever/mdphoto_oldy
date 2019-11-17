package labs.com.mdfoto.models;

import android.os.Environment;
import android.util.Log;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import labs.com.mdfoto.gson.MySharedPrefs;

/**
 * Created by oktay on 2/2/17.
 */

public class PatientManager {

    private static PatientManager manager;

    private static Gson gson;

    private HashMap<String, Patient> patientHashMap;

    private static final String KEY_OBJECT = "patient_v1";

    private void save() {
        MySharedPrefs.saveObject(KEY_OBJECT, this);
    }

    boolean storePhotoInLib;

    public boolean isStorePhotoInLib() {
        return storePhotoInLib;
    }

    public void setStorePhotoInLib(boolean storePhotoInLib) {

        this.storePhotoInLib = storePhotoInLib;

        save();
    }

    public static PatientManager getInstance() {
        manager = getSaved();
        if (manager == null) {
            manager = new PatientManager();
        }
        return manager;
    }

    public Patient getPatient(long id) {
        if (patientHashMap != null) {
            if (patientHashMap.containsKey("_" + id)) {
                return patientHashMap.get("_" + id);
            } else return null;
        } else return null;
    }


    public Patient updatePatient(Patient patient) {
        if (patientHashMap != null) {
            if (patientHashMap.containsKey("_" + patient.getId())) {
                Patient put = patientHashMap.put("_" + patient.getId(), patient);
                manager.save();
                return put;
            } else return null;
        } else return null;
    }

    public long addPatient(Patient p) {

        if (!manager.getPatientList().contains(p)) {

            p.setId(patientHashMap.size());
            patientHashMap.put("_" + p.getId(), p);
            manager.save();
            return p.getId();

        } else return 0;

    }

    public List<Patient> getPatientList(){
        return new ArrayList<>(patientHashMap.values());
    }

    private PatientManager() {
        patientHashMap = new HashMap<>();
    }

    private static PatientManager getSaved() {

        return MySharedPrefs.loadObject(KEY_OBJECT, PatientManager.class);

    }

    private static final String TAG = "PatientManager";

    public void removePatient(Patient patient){
        if (patientHashMap != null) {

            for (Image i:patient.getImageList()){

                Log.d(TAG, "removePatient: url "+ i.getURL());

                new File(i.getURL()).delete();
            }

            patientHashMap.remove("_"+patient.getId());

            manager.save();
        }
    }

    public void export(File file){

        MySharedPrefs.exportTo(this, ""+file.getAbsolutePath());

    }
}
