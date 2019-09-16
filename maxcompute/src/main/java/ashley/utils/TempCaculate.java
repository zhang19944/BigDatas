package ashley.utils;

/**
 * Created by AshleyZHANG on 2019/5/31.
 */
public class TempCaculate {
    private static final double R1 = 100;
    private static final double R6 = 100;
    private static final double Ldo = 3.3;
    public static final double TEMPER_CALC_B1 = 3950; //柜体温度的B值
    public static final double TEMPER_CALC_B2 = 3435; //柜子加热的B值
    private static final double T0 = 273.15;
    private static final double T1 = 25;

    /**
     * 计算温度
     * @param v 转换成电压之前的值
     * @param B
     * @return
     */
    public static int calcTemper(int v, double B){
        double Va = v * 3.3 / 4096;
        double t2 = 1/(Math.log(Va*R6/((Ldo-Va)*R1))/B+1/(T0+T1))-T0;
        return (int)(t2*10+(t2<0?-0.5:0.5));

    }
    public static void main(String[] args) {

        System.out.println( calcTemper(781,TEMPER_CALC_B2));


    }


}

