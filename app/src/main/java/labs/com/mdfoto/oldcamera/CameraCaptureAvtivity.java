package labs.com.mdfoto.oldcamera;

import android.Manifest;
import android.annotation.TargetApi;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.system.ErrnoException;
import android.system.OsConstants;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Policy;
import java.util.Calendar;

import labs.com.mdfoto.R;
import labs.com.mdfoto.models.Image;
import labs.com.mdfoto.models.PatientManager;
import labs.com.mdfoto.ui.activities.ImageListActivity;
import labs.com.mdfoto.utils.DisplayUtil;

public class CameraCaptureAvtivity extends AppCompatActivity {

    private static final String TAG = "CameraCaptureAvtivity";

    File file = new File(Environment.getExternalStorageDirectory() + "/mdPhotoSyncFolder/mdphoto_database.json");

    ImageButton torchButton;

    private static final int RC_HANDLE_GMS = 9001;
    private Parameters params;
    private boolean flashAcik;
    private boolean flashVarmı;


    //İzin için geri donuş dğerei 256 sayısnın altında olmalı...
    private static final int RC_HANDLE_CAMERA_PERM = 2;
    public static Camera cam = null;
    public static final String AutoFocus = "AutoFocus";
    public static final String UseFlash = "UseFlash";
    private static final String KEY_INDEX = "key";
    // Intent kullanarak değer atıcagımızda kullancagımız key değe
    private CameraSource mCameraSource;

    private CameraSourcePreview mPreview;
    private long index;

    public static Intent newIntent(Context context, long index) {
        Intent intent = new Intent(context, CameraCaptureAvtivity.class);
        intent.putExtra(KEY_INDEX, index);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_capture_avtivity);

        mPreview = (CameraSourcePreview) findViewById(R.id.preview);

        torchButton = (ImageButton) findViewById(R.id.torchButton); //ImageButton'u tanımladık

        torchButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if (flashAcik) { // Flash Acik true dönücek. Eğer Flash Açıksa Kapatıcak

                        flashKapat();
                    Log.e(TAG, "Flash açık flashı kapat");

                    int parametre = mCameraSource.getCameraFacing();

                }

