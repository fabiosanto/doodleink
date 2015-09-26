package com.fabiosanto.doodleink;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.fabiosanto.doodleink.tasks.SaveImageToGalleryTask;
import com.fabiosanto.doodleink.tasks.SetWallpaperFromViewTask;
import com.fabiosanto.doodleink.utils.ColorPickerDialog;

public class MainActivity extends AppCompatActivity {

    String[] sheets = new String[]{"Sheet 1","Sheet 2"};

    private TabLayout tabLayout;
    private int selectedTab = 0;
    private FloatingActionButton pickColorFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));

        // Inflate a menu to be displayed in the toolbar
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setOnMenuItemClickListener(onMenuItemClick);

        tabLayout = (TabLayout) findViewById(R.id.tabs);

        pickColorFab = (FloatingActionButton)  findViewById(R.id.pickColor);
        pickColorFab.setOnClickListener(onPickColorFab);

        for(int i=0 ;i < sheets.length;i++){
            tabLayout.addTab(tabLayout.newTab().setText(sheets[i]));

            getSupportFragmentManager().beginTransaction().add(R.id.frameContainer,new DrawingSheet(),sheets[i]).commit();
        }
        tabLayout.setOnTabSelectedListener(onTabSelected);
    }

    public Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){
                case R.id.action_wallpaper :
                    new SetWallpaperFromViewTask(MainActivity.this, getSelectedSheet().getDoodleView()).execute();
                    break;
                case R.id.action_save :
                    new SaveImageToGalleryTask(MainActivity.this, getSelectedSheet().getDoodleView()).execute();
                    break;
                case R.id.action_delete :
                    deleteDoodle();
                    break;
                case R.id.action_changebckg :
                    changeBckg();
                    break;
                default:
                    break;
            }
            return false;
        }
    };


public void changeBckg(){
    AlertDialog alertDialog = new AlertDialog.Builder(this)
            .setTitle(getString(R.string.change_bckg_title))
            .setMessage(getString(R.string.change_bckg))
            .setPositiveButton(getString(android.R.string.ok),new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new ColorPickerDialog(MainActivity.this,onColorChangedBackground,getResources().getColor(android.R.color.white),getString(R.string.pick_bckg)).show();
                }
            })
            .setNegativeButton(getString(android.R.string.cancel),new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create();

    alertDialog.show();
}

public void deleteDoodle(){
    AlertDialog alertDialog = new AlertDialog.Builder(this)
            .setTitle(getString(R.string.sure_delete_title))
            .setMessage(getString(R.string.sure_delete))
            .setPositiveButton(getString(android.R.string.ok),new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new ColorPickerDialog(MainActivity.this,onColorChangedBackground,getResources().getColor(android.R.color.white),getString(R.string.pick_bckg)).show();
                }
            })
            .setNegativeButton(getString(android.R.string.cancel),new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create();

    alertDialog.show();
}


    @Override
    protected void onStart() {
        super.onStart();
        selectSheet();
    }

    View.OnClickListener onPickColorFab = new View.OnClickListener() {
        public void onClick(View v) {
            new ColorPickerDialog(MainActivity.this,onColorChangedLine, getSelectedSheet().getPaintColor(),getString(R.string.pick_line)).show();
        }
    };

    TabLayout.OnTabSelectedListener onTabSelected = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            selectedTab = tab.getPosition();
            selectSheet();
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    private void selectSheet() {
        DrawingSheet sheet = (DrawingSheet) getSupportFragmentManager().findFragmentByTag(sheets[selectedTab]);
        getSupportFragmentManager().beginTransaction().show(sheet).commit();
        pickColorFab.setBackgroundTintList(getColorStateList(sheet.getPaintColor()));

        for(int i=0 ;i < sheets.length;i++) {
            if(i!=selectedTab){
                Fragment f = getSupportFragmentManager().findFragmentByTag(sheets[i]);
                getSupportFragmentManager().beginTransaction().hide(f).commit();
            }
        }
    }

    private ColorStateList getColorStateList(int color){
        int[][] states = new int[][] {
                new int[] { android.R.attr.state_enabled}, // enabled
                new int[] {-android.R.attr.state_enabled}, // disabled
                new int[] {-android.R.attr.state_checked}, // unchecked
                new int[] { android.R.attr.state_pressed}  // pressed
        };

        int[] colors = new int[] {
                color,
                color,
                color,
                color
        };

        return new ColorStateList(states, colors);
    }


    public DrawingSheet getSelectedSheet(){
        DrawingSheet sheet = (DrawingSheet) getSupportFragmentManager().findFragmentByTag(sheets[selectedTab]);
        return sheet;
    }

    ColorPickerDialog.OnColorChangedListener onColorChangedLine = new ColorPickerDialog.OnColorChangedListener() {
        @Override
        public void changePaintColor(int color) {
            getSelectedSheet().changePaintColor(color);
            pickColorFab.setBackgroundTintList(getColorStateList(color));
        }
    };
    ColorPickerDialog.OnColorChangedListener onColorChangedBackground = new ColorPickerDialog.OnColorChangedListener() {
        @Override
        public void changePaintColor(int color) {
            getSelectedSheet().changeBckgColor(color);
        }
    };
}
