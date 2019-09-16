package ashley.utils;

/**
 * Created by AshleyZHANG on 2019/5/30.
 */
public class ShortUtil {
    public int parseShort(String a,int radix){

        int i=Integer.parseInt(a,radix);
        int s;
        if (i>0x7fff) {
            s=-(i-0x8000);
        } else {
            s=i;
        }
       return s ;
    }
}
