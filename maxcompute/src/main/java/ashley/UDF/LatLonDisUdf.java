package ashley.UDF;

import com.aliyun.odps.udf.UDF;

/**
 * Created by AshleyZHANG on 2019/6/4.
 */
public class LatLonDisUdf extends UDF {
    public double evaluate(double s, double k,double m,double n) {

        double distance=(m-s)*(m-s)+(k-n)*(k-n);

        return Math.sqrt(distance);
    }
}


