package saikiran.multiplerobotcontroller;

import android.content.Context;

import org.ros.concurrent.CancellableLoop;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.NodeMain;
import org.ros.node.topic.Publisher;
import org.json.*;

import java.util.ArrayList;
import java.util.Map;

import saikiran.multiplerobotcontroller.HelperFunctions;


public class pathPublisher extends AbstractNodeMain {
    public Publisher<std_msgs.String> publisher;
    private boolean newDataAvailable;
    private Map<String , ArrayList<RobotCanvas.WayPoint>> newPathData;
    DatabaseInterface db;

    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of("MultipleRobotController/talker");
    }

    public void initDatabase(Context c){
        db = new DatabaseInterface(c);
        db.retrievePreferences();
    }

    @Override
    public void onStart(final ConnectedNode connectedNode) {
        newDataAvailable = false;

        final Publisher<std_msgs.String> publisher = connectedNode.newPublisher("chatter", std_msgs.String._TYPE);
        // This CancellableLoop will be canceled automatically when the node shuts
        // down.
        connectedNode.executeCancellableLoop(new CancellableLoop() {
            private int sequenceNumber;

            @Override
            protected void setup() {
                sequenceNumber = 0;
            }

            @Override
            protected void loop() throws InterruptedException {
                publishPathMessage(publisher , "Hello world! " + sequenceNumber);
                sequenceNumber++;
                Thread.sleep(1000);
            }
        });
    }

    public void stagePaths(Map<String , ArrayList<RobotCanvas.WayPoint>> pointData){
        newDataAvailable = true;
        newPathData = pointData;
        processPaths();
    }

    private void processPaths(){
        int resScale = db.getResScaleSaved();

    }



    public void publishPathMessage(Publisher<std_msgs.String> pub , String s){
        if(newDataAvailable) {
            std_msgs.String str = pub.newMessage();
            String message = HelperFunctions.JSONEncode(newPathData);
            System.out.println(message);
            str.setData(message);
            pub.publish(str);
            newDataAvailable = false;
        }
    }
}