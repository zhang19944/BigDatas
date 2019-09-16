package mr.test;

import java.util.List;

/**
 * Created by AshleyZHANG on 2019/5/8.
 */
public class StationList {
     private  int  empty;
    public List<StationBean> getStationBeans() {
        return stationBeans;
    }

    public void setStationBeans(List<StationBean> stationBeans) {
        this.stationBeans = stationBeans;
    }

    private List<StationBean> stationBeans;
}
