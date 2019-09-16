package ashley.UDF;

import ashley.mongo.MongoPromotion;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by AshleyZHANG on 2019/8/8.
 */
public class MonthOfDistance {
    public String monthDistance (String s) {
        SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天 
        String first = formater.format(c.getTime());
        return first;
    }
}
