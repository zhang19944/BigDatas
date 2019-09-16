package battery;

import java.util.Date;

public class BattryCurrentAndSocBytime implements Comparable<BattryCurrentAndSocBytime>{
    private  Long  c;
    private  Long soc;
    private Date time;
    private  Long  t;

    public BattryCurrentAndSocBytime(Long c, Long soc, Long t, Date time) {
        this.c = c;
        this.soc = soc;
        this.time = time;
        this.t=t;
    }

    public Long getT() {
        return t;
    }

    public void setT(Long t) {
        this.t = t;
    }

    public Long getC() {
        return c;
    }

    public void setC(Long c) {
        this.c = c;
    }

    public Long getSoc() {
        return soc;
    }

    public void setSoc(Long soc) {
        this.soc = soc;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
    public int compareTo(BattryCurrentAndSocBytime o) {

        return this.time.compareTo(o.getTime());
    }
}
