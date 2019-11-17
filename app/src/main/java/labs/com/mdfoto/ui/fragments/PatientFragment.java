package labs.com.mdfoto.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import labs.com.mdfoto.R;
import labs.com.mdfoto.models.ClickCall;
import labs.com.mdfoto.ui.activities.PatientDetail;

/**
 * Created by oktay on 19.06.2016.
 */
public class PatientFragment extends Fragment{
    View mainFragmentView;


    private static ClickCall clickCall;

    ListView lv;
    ArrayAdapter<String> adapter;
    ArrayList<HashMap<String, String>> patient_list;
    String patient_names[];
    String patient_surnames[];
    int patient_id[];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainFragmentView = inflater.inflate(R.layout.fargment_patient,container,false);

        Button addButton = (Button) mainFragmentView.findViewById(R.id.fab);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (clickCall != null){
                    clickCall.call(v);
                }
            }
        });

        return mainFragmentView;
    }

    public static void setClickCall(ClickCall clickCall) {
        PatientFragment.clickCall = clickCall;
    }

    @Override
    public void onResume(){
        super.onResume();
  /*      Database db = new Database(getActivity());
        patient_list = db.patients();*/
        if(patient_list.size()==0){
            Toast.makeText(getActivity(), "There is no patient record.\nYou can add by using side plus button.", Toast.LENGTH_LONG).show();
        }else{
            patient_names = new String[patient_list.size()];
            patient_surnames = new String[patient_list.size()];
            patient_id = new int[patient_list.size()];
            for(int i = 0; i< patient_list.size(); i++){
                patient_names[i] = patient_list.get(i).get("patient_name");
                patient_surnames[i] = patient_list.get(i).get("patient_surname");
                patient_id[i] = Integer.parseInt(patient_list.get(i).get("patient_id"));
            }

            for(int j = 0; j< patient_list.size(); j++){
                patient_names[j]= patient_names[j] + " " + patient_surnames[j];
            }






           lv = (ListView) mainFragmentView.findViewById(R.id.list_view);
            adapter = new ArrayAdapter<String>(getContext(), R.layout.list_item, R.id.hasta_adi, patient_names);
            lv.setAdapter(adapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                        long arg3) {

                    Intent intent = new Intent(getActivity(), PatientDetail.class);
                    intent.putExtra("id", patient_id[arg2]);
                    startActivity(intent);

                }
            });
            EditText searchView = (EditText) mainFragmentView.findViewById(R.id.search_view);

            searchView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                   PatientFragment.this.adapter.getFilter().filter(s);
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    PatientFragment.this.adapter.getFilter().filter(s);
                }

                @Override
                public void afterTextChanged(Editable s) {
                    PatientFragment.this.adapter.getFilter().filter(s);
                }
            });

        }


    }
}
