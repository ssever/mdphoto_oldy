package labs.com.mdfoto.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import labs.com.mdfoto.GuideActivity;
import labs.com.mdfoto.R;
import labs.com.mdfoto.models.Doctor;
import labs.com.mdfoto.models.ClickCall;
import labs.com.mdfoto.models.UserManager;
import labs.com.mdfoto.ui.activities.SettingsActivity;
import labs.com.mdfoto.ui.dialogs.LogoutDialog;

/**
 * Created by oktay on 08.05.2016.
 */
public class MainFragment extends Fragment {

    public static ClickCall clickCall;

    View mainFragmentView;

    Button registerButton;

    Button loginButton;

    @Bind(R.id.welcome_text) TextView welcomeTextView;

    //@Bind(R.id.image) ImageView image;

    private static final String TAG = "MainFragment";

    public static void setClickCall(ClickCall clickCall) {
        MainFragment.clickCall = clickCall;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mainFragmentView = inflater.inflate(R.layout.main_fragment, container, false);

        ButterKnife.bind(this, mainFragmentView);

        registerButton = (Button) mainFragmentView.findViewById(R.id.register_button);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (clickCall != null) {

                    clickCall.call(v);

                }
            }
        });

        Button loginButton = (Button) mainFragmentView.findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             startActivity(new Intent(getActivity(), SettingsActivity.class));

             getActivity().finish();
            }
        });


        if (UserManager.getStored(Doctor.KEY_DOCTOR).length()>0){

            Log.d(TAG, "onCreate: doctor " + UserManager.getStored(Doctor.KEY_DOCTOR));

            registerButton.setVisibility(View.GONE);

            loginButton.setVisibility(View.GONE);

            welcomeTextView.setVisibility(View.VISIBLE);

            if(UserManager.getStoredDoctor().getEmail().length()<30){

                welcomeTextView.setTextSize(14);

            }

            welcomeTextView.setText(""+ welcomeTextView.getText() +"\n"+ UserManager.getStoredDoctor().getEmail());

            welcomeTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    LogoutDialog.showDialog(getActivity(), new Runnable() {
                        @Override
                        public void run() {

                            UserManager.removeDoctor();

                            clickCall.call(null);
                        }
                    });
                }
            });

        } else {

            Log.d(TAG, "onCreate: doktor yok " + UserManager.getStored(Doctor.KEY_DOCTOR));

            registerButton.setVisibility(View.VISIBLE);

            loginButton.setVisibility(View.VISIBLE);

        }

        /*
        Picasso.with(image.getContext()).load("http://client.mdphotoapp.com/ads/768x1024_test.jpg").into(image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://mdphoto.com/"));
                startActivity(browserIntent);
            }
        });
        */
        return mainFragmentView;
    }


    @Override
    public void onDestroyView() {

        super.onDestroyView();

    }
}
