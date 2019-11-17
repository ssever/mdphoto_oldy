package labs.com.mdfoto;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
// import labs.com.mdfoto.dropbox.Dropbox;
// import labs.com.mdfoto.dropbox.DropboxClientFactory;
// import labs.com.mdfoto.dropbox.DropboxSync;
// import labs.com.mdfoto.dropbox.PicassoClient;
import labs.com.mdfoto.gson.MySharedPrefs;
import labs.com.mdfoto.models.ClickCall;
import labs.com.mdfoto.models.PatientManager;
import labs.com.mdfoto.models.UserManager;
import labs.com.mdfoto.ui.activities.AddDoctorActivity;
import labs.com.mdfoto.ui.activities.SettingsActivity;
import labs.com.mdfoto.ui.fragments.LoginFragment;
import labs.com.mdfoto.ui.fragments.MainFragment;
import labs.com.mdfoto.ui.fragments.PatientFragment;

public class MainActivity extends FragmentActivity implements ClickCall {


    private static final String TAG = "MainActivity";

    private static final String KEY_OBJECT = "patient_v1";

    Fragment fragment;

    @Bind(R.id.button_2)

    ImageButton button2;

    int counter;

    private FragmentManager fragmentManager;

    private static PatientManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // DropboxSync dropboxSync = new DropboxSync(this);

        // PicassoClient.init(this, dropboxSync.getUserDropboxClient());

        MySharedPrefs.getInstance(this);

        ButterKnife.bind(this);

        counter = 0;

        fragmentManager = getSupportFragmentManager();

        // Check if has patient json file

        manager = MySharedPrefs.loadObject(KEY_OBJECT, PatientManager.class);

        if (manager == null) {

            manager = MySharedPrefs.loadFromJson(KEY_OBJECT,PatientManager.class);

            //String patients = getJsonData(this);

            Log.d(TAG, "Empty Manager");

            //File file = new File(Environment.getExternalStorageDirectory() + "/mdPhotoSyncFolder/mdphoto_last.json");

            //manager = new PatientManager();
        }

        new UserManager(getApplicationContext());

        Utils.toWhite(button2);

        MainFragment.setClickCall(this);

        LoginFragment.setClickCall(this);

