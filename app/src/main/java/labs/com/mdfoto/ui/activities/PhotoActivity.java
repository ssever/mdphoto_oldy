package labs.com.mdfoto.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import labs.com.mdfoto.R;
import labs.com.mdfoto.models.Image;
import labs.com.mdfoto.ui.adapters.HackyViewPager;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

import static labs.com.mdfoto.utils.DisplayUtil.getPoint;

public class PhotoActivity extends Activity {

    public static final String KEY_PATH ="llddl";

    public static void startSelf(Context context, String path){

        Intent intent = new Intent(context,PhotoActivity.class);
        intent.putExtra(KEY_PATH,path);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);


        String path = getIntent().getExtras().getString(KEY_PATH);

        ViewPager viewPager = (HackyViewPager) findViewById(R.id.view_pager);

        viewPager.setAdapter(new SamplePagerAdapter(ImageListActivity.ImageData.getInstance().imageList));
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            View decorView= getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}
    }

    static class SamplePagerAdapter extends PagerAdapter {


        List<Image> imageList;


        public SamplePagerAdapter(List<Image> imageList) {
            this.imageList = imageList;
        }

        @Override
        public int getCount() {

            return imageList.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());



            Point point = getPoint((Activity) container.getContext());
            final PhotoViewAttacher attacher = new PhotoViewAttacher(photoView);

            String path = imageList.get(position).getURL();
            if (path != null) {
                Picasso.with(container.getContext())
                        .load(new File(path)).resize(point.y,point.x).centerInside()
                        .into(photoView, new Callback() {
                            @Override
                            public void onSuccess() {
                                attacher.update();
                            }

                            @Override
                            public void onError() {
                            }
                        });
            } else {

            }

            // Now just add PhotoView to ViewPager and return it
            container.addView(photoView, ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);

            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }
}
