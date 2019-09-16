package battery;

import java.util.List;
import java.util.Map;

public class Actor {
    private String name;
    private List<Map> fans;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Map> getFans() {
        return fans;
    }

    public void setFans(List<Map> fans) {
        this.fans = fans;
    }

}
