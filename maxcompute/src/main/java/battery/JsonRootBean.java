package battery;


public class JsonRootBean {

   private int type;
   private int version;
   private String pID;
   private Datas datas;
   private String time;
   public void setType(int type) {
        this.type = type;
    }
    public int getType() {
        return type;
    }

   public void setVersion(int version) {
        this.version = version;
    }
    public int getVersion() {
        return version;
    }

   public void setPID(String pID) {
        this.pID = pID;
    }
    public String getPID() {
        return pID;
    }

   public void setDatas(Datas datas) {
        this.datas = datas;
    }
    public Datas getDatas() {
        return datas;
    }

   public void setTime(String time) {
        this.time = time;
    }
    public String getTime() {
        return time;
    }

}
