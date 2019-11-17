package labs.com.mdfoto.ui.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import labs.com.mdfoto.MainActivity;
import labs.com.mdfoto.R;
import labs.com.mdfoto.models.ClickCall;
import labs.com.mdfoto.models.Doctor;
import labs.com.mdfoto.models.LoginState;
import labs.com.mdfoto.models.PatientManager;
import labs.com.mdfoto.models.UserManager;
import labs.com.mdfoto.network.ApiClient;
import labs.com.mdfoto.network.DoctorApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.*;

public class SettingsActivity extends BaseBackActivity {

    private static final String TAG = "SettingsActivity";
    private static ClickCall clickCall;
    @Bind(R.id.email)
    EditText mailEditText;
    @Bind(R.id.editPass)
    EditText passEditText;
    @Bind(R.id.switch1)
    Switch autoLoginASwitch;

    @Bind(R.id.free_app_size)
    Button freeButton;

    @Bind(R.id.btn_register)
    Button registerButton;

    @Bind(R.id.btn_login)
    Button loginButton;

    @Bind(R.id.btn_forget)
    Button forgotButton;

    @Bind(R.id.switch_store) Switch switchStore;

    public static void setClickCall(ClickCall clickCall) {
        SettingsActivity.clickCall = clickCall;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ButterKnife.bind(this);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        float d =Environment.getExternalStorageDirectory().getUsableSpace()/(1024*1024*1024f);
        freeButton.setText("" +String.format("%.2f",d) + " GB"  );

        if (UserManager.getStoredDoctor() != null){
            mailEditText.setText("" + UserManager.getStoredDoctor().getEmail());
            registerButton.setVisibility(GONE);
            loginButton.setVisibility(GONE);
            forgotButton.setVisibility(GONE);
        }

        autoLoginASwitch.setChecked(UserManager.isEnabled());

        autoLoginASwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                UserManager.setKeySwitch(isChecked);


            }
        });

        switchStore.setChecked(PatientManager.getInstance().isStorePhotoInLib());
        Toast.makeText(this, ""+PatientManager.getInstance().isStorePhotoInLib(), Toast.LENGTH_SHORT).show();
        switchStore.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(SettingsActivity.this, "içer de"+isChecked, Toast.LENGTH_SHORT).show();
                PatientManager.getInstance().setStorePhotoInLib(isChecked);
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void settingsClick(View view) {

        switch (view.getId()) {
            case R.id.btn_login:
                loginProcessWithRetrofit(mailEditText.getText().toString(), "" + passEditText.getText().toString());
                break;
            case R.id.btn_register:

                startActivity(new Intent(this, AddDoctorActivity.class));
                break;

            case R.id.btn_logout:
                UserManager.removeDoctor();
                finish();
                break;
        }
    }

    private void loginProcessWithRetrofit(final String email, String password) {
        final ProgressDialog progressdialog = new ProgressDialog(SettingsActivity.this);
        progressdialog.setMessage("Please Wait.... \n Log in MDPhoto");
        progressdialog.show();
        progressdialog.setCancelable(false);
        //DoctorApi mApiService = this.getInterfaceService();
        DoctorApi mApiService = ApiClient.getClient().create(DoctorApi.class);
        Call<LoginState> mService = mApiService.login(email, password);
        mService.enqueue(new Callback<LoginState>() {
            @Override
            public void onResponse(Call<LoginState> call, Response<LoginState> response) {


                if (response.body().isLogin()) {
                    Toast.makeText(SettingsActivity.this, " Giriş Başarılı ", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse() called with: " + "call = [" + call + "], response = [" + response + "]");


                    UserManager.storeDoctor(new Doctor(email));
                    progressdialog.dismiss();
                    startActivity(new Intent(SettingsActivity.this, MainActivity.class));

                    SettingsActivity.this.finish();

                } else {
                    progressdialog.dismiss();
                    passEditText.setError(getString(R.string.error_incorrect_password));
                    passEditText.requestFocus();
                }

            }

            @Override
            public void onFailure(Call<LoginState> call, Throwable t) {

                Log.d(TAG, "onFailure() called with: " + "call = [" + call + "], t = [" + t + "]");
                Toast.makeText(SettingsActivity.this, "Giriş hatalı ", Toast.LENGTH_SHORT).show();
                progressdialog.dismiss();
                UserManager.store(Doctor.KEY_DOCTOR, "");

            }
        });

    }
}
