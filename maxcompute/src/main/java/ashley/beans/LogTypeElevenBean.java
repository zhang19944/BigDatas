package ashley.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by AshleyZHANG on 2019/8/21.
 */
public class LogTypeElevenBean implements Serializable {
    private String pId;
    private String type;
    private String list;

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
