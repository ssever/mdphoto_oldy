package labs.com.mdfoto.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by muslum on 21.06.2016.
 */
public class Image {


    int Image_id;

    String name;

    int sync;

    boolean selected;

    String URL;

    long patientId;

    public static List<Image> getAllImages(){
        List<Image> imageList = new ArrayList<>();

        for (Patient p:PatientManager.getInstance().getPatientList()){
            imageList.addAll(p.getImageList());
        }
        return imageList;
    }


    public void update() {
        Patient patient = PatientManager.getInstance().getPatient(patientId);
        patient.updateImage(this);
        PatientManager.getInstance().updatePatient(patient);
    }

    public void save() {
        Patient patient = PatientManager.getInstance().getPatient(patientId);
        patient.addImage(this);
        PatientManager.getInstance().updatePatient(patient);
    }

    public void remove() {
        Patient patient = PatientManager.getInstance().getPatient(patientId);
        patient.removeImage(this);
        PatientManager.getInstance().updatePatient(patient);
    }

    @Override
    public boolean equals(Object obj) {
        Image image = (Image) obj;

        return image.getURL().equalsIgnoreCase(getURL());
    }

    public int getImage_id() {
        return Image_id;
    }

    public void setImage_id(int image_id) {
        Image_id = image_id;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public long getPatientId() {
        return patientId;
    }

    public void setPatientId(long patientId) {
        this.patientId = patientId;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSync() {
        return sync;
    }

    public void setSync(int sync) {
        this.sync = sync;
    }
}
