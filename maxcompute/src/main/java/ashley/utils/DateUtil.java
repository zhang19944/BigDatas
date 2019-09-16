package ashley.utils;

/**
 * Created by AshleyZHANG on 2019/8/19.
 */
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
/**
 * 该模块可实现自动计算当前日期对应的周，月，年的第一天与最后一天日期，并且可直接指定当前日期为任意日期。
 *
 * @author Administrator
 *
 */
public class DateUtil {
    // 获取当前日期所在周的上下界
    public final static int CURRENT_WEEK = 0;
    // 获取当前日期上一周的上下界
    public final static int LAST_WEEK = 1;
    // 获取当前日期所在月的上下界
    public final static int CURRENT_MONTH = 2;
    // 获取当前日期上一月的上下界
    public final static int LAST_MONTH = 3;
    // 获取当前日期所在月的上下界
    public final static int CURRENT_YEAR = 4;
    // 获取当前日期上一月的上下界
    public final static int LAST_YEAR = 5;

    /**
     * 根据日期计算所在周的上下界
     *
     * @param time
     */
    public static Map<String, Object> convertWeekByDate(Date time) {
        Map<String, Object> map = new HashMap<String, Object>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 设置时间格式
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        System.out.println("要计算日期为:" + sdf.format(cal.getTime())); // 输出要计算日期
        cal.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        int day = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        String imptimeBegin = sdf.format(cal.getTime());
        // System.out.println("所在周星期一的日期：" + imptimeBegin);
        cal.add(Calendar.DATE, 6);
        String imptimeEnd = sdf.format(cal.getTime());
        // System.out.println("所在周星期日的日期：" + imptimeEnd);

        map.put("first", imptimeBegin);

        map.put("last", imptimeEnd);

        return map;
    }

    /**
     * 根据日期计算当月的第一天与最后一天
     *
     * @param time
     * @return
     */
    public static Map<String, Object> convertMonthByDate(Date date) {
        Map<String, Object> map = new HashMap<String, Object>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1);
        Date theDate = calendar.getTime();
        // 上个月第一天
        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        gcLast.setTime(theDate);
        gcLast.set(Calendar.DAY_OF_MONTH, 1);
        String day_first = df.format(gcLast.getTime());
        // 上个月最后一天
        calendar.add(Calendar.MONTH, 1); // 加一个月
        calendar.set(Calendar.DATE, 1); // 设置为该月第一天
        calendar.add(Calendar.DATE, -1); // 再减一天即为上个月最后一天
        String day_last = df.format(calendar.getTime());
        map.put("first", day_first);
        map.put("last", day_last);
        return map;

    }

    /**
     * 根据日期计算当年的第一天与最后一天
     *
     * @param time
     * @return
     */
    public static Map<String, Object> convertYearByDate(Date date) {
        Map<String, Object> map = new HashMap<String, Object>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        int year = date.getYear() + 1900;
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        calendar.set(Calendar.YEAR, year + 1);
        calendar.add(calendar.DATE, -1);
        Date lastYearFirst = calendar.getTime();
        map.put("first", sdf.format(currYearFirst));
        map.put("last", sdf.format(lastYearFirst));
        return map;

    }
}