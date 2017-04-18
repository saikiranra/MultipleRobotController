package saikiran.multiplerobotcontroller;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

public class RobotCardViewHolder extends RecyclerView.ViewHolder {
    protected EditText rName;
    protected EditText rIP;
    protected EditText rColor;

    public RobotCardViewHolder(View v) {
        super(v);
        rName =  (EditText) v.findViewById(R.id.RobotName);
        rIP = (EditText)  v.findViewById(R.id.RobotIP);
        rColor = (EditText)  v.findViewById(R.id.RobotColor);
    }
}