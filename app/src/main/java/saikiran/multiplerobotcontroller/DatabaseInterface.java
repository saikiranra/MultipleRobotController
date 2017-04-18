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
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import saikiran.multiplerobotcontroller.RobotInfo;


/**
 * Created by Saikiran on 4/14/2017.
 */

public class DatabaseInterface {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    Context context;
    String ROSCoreSavedIP;
    int resScaleSaved;

    List<String> robotKeys;
    Map<String , List<String>> robotData;


    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    public DatabaseInterface(Context c) {
        context = c;
        sharedPref = context.getSharedPreferences(context.getString(R.string.preference_key_file), Context.MODE_PRIVATE);
        //editor = sharedPref.edit();
    }

    public void retrievePreferences(){

        //For roscore IP address
        String defaultROSCoreIP = context.getResources().getString(R.string.default_ros_core_ip);
        ROSCoreSavedIP = sharedPref.getString(context.getString(R.string.ros_core_ip_key) , defaultROSCoreIP);


        //For resolutionScale address
        int defaultResScale = context.getResources().getInteger(R.integer.default_resolution_scale);
        resScaleSaved = sharedPref.getInt(context.getString(R.string.resolution_scale_key) , defaultResScale);

        //IDs for Robot Data Retrieval
        String defaultRobotIDList = "";
        String rawRobotIDList = sharedPref.getString(context.getString(R.string.robot_id_key) , defaultRobotIDList);
        robotKeys = new ArrayList<String>(Arrays.asList(rawRobotIDList.split(",")));
        if(rawRobotIDList == defaultRobotIDList){
            robotKeys = new ArrayList<String>();
        }


        robotData = new HashMap<String , List<String>>();

        for(int i = 0; i != robotKeys.size(); i++){
            String rkid = robotKeys.get(i);
            String defaultRobotItems = "";
            String rawRobotItems = sharedPref.getString(rkid , defaultRobotItems);
            if(rawRobotItems == defaultRobotItems){
                //Shouldn't happen!
                System.out.println("Robot ID with " + rkid + " has no data associated with it!");
            }
            robotData.put(rkid , new ArrayList<String>(Arrays.asList(rawRobotItems.split(","))));
        }
    }

    public List<RobotInfo> getRobotInfo(){
        int ipIndex = context.getResources().getInteger(R.integer.robot_data_ip_key);
        int colorIndex = context.getResources().getInteger(R.integer.robot_data_color_key);
        int nameIndex = context.getResources().getInteger(R.integer.robot_data_name_key);

        List<RobotInfo> out = new ArrayList<RobotInfo>();
        System.out.println(robotKeys.size());
        for(int i = 0; i < robotKeys.size(); i++){
            String rid = robotKeys.get(i);
            List<String> rdat = robotData.get(rid);
            System.out.println(nameIndex);
            RobotInfo newrinfo = new RobotInfo(rid , rdat.get(nameIndex) , rdat.get(ipIndex) , rdat.get(colorIndex));
            out.add(newrinfo);
        }
        return out;
    }

    public void saveItem(String key , String save){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key , save);
        editor.commit();
    }

    public void saveItem(int contextInt , String save){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(context.getString(contextInt) , save);
        editor.commit();
    }

    public void saveItem(int contextInt , int save){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(context.getString(contextInt) , save);
        editor.commit();
    }

    public void saveItem(int contextInt , boolean save){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(context.getString(contextInt) , save);
        editor.commit();
    }

    public void changeRosCoreSavedIP(String ip){
        saveItem(R.string.ros_core_ip_key , ip);
    }

    public void changeDefaultResScale(int scale){
        saveItem(R.string.resolution_scale_key , scale);
    }

    public void clearRobotData(){
        robotKeys.clear();
        robotData.clear();
    }

    public void saveRobotKeys(){
        String out = "";
        boolean notFirst = false;
        for(int i = 0; i != robotKeys.size(); i++){
            if (notFirst) {
                out += ",";
            }
            else{
                notFirst = true;
            }
            out += robotKeys.get(i);
        }

        saveItem(R.string.robot_id_key , out);
    }

    public boolean deleteTeam(String Name){
        for(int i = 0; i != robotKeys.size(); i++){
            String key = robotKeys.get(i);
            List<String> data = robotData.get(key);
            int nameIndex = context.getResources().getInteger(R.integer.robot_data_name_key);
            if(data.get(nameIndex) == Name){
                robotKeys.remove(i);
                saveRobotKeys();
                sharedPref.edit().remove(key);
                return true;
            }
        }
        return false;
    }

    public boolean saveRobotData(String id , String name, String newIP , String newColor){
        int ipIndex = context.getResources().getInteger(R.integer.robot_data_ip_key);
        int colorIndex = context.getResources().getInteger(R.integer.robot_data_color_key);
        int nameIndex = context.getResources().getInteger(R.integer.robot_data_name_key);

        if(robotKeys.contains(id)){
            //Data exists
            robotData.get(id).add(nameIndex, name);
            robotData.get(id).add(ipIndex, newIP);
            robotData.get(id).add(colorIndex, newColor);


            return true;
        }
        else{
            //New Data
            List <String> rdat = new ArrayList<String>();

            rdat.add(nameIndex , name);
            rdat.add(ipIndex , newIP);
            rdat.add(colorIndex , newColor);
            /*
            rdat.add(name);
            rdat.add(newIP);
            rdat.add(newColor);
            */


            robotData.put(id , rdat);
        }

        String out = "";
        boolean notFirst = false;
        for (int i = 0; i != robotData.get(id).size(); i++) {
            if (notFirst) {
                out += ",";
            } else {
                notFirst = true;
            }

            out += robotData.get(id).get(i);
        }

        saveItem(id, out);
        return false;
    }


    public boolean saveRobotData(RobotInfo rinfo){
        String id = rinfo.id;
        String name = rinfo.name;
        String newIP = rinfo.ip;
        String newColor = rinfo.color;

        int ipIndex = context.getResources().getInteger(R.integer.robot_data_ip_key);
        int colorIndex = context.getResources().getInteger(R.integer.robot_data_color_key);
        int nameIndex = context.getResources().getInteger(R.integer.robot_data_name_key);

        if(robotKeys.contains(id)){
            //Data exists
            robotData.get(id).add(nameIndex, name);
            robotData.get(id).add(ipIndex, newIP);
            robotData.get(id).add(colorIndex, newColor);

        }
        else{
            //New Data
            List <String> rdat = new ArrayList<String>();

            rdat.add(nameIndex , name);
            rdat.add(ipIndex , newIP);
            rdat.add(colorIndex , newColor);
            /*
            rdat.add(name);
            rdat.add(newIP);
            rdat.add(newColor);
            */

            robotData.put(id , rdat);
            robotKeys.add(id);
        }

        String out = "";
        boolean notFirst = false;
        for (int i = 0; i < robotData.get(id).size(); i++) {
            if (notFirst) {
                out += ",";
            } else {
                notFirst = true;
            }

            out += robotData.get(id).get(i);
        }

        saveItem(id, out);
        return false;
    }
}


