package labs.com.mdfoto.ui.activities;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import labs.com.mdfoto.MainActivity;
import labs.com.mdfoto.R;
import labs.com.mdfoto.models.Patient;
import labs.com.mdfoto.models.PatientManager;
import labs.com.mdfoto.oldcamera.CameraCaptureAvtivity;

public class PatientDetail extends AppCompatActivity {

    private static final String TAG = "PatientDetail";

    @Bind(R.id.user_tick)
    ImageButton userTickButton;


    @Bind(R.id.user_edit)
    ImageButton userEditButton;

    @Bind(R.id.user_delete)
    ImageButton userDeleteButton;

    TextView t1, t2, t3, t4, t5, t6;

    LinearLayout image_layout;

    EditText e1;

    EditText e2, e3, e4;

    Patient p;


    public static void startSelf(Context context, long value) {
        Intent intent = new Intent(context, PatientDetail.class);
        intent.putExtra("PID", value);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_detail);
        setTitle("Patient Details");

        ButterKnife.bind(this);

        userEditButton.setColorFilter(Color.WHITE);
        userDeleteButton.setColorFilter(Color.WHITE);
        userTickButton.setClickable(false);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        t1 = (TextView) findViewById(R.id.e1);
        t2 = (TextView) findViewById(R.id.e2);
        t3 = (TextView) findViewById(R.id.e3);
        t4 = (TextView) findViewById(R.id.e4);

        image_layout = (LinearLayout) findViewById(R.id.image_layout);

        final Intent intent = getIntent();

        long x = intent.getLongExtra("PID", -1);

        p = PatientManager.getInstance().getPatient(x);

        if (p != null) {

            t1.setText("" + p.getName());
            t2.setText(p.getSurname());
            t3.setText(p.getPhone());
            t4.setText(p.getNote());
        }


    }

    private void deleteUser() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PatientDetail.this);
        alertDialog.setTitle("Warning");
        alertDialog.setMessage("Do you want to delete patient record ?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                Patient patient = PatientManager.getInstance().getPatient(p.getId());


                if (patient != null) {
                    PatientManager.getInstance().removePatient(patient);
                }

                Toast.makeText(getApplicationContext(), "Deleting Successfull", Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void footerClick(View view) {
        switch (view.getId()) {
            case R.id.user_delete:
                deleteUser();
                break;
            case R.id.user_edit:

                setContentView(R.layout.activity_patient_update);
                ButterKnife.bind(this);
                userTickButton.setColorFilter(Color.WHITE);

                e1 = (EditText) findViewById(R.id.e1);
                e2 = (EditText) findViewById(R.id.e2);


                e1.setText(p.getName());
                e2.setText(p.getSurname());

                e3 = (EditText) findViewById(R.id.e3);
                e3.setText(p.getPhone());

                e4 = (EditText) findViewById(R.id.e4);
                e4.setText(p.getNote());


                break;

            case R.id.user_tick:
                p.setName(e1.getText().toString());
                p.setSurname(e2.getText().toString());

                p.setPhone(e3.getText().toString());

                p.setNote(e4.getText().toString());

                PatientManager.getInstance().updatePatient(p);
                Toast.makeText(getApplicationContext(), "Record Successfull", Toast.LENGTH_SHORT).show();
                startActivity(getIntent());
                finish();
                break;

            case R.id.user_add:
                startActivity(ImageListActivity.newIntent(PatientDetail.this, p.getId()));

                break;


            case R.id.camera2:
                cameraRequest();

                break;

            default:
                break;


        }
    }

    private void cameraRequest() {
//        kamrayı başlatma
        startActivity(CameraCaptureAvtivity.newIntent(this, p.getId()));
        finish();
    }


}
