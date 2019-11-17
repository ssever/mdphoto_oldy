package labs.com.mdfoto.ui.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import labs.com.mdfoto.R;

import labs.com.mdfoto.models.Image;
import labs.com.mdfoto.ui.activities.ImageListActivity;

/**
 * Created by okt on 10/31/16.
 */

public class ImageAdapter extends ArrayAdapter<Image> {

     static  List<Metadata> metadataList = new ArrayList<>();
    private static final int THUMBSIZE = 300;

    Activity context;

    private static final String TAG = "ImageAdapter";

    List<Image> infoList;

    public ImageAdapter(Activity context, List<Image> objects,DownloadListener listener) {
        super(context, R.layout.image_row, objects);
        this.context = context;
        this.infoList = objects;
        this.downloadListener = listener;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        View rowView = convertView;
        // reuse views
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.image_row, null);
            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = (TextView) rowView.findViewById(R.id.text);
            viewHolder.text.setSingleLine(true);
            viewHolder.image = (ImageView) rowView
                    .findViewById(R.id.image_view);
            viewHolder.state = (TextView) rowView.findViewById(R.id.state);
            rowView.setTag(viewHolder);
        }


        final Image imageInfo = infoList.get(position);
        final ViewHolder holder = (ViewHolder) rowView.getTag();



        final File file = new File(imageInfo.getURL());
       if (file.exists()){// eğer dosya var ise direk göster
           Picasso.with(context).load(file).resize(300,600).centerInside().into(holder.image);
       }else {// yok ise uzaktan göster

           imageInfo.setSync(2);
           imageInfo.update();
           getFormDropbox(holder, file,position);

       }



        final LinearLayout l = (LinearLayout) holder.image.getParent();


       rowView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               if (infoList.get(position).isSelected()) {
                   infoList.get(position).setSelected(false);
                   l.setBackgroundColor(Color.WHITE);


                   if (imageInfo.getSync() ==2){

                       /*

                       new DownloadFileTask(holder.state.getContext(), DropboxSync.getInstance().getUserDropboxClient(), new DownloadFileTask.Callback() {
                           @Override
                           public void onDownloadComplete(File result) {

                               if (downloadListener != null) {
                                   downloadListener.onDownload();
                               }

                               imageInfo.setSync(1);
                               imageInfo.update();
                           }

                           @Override
                           public void onError(Exception e) {

                               if (downloadListener != null) {
                                   downloadListener.onError();
                               }
                           }
                       }).execute(fileMetadata);

                   */
                   }
               } else {
                   infoList.get(position).setSelected(true);
                   l.setBackgroundColor(Color.GRAY);
               }

           }

       });
        holder.text.setText(imageInfo.getName() + "\n  ");


        if (imageInfo.getSync() == 1) {
            holder.state.setText("Synced");
        } else if (imageInfo.getSync() ==2)
        {
            holder.state.setTextColor(Color.RED);
            holder.state.setText("On dropbox");
        }else
        {
            holder.state.setTextColor(Color.RED);
            holder.state.setText("Not Synced");
        }


        return rowView;

    }
    FileMetadata fileMetadata;


    private void getFormDropbox(final ViewHolder holder, final File file, final int index) {
        Metadata selectedMetadata = null;
        for (Metadata metadata:metadataList){
            if (metadata.getName().equalsIgnoreCase(file.getName())){
                selectedMetadata= metadata;
                continue;
            }
        }

         fileMetadata = (FileMetadata) selectedMetadata;

        ((View) holder.state.getParent()).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        if (metadataList.isEmpty()){
            new Files(new PostListener() {
                @Override
                public void onPost() {

                   getFormDropbox(holder,file,index);
                }
            }).execute();
        } else {

            /*
            PicassoClient.getPicasso().load(FileThumbnailRequestHandler.buildPicassoUri(fileMetadata)).resize(300,600).centerInside().into(holder.image);
            */
        }

    }

    static class ViewHolder {
        public TextView text;
        public TextView state;
        public ImageView image;
    }

    private class Files extends AsyncTask<Void,Void,Void>{
        PostListener postListener;

        public Files(PostListener postListener) {
            this.postListener = postListener;
        }

        @Override
        protected Void doInBackground(Void... params) {

            /*

            try {

                ListFolderResult listFolderResult = DropboxSync.getInstance().getUserDropboxClient().files().listFolder("/MdPhoto");
                metadataList = listFolderResult.getEntries();
                for (int i = 0; i < listFolderResult.getEntries().size(); i++) {
                    Metadata metadata = listFolderResult.getEntries().get(i);
                    Log.d(TAG, "getView: "+ metadata.getName());
                }


            } catch (DbxException e) {
                e.printStackTrace();
            }
            */
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (postListener != null) {
                postListener.onPost();
            }

        }


    }
    interface PostListener{
        void onPost();
    }

    public interface DownloadListener{
        void onDownload();

        void onError();
    }

    DownloadListener downloadListener;
}
