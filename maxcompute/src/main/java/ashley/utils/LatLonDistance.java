package ashley.utils;

import com.aliyun.odps.udf.UDF;

/**
 * Created by AshleyZHANG on 2019/6/4.
 */
public class LatLonDistance extends UDF {

    // 两点距离
    public static double latDistance(double latA, double logA, double latB, double logB) {
        int earthR = 6371000;
        double x = Math.cos(latA * Math.PI / 180) * Math.cos(latB * Math.PI / 180) * Math.cos((logA - logB) * Math.PI / 180);
        double y = Math.sin(latA * Math.PI / 180) * Math.sin(latB * Math.PI / 180);
        double s = x + y;
        if (s > 1)
            s = 1;
        if (s < -1)
            s = -1;
        double alpha = Math.acos(s);
        double distance = alpha * earthR;
        return distance;
    }
    public static void main(String[] args) {
        double a = 30.637614;
        double b = 104.086990;
        double c = 30.637614;
        double d = 104.086990;
        double k = latDistance(a, b, c, d);
        //返回M
        System.out.println(k);
    }
}