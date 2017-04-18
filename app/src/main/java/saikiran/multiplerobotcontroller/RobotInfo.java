package saikiran.multiplerobotcontroller;

/**
 * Created by Saikiran on 4/15/2017.
 */

public class RobotInfo {
    protected String name;
    protected String ip;
    protected String color;
    protected String id;
    protected static final String NAME_PREFIX = "Name_";
    protected static final String SURNAME_PREFIX = "Surname_";
    protected static final String EMAIL_PREFIX = "email_";

    public RobotInfo(String id, String rname , String rip , String rcolor){
        this.id = id;
        this.name = rname;
        this.color = rcolor;
        this.ip = rip;
    }
}