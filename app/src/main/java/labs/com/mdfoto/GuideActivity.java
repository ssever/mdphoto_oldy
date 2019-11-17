package labs.com.mdfoto;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import labs.com.mdfoto.ui.activities.SettingsActivity;

public class GuideActivity extends AppCompatActivity {

    private static final String TAG = "GuideActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_guide);

        TextView guideTv = (TextView) findViewById(R.id.guide_tv);
        if (!isNetworkConnected()){
            guideTv.setText(R.string.internet_connection_error);
            guideTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }else {
            guideTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "guideTv: ");
                    startActivity(new Intent(GuideActivity.this, SettingsActivity.class));
                }
            });
        }
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

}
