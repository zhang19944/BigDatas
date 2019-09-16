package ashley.beans;

import java.util.List;

/**
 * Created by AshleyZHANG on 2019/5/29.
 */
public class LogListBeanSeven {
    private String pId;
    private int type;
    private List<LogBeanSeven> list;

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<LogBeanSeven> getList() {
        return list;
    }

    public void setList(List<LogBeanSeven> list) {
        this.list = list;
    }
}
