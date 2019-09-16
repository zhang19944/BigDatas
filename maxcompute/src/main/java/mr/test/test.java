package mr.test;

import battery.HardBatteryRealTimePartOnlyData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class test {
    public static void main(String[] args) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//这个是你要转成后的时间的格式
        SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");//这个是你要转成后的时间的格式
        System.out.println(Integer.parseInt("04", 16));
        try {
            System.out.println(sdf.parse("2019-01-28 16:25:16.579".substring(0,19)));
            System.out.println(sdf.parse("2019-01-28 16:25:16.579"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<HardBatteryRealTimePartOnlyData> list=new ArrayList<HardBatteryRealTimePartOnlyData>();
        HardBatteryRealTimePartOnlyData hardBatteryRealTimePartOnlyData0=new HardBatteryRealTimePartOnlyData();
        try {
            hardBatteryRealTimePartOnlyData0.setCollecttime(sdf.parse("2018-10-11 00:00:00"));
            hardBatteryRealTimePartOnlyData0.setCycle(12);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        HardBatteryRealTimePartOnlyData hardBatteryRealTimePartOnlyData1=new HardBatteryRealTimePartOnlyData();
        try {
            hardBatteryRealTimePartOnlyData1.setCollecttime(sdf.parse("2018-10-12 00:00:00"));
            hardBatteryRealTimePartOnlyData1.setCycle(12);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        HardBatteryRealTimePartOnlyData hardBatteryRealTimePartOnlyData2=new HardBatteryRealTimePartOnlyData();
        try {
            hardBatteryRealTimePartOnlyData2.setCollecttime(sdf.parse("2018-10-13 00:00:00"));
            hardBatteryRealTimePartOnlyData2.setCycle(12);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        HardBatteryRealTimePartOnlyData hardBatteryRealTimePartOnlyData3=new HardBatteryRealTimePartOnlyData();
        try {
            hardBatteryRealTimePartOnlyData3.setCollecttime(sdf.parse("2018-10-14 00:00:00"));
            hardBatteryRealTimePartOnlyData3.setCycle(12);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        HardBatteryRealTimePartOnlyData hardBatteryRealTimePartOnlyData4=new HardBatteryRealTimePartOnlyData();
        try {
            hardBatteryRealTimePartOnlyData4.setCollecttime(sdf.parse("2018-10-15 00:00:00"));
            hardBatteryRealTimePartOnlyData4.setCycle(12);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        HardBatteryRealTimePartOnlyData hardBatteryRealTimePartOnlyData5=new HardBatteryRealTimePartOnlyData();
        try {
            hardBatteryRealTimePartOnlyData5.setCollecttime(sdf.parse("2018-10-16 00:00:00"));
            hardBatteryRealTimePartOnlyData5.setCycle(12);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        HardBatteryRealTimePartOnlyData hardBatteryRealTimePartOnlyData6=new HardBatteryRealTimePartOnlyData();
        try {
            hardBatteryRealTimePartOnlyData6.setCollecttime(sdf.parse("2018-10-17 00:00:00"));
            hardBatteryRealTimePartOnlyData6.setCycle(160);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        HardBatteryRealTimePartOnlyData hardBatteryRealTimePartOnlyData7=new HardBatteryRealTimePartOnlyData();
        try {
            hardBatteryRealTimePartOnlyData7.setCollecttime(sdf.parse("2018-10-18 00:00:00"));
            hardBatteryRealTimePartOnlyData7.setCycle(12);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        list.add(hardBatteryRealTimePartOnlyData0);
        list.add(hardBatteryRealTimePartOnlyData1);
        list.add(hardBatteryRealTimePartOnlyData2);
        list.add(hardBatteryRealTimePartOnlyData3);
        list.add(hardBatteryRealTimePartOnlyData4);
        list.add(hardBatteryRealTimePartOnlyData5);
        list.add(hardBatteryRealTimePartOnlyData6);
        list.add(hardBatteryRealTimePartOnlyData7);
        Collections.sort(list);
        for(int m=0;m<list.size();m++){
            System.out.println("origin="+list.get(m).getCollecttime()+" "+list.get(m).getCycle());
        }
        HardBatteryRealTimePartOnlyData[] array = list.toArray(new HardBatteryRealTimePartOnlyData[list.size()] );



        for(int n = 0;n<array.length;n++){
            //遍历数组，判断找出需删除元素的位置
            //System.out.println(array[n].getSoc());
            if(n==array.length-2&&array[n].getCycle()!=-1){
            if( (array[n].getCycle()!=array[n-1].getCycle()||array[n].getCycle()!=array[n+1].getCycle())&&(
                    array[n].getCycle()!=array[n-1].getCycle()||array[n].getCycle()!=array[n-2].getCycle()
                    )){

                for (int k = n; k<array.length-1; k++) {
                    array[k] = array[k+1];
                }
                //数组最后一个元素赋为控制
                HardBatteryRealTimePartOnlyData hardBatteryRealTimePartOnlyData=new HardBatteryRealTimePartOnlyData();
                hardBatteryRealTimePartOnlyData.setCycle(-1);
                array[array.length-1]=hardBatteryRealTimePartOnlyData;
                n--;

            }
            }else  if(n==array.length-1&&array[n].getCycle()!=-1){
                if( (
                        array[n].getCycle()!=array[n-1].getCycle()||array[n].getCycle()!=array[n-2].getCycle()
                )){


                    //数组最后一个元素赋为控制
                    HardBatteryRealTimePartOnlyData hardBatteryRealTimePartOnlyData=new HardBatteryRealTimePartOnlyData();
                    hardBatteryRealTimePartOnlyData.setCycle(-1);
                    array[array.length-1]=hardBatteryRealTimePartOnlyData;
                    n--;

                }

            }else if(array[n].getCycle()!=-1){
                if(array[n].getCycle()!=array[n+1].getCycle()||array[n].getCycle()!=array[n+2].getCycle()){
                if(n==1){
                    if(array[n].getCycle()!=array[n-1].getCycle()||array[n].getCycle()!=array[n+1].getCycle()){
                        for (int k = n; k<array.length-1; k++) {
                            array[k] = array[k+1];
                        }
                        //数组最后一个元素赋为控制
                        HardBatteryRealTimePartOnlyData hardBatteryRealTimePartOnlyData=new HardBatteryRealTimePartOnlyData();
                        hardBatteryRealTimePartOnlyData.setCycle(-1);
                        array[array.length-1]=hardBatteryRealTimePartOnlyData;
                        n--;
                    }
                }
                else if(n>1){
                    System.out.println(n+":"+array[n].getCycle());
                    if((array[n].getCycle()!=array[n-1].getCycle()||array[n].getCycle()!=array[n-2].getCycle())&&
                            (array[n].getCycle()!=array[n-1].getCycle()||array[n].getCycle()!=array[n+1].getCycle())){
                        System.out.println("====11111======");
                        for (int k = n; k<array.length-1; k++) {
                            array[k] = array[k+1];
                        }
                        //数组最后一个元素赋为控制
                        HardBatteryRealTimePartOnlyData hardBatteryRealTimePartOnlyData=new HardBatteryRealTimePartOnlyData();
                        hardBatteryRealTimePartOnlyData.setCycle(-1);
                        array[array.length-1]=hardBatteryRealTimePartOnlyData;
                        n--;
                    }
                }else{
                    //把需删除元素后面的元素依次覆盖前面的元素
                    for (int k = n; k<array.length-1; k++) {
                        array[k] = array[k+1];
                    }
                    //数组最后一个元素赋为控制
                    HardBatteryRealTimePartOnlyData hardBatteryRealTimePartOnlyData=new HardBatteryRealTimePartOnlyData();
                    hardBatteryRealTimePartOnlyData.setCycle(-1);
                    array[array.length-1]=hardBatteryRealTimePartOnlyData;
                    n--;
                }
            }
        }
        }







            for(int m=0;m<array.length;m++){
                System.out.println("array="+array[m].getCycle()+" "+array[m].getCollecttime());
            }




//        for(int n = 0;n<array.length-1;n++){
//            //遍历数组，判断找出需删除元素的位置
//            System.out.println(
//                    n
//            );
//            if((array[n].getCycle()>array[n+1].getCycle())&&(array[n+1].getCycle()!=-1)){
//                for (int m=n+1;m<array.length-2;m++){
//                    array[m]=array[m+1];
//                }
//                HardBatteryRealTimePartOnlyData hardBatteryRealTimePartOnlyData01=new HardBatteryRealTimePartOnlyData();
//                hardBatteryRealTimePartOnlyData01.setCycle(-1);
//                array[array.length-1]=hardBatteryRealTimePartOnlyData01;
//                n--;
//            }
//        }
//        for(int m=0;m<array.length;m++){
//            System.out.println("array="+array[m].getCollecttime()+" "+array[m].getCycle());
//        }

    }
}
