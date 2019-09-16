package scooter;

public class Data {

   private String eLocation;
   private int soc;
   private String sPlaceName;
   private int costTime;
   private String ePlaceName;
   private long eTime;
   private long sTime;
   private String sLocation;
   private double energy;
   public double getEnergy() {
	return energy;
}
public void setEnergy(double energy) {
	this.energy = energy;
}
public void setELocation(String eLocation) {
        this.eLocation = eLocation;
    }
    public String getELocation() {
        return eLocation;
    }

   public void setSoc(int soc) {
        this.soc = soc;
    }
    public int getSoc() {
        return soc;
    }

   public void setSPlaceName(String sPlaceName) {
        this.sPlaceName = sPlaceName;
    }
    public String getSPlaceName() {
        return sPlaceName;
    }

   public void setCostTime(int costTime) {
        this.costTime = costTime;
    }
    public int getCostTime() {
        return costTime;
    }

   public void setEPlaceName(String ePlaceName) {
        this.ePlaceName = ePlaceName;
    }
    public String getEPlaceName() {
        return ePlaceName;
    }

   public void setETime(long eTime) {
        this.eTime = eTime;
    }
    public long getETime() {
        return eTime;
    }

   public void setSTime(long sTime) {
        this.sTime = sTime;
    }
    public long getSTime() {
        return sTime;
    }

   public void setSLocation(String sLocation) {
        this.sLocation = sLocation;
    }
    public String getSLocation() {
        return sLocation;
    }

}
