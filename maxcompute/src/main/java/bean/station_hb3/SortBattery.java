package bean.station_hb3;

import java.util.Date;

public class SortBattery  implements Comparable<SortBattery> {
    private Long  f;
    private Long  d;
    private String  pid;
    private Date  time1;
    private String  id;
    private  Long p;

    public SortBattery(Long p,Long f, Long d, String pid, Date time1, String id) {
        this.p=p;
        this.f = f;
        this.d = d;
        this.pid = pid;
        this.time1 = time1;
        this.id = id;
    }

    public Long getP() {
        return p;
    }

    public void setP(Long p) {
        this.p = p;
    }

    public Long getF() {
        return f;
    }

    public void setF(Long f) {
        this.f = f;
    }

    public Long getD() {
        return d;
    }

    public void setD(Long d) {
        this.d = d;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public Date getTime1() {
        return time1;
    }

    public void setTime1(Date time1) {
        this.time1 = time1;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int compareTo(SortBattery o) {
        return this.time1.compareTo(o.time1);
    }

    @Override
    public String toString() {
        return "SortBattery{" +
                "f=" + f +
                ", d=" + d +
                ", pid='" + pid + '\'' +
                ", time1=" + time1 +
                ", id='" + id + '\'' +
                ", p=" + p +
                '}';
    }
}
