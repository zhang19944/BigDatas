package bean.track;

import battery.Bats;

import java.util.Date;
import java.util.List;

public class TrackEchb implements Comparable<TrackEchb> {

    private  String sid;
    private   List<Bats> list;
    private  Double latitude;
    private  Double longitude;

    public Long getSum_soc() {
        return sum_soc;
    }

    public void setSum_soc(Long sum_soc) {
        this.sum_soc = sum_soc;
    }

    private  Long sum_soc;

    public Long getCurrents() {
        return currents;
    }

    public void setCurrents(Long currents) {
        this.currents = currents;
    }

    private  Long currents;

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    private  Date  time;

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public List<Bats> getList() {
        return list;
    }

    public void setList(List<Bats> list) {
        this.list = list;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int compareTo(TrackEchb o) {
        return this.time.compareTo(o.time);
    }

    @Override
    public String toString() {
        return "TrackEchb{" +
                "sid='" + sid + '\'' +
                ", list=" + list +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", time=" + time +
                '}';
    }
}
