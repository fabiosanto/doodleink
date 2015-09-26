package com.fabiosanto.doodleink.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.fabiosanto.doodleink.R;
import com.fabiosanto.doodleink.utils.ImageUtils;

/**
 * Created by fabiosanto on 16/08/15.
 */
public class SaveImageToGalleryTask extends AsyncTask<Void,Void,Boolean> {

    private static final String SAVE_IMAGE_TASK = "SaveImageTask";
    Context context;
    View view;

    public SaveImageToGalleryTask(Context context, View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    protected Boolean doInBackground(Void... params) {

             String urlImage = MediaStore.Images.Media.insertImage(context.getContentResolver(), ImageUtils.getBitmapFromView(view), getTitle() , null);

               if (urlImage==null){
                   return false;
               }

        return true;
    }


    private String getTitle() {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(context.getString(R.string.app_name));
        stringBuilder.append("_");
        stringBuilder.append(String.valueOf(System.currentTimeMillis()));

        return stringBuilder.toString();
    }

    @Override
    protected void onPostExecute(Boolean result) {

        if (result){
            Toast.makeText(context,context.getString(R.string.save_image_ok),Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context,context.getString(R.string.wallpaper_fail),Toast.LENGTH_SHORT).show();
        }

        super.onPostExecute(result);
    }

}
