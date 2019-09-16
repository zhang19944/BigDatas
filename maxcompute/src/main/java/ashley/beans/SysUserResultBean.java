package ashley.beans;

/**
 * Created by AshleyZHANG on 2019/7/1.
 */
public class SysUserResultBean implements Comparable<SysUserResultBean> {

    private String user_id;
    private String sn;
    private String pid;
    private String batteryid;
    private String content;
    private String timelonig;
    private String nextcontext;
    private String timeloginout;
    private String p;
    private String d;
    private String f;
    private String nextd;
    private String nextf;
    private String time;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getBatteryid() {
        return batteryid;
    }

    public void setBatteryid(String batteryid) {
        this.batteryid = batteryid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimelonig() {
        return timelonig;
    }

    public void setTimelonig(String timelonig) {
        this.timelonig = timelonig;
    }

    public String getNextcontext() {
        return nextcontext;
    }

    public void setNextcontext(String nextcontext) {
        this.nextcontext = nextcontext;
    }

    public String getTimeloginout() {
        return timeloginout;
    }

    public void setTimeloginout(String timeloginout) {
        this.timeloginout = timeloginout;
    }

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getF() {
        return f;
    }

    public void setF(String f) {
        this.f = f;
    }

    public String getNextd() {
        return nextd;
    }

    public void setNextd(String nextd) {
        this.nextd = nextd;
    }

    public String getNextf() {
        return nextf;
    }

    public void setNextf(String nextf) {
        this.nextf = nextf;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    @Override
    public int compareTo(SysUserResultBean o) {
        return this.time.compareTo(o.getTime());
    }
}
