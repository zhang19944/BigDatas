package util;

import java.util.Iterator;
import java.util.Map;

public class MapUtil {
    public  static   void  deleteMap(String id,Map map){
       Iterator iterator= map.entrySet().iterator();
       while (iterator.hasNext()){
           Map.Entry entry=(Map.Entry)iterator.next();
           if(id.equals(entry.getKey())){
               iterator.remove();;
           }
       }
    }
}