        PatientFragment.setClickCall(this);

    }


    @Override
    protected void onResume() {

        super.onResume();

//        Database.exportDB(this);
        // TODO: 2/16/17 burda export et
        footerClick(null);


    }

    @Override
    protected void onDestroy() {
        if (!UserManager.isEnabled()) {
            UserManager.removeDoctor();
        }
        super.onDestroy();

    }

    public void footerClick(View view) {

        /*

        if (UserManager.getStoredDoctor() != null) {

            Log.d(TAG, "onResume: Doctor  " + UserManager.getStoredDoctor().getAndroidId());

        }
        else {
            Log.d(TAG, "onResume: no doctor");

            startActivity(new Intent(this, GuideActivity.class));

            finish();
            return;
        }

         */
        counter = 1;
        if (view != null) {

            switch (view.getId()) {

                case R.id.button_0:
                    startActivity(new Intent(this, PatientActivity.class));
                    fragment = null;
                    break;


                case R.id.button_1:
                   /* fragment = new DropboxFragment();*/

                    /*

                    DropboxSync dropboxSync = new DropboxSync(this);
                    dropboxSync.syncFolderToDropbox();
                    Context context = this;

                    SharedPreferences prefs = context.getSharedPreferences(Dropbox.prefName, Context.MODE_PRIVATE);
                    String accessToken = prefs.getString(Dropbox.mdphoto_acces_token, null);

                    if (accessToken == null) {
                        accessToken = Auth.getOAuth2Token();
                        if (accessToken != null) {
                            prefs.edit().putString(Dropbox.mdphoto_acces_token, accessToken).apply();
                            Toast.makeText(context, "Login Successfull", Toast.LENGTH_SHORT).show();

                        } else {
                                Toast.makeText(context, "Login Failed!!", Toast.LENGTH_SHORT).show();
                            fragment = new DropboxFragment();
                        }

                    }else {
                                         }

                    */

                    break;


                case R.id.button_2:
                    SettingsActivity.setClickCall(this);
                    startActivity(new Intent(this, SettingsActivity.class));
                    fragment = null;
                    break;

                case R.id.register_button:
                    SettingsActivity.setClickCall(this);
                    startActivity(new Intent(this, AddDoctorActivity.class));
                    fragment = null;
                    break;

                case R.id.login_button:

                    fragment = new LoginFragment();
                    break;


                case R.id.fab:

//                    fragment = new AddPatientFragment();
                    break;

                default:
                    fragment = new MainFragment();
            }
        } else {
            fragment = new MainFragment();
            counter = 15;
        }


        if (fragment != null) {

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.replace(R.id.container1, fragment);

            fragmentTransaction.addToBackStack(null);

            fragmentTransaction.commit();
        }
    }

    public Boolean readFromFile() {

        HashMap<String, String> parsedData = new HashMap<String, String>();

        // String file = Environment.getExternalStorageDirectory() + "/mdPhotoSyncFolder/mdphoto_last.json";

        try {

            File yourFile = new File(Environment.getExternalStorageDirectory(), "/mdPhotoSyncFolder/mdphoto_last.json");

            FileInputStream stream = new FileInputStream(yourFile);

            String jsonStr = null;

            try {
                FileChannel fc = stream.getChannel();

                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

                jsonStr = Charset.defaultCharset().decode(bb).toString();
            }

            catch(Exception e){

                e.printStackTrace();
            }

            finally {

                stream.close();

            }
            /*  String jsonStr = "{\n\"data\": [\n    {\n        \"id\": \"1\",\n        \"title\": \"Farhan Shah\",\n        \"duration\": 10\n    },\n    {\n        \"id\": \"2\",\n        \"title\": \"Noman Shah\",\n        \"duration\": 10\n    },\n    {\n        \"id\": \"3\",\n        \"title\": \"Ahmad Shah\",\n        \"duration\": 10\n    },\n    {\n        \"id\": \"4\",\n        \"title\": \"Mohsin Shah\",\n        \"duration\": 10\n    },\n    {\n        \"id\": \"5\",\n        \"title\": \"Haris Shah\",\n        \"duration\": 10\n    }\n  ]\n\n}\n";
             */
            JSONObject jsonObj = new JSONObject(jsonStr);

            // Getting data JSON Array nodes
            JSONArray data  = jsonObj.getJSONArray("data");

            // looping through All nodes
            for (int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);

                String id = c.getString("id");
                String title = c.getString("title");
                String duration = c.getString("duration");
                //use >  int id = c.getInt("duration"); if you want get an int


                // tmp hashmap for single node
                // HashMap<String, String> parsedData = new HashMap<String, String>();

                // adding each child node to HashMap key => value
                parsedData.put("id", id);
                parsedData.put("title", title);
                parsedData.put("duration", duration);


                // do what do you want on your interface
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        /*

        String ret = "";

            try {

                //InputStream inputStream = context.openFileInput(file);

                FileInputStream fis = new FileInputStream (new File(file));  // 2nd lin

                Log.e("readFromFile", "File path: " + file);

                if (fis != null) {

                    InputStreamReader inputStreamReader = new InputStreamReader(fis);

                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    String receiveString = "";

                    StringBuilder stringBuilder = new StringBuilder();

                    while ((receiveString = bufferedReader.readLine()) != null) {

                        stringBuilder.append(receiveString);

                    }

                    inputStream.close();

                    ret = stringBuilder.toString();

                }
            } catch (FileNotFoundException e) {

                Log.e("readFromFile", "File not found: " + e.toString());

            } catch (IOException e) {

                Log.e("readFromFile", "Can not read file: " + e.toString());
            }
            return ret;

         */



        return true;

        }



    @Override
    public void call(View view) {
        footerClick(view);
    }

    @Override
    public void onTick(String s) {

    }

    @Override
    public void onBackPressed() {


        if (counter == 15) {
            finish();
        } else {
            footerClick(null);
        }
        Log.d(TAG, "onBackPressed() called with: " + "");

    }
}
