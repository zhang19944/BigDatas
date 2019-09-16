package bean.track;

public class TrackCompare extends  Track  implements  Comparable<TrackCompare> {
    private  int costsoc;
    private  int costtime;
    private  int ccode;
    private  String sid;
    private  long uid;
    private  long gcode;

    public int compareTo(TrackCompare o) {
        return (int)(this.sTime-o.sTime)==0?(int)(o.eTime-this.eTime):(int)(this.sTime-o.sTime);
    }

    public int getCostsoc() {
        return costsoc;
    }

    public void setCostsoc(int costsoc) {
        this.costsoc = costsoc;
    }

    public int getCosttime() {
        return costtime;
    }

    public void setCosttime(int costtime) {
        this.costtime = costtime;
    }

    public int getCcode() {
        return ccode;
    }

    public void setCcode(int ccode) {
        this.ccode = ccode;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getGcode() {
        return gcode;
    }

    public void setGcode(long gcode) {
        this.gcode = gcode;
    }
}
