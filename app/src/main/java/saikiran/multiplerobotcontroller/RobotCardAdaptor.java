package saikiran.multiplerobotcontroller;

import saikiran.multiplerobotcontroller.RobotCardViewHolder;
import saikiran.multiplerobotcontroller.RobotInfo;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.EditText;
import java.util.ArrayList;
import java.util.List;
import android.support.v7.widget.helper.ItemTouchHelper;
import java.util.Set;
import java.util.HashSet;

public class RobotCardAdaptor extends RecyclerView.Adapter<RobotCardViewHolder> {

    private List<RobotInfo> robotList;
    private List<RobotCardViewHolder> viewList;


    public RobotCardAdaptor(List<RobotInfo> robotInfoList) {
        this.robotList = robotInfoList;
        this.viewList = new ArrayList<RobotCardViewHolder>();
    }

    public void removeByPos(int pos){
        this.robotList.remove(pos);
        this.viewList.remove(pos);
    }

    public boolean modifyItemByName(String rname , String nip){
        for(int i = 0; i != viewList.size(); i++){
            if(viewList.get(i).rName.getText().toString() == rname){
                viewList.get(i).rIP.setText(nip);
                return true;
            }
        }
        return false;
    }

    public List<RobotInfo> getCurrentRobotData(){
        return robotList;
    }

    public boolean updateRobotData(){
        Set<String> names = new HashSet<String>();
        for(int i = 0; i != robotList.size(); i++){
            RobotInfo curr = robotList.get(i);
            RobotCardViewHolder currView = viewList.get(i);
            String n = currView.rName.getText().toString();

            if(names.contains(n)){
                return false;
            }
            names.add(n);
            curr.name = n;
            curr.id = n;
            curr.ip = currView.rIP.getText().toString();
            curr.color = currView.rColor.getText().toString();
        }
        return true;
    }

    public void addNewItem(RobotInfo newrinfo){
        this.robotList.add(newrinfo);
    }

    public void cleanViewList(){
        this.viewList = new ArrayList<RobotCardViewHolder>();
    }

    @Override
    public int getItemCount() {
        return robotList.size();
    }

    @Override
    public void onBindViewHolder(RobotCardViewHolder contactViewHolder, int i) {
        RobotInfo ci = robotList.get(i);
        contactViewHolder.rName.setText(ci.name);
        contactViewHolder.rIP.setText(ci.ip);
        contactViewHolder.rColor.setText(ci.color);
        this.viewList.add(contactViewHolder);
        System.out.println(viewList.size());
    }


    @Override
    public RobotCardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.robot_card_view, viewGroup, false);

        return new RobotCardViewHolder(itemView);
    }

}