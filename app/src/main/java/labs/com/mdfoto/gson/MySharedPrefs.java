package labs.com.mdfoto.gson;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import labs.com.mdfoto.Utils;
import labs.com.mdfoto.models.PatientManager;


public class MySharedPrefs {

    private static SharedPreferences sharedPreferences;

    private static Gson gson;

    private static MySharedPrefs mySharedPrefs;

    public static MySharedPrefs getInstance(Context context) {

        if (mySharedPrefs == null) {

            mySharedPrefs = new MySharedPrefs(context);

        }

        return mySharedPrefs;
    }

    private MySharedPrefs(Context context) {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());

        gson = new GsonBuilder().create();

    }

    private static void saveString(String key, String string) {

        sharedPreferences.edit().putString(key, string).apply();

    }

    public static void saveObject(String key, Object object) {

        String json = gson.toJson(object);

        MySharedPrefs.saveString(key, json);

    }

    public static <T> T loadObject(String key, Class<T> type) {

        String object = sharedPreferences.getString(key, null);

        if (object != null) {

            return gson.fromJson(object, type);

        } else {

            return null;

        }

    }

    public static <T> T loadFromJson(String key, Class<T> type) {

        // Sertaç 11_19 : If Mdphoto is a fresh update or download it searches for json file if found Mdphoto import data from json file

        String jsonStr = null;

            try {

            File yourFile = new File(Environment.getExternalStorageDirectory(), "/mdPhotoSyncFolder/mdphoto_database.json");

            FileInputStream stream = new FileInputStream(yourFile);

            try {

                FileChannel fc = stream.getChannel();

                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

                jsonStr = Charset.defaultCharset().decode(bb).toString();

            } catch (Exception e) {

                e.printStackTrace();

            } finally {

                stream.close();

            }

            // JSONObject jsonObj = new JSONObject(jsonStr);

            } catch (Exception e) {

                e.printStackTrace();
            }

        MySharedPrefs.saveString(key, jsonStr);

        return gson.fromJson(jsonStr, type);

    }

    public static void exportTo(Object o, String path){

        String json = gson.toJson(o);

        Utils.mCreateAndSaveFile(json,path);

    }
}