package ashley.UDF;

import com.aliyun.odps.udf.UDF;

/**
 * Created by AshleyZHANG on 2019/6/24.
 */
public class NumToByte {
        public int evaluate(int n) {
            int sum;
            String result = "";
            if (n<=1){
                return 0;
            }
            for (int i = n; i >= 1; i = i / 2) {
                   if (i % 2 == 0) {
                     sum = 0;
               } else {
                         sum = 1;
                     }
               result = sum + result;
             }
            System.out.println(result);
            String[] str= result.split("");
           // for (int i=0;i<str.length;i++){}
            int s=Integer.parseInt(str[str.length-2]);
            return s;

        }

    public static void main(String[] args) {
        int s =34;
        NumToByte b=new NumToByte();
        int k=b.evaluate(s);
        System.out.println(k);
    }
}