package labs.com.mdfoto.ui.activities;

import android.app.DatePickerDialog;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import butterknife.ButterKnife;
import labs.com.mdfoto.R;
import labs.com.mdfoto.models.AddState;
import labs.com.mdfoto.models.Doctor;
import labs.com.mdfoto.models.UserManager;
import labs.com.mdfoto.network.ApiClient;
import labs.com.mdfoto.network.DoctorApi;
import labs.com.mdfoto.ui.dialogs.RegisterDialog;
import retrofit2.Callback;

import static java.util.Calendar.YEAR;

public class AddDoctorActivity extends AppCompatActivity {
    TextView date;
    Long birthdate;
    EditText name, surname, email, pass;
    RadioGroup gender;
    Spinner speciality, country;
    Button register;

    String con,spy,gen="";

    private static final String TAG = "AddDoctorFragment";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_doctor);
        date = (TextView) findViewById(R.id.date);
        name = (EditText) findViewById(R.id.name);
        surname = (EditText) findViewById(R.id.surname);
        email = (EditText) findViewById(R.id.email);
        pass = (EditText) findViewById(R.id.pass);
        gender = (RadioGroup) findViewById(R.id.radioSex);
        speciality = (Spinner) findViewById(R.id.spcial);
        country = (Spinner) findViewById(R.id.country);
        register = (Button) findViewById(R.id.register);


        ButterKnife.bind(this);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().trim().equals("")||surname.getText().toString().trim().equals("")||
                        email.getText().toString().trim().equals("")||pass.getText().toString().trim().equals("")
                        ||date.getText().toString().trim().equals("Day/Mount/Year") ||con.equals("Select a Country")||spy.equals("Select a Speciality")
                        ||gen.equals("")){
                    RegisterDialog registerDialog = new RegisterDialog(AddDoctorActivity.this, "empty");
                    registerDialog.show();

                }else{

                DoctorApi doctorApi = ApiClient.getClient().create(DoctorApi.class);
                final Doctor doctor = new Doctor();

                doctor.setName(name.getText().toString());
                doctor.setSurname(surname.getText().toString());
                doctor.setEmail(email.getText().toString());
                doctor.setPassword(pass.getText().toString());
                doctor.setCountry(con);

                retrofit2.Call<AddState> stringCall = doctorApi.addDoctor(doctor);

                stringCall.enqueue(new Callback<AddState>() {
                    @Override
                    public void onResponse(retrofit2.Call<AddState> call, retrofit2.Response<AddState> response) {
                        Log.d(TAG, "onResponse() called with: " + "call = [" + call + "], response = [" + response + "]");

                        UserManager.storeDoctor(doctor);
                        RegisterDialog registerDialog = new RegisterDialog(AddDoctorActivity.this, "successful");
                        registerDialog.show();
                    }

                    @Override
                    public void onFailure(retrofit2.Call<AddState> call, Throwable t) {

                        t.printStackTrace();
                        Log.d(TAG, "onFailure() called with: " + "call = [" + call + "], t = [" + t + "]");
                        RegisterDialog registerDialog = new RegisterDialog(AddDoctorActivity.this, "failed");
                        registerDialog.show();
                    }
                });
            }

            }
        });

        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                con = country.getItemAtPosition(position).toString();
                Toast.makeText(AddDoctorActivity.this,con+" select",Toast.LENGTH_SHORT).show();




            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        speciality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spy = speciality.getItemAtPosition(position).toString();
                Toast.makeText(AddDoctorActivity.this,spy+" select",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int secilen=gender.getCheckedRadioButtonId();


                switch (secilen){
                    case R.id.radioMale: { Toast.makeText(AddDoctorActivity.this,"Male",Toast.LENGTH_SHORT).show(); gen ="MALE";  break; }
                    case R.id.radioFemale: {Toast.makeText(AddDoctorActivity.this,"Famale",Toast.LENGTH_SHORT).show(); gen ="FEMALE"; break;}

                }
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar mcurrentTime = Calendar.getInstance();

                int year = mcurrentTime.get(YEAR);//Güncel Yılı alıyoruz
                int month = mcurrentTime.get(Calendar.MONTH);//Güncel Ayı alıyoruz
                int day = mcurrentTime.get(Calendar.DAY_OF_MONTH);//Güncel Günü alıyoruz

                DatePickerDialog datePicker;//Datepicker objemiz
                datePicker = new DatePickerDialog(AddDoctorActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int Year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        monthOfYear++;
                        date.setText(dayOfMonth + "/" + monthOfYear + "/" + Year);

                    }
                }, year, month, day);//başlarken set edilcek değerlerimizi atıyoruz
                datePicker.setTitle("Select Birthdate");
                datePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "Select", datePicker);
                datePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Cancel", datePicker);

                datePicker.show();


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

    public void footerClick(View view) {

    }
}


