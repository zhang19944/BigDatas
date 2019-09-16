package util;


import bean.track.TrackEchb;
import com.google.gson.InstanceCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Utils {
    private static Logger logger = LoggerFactory.getLogger(Utils.class);

    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }


















    public static String inputStream2String(InputStream in) throws IOException {
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) != -1; ) {
            out.append(new String(b, 0, n));
        }
        return out.toString();
    }

    public static Date String2Date(String date) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sf.parse(date);
        } catch (ParseException e) {
            logger.error("##String2Date", e);
        }
        return null;
    }

    public static Date DateCompute(Date date, int value) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(5, value);
        return gc.getTime();
    }

    public static String DateCompute(String date, int value) {
        GregorianCalendar gc = new GregorianCalendar();

        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            gc.setTime(sf.parse(date));
        } catch (ParseException e) {
            logger.error("##DateCompute", e);
        }
        gc.add(5, -value);
        return sf.format(gc.getTime());
    }

    /**
     * 创建HashMap实例
     *
     * @return HashMap实例
     */
    public static final <K, V> Map<K, V> newMap() {
        return new HashMap<K, V>();
    }

    /**
     * 判断对象是否为空
     *
     * @param obj 待判断对象
     * @return boolean true-空(null/空字符串/纯空白字符),false-非空
     */
    public static final boolean isEmpty(Object obj) {
        return obj == null || obj.toString().trim().length() == 0;
    }

    /**
     * 判断Map是否为空(null||empty)
     *
     * @param map
     * @return boolean
     */
    public static final boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * 判断List是否为空(null||empty)
     *
     * @param c
     * @return boolean
     */
    public static final boolean isEmpty(Collection<?> c) {
        return c == null || c.isEmpty();
    }

    /**
     * 创建ArrayList实例
     *
     * @return ArrayList实例
     */
    public static final <E> List<E> newList() {
        return new ArrayList<E>();
    }

    /**
     * 创建ArrayList实例
     *
     * @param initialCapacity 初始化容量
     * @return ArrayList实例
     */
    public static final <E> List<E> newList(int initialCapacity) {
        return new ArrayList<E>(initialCapacity);
    }

    /**
     * 转换为字符串
     *
     * @param obj 待转换对象
     * @return String null-空字符串;
     */
    public static final String toString(Object obj) {
        return obj == null ? null : obj.toString();
    }

    /**
     * 转为String集合
     *
     * @param obj      字符串
     * @param separtor 分隔符
     * @return String集合
     */
    public static final List<String> toList(Object obj, String separtor) {
        List<String> list = null;
        if (!isEmpty(obj)) {
            list = newList();
            String[] temp = String.valueOf(obj).trim().split(separtor);
            for (String str : temp) {
                if (!isEmpty(str)) {
                    list.add(str);
                }
            }
        }
        return list;
    }

    /**
     * 转为String集合(分隔符为",")
     *
     * @param obj 字符串
     * @return String集合
     */
    public static final List<String> toList(Object obj) {
        return toList(obj, ",");
    }

    /*
     * 删除map中的key
     */
    public static void removeMapKeys(Map<?, ?> map, Object... keys) {
        if (isEmpty(map)) {
            return;
        }
        for (Object key : keys) {
            map.remove(key);
        }
    }

    /**
     * 过滤Map中的空键或空值
     *
     * @param map Map实例
     * @return 过滤后的Map实例
     */
    public static final <K, V> Map<K, V> cleanMapEmptyField(Map<K, V> map) {
        if (!isEmpty(map)) {
            Iterator<K> it = map.keySet().iterator();
            while (it.hasNext()) {
                K key = it.next();
                if (isEmpty(key) || isEmpty(map.get(key))) {
                    it.remove();
                }
            }
        }
        return map;
    }





    private static String handleEmptyField(String emptyResp, String lengthLimit) {
        if (!Utils.isEmpty(emptyResp)) {
            emptyResp = emptyResp.substring(0, emptyResp.lastIndexOf(","))
                    + " not be null";
        }
        if (!Utils.isEmpty(lengthLimit)) {
            lengthLimit = lengthLimit.substring(0, lengthLimit.lastIndexOf(","));
        }
        return emptyResp + "  " + lengthLimit;
    }



    /**
     * 长整型转换为日期类型
     *

     * @return String 长整型对应的格式的时间
     */
    public static String long2String(long longTime) {
        Date d = new Date(longTime);
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = s.format(d);
        return str;

    }

    public static String long2String(long longTime, String format) {
        Date d = new Date(longTime);
        SimpleDateFormat s = new SimpleDateFormat(format);
        String str = s.format(d);
        return str;

    }

    /**
     * 格式化Date
     *

     * @param date    date对象
     * @return String 格式化日期字符串
     */
    public static final String Date2String(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public static final String Date2String(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    /**
     * 加减Date各字段的值
     *
     * @param date      Date
     * @param field     字段,如Calendar.DAY_OF_YEAR,Calendar.MINUTE等
     * @param increment 增量,可为负值
     * @return Date
     */
    public static Date dateAdd(Date date, int field, int increment) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        cal.add(field, increment);
        return cal.getTime();
    }

    /*
     * 生成6位验证码
     */
    public static String generatePassCode() {
        Random r = new Random();
        StringBuffer ps = new StringBuffer();
        for (int i = 0; i < 6; i++) {
            int num = r.nextInt(10);
            ps.append(num);
        }
        return ps.toString();
    }

    /*
     * IP地址获取
     
    public static String getIP(HttpServletRequest request) {
    	if (request.getHeader("x-forwarded-for") == null) {
    		return request.getRemoteAddr();
    	}
    	return request.getHeader("x-forwarded-for");
    }*/



    /**
     * 判断手机的操作系统 IOS/android
     * 0：android，1：ios，2：论坛, 3：官网，4: 其他
     */
    public static Integer getDeviceType(String UA) {
        UA = UA.toUpperCase();
        if (UA == null) {
            return 4;
        }
        // IOS 判断字符串
        String iosString = "IOS";
        if (UA.indexOf(iosString) != -1) {
            return 1;
        }
        // Android 判断
        String androidString = "ANDROID";
        String OKHTTP = "OKHTTP";// android 采用的okhttp的方式
        if (UA.indexOf(androidString) != -1 || UA.indexOf(OKHTTP) != -1) {
            return 0;
        }

        return 4;
    }

    /*
     * List<Map<String, Object>> --> List<String>
     */
    public static List<String> listMap2Str(String key, List<Map<String, Object>> list) {
        List<String> resp = Utils.newList();
        for (Map<String, Object> map : list) {
            resp.add(map.get(key).toString());
        }
        return resp;
    }

    /*
     * 1,1,2 --> ('1','2','3')
     */
    public static String str2SqlIn(String str) {
        List<String> list = toList(str);
        String app = "(";
        for (String cell : list) {
            app += "'" + cell + "',";
        }
        return app.substring(0, app.length() - 1) + ")";
    }



    /**
     * v1 上传的版本号
     * v2 服务器端最新的版本号
     * <p>
     * 上传的版本号大于等于我服务器端的版本号，不需要升级
     * 不需要升级[ 即 v1 >= v2 ], 返回true
     * 需要升级[ 即 v1 <  v2 ], 返回false
     * 如果位数不同， 也按照前面的比较， 前面的都相同才比较后面的数
     */
    public static boolean versionCompare(String v1, String v2, boolean isFW) {
        if (v1.equals(v2)) {
            return true;
        }
        String[] arr1 = v1.split("\\.");
        String[] arr2 = v2.split("\\.");
        int len1 = arr1.length;
        int len2 = arr2.length;
        int len = len1 > len2 ? len2 : len1;//用位数多的作为for循环长度
        for (int i = 0; i < len; i++) {
            if (isFW && i == 1) {//fw版本号第二位是国家不需要比较
                continue;
            }
            int tmp1 = Integer.parseInt(arr1[i]);
            int tmp2 = Integer.parseInt(arr2[i]);
            if (tmp1 < tmp2) {
                return false;
            } else if (tmp1 > tmp2) {
                return true;
            }
        }
        //v1小数点少 返回false， 表示需要升级
        return len1 < len2 ? false : true;
    }

}
