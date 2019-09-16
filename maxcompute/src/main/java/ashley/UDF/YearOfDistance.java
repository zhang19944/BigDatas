package ashley.UDF;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by AshleyZHANG on 2019/8/8.
 */
public class YearOfDistance {
    public String yearDistance (String s){
        SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, 0);
        c.set(Calendar.DAY_OF_YEAR, 1);//设置为1号,当前日期既为本月第一天 
        String first = formater.format(c.getTime());
        return first;
    }
}
