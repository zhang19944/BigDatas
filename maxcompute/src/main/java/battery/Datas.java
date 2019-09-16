package battery;

import java.util.List;


public class Datas {

   private List<Battery> battery;
   private String pID;
   public void setBattery(List<Battery> battery) {
        this.battery = battery;
    }
    public List<Battery> getBattery() {
        return battery;
    }

   public void setPID(String pID) {
        this.pID = pID;
    }
    public String getPID() {
        return pID;
    }


}
