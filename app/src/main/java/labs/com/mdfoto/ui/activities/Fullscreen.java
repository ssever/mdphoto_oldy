package labs.com.mdfoto.ui.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.InputStream;

import labs.com.mdfoto.R;
import uk.co.senab.photoview.PhotoViewAttacher;

public class Fullscreen extends AppCompatActivity {
    private static final int THUMBSIZE = 1080;
    ImageView img_full;
    LinearLayout full_layout;
    PhotoViewAttacher mAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        img_full=(ImageView)findViewById(R.id.img_full);


        Intent k=getIntent();
        String url = k.getStringExtra("url");

        Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(url), 1920, 1080);
        img_full.setImageBitmap(ThumbImage);
        mAttacher = new PhotoViewAttacher(img_full);
    }


}
