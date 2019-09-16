package ashley.beans;

/**
 * Created by AshleyZHANG on 2019/5/24.
 */
public class LogBean {

    private String content;
    private String time;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "LogListBean{" +
                "content='" + content + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
