package labs.com.mdfoto;

import android.app.Application;

// import labs.com.mdfoto.dropbox.DropboxSync;
import labs.com.mdfoto.gson.MySharedPrefs;

/**
 * Created by oktay on 2/26/17.
 */

public class MdPohotApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MySharedPrefs.getInstance(this);

    }
}
