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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import saikiran.multiplerobotcontroller.HelperFunctions;


public class pathPublisher extends AbstractNodeMain {
    public Publisher<std_msgs.String> publisher;
    private boolean newDataAvailable;
    private Map<String , ArrayList<RobotCanvas.WayPoint>> newPathData;
    DatabaseInterface db;
    String encodedPaths;

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

        final Publisher<std_msgs.String> publisher = connectedNode.newPublisher("robot_paths", std_msgs.String._TYPE);
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
                publishPathMessage(publisher);
                sequenceNumber++;
                Thread.sleep(1000);
            }
        });
    }

    public void stagePaths(Map<String , ArrayList<RobotCanvas.WayPoint>> pointData){
        newPathData = pointData;
        processPaths();
        newDataAvailable = true;
    }

    private void processPaths(){
        int resScale = db.getResScaleSaved();
        Iterator it = newPathData.entrySet().iterator();
        Map<String , ArrayList<RobotCanvas.WayPoint>> adjPath = new HashMap<String , ArrayList<RobotCanvas.WayPoint>>();
        while(it.hasNext()){
            Map.Entry<String , ArrayList<RobotCanvas.WayPoint>> pair = (Map.Entry) it.next();
            adjPath.put(pair.getKey() , new  ArrayList<RobotCanvas.WayPoint>());
            int size = pair.getValue().size();
            for(int i = 0; i != size; i++){
                adjPath.get(pair.getKey()).add(i , new RobotCanvas.WayPoint(pair.getValue().get(i).x/resScale, pair.getValue().get(i).y/resScale));
                System.out.println(adjPath.get(pair.getKey()).get(i));
            }

        }
        encodedPaths = HelperFunctions.JSONEncode(adjPath);
    }



    public void publishPathMessage(Publisher<std_msgs.String> pub){
        if(newDataAvailable) {
            std_msgs.String str = pub.newMessage();
            System.out.println(encodedPaths);
            str.setData(encodedPaths);
            pub.publish(str);
            newDataAvailable = false;
        }
    }
}