package scooter;



import java.util.List;

public class Scooter {

   private int status;
   private int costSoc;
   private int costTime;
   private String ccode;
   private int gcode;
   private String sID;
   private String date;
   private List<Data> data;
   private int uid;
   private double costEnergy;
   public double getCostEnergy() {
	return costEnergy;
}
public void setCostEnergy(double costEnergy) {
	this.costEnergy = costEnergy;
}
public void setStatus(int status) {
        this.status = status;
    }
    public int getStatus() {
        return status;
    }

   public void setCostSoc(int costSoc) {
        this.costSoc = costSoc;
    }
    public int getCostSoc() {
        return costSoc;
    }

   public void setCostTime(int costTime) {
        this.costTime = costTime;
    }
    public int getCostTime() {
        return costTime;
    }

   public void setCcode(String ccode) {
        this.ccode = ccode;
    }
    public String getCcode() {
        return ccode;
    }

   public void setGcode(int gcode) {
        this.gcode = gcode;
    }
    public int getGcode() {
        return gcode;
    }

   public void setSID(String sID) {
        this.sID = sID;
    }
    public String getSID() {
        return sID;
    }

   public void setDate(String date) {
        this.date = date;
    }
    public String getDate() {
        return date;
    }

   public void setData(List<Data> data) {
        this.data = data;
    }
    public List<Data> getData() {
        return data;
    }

   public void setUid(int uid) {
        this.uid = uid;
    }
    public int getUid() {
        return uid;
    }

}
