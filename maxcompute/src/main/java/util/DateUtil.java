package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtil {
	public static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMdd");
    public static List<String> getMonthBetween(String minDate, String maxDate) throws ParseException {
	      ArrayList<String> result = new ArrayList<String>();
	      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");//格式化为年月
	  
	      Calendar min = Calendar.getInstance();
	      Calendar max = Calendar.getInstance();
	  
	      min.setTime(sdf.parse(minDate));
	      min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);
	 
	     max.setTime(sdf.parse(maxDate));
	     max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

         Calendar curr = min;
         while (curr.before(max)) {
	        result.add(sdf.format(curr.getTime()));
	        curr.add(Calendar.MONTH, 1);
	    }
	 
	     return result;
	   } 
    
    /**
     * 获取当前月份最后一天
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static String getMaxMonthDate(Date date) {
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return dft.format(calendar.getTime());
    }
    
    public static boolean isEmpty(Object str) {
        return str == null || "".equals(str);
    }
    /**
     * @param date2  日期
     * @param amount 正数表示该日期后n天，负数表示该日期的前n天
     * @return
     * @throws ParseException
     */
    public static String findPreviousOrAfterDays(String date2, Integer amount) throws ParseException {

        Date date = SDF.parse(date2);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // add方法中的第二个参数n中，正数表示该日期后n天，负数表示该日期的前n天
        calendar.add(Calendar.DATE, amount);
        return SDF.format(calendar.getTime());

    }

    /**
     *将字符串格式yyyyMMdd的字符串转为日期，格式"yyyy-MM-dd"
     *
     * @param date 日期字符串
     * @return 返回格式化的日期
     * @throws ParseException 分析时意外地出现了错误异常
     */
    public static String strToDateFormat(String date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        formatter.setLenient(false);
        Date newDate= formatter.parse(date);
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(newDate);
    }

    public static void main(String[] args)  {
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            System.out.println(strToDateFormat("20181001"));
            System.out.println(sdf.parse(strToDateFormat("20181001")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


}
