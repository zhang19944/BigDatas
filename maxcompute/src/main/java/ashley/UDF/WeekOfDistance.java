package ashley.UDF;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by AshleyZHANG on 2019/8/8.
 */
public class WeekOfDistance {

    public String weekdistance(String s){
        /*
        計算本週第一天的日期
         */
        SimpleDateFormat formater=new SimpleDateFormat("yyyyMMdd");
        Calendar cal=new GregorianCalendar();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        Date first=cal.getTime();
        System.out.println(formater.format(first));
        return formater.format(first);
    }

    public static void main(String[] args) {
        WeekOfDistance dis=new WeekOfDistance();
    }
}
