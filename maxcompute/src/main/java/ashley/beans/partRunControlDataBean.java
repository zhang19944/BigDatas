package ashley.beans;

import java.util.Date;

/**
 * Created by AshleyZHANG on 2019/5/15.
 */
public class partRunControlDataBean {
    private String bId;
    private String collectTime;
    private String pId;
    private String partRunControlData;
    private int port;
    private int type;

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }

    public String getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(String collectTime) {
        this.collectTime = collectTime;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getPartRunControlData() {
        return partRunControlData;
    }

    public void setPartRunControlData(String partRunControlData) {
        this.partRunControlData = partRunControlData;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
