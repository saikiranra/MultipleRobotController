package saikiran.multiplerobotcontroller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Saikiran on 4/17/2017.
 */

class RobotCanvas extends View {
    Bitmap bitmap;
    Canvas canvas;
    Path path2;
    Paint paint;
    boolean active;
    ArrayList<String> robotNameList;
    Map<String , RobotInfo> robotInfoMap;
    Map<String , Path> robotPathMap;
    Map<String , Paint> robotPaintMap;
    Map<String , ArrayList<RobotCanvas.DrawingClass>> DrawingClassArrayListMap;
    String currentRobotKey;


    public RobotCanvas(Context context) {
        super(context);
        robotNameList = new ArrayList<String>();
        robotInfoMap = new HashMap<String , RobotInfo>();
        robotPaintMap = new HashMap<String , Paint>();
        robotPathMap = new HashMap<String , Path>();
        DrawingClassArrayListMap = new HashMap<String , ArrayList<RobotCanvas.DrawingClass>>();

        this.bitmap = Bitmap.createBitmap(820, 480, Bitmap.Config.ARGB_4444);
        canvas = new Canvas(bitmap);

        this.path2 = new Path();
        this.paint = new Paint();

        this.setBackgroundColor(Color.WHITE);
        active = false;
    }

    //private ArrayList<RobotCanvas.DrawingClass> DrawingClassArrayList = new ArrayList<RobotCanvas.DrawingClass>();

    public void addRobot(String key , RobotInfo ri){
        currentRobotKey = key;
        DrawingClassArrayListMap.put(currentRobotKey , new ArrayList<RobotCanvas.DrawingClass>());;
        robotInfoMap.put(key , ri);
        robotNameList.add(key);
        robotPaintMap.put(key , new Paint());
        robotPathMap.put(key , new Path());
    }

    public void setActiveRobot(String key){
        path2 = robotPathMap.get(key);
        paint = robotPaintMap.get(key);
        setPaintOptions(robotInfoMap.get(key).color);
    }

    public void resetPaths(){
        for(int i = 0; i < robotNameList.size(); i++) {
            String key = robotNameList.get(i);
            robotPathMap.get(key).reset();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        RobotCanvas.DrawingClass pathWithPaint = new RobotCanvas.DrawingClass();

        for(int i = 0; i < robotNameList.size(); i++){
            String key = robotNameList.get(i);
            canvas.drawPath(robotPathMap.get(key), robotPaintMap.get(key));
        }


        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            path2.moveTo(event.getX(), event.getY());

            //path2.lineTo(event.getX(), event.getY());
        }
        else if (event.getAction() == MotionEvent.ACTION_MOVE) {

            path2.lineTo(event.getX(), event.getY());

            pathWithPaint.setPath(path2);

            pathWithPaint.setPaint(paint);

            //DrawingClassArrayList.add(pathWithPaint);
            DrawingClassArrayListMap.get(currentRobotKey).add(pathWithPaint);
        }

        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //if (DrawingClassArrayList.size() > 0) {
        for(int i = 0; i < robotNameList.size(); i++) {
            String key = robotNameList.get(i);
            if (DrawingClassArrayListMap.get(currentRobotKey).size() > 0) {
                canvas.drawPath(robotPathMap.get(key) , robotPaintMap.get(key));
                //canvas.drawPath(
                //        //DrawingClassArrayList.get(DrawingClassArrayList.size() - 1).getPath(),
                //        DrawingClassArrayListMap.get(currentRobotKey).get(DrawingClassArrayListMap.get(currentRobotKey).size() - 1).getPath(),
                //        //DrawingClassArrayList.get(DrawingClassArrayList.size() - 1).getPaint());
                //        DrawingClassArrayListMap.get(currentRobotKey).get(DrawingClassArrayListMap.get(currentRobotKey).size() - 1).getPaint());
            }
        }
    }

    public void setPaintOptions(String color){
        paint.setDither(true);

        paint.setColor(Color.parseColor(color));

        paint.setStyle(Paint.Style.STROKE);

        paint.setStrokeJoin(Paint.Join.ROUND);

        paint.setStrokeCap(Paint.Cap.ROUND);

        paint.setStrokeWidth(2);
    }

    public class DrawingClass {

        Path DrawingClassPath;
        Paint DrawingClassPaint;

        public Path getPath() {
            return DrawingClassPath;
        }

        public void setPath(Path path) {
            this.DrawingClassPath = path;
        }


        public Paint getPaint() {
            return DrawingClassPaint;
        }

        public void setPaint(Paint paint) {
            this.DrawingClassPaint = paint;
        }
    }
}
