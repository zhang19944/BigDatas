package ashley.beans;

import battery.BattryCurrentAndSocBytime;

import java.util.Date;

/**
 * Created by AshleyZHANG on 2019/6/25.
 */
public class BatteryInfoBean implements Comparable<BatteryInfoBean>{
    private String pid_sn;
    private String pid;
    private String id;
    private String id_sn;
    private Date time;
    private long p;
    private long bv0;
    private long bv1;
    private long bv2;
    private long bv3;
    private long bv4;
    private long bv5;
    private long bv6;
    private long bv7;
    private long bv8;
    private long bv9;
    private long bv10;
    private long bv11;
    private long bv12;
    private long bv13;
    private long bv14;
    private long bv15;
    private long f;
    private long f_b2;
    private long c;

    public long getC() {
        return c;
    }

    public void setC(long c) {
        this.c = c;
    }

    public String getPid_sn() {
        return pid_sn;
    }

    public void setPid_sn(String pid_sn) {
        this.pid_sn = pid_sn;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_sn() {
        return id_sn;
    }

    public void setId_sn(String id_sn) {
        this.id_sn = id_sn;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public long getP() {
        return p;
    }

    public void setP(long p) {
        this.p = p;
    }

    public long getBv0() {
        return bv0;
    }

    public void setBv0(long bv0) {
        this.bv0 = bv0;
    }

    public long getBv1() {
        return bv1;
    }

    public void setBv1(long bv1) {
        this.bv1 = bv1;
    }

    public long getBv2() {
        return bv2;
    }

    public void setBv2(long bv2) {
        this.bv2 = bv2;
    }

    public long getBv3() {
        return bv3;
    }

    public void setBv3(long bv3) {
        this.bv3 = bv3;
    }

    public long getBv4() {
        return bv4;
    }

    public void setBv4(long bv4) {
        this.bv4 = bv4;
    }

    public long getBv5() {
        return bv5;
    }

    public void setBv5(long bv5) {
        this.bv5 = bv5;
    }

    public long getBv6() {
        return bv6;
    }

    public void setBv6(long bv6) {
        this.bv6 = bv6;
    }

    public long getBv7() {
        return bv7;
    }

    public void setBv7(long bv7) {
        this.bv7 = bv7;
    }

    public long getBv8() {
        return bv8;
    }

    public void setBv8(long bv8) {
        this.bv8 = bv8;
    }

    public long getBv9() {
        return bv9;
    }

    public void setBv9(long bv9) {
        this.bv9 = bv9;
    }

    public long getBv10() {
        return bv10;
    }

    public void setBv10(long bv10) {
        this.bv10 = bv10;
    }

    public long getBv11() {
        return bv11;
    }

    public void setBv11(long bv11) {
        this.bv11 = bv11;
    }

    public long getBv12() {
        return bv12;
    }

    public void setBv12(long bv12) {
        this.bv12 = bv12;
    }

    public long getBv13() {
        return bv13;
    }

    public void setBv13(long bv13) {
        this.bv13 = bv13;
    }

    public long getBv14() {
        return bv14;
    }

    public void setBv14(long bv14) {
        this.bv14 = bv14;
    }

    public long getBv15() {
        return bv15;
    }

    public void setBv15(long bv15) {
        this.bv15 = bv15;
    }

    public long getF() {
        return f;
    }

    public void setF(long f) {
        this.f = f;
    }

    public long getF_b2() {
        return f_b2;
    }

    public void setF_b2(long f_b2) {
        this.f_b2 = f_b2;
    }

    @Override
    public int compareTo(BatteryInfoBean o) {

        return this.time.compareTo(o.getTime());
    }
}
