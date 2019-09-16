package util;

import java.util.List;

/**
 * 字符串处理工具类
 */
public class StringUtil {

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
     * 判断字符串是否包含可见字符(与{@link #isEmpty(Object)}方法相反)
     *
     * @param obj 待判断对象
     * @return boolean true-有内容,false-空(null/空字符串/纯空白字符)
     */
    public static final boolean hasText(Object obj) {
        return !isEmpty(obj);
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
     * 返回一个对象的字符串,并去除两端空白字符
     *
     * @param obj 对象
     * @return 字符串
     */
    public static final String trim(Object obj) {
        return obj == null ? "" : String.valueOf(obj).trim();
    }

    /**
     * 格式化字符串
     *
     * @param tpl    模板,参数占位符为{}, 如 "My name is {}, i'm {} years old."
     * @param params 参数
     * @return 格式化后的字符串
     */
    public static final String format(String tpl, Object... params) {
        if (StringUtil.hasText(tpl)) {
            for (Object param : params) {
                tpl = tpl.replaceFirst("\\{\\}", toString(param));
            }
        }
        return tpl;
    }



    /**
     * 转为小写字符串
     *
     * @param obj 字符串
     * @return 小写字符串
     */
    public static final String toLowerCase(Object obj) {
        if (hasText(obj)) {
            return toString(obj).toLowerCase();
        } else {
            return null;
        }
    }

    /**
     * 转为大写字符串
     *
     * @param obj 字符串
     * @return 大写字符串
     */
    public static final String toUpperCase(Object obj) {
        if (hasText(obj)) {
            return toString(obj).toUpperCase();
        } else {
            return null;
        }
    }

    /**
     * 对比两个对象是否相同
     * <p>
     * <pre>
     * 当obj1==null或obj2==null时,返回false;
     * 否则返回 obj1.equals(obj2)
     * </pre>
     *
     * @param obj1 对象1
     * @param obj2 对象2
     * @return boolean
     */
    public static final boolean equals(Object obj1, Object obj2) {
        boolean equals = false;

        if (obj1 != null && obj2 != null) {
            equals = obj1.equals(obj2);
        }

        return equals;
    }

    /**
     * 对比两个字符串是否相同(忽略大小写)
     * <p>
     * <pre>
     * 当obj1==null或obj2==null时,返回false;
     * 否则返回 obj1.toString().equalsIgnoreCase(obj2.toString())
     * </pre>
     *
     * @param obj1 对象1
     * @param obj2 对象2
     * @return boolean
     */
    public static final boolean equalsIgnoreCase(Object obj1, Object obj2) {
        boolean equals = false;

        if (obj1 != null && obj2 != null) {
            equals = obj1.toString().equalsIgnoreCase(obj2.toString());
        }

        return equals;
    }

}
