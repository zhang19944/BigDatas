package ashley.beans;

import java.util.Date;

/**
 * Created by AshleyZHANG on 2019/5/15.
 */
public class partReadOnlyDataBean {
    private String bId;
    private String collectTime;
    private String pId;
    private String partReadOnlyData;
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

    public String getPartReadOnlyData() {
        return partReadOnlyData;
    }

    public void setPartReadOnlyData(String partReadOnlyData) {
        this.partReadOnlyData = partReadOnlyData;
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