                else {
                    // Açık değilse button'a basınca açıcak
                    Log.e(TAG, "Flash kapalı flashı aç.");
                        flashAc();

                }
            }
        });

        index = getIntent().getExtras().getLong(KEY_INDEX);
        boolean autoFocus = true;
        boolean useFlash = false;

        //Kamera ile baglantı kurmadan önce kamera izni verilip verilmediğini kontrol ediyoruz
        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (rc == PackageManager.PERMISSION_GRANTED) {
            createCameraSource(autoFocus, useFlash);
            //İlk işlem kameranın flash'ının olup olmadığı
            flashVarmı = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
            if (!flashVarmı) {

                Log.e(TAG, "Flash yok.");

                torchButton.setEnabled(false);

                    } else {

                //cam = Camera.open();
                //params = cam.getParameters();
                Log.e(TAG, "Flash Var.");


            }

            }
        else {
            requestCameraPermission();
        }

    }

    /*
 * Flash'ı açma kısmı burada
 */

    private void flashAc() {

        if (!flashAcik) {

            Log.e(TAG, "Flash Açık.");
            //mCameraSource.stop();
            //mPreview.stop();
            mCameraSource.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            //mCameraSource.start();
            flashAcik = true;


        }

    }


    private void flashKapat() {

        if (flashAcik) {

            mCameraSource.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            flashAcik = false;

        }
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }


    /**
     * kamera oluşturur ve  başlatır. Not Bu küçük metin örnekleri uzun mesafelerde algılamak Ocr dedektörü
     * etkinleştirmek için diğer algılama örnekler ile karşılaştırıldığında daha yüksek bir çözünürlük kullanır
     */
    @SuppressLint("InlinedApi")
    private void createCameraSource(boolean autoFocus, boolean useFlash) {
        Context context = getApplicationContext();

        // Bir metin tanıyıcı metin bulmak için oluşturulur. İlişkili birden çok işlemci örneğin metin tanıma
        //sonuçlarını almak, metni izlemek ve ekranda her metin bloğu için grafik cercevesine almak için kullanılır.

        //kamera oluşturur ve  başlatır...
        mCameraSource =
                new CameraSource.Builder(getApplicationContext(), null)
                        .setFacing(CameraSource.CAMERA_FACING_BACK)
                        .setRequestedPreviewSize(DisplayUtil.getPoint(this).y, DisplayUtil.getPoint(this).x)
                        .setRequestedFps(30)
                        //.setFlashMode(useFlash ? Camera.Parameters.FLASH_MODE_TORCH : null)
                        //.setFocusMode(autoFocus ? Camera.Parameters.FOCUS )
                        .build();
    }

    /**
     * Kamerayı yeniden baslatan metod
     */
    @Override
    protected void onResume() {
        super.onResume();
        startCameraSource();
    }


    /**
     * Kamera izin olması gerekli işlemdir.Neden izne ihtiyac duyuldugu ile alakalı bir
     * "Snackbar" mesaj uyarı pencerisiyle acıklama yapan metod
     */
    private void requestCameraPermission() {
        Log.w(TAG, "Camera permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.CAMERA};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM);
            return;
        }

        final Activity thisActivity = this;

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(thisActivity, permissions,
                        RC_HANDLE_CAMERA_PERM);
            }
        };


    }

    /**
     * Kamerayı durduran metod
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (mPreview != null) {
            mPreview.stop();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPreview != null) {
            mPreview.release();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != RC_HANDLE_CAMERA_PERM) {
            Log.d(TAG, "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Camera permission granted - initialize the camera source");

            boolean autoFocus = getIntent().getBooleanExtra(AutoFocus, false);
            boolean useFlash = getIntent().getBooleanExtra(UseFlash, false);
            createCameraSource(autoFocus, useFlash);
            return;
        }



            if (requestCode == REQUEST_WRITE_STORAGE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {

                onCek(null);
                //reload my activity with permission granted or use the features what required the permission
            }
        Log.e(TAG, "Permission not granted: results len = " + grantResults.length +
                " Result code = " + (grantResults.length > 0 ? grantResults[0] : "(empty)"));

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Multitracker sample")
                .setMessage(R.string.no_camera_permission)
                .setPositiveButton(R.string.ok, listener)
                .show();
    }

    /**
     * Kamera durdurulduğunda yeniden başlamasını saglayan metod
     */
    private void startCameraSource() throws SecurityException {
        //Cihazın google play services mevcutluguna bakılır.
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                getApplicationContext());
        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg =
                    GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS);
            dlg.show();
        }

        if (mCameraSource != null) {
            try {
                mPreview.start(mCameraSource, null);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }

    private static final int REQUEST_WRITE_STORAGE = 112;
    public void onCek(View view) {



        boolean hasPermission = (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        }else {
            if (mCameraSource != null) {

                mCameraSource.takePicture(null, new CameraSource.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data) {

                        File mFile = getFilePath(index);

                        FileOutputStream fos;

                        try {

                            fos = new FileOutputStream(mFile.getAbsoluteFile());
                            fos.write(data);
                            fos.close();

                            Calendar calendar = Calendar.getInstance();
                            Context context = getApplicationContext();
                            String path = mFile.getAbsolutePath();
                            Image image = new Image();
                            image.setURL(path);
                            image.setPatientId(index);
                            image.setName("" + calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + "  " +
                                    "" + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND));

                            image.setSync(0);

                            image.save();

                            addImageToGallery(path,context);

                            // unlockFocus();

                            PatientManager.getInstance().export(file);

                            startActivity(ImageListActivity.newIntent(CameraCaptureAvtivity.this, index));

                            finish();

                        } catch (IOException e) {

                            e.printStackTrace();

                                if (e.getCause() instanceof ErrnoException) {
                                    int errorNumber = ((ErrnoException) e.getCause()).errno;
                                    if (errorNumber == OsConstants.ENOSPC) {

                                        AlertDialog.Builder builder = new AlertDialog.Builder(CameraCaptureAvtivity.this);
                                        builder.setTitle("MDPHOTO");
                                        builder.setMessage("You run out of memory in your phone/tablet please free some space. Telefon/tabletinizde fotoğraf saklamak için yer kalmamış lütfen temizleme işlemi yapın");
                                        builder.setNegativeButton("Ok / Tamam", null);
                                        builder.show();


                                        // Out of space
                                    }
                                }

                            //do something about it
                        }
                    }
                });
            }
        }

    }

    @NonNull
    public static File getFilePath(long index) {

        String name = "/mdPhotoSyncFolder/" + index + "__" + Calendar.getInstance().getTimeInMillis();

        boolean b = new File(Environment.getExternalStorageDirectory() + "/mdPhotoSyncFolder/").mkdirs();

        File mFile = new File(Environment.getExternalStorageDirectory(), name + ".jpg");

        if (PatientManager.getInstance().isStorePhotoInLib()){

            mFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),index + "__" + Calendar.getInstance().getTimeInMillis()+".jpg");

        }

        File file = new File(Environment.getExternalStorageDirectory() + "/mdPhotoSyncFolder/mdphoto_database.json");

        // File file = new File(Environment.getExternalStorageDirectory() + "/mdPhotoSyncFolder/mdphoto"+ Calendar.getInstance().getTimeInMillis()+".json");

        // PatientManager.getInstance().export(file);

        return mFile;
    }

    public static void addImageToGallery(final String filePath, final Context context) {

        ContentValues values = new ContentValues();

        values.put(Images.Media.DATE_TAKEN, System.currentTimeMillis());


        values.put(Images.Media.MIME_TYPE, "image/jpeg");

        values.put(MediaStore.MediaColumns.DATA, filePath);

        context.getContentResolver().insert(Images.Media.EXTERNAL_CONTENT_URI, values);
    }

}
