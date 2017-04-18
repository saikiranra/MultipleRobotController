package saikiran.multiplerobotcontroller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.view.View;
import android.support.design.widget.FloatingActionButton;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import saikiran.multiplerobotcontroller.RobotCardAdaptor;
import java.util.List;
import java.util.ArrayList;
import saikiran.multiplerobotcontroller.RobotInfo;
import saikiran.multiplerobotcontroller.DatabaseInterface;
import android.support.v7.widget.helper.ItemTouchHelper;
import saikiran.multiplerobotcontroller.duplicateNameAlert;
import android.app.FragmentManager;


public class RobotSettings extends AppCompatActivity {
    RecyclerView recList;
    LinearLayoutManager layout;
    RobotCardAdaptor cardAdapt;
    DatabaseInterface db;
    duplicateNameAlert alertBox;

    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        /*
            Code to remove robots by swiping
         */
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {

            cardAdapt.removeByPos(viewHolder.getAdapterPosition());
            cardAdapt.notifyItemRemoved(viewHolder.getAdapterPosition());
        }

        @Override
        public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
            super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
        }
    };

    ItemTouchHelper itemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robot_settings);

        db = new DatabaseInterface(this);

        retrievePreferences();

        recList = (RecyclerView) findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        layout = new LinearLayoutManager(this);
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(layout);

        cardAdapt = new RobotCardAdaptor(db.getRobotInfo());
        recList.setAdapter(cardAdapt);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addFab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {addTeam(view);}
        });

        itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recList);

        alertBox = new duplicateNameAlert();

    }

    public void addTeam(View view){
        cardAdapt.addNewItem(new RobotInfo("NR" , "New Robot" , "127.0.0.1" , "#FFFFFF"));
        cardAdapt.cleanViewList();
        cardAdapt.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_robot_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.save_robot_settings:
                saveSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void saveSettings() {
        //Deals with the over all project settings
        String ROSCoreIP = ((EditText) findViewById(R.id.ROSCoreIPAddressInput)).getText().toString();
        float resScaleFloat = Float.parseFloat(((EditText) findViewById(R.id.ResolutionScaleInput)).getText().toString());
        int resScale = (int) resScaleFloat;
        updateResolutionScaleLabel(resScale);

        db.changeRosCoreSavedIP(ROSCoreIP);
        db.changeDefaultResScale(resScale);

        //Deals with individual robot card settings
        boolean canUpdate = cardAdapt.updateRobotData();
        if(!canUpdate){
            //Show message here!
            alertBox.show(getFragmentManager() , "DuplicateRobotName");
            return;
        }

        List<RobotInfo> newRobotData = cardAdapt.getCurrentRobotData();
        db.clearRobotData();
        for(int i = 0; i != newRobotData.size(); i++){
            db.saveRobotData(newRobotData.get(i));
        }

        db.saveRobotKeys();
    }

    public void updateRosCoreIPLabel(String newIP){
        EditText roscoreText = (EditText) findViewById(R.id.ROSCoreIPAddressInput);
        roscoreText.setText(newIP);
    }

    public void updateResolutionScaleLabel(int newScale){
        EditText resScaleText = (EditText) findViewById(R.id.ResolutionScaleInput);
        resScaleText.setText(((Integer) newScale).toString());
    }


    protected void retrievePreferences(){
        Context context = this;
        db.retrievePreferences();

        //For roscore IP address
        updateRosCoreIPLabel(db.ROSCoreSavedIP);

        //For resolutionScale address
        updateResolutionScaleLabel(db.resScaleSaved);

    }
}
