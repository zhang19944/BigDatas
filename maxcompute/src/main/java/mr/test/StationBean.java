package mr.test;

import java.io.Serializable;

/**
 * Created by AshleyZHANG on 2019/5/7.
 */
public class StationBean implements Serializable {


    private int empty;
    private double latitude;
    private double longitude;
    private int num;
    private String pID;
    private int valid;
    private int valid48;

    public StationBean() {
    }

    public StationBean(int empty, double latitude, double longitude, int num, String pID, int valid, int valid48) {
        this.empty = empty;
        this.latitude = latitude;
        this.longitude = longitude;
        this.num = num;
        this.pID = pID;
        this.valid = valid;
        this.valid48 = valid48;
    }

    public int getEmpty() {
        return empty;
    }

    public void setEmpty(int empty) {
        this.empty = empty;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getpID() {
        return pID;
    }

    public void setpID(String pID) {
        this.pID = pID;
    }

    public int getValid() {
        return valid;
    }

    public void setValid(int valid) {
        this.valid = valid;
    }

    public int getValid48() {
        return valid48;
    }

    public void setValid48(int valid48) {
        this.valid48 = valid48;
    }


/**
        return "StationBean{" +
                "empty=" + empty +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", num=" + num +
                ", pid='" + pid + '\'' +
                ", type=" + type +
                ", time=" + time +
                ", pt='" + pt + '\'' +
                ", valid=" + valid +
                ", valid48=" + valid48 +
                '}';
         */

}
