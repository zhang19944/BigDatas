package util;

import bean.track.TrackEchb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ListUtil {
    public  static  List<Integer> isAvail(List<TrackEchb> list, Date start, Date end, int num){
        List<TrackEchb> list1=new ArrayList<TrackEchb>();
        for(int i=0;i<list.size();i++){
            Date time= list.get(i).getTime();
            if(time.compareTo(start)>=0&&time.compareTo(end)<=0&&list.get(i).getList().size()==(num)){

                list1.add( list.get(i));
            }
        }
        Collections.sort(list1);



          for(int i=0;i<list1.size()-2;i++){
            if(((list1.get(i).getSum_soc()-list1.get(i+1).getSum_soc())<5)
                    &&((list1.get(i).getSum_soc()-list1.get(i+1).getSum_soc())>-5)
                    &&(list1.get(i+1).getSum_soc()-list1.get(i+2).getSum_soc())<5
                    && (list1.get(i+1).getSum_soc()-list1.get(i+2).getSum_soc())>-5){

                 List<Integer> list2=new ArrayList<Integer>();
                 for(int m=0;m<list1.get(i).getList().size();m++){

                     list2.add(list1.get(i).getList().get(m).getSoc());
                 }

                return list2;
            }
      //  }
        }
        return null;
    }
}
