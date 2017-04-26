package saikiran.multiplerobotcontroller;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Saikiran on 4/25/2017.
 */

public class HelperFunctions {
    public static String JSONEncode(RobotCanvas.WayPoint point){
        return "{\"X\": " + point.x + " , \"Y\" " + point.y + "}";
    }
    public static <A,B> String JSONEncode(Map<A, B> map) {
        String out = "{";
        Set<Map.Entry<A , B>> kvSet = map.entrySet();
        Iterator<Map.Entry<A,B>> kvIt = kvSet.iterator();
        for(int i = 0; kvIt.hasNext(); i++){
            Map.Entry<A,B> kv = kvIt.next();
            if(i != 0){
                out += " , ";
            }
            out += "\""+JSONEncode(kv.getKey())+"\": " + JSONEncode(kv.getValue());
        }
        out += "}";
        return out;
    }

    public static <A> String JSONEncode(ArrayList<A> a){
        String out = "[";
        for(int i = 0; i != a.size(); i++){
            if(i != 0){
                out += ",";
            }
            out += JSONEncode(a.get(i));
        }
        out += "]";
        return out;
    }

    public static <A> String JSONEncode(A a){
        return a.toString();
    }
}
