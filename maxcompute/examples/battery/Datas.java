package battery;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.List;

import com.aliyun.odps.io.WritableComparable;




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
