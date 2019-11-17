package labs.com.mdfoto.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import labs.com.mdfoto.R;
import labs.com.mdfoto.Utils;
import labs.com.mdfoto.models.Patient;
import labs.com.mdfoto.models.PatientManager;

public class AddPatientActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static ImageView mImageView;
    EditText e1, e2, e3, e4, e5, e6;
    Patient patient;

    @Bind(R.id.user_tick)
    ImageButton userTickButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);

        ButterKnife.bind(this);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Utils.toWhite(userTickButton);
        e1 = (EditText) findViewById(R.id.e1);
        e2 = (EditText) findViewById(R.id.e2);
        e3 = (EditText) findViewById(R.id.e3);
        e4 = (EditText) findViewById(R.id.e4);
        e5 = (EditText) findViewById(R.id.e5);
        e6 = (EditText) findViewById(R.id.e6);

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

    private static final String TAG = "AddPatientActivity";

    public void footerClick(View view) {


        patient = new Patient();
        patient.setName(e1.getText().toString());
        patient.setSurname(e2.getText().toString());
        patient.setCardno(e3.getText().toString());
        patient.setPhone(e4.getText().toString());
        patient.setNote(e5.getText().toString());
        patient.setWeight(e6.getText().toString());

        if (patient.getName().matches("") || patient.getSurname().matches("")) {
            Toast.makeText(AddPatientActivity.this, "Please..\nFill in the blanks", Toast.LENGTH_SHORT).show();
        } else {

            long pID = PatientManager.getInstance().addPatient(patient);

            Log.d(TAG, "footerClick: " + pID);

            Toast.makeText(AddPatientActivity.this, "Record Successfull", Toast.LENGTH_SHORT).show();
            e1.setText("");
            e2.setText("");
            e3.setText("");
            e4.setText("");
            e5.setText("");
            e6.setText("");

            Intent intent = new Intent(this, PatientDetail.class);

            intent.putExtra("PID", pID);
            startActivity(intent);

            this.finish();

        }
    }
}
