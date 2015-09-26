package com.fabiosanto.doodleink.tasks;

import android.app.WallpaperManager;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.fabiosanto.doodleink.R;
import com.fabiosanto.doodleink.utils.ImageUtils;

/**
 * Created by fabiosanto on 16/08/15.
 */
public class SetWallpaperFromViewTask extends AsyncTask<Void,Void,Boolean> {

    private static final String SET_WALL_TASK = "SetWallTask";
    Context context;
    View view;

    public SetWallpaperFromViewTask(Context context, View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    protected Boolean doInBackground(Void... params) {

            try{
                setWallpaper(view);
            }catch(Exception e){
                Log.e(SET_WALL_TASK,e.getMessage());
                return false;
            }

        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {

        if (result){
            Toast.makeText(context,context.getString(R.string.wallpaper_ok),Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context,context.getString(R.string.wallpaper_fail),Toast.LENGTH_SHORT).show();
        }

        super.onPostExecute(result);
    }

    public void setWallpaper(View view) throws Exception{
            WallpaperManager myWallpaperManager = WallpaperManager.getInstance(context);
            // below line of code will set your current visible pager item to wallpaper
            // first we have to fetch bitmap from visible view and then we can pass it to wallpaper
            myWallpaperManager.setBitmap(ImageUtils.getBitmapFromView(view));
    }
}
