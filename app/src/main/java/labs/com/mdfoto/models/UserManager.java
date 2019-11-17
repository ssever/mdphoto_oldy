package labs.com.mdfoto.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

/**
 * Created by oktay on 24.07.2016.
 */
public class UserManager

{
    private static final String TAG = "UserManager";
    private static SharedPreferences mPreferences;
    private static SharedPreferences.Editor mEditor;

    private static Context mContext;

    public static final String KEY_SWITCH = "key_switch";

    public UserManager(Context context) {
        mContext = context;
        mPreferences = mContext.getSharedPreferences("doc", Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
    }

    public static boolean store(String key , String value ){

        mEditor.putString(key,value);
        return  mEditor.commit();

    }

    public static String getStored(String key){

        String s = mPreferences.getString(key,"");
        return  s;

    }

    public static void storeDoctor(Doctor doctor){

        Gson gson = new Gson();
        String s = gson.toJson(doctor);
        Log.d(TAG, "storeDoctor: "  + s);
        store(Doctor.KEY_DOCTOR,s);

    }

    public static Doctor getStoredDoctor(){

        Gson gson = new Gson();
        Doctor doctor = gson.fromJson(getStored(Doctor.KEY_DOCTOR),Doctor.class);
        return  doctor;

    }

    public static void removeDoctor(){
        mEditor.remove(Doctor.KEY_DOCTOR);
        mEditor.commit();
    }

    public static void setKeySwitch(boolean keySwitch) {
        mEditor.putBoolean(KEY_SWITCH,keySwitch);
        mEditor.commit();
    }

    public static boolean isEnabled(){
        return mPreferences.getBoolean(KEY_SWITCH,false);
    }
}
