package ashley.origin_andriod_cabinetmonitoringinfo;

/**
 * Created by AshleyZHANG on 2019/8/19.
 */
import java.util.Iterator;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class TestFastJson {

    @Test
    public void testFastJson() {
        // json格式的字符串
        String jsonStr = "{\r\n" + "\"name\":\"jarWorker\",\r\n" + "\"sex\":\"男\",\r\n" + "\"age\":26,\r\n"
                + "\"love\":[{\"hobby\":\"足球\",\"color\":\"White\"},{\"hobby\":\"篮球\",\"color\":\"Brown\"},{\"hobby\":\"简书\",\"color\":\"Yellow\"}],\r\n"
                + "\"goodAt\":\"Java\"\r\n" + "}";

        Object jsonObj = JSON.toJSON(jsonStr);
        System.out.println("toJSON>>>" + jsonObj);
        System.out.println("——————————————————————————————————");
        Object jsonParse = JSON.parse(jsonStr);
        System.out.println("parse>>>" + jsonParse);
        System.out.println("——————————————————————————————————");
        String jsonString = JSON.toJSONString(jsonStr);
        System.out.println("jsonString>>>" + jsonString);
        System.out.println("——————————————————————————————————");

        JSONObject jsonObject = JSON.parseObject(jsonStr);// json对象
        String name = jsonObject.getString("name");
        String sex = jsonObject.getString("sex");
        String age = jsonObject.getString("age");
        String goodAt = jsonObject.getString("goodAt");

        System.out.println("name====" + name);
        System.out.println("sex====" + sex);
        System.out.println("age====" + age);
        System.out.println("goodAt====" + goodAt);

        String love = jsonObject.getString("love");
        JSONArray jsonArray = JSON.parseArray(love);// json数组对象
        String ArrayStr=JSONArray.toJSONString(jsonArray);
        System.out.println("ArrayStr====" + ArrayStr);
        System.out.println("——————————————————————————————————");
        int count = 0;// 测试用
        Iterator<Object> it = jsonArray.iterator();// 使用Iterator迭代器
        while (it.hasNext()) {
            // 遍历数组
            JSONObject arrayObj = (JSONObject) it.next();// JSONArray中是很多个JSONObject对象
            String hobby = arrayObj.getString("hobby");
            String color = arrayObj.getString("color");
            count++;
            System.out.println("hobby>>>" + hobby);
            System.out.println("color>>>" + color);
            System.out.println("------------------------------------");
        }
        System.out.println("数组中的JSONObject个数：" + count);
    }


}