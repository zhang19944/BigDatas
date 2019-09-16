package mr.cabinet.bean;

import java.util.Date;

public class BIDTime implements  Comparable<BIDTime> {
    private Date date;

    public BIDTime(Date date, String bid) {
        this.date = date;
        this.bid = bid;
    }

    public Date getDate() {
        return date;

    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    private String bid;
    public int compareTo(BIDTime o) {
        return this.date.compareTo(o.date);
    }
}
