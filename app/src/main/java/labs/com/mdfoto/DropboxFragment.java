package labs.com.mdfoto;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dropbox.core.android.Auth;

// import labs.com.mdfoto.dropbox.Dropbox;
// import labs.com.mdfoto.dropbox.DropboxSync;


/**
 * A simple {@link Fragment} subclass.
 */
public class DropboxFragment extends Fragment {


    View mainFragmentView;

    boolean checkLogin;

    public DropboxFragment() {
        checkLogin = false;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainFragmentView = inflater.inflate(R.layout.fragment_dropbox, container, false);
        // todo burada işlem yap auth
        checkLogin = true;

        //Dropbox.startOAuth2Authentication(getContext());

        return mainFragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();

        //DropboxSync dropboxSync = new DropboxSync(getActivity());

        //dropboxSync.syncFolderToDropbox();

        if (checkLogin) {
            Context context = getContext();

            /*

            SharedPreferences prefs = context.getSharedPreferences(Dropbox.prefName, Context.MODE_PRIVATE);
            String accessToken = prefs.getString(Dropbox.mdphoto_acces_token, null);

            if (accessToken == null) {
                accessToken = Auth.getOAuth2Token();
                if (accessToken != null) {
                    prefs.edit().putString(Dropbox.mdphoto_acces_token, accessToken).apply();
                    Toast.makeText(context, "Login Successfull", Toast.LENGTH_SHORT).show();

                } else {
//                    Toast.makeText(context, "Login Failed!!", Toast.LENGTH_SHORT).show();
                }

            }
            */
            checkLogin = false;
        }

    }
}
