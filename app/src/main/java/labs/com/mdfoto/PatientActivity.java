package labs.com.mdfoto;

import android.content.Context;
import android.graphics.Color;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.CoordinatorLayout.LayoutParams;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Gravity;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.BaseAdapter;
import android.widget.Toast;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import labs.com.mdfoto.models.ClickCall;
import labs.com.mdfoto.models.Patient;
import labs.com.mdfoto.models.PatientList;
import labs.com.mdfoto.models.PatientManager;
import labs.com.mdfoto.ui.activities.AddPatientActivity;
import labs.com.mdfoto.ui.activities.PatientDetail;
import labs.com.mdfoto.ui.adapters.CustomListViewAdapter;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.RelativeLayout.ALIGN_PARENT_RIGHT;

public class PatientActivity extends AppCompatActivity {

    private static ClickCall clickCall;

    ListView lv;
    ArrayAdapter<String> adapter;
    private ArrayList<PatientList> patientLists;
    ArrayList<HashMap<String, String>> patient_list;
    private CustomListViewAdapter listViewAdapter;
    String patient_names[];
    String patient_surnames[];
    long patient_id[];


    @Bind(R.id.user_plus)
    ImageButton userPlusButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_patient);

        ButterKnife.bind(this);

        setTitle("PatientList");

        Utils.toWhite(userPlusButton);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        View mainb = this.findViewById(android.R.id.content);

        ImageButton btnGreen = new ImageButton(this);
        btnGreen.setImageResource(R.drawable.camera);
        btnGreen.setBackgroundColor(Color.TRANSPARENT);
        btnGreen.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        //mainb.addView(btnGreen);

        setMargins(btnGreen , 650, 0, 0, 0);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


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

    @Override
    public void onResume() {
        super.onResume();
        PatientManager manager = PatientManager.getInstance();
        if (manager.getPatientList().size() == 0) {
            Toast.makeText(this, "There is no patient record.\nYou can add by using side plus button.", Toast.LENGTH_LONG).show();
        } else {

            patientLists = new ArrayList<PatientList>();
            // patientLists = new String[manager.getPatientList().size()];


            //
            // patient_names = new String[manager.getPatientList().size()];
            // patient_surnames = new String[manager.getPatientList().size()];
            // patient_id = new long[manager.getPatientList().size()];

            int i = 0;
            for (Patient patient : manager.getPatientList()) {
                //patient_names[i] = patient.getName() +"  "+ patient.getSurname();
                //patient_surnames[i] = patient.getName();
                //patient_id[i] = patient.getId();
                PatientList patientList = new PatientList( patient.getName() +"  "+ patient.getSurname(),String.valueOf(patient.getId()));
                patientLists.add(patientList);
                i++;
            }

            // Hasta listesi ile adapter bağlanıyor
            lv = (ListView) findViewById(R.id.list_view);
            listViewAdapter = new CustomListViewAdapter(PatientActivity.this,patientLists);
            lv.setAdapter(listViewAdapter);
            //adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.hasta_adi, patient_names);
            //lv.setAdapter(adapter);




            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    PatientList patientList = (PatientList) parent.getItemAtPosition(position);
                    Toast.makeText(getApplicationContext(),
                            patientList.getId(), Toast.LENGTH_SHORT).show();


                     PatientDetail.startSelf(PatientActivity.this, Integer.parseInt(patientList.getId()));

                }
            });
            EditText searchView = (EditText) findViewById(R.id.search_view);

            searchView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    //listViewAdapter.getFilter().filter(s);
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    listViewAdapter.getFilter().filter(s);
                }

                @Override
                public void afterTextChanged(Editable s) {

                    //listViewAdapter.getFilter().filter(s);
                }
            });

        }


    }

    private void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    public void footerClick(View view) {

        startActivity(new Intent(this, AddPatientActivity.class));
    }

}


