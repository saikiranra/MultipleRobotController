package saikiran.multiplerobotcontroller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Path;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.Icon;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.view.ViewGroup.LayoutParams;
import java.util.ArrayList;
//import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import saikiran.multiplerobotcontroller.DatabaseInterface;
import saikiran.multiplerobotcontroller.RobotInfo;
import saikiran.multiplerobotcontroller.RobotCanvas;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;


public class MainController extends AppCompatActivity {

    RelativeLayout relativeLayout;
    Paint paint;
    RobotCanvas view;
    Path path2;
    Bitmap bitmap;
    Canvas canvas;
    Button button;
    Menu robotSelectionMenu;
    DatabaseInterface db;
    Map<String , RobotInfo> robotInfoMap;
    Map<String , RobotCanvas> robotCanvasMap;
    List<RobotInfo> robotInfoList;
    MenuItem selectedRobotMenuItem;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SubMenu subMenu = menu.addSubMenu(0 , 0 , 0 , "Robot Selection");
        subMenu.setGroupCheckable(0 , true, true);

        for(int i = 0; i < robotInfoList.size(); i++){
            RobotInfo currInfo = robotInfoList.get(i);
            MenuItem currentItem = subMenu.add(0 , i , i , currInfo.name);
            currentItem.setCheckable(true);
            int col = Color.parseColor(currInfo.color);
            Bitmap painIcon =  BitmapFactory.decodeResource(getResources() , R.drawable.ic_format_paint_white_24dp);
            Drawable iconDraw = new BitmapDrawable(getResources() , painIcon);
            iconDraw.setColorFilter(col , PorterDuff.Mode.MULTIPLY);
            currentItem.setIcon(iconDraw);
            if(i == 0){
                selectRobot(currentItem);
            }
        }



        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_controller, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.robot_settings:
                openRobotSettings();
                return true;
            default:
                if(item.getTitle() != "Robot Selection") {
                    selectRobot(item);
                }
                return true;
                //return super.onOptionsItemSelected(item);
        }
    }

    public void selectRobot(MenuItem item){
        if(selectedRobotMenuItem != null){
            if(item.getTitle() == selectedRobotMenuItem.getTitle()){
                //Clicked Self, don't do anything
                return;
            }
            else{
                //Previous item exists and is different
                selectedRobotMenuItem.setChecked(false);
            }
        }
        item.setChecked(true);
        RobotInfo currInfo = robotInfoMap.get(item.getTitle());
        //((RobotCanvas) view).setPaintOptions(currInfo.color);
        view.setActiveRobot(currInfo.name);
        selectedRobotMenuItem = item;
    }

    public void openRobotSettings(){
        Intent intent = new Intent(this , RobotSettings.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_controller);
        //Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //setSupportActionBar(myToolbar);

        if(db == null){
            initializeRobotData();
        }

        relativeLayout = (RelativeLayout) findViewById(R.id.relativelayout1);
        button = (Button)findViewById(R.id.button);

        view = new RobotCanvas(MainController.this);
        relativeLayout.addView(view, new LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));

        for(int i = 0; i < robotInfoList.size(); i++) {
            RobotInfo currInfo = robotInfoList.get(i);
            view.addRobot(currInfo.name , currInfo);
            //RobotCanvas currView = new RobotCanvas(MainController.this);
            //currView.setPaintOptions(currInfo.color);
            //robotCanvasMap.put(currInfo.name , currView);
            //relativeLayout.addView(currView, new LayoutParams(
            //        RelativeLayout.LayoutParams.MATCH_PARENT,
            //        RelativeLayout.LayoutParams.MATCH_PARENT));

        }




        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.resetPaths();
            }
        });

    }

    public void initializeRobotData(){
        db = new DatabaseInterface(this);
        db.retrievePreferences();

        robotInfoList = db.getRobotInfo();
        robotInfoMap = new HashMap<String , RobotInfo>();
        robotCanvasMap = new HashMap<String , RobotCanvas>();

        for(int i = 0; i < robotInfoList.size(); i++) {
            RobotInfo currInfo = robotInfoList.get(i);
            robotInfoMap.put(currInfo.name, currInfo);
        }
    }



}


