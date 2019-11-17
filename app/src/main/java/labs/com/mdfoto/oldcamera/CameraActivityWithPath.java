package labs.com.mdfoto.oldcamera;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import labs.com.mdfoto.R;
import labs.com.mdfoto.models.Image;
import labs.com.mdfoto.ui.activities.ImageListActivity;

import static labs.com.mdfoto.utils.DisplayUtil.getPoint;

public class CameraActivityWithPath extends AppCompatActivity {

    private static final int RC_HANDLE_GMS = 9001;

    private static final String TAG = "CameraActivityWithPath";

    //İzin için geri donuş dğerei 256 sayısnın altında olmalı...
    private static final int RC_HANDLE_CAMERA_PERM = 2;


    public static final String AutoFocus = "AutoFocus";
    public static final String UseFlash = "UseFlash";
    private static final String KEY_INDEX = "key";
    // Intent kullanarak değer atıcagımızda kullancagımız key değe
    private CameraSource mCameraSource;

    private CameraSourcePreview mPreview;
    private long index;

    ImageView imageLayer;
    private String image_path;

    public static Intent newIntent(Context context, long index, String path) {
        Intent intent = new Intent(context, CameraActivityWithPath.class);
        intent.putExtra(KEY_INDEX, index);
        intent.putExtra("pathim", path);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_with_path);

        mPreview = (CameraSourcePreview) findViewById(R.id.preview);


        imageLayer = (ImageView) findViewById(R.id.image_layer);


        index = getIntent().getExtras().getLong(KEY_INDEX);
        String pathim = "pathim";
        image_path = getIntent().getExtras().getString(pathim);


        Point point = getPoint(this);

        Picasso.with(this).load(new File(image_path)).resize(point.y, point.x).centerInside().into(imageLayer);
        boolean autoFocus = true;
        boolean useFlash = false;
        //Kamera ile baglantı kurmadan önce kamera izni verilip verilmediğini kontrol ediyoruz
        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (rc == PackageManager.PERMISSION_GRANTED) {
            createCameraSource(autoFocus, useFlash);
        } else {
            requestCameraPermission();
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
                        .setRequestedPreviewSize(getPoint(this).y, getPoint(this).x)
                        .setRequestedFps(2.0f)
                        .setFlashMode(useFlash ? Camera.Parameters.FLASH_MODE_TORCH : null)
                        .setFocusMode(autoFocus ? Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE : null)
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


    boolean a = false;
    private Map<byte[], ByteBuffer> mBytesToByteBuffer = new HashMap<>();

    public void onCek(View view) {


        if (mCameraSource != null) {

            mCameraSource.takePicture(null, new CameraSource.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data) {

                    final File mFile = CameraCaptureAvtivity.getFilePath(index);


                    FileOutputStream fos;
                    try {
                        fos = new FileOutputStream(mFile.getAbsoluteFile());
                        fos.write(data);
                        fos.close();

                        Calendar calendar = Calendar.getInstance();
                        String path = mFile.getAbsolutePath();
                        Image image = new Image();
                        image.setURL(path);
                        image.setPatientId(index);
                        image.setName("" + calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + "  " +
                                "" + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND));

                        image.setSync(0);
                        image.save();
                        // unlockFocus();
                        startActivity(ImageListActivity.newIntent(CameraActivityWithPath.this, index));
                        finish();
                    } catch (IOException e) {
                        //do something about it
                    }
                }
            });
        } else {

        }
    }

    /*private class CaptureGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return onTap(e.getRawX(), e.getRawY()) || super.onSingleTapConfirmed(e);
        }
    }*/

    private class ScaleListener implements ScaleGestureDetector.OnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            return false;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            if (mCameraSource != null) {
                mCameraSource.doZoom(detector.getScaleFactor());
            }
        }
    }


}
