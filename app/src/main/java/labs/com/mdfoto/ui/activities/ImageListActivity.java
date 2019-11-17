package labs.com.mdfoto.ui.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import labs.com.mdfoto.R;
import labs.com.mdfoto.gson.MySharedPrefs;
import labs.com.mdfoto.models.Image;
import labs.com.mdfoto.models.PatientManager;
import labs.com.mdfoto.oldcamera.CameraActivityWithPath;
import labs.com.mdfoto.oldcamera.CameraCaptureAvtivity;
import labs.com.mdfoto.ui.adapters.ImageAdapter;

public class ImageListActivity extends AppCompatActivity {

    private static final String KEY_INDEX = "index";
    private static final int CAMERA_REQUEST = 1;
    private static final String TAG = "ImageListActivity";
    private static final int THUMBSIZE = 600;
    private static final int REQUEST_EXTERNAL_STORAGE = 4;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    ListView listView;
    List<Image> infoList = new ArrayList<>();

    private long index;

    public static Intent newIntent(Context context, long index) {
        Intent intent = new Intent(context, ImageListActivity.class);
        intent.putExtra(KEY_INDEX, index);
        return intent;
    }

    /**
     * Checks if the app has permission to write to device storage
     * <p>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);

        index = getIntent().getExtras().getLong(KEY_INDEX);

        listView = (ListView) findViewById(R.id.list_view);

        infoList = PatientManager.getInstance().getPatient(index).getImageList();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        final ImageAdapter imageAdapter = new ImageAdapter(this, infoList, new ImageAdapter.DownloadListener() {
            @Override
            public void onDownload() {
                restart();
            }

            @Override
            public void onError() {

            }
        });
        listView.setAdapter(imageAdapter);

        Collections.reverse(infoList);
        imageAdapter.notifyDataSetChanged();
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


        switch (view.getId()) {
            case R.id.file_trash_btn:

                List<Image> selectedUrlList = new ArrayList<>();
                for (Image u : infoList) {
                    if (u.isSelected() && !selectedUrlList.contains(u)) {
                        selectedUrlList.add(u);
                    }
                }

                if (!selectedUrlList.isEmpty()) {
                    final Image image = selectedUrlList.get(0);
                    if (image.getSync() == 1) {
                        image.setSync(2);
                        image.update();
                        new File(image.getURL()).delete();
                        restart();
                    }
                }
                break;

            case R.id.path_unite:
                selectedUrlList = new ArrayList<>();
                for (Image u : infoList) {
                    if (u.isSelected() && !selectedUrlList.contains(u)) {
                        selectedUrlList.add(u);
                    }
                }

                if (!selectedUrlList.isEmpty()) {

                    startActivity(CameraActivityWithPath.newIntent(this, index, selectedUrlList.get(0).getURL()));
                }
                break;

            case R.id.path_scale:
                selectedUrlList = new ArrayList<>();
                for (Image u : infoList) {
                    if (u.isSelected() && !selectedUrlList.contains(u)) {
                        selectedUrlList.add(u);
                    }
                }

                if (!selectedUrlList.isEmpty()) {

                    ImageData.getInstance().setImageList(selectedUrlList);
                    PhotoActivity.startSelf(this, selectedUrlList.get(0).getURL());
                }
                break;

            default:
                cameraRequest();
                verifyStoragePermissions(this);
                break;
        }

    }

    private void restart() {
        startActivity(ImageListActivity.newIntent(this, index));
        finish();
    }

    private void cameraRequest() {
//        kamrayı başlatma
        startActivity(CameraCaptureAvtivity.newIntent(this, index));
        finish();
    }

    public static class ImageData {
        private static ImageData mImage;

        public void setImageList(List<Image> imageList) {
            this.imageList = imageList;
            save();
        }

        public static ImageData getmImage() {
            return mImage;
        }

        public static final String KEYOBJECT = "key_lis";
        List<Image> imageList;

        public ImageData(ArrayList<Image> imageList) {
            this.imageList = imageList;
        }


        public void save() {
            MySharedPrefs.saveObject(KEYOBJECT, ImageData.this);
        }

        public static ImageData getSaved() {
            return MySharedPrefs.loadObject(KEYOBJECT, ImageData.class);
        }

        public static ImageData getInstance() {
            mImage = getSaved();
            if (mImage == null) {
                mImage = new ImageData(new ArrayList<Image>());
                mImage.save();
            }
            return mImage;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: ççç    " + requestCode);
        switch (requestCode) {

            case REQUEST_EXTERNAL_STORAGE: {
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "coarse location permission granted");
                        cameraRequest();

                    } else {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("Functionality limited");
                        builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                        builder.setPositiveButton(android.R.string.ok, null);
                        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                            @Override
                            public void onDismiss(DialogInterface dialog) {
                            }

                        });
                        builder.show();
                    }
                }
            }
            break;

        }
    }
}
