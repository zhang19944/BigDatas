package bean.cabinet;


import java.util.List;

public class CabinetMonitor {

    private List<HeatDatas> heatDatas;
    private String pID;
    public void setHeatDatas(List<HeatDatas> heatDatas) {
        this.heatDatas = heatDatas;
    }
    public List<HeatDatas> getHeatDatas() {
        return heatDatas;
    }

    public void setPID(String pID) {
        this.pID = pID;
    }
    public String getPID() {
        return pID;
    }

}