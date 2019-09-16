package ashley.UDF;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by AshleyZHANG on 2019/9/2.
 * 心跳中取出和放入的电池ID
 */

public class ArrayCompare {

    public String compareArray(String m, String n){
        if (m.isEmpty()){return null;}
        String[] as= m.split(",");
        String[] bs=n.split(",");
        List<String> list =null;
        List<String> result=null;
        List<String> list2 =null;
        List<String> result2=null;

        String flag = null;
        String flag2=null;
        //as.length>=bs.length  说明一定是取出了电池
             list = Arrays.asList(bs);
             result = new ArrayList<String>();
            for(String a : as ){
                //判断是否包含
                if(!list.contains(a)){
                    result.add(a);
                }
            }
            flag="取走电池ID";
            list2 = Arrays.asList(as);
            result2 = new ArrayList<String>();
            for(String a : bs ){
                //判断是否包含
                if(!list2.contains(a)){
                    result2.add(a);
                }
            }
            flag2="放入电池ID";
//用来装差集
        System.out.println(result +":"+ result2);
        return result +":"+ result2;

    }
    public static void main(String[] args) {

        ArrayCompare arrayCompare = new ArrayCompare();
        arrayCompare.compareArray("1027985f0203,1027b551b680,102799b4735b,1027abdbd766,1027512d1712,1027aa47b002",
                                  "10278c1bd6bd,10278b51ce32,10275872ab05,1027985f0203,1027b551b680,102799b4735b,1027abdbd766,1027512d1712,1027aa47b002");
    }
}
