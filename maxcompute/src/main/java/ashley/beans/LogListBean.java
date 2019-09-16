package ashley.beans;

import java.util.List;

/**
 * Created by AshleyZHANG on 2019/5/24.
 */
public class LogListBean {
    /*{"list":[{"content":"[B7,05,D3,06,00,00,00,00,00,00,00,00,00,00,00,00,00,00,
            * 00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,
             * 00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,
             * 00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,
             * 00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,
             * 00,00,00,00,00,00,00,00,00,00,00,00,00]","time":1558678789259}],
        "pId":"7849ae5b03623c24","type":6}
    **/
    private String pId;
    private String type;
    private List<LogBean> list;


    public List<LogBean> getList() {
        return list;
    }

    public void setList(List<LogBean> list) {
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
