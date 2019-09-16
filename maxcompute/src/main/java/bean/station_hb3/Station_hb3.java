package bean.station_hb3;

import battery.Battery;

import java.util.List;

public class Station_hb3 {


    private List<Battery> battery;
    public void setBattery(List<Battery> battery) {
        this.battery = battery;
    }
    public List<Battery> getBattery() {
        return battery;
    }

}
