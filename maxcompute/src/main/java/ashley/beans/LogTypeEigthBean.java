package ashley.beans;

import java.util.List;

/**
 * Created by AshleyZHANG on 2019/7/1.
 */
public class LogTypeEigthBean {
    private String pId;
    private long type;
    private List list;

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }
}
