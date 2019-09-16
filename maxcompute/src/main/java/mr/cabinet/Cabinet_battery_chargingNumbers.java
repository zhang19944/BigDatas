package mr.cabinet;

import battery.Battery;
import battery.DateSoc;
import bean.station_hb3.Station_hb3;
import com.aliyun.odps.data.Record;
import com.aliyun.odps.mapred.JobClient;
import com.aliyun.odps.mapred.MapperBase;
import com.aliyun.odps.mapred.ReducerBase;
import com.aliyun.odps.mapred.utils.SchemaUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import util.ToolUtil;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Cabinet_battery_chargingNumbers {




    public static class TokenizerMapper extends MapperBase {
        private Record key;
        private Record value;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//这个是你要转成后的时间的格式

        @Override
        public void setup(TaskContext context) throws IOException {
            key = context.createMapOutputKeyRecord();
            value = context.createMapOutputValueRecord();
        }

        @Override
        public void map(long recordNum, Record record, TaskContext context)
                throws IOException {
            if(record.getBigint("type")!=3)
                return;
            String jsonString =record.get("datas").toString().trim();
            JsonObject obj = new JsonParser().parse(jsonString).getAsJsonObject();
            Station_hb3 station_hb3 = new Gson().fromJson(obj, Station_hb3.class);

            List<Battery> batteries=station_hb3.getBattery();

            /**
             *
             * 计算48v电池和60v电池数量
             *
             */
            int count48v=0;
            int count60v=0;
           for(int i=0;i<batteries.size();i++){
               if(batteries.get(i).getNv()<5000){
                  count48v++;
               }else{
                  count60v++;
               }

           }

            key.setString(0, record.get("pid").toString());
            try {
                value.setDatetime(0,sdf.parse(record.get("time1").toString()));
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            value.setBigint(1, Long.parseLong(String.valueOf(batteries.size())));
            value.setBigint(2,Long.valueOf(count48v));
            value.setBigint(3,Long.valueOf(count60v));
            context.write(key, value);
        }
    }


    public static class SumReducer extends ReducerBase {
        private Record result = null;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//这个是你要转成后的时间的格式
        @Override
        public void setup(TaskContext context) throws IOException {
            result = context.createOutputRecord();
        }

        @Override
        public void reduce(Record key, Iterator<Record> values, TaskContext context)
                throws IOException {
            List<DateSoc> arraylist=new ArrayList();
            long startNum=0;
            Date startDate=null;
            long endNum=0;
            Date endDate=null;
            long count48v=0;
            long count60v=0;
            Date time=null;
            try {
                time = sdf.parse("1999-01-02 00:00:00");
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Long  c=0L;

            while (values.hasNext()) {
                Record val = values.next();
                time=(Date)val.get(0);
                c=(Long)val.get(1);
                DateSoc dateSoc=new DateSoc(time, c.intValue(),(Long)val.get(2),(Long)val.get(3));
                arraylist.add(dateSoc);

            }
            Collections.sort(arraylist);

            int j=2;
            int startTemp=0;
            boolean flag_out=false;
            for(int m=0;m<arraylist.size();m++) {
                if(((String)key.get(0)).equals("2f36935b06623c24")){
                    System.out.println("颗数："+arraylist.get(m).getSoc()+" 时间："+  arraylist.get(m).getDate());
                }
            }

            //删除少于连续3次的
            DateSoc[] array = arraylist.toArray(new DateSoc[arraylist.size()] );
            for(int n = 0;n<array.length-2;n++){
                //遍历数组，判断找出需删除元素的位置
                if(array[n].getSoc()!=array[n+1].getSoc()||array[n].getSoc()!=array[n+2].getSoc()){
                    if(n==1){
                        if(array[n].getSoc()!=array[n-1].getSoc()||array[n].getSoc()!=array[n+1].getSoc()){
                            for (int k = n; k<array.length-1; k++) {
                                array[k] = array[k+1];
                            }
                            //数组最后一个元素赋为控制
                            array[array.length-1] = new DateSoc(new Date(),-1);;
                            n--;
                        }
                    }else if(n>1){
                        if((array[n].getSoc()!=array[n-1].getSoc()||array[n].getSoc()!=array[n-2].getSoc())&&
                                (array[n].getSoc()!=array[n-1].getSoc()||array[n].getSoc()!=array[n+1].getSoc())){
                            for (int k = n; k<array.length-1; k++) {
                                array[k] = array[k+1];
                            }
                            //数组最后一个元素赋为控制
                            array[array.length-1] = new DateSoc(new Date(),-1);;
                            n--;
                        }
                    }else{
                        //把需删除元素后面的元素依次覆盖前面的元素
                        for (int k = n; k<array.length-1; k++) {
                            array[k] = array[k+1];
                        }
                        //数组最后一个元素赋为控制
                        array[array.length-1] = new DateSoc(new Date(),-1);;
                        n--;
                    }
                }
            }
            List list2=Arrays.asList(array);
            List<DateSoc> list = new ArrayList(list2);
            //list=Arrays.asList(array);
            Iterator<DateSoc> it=list.iterator();
            //去除数组中"a"的元素
            while(it.hasNext()){
                DateSoc st=it.next();
                if(st.getSoc()==-1){
                    it.remove();
                }
            }

            for(int m=0;m<list.size();m++) {
                if(((String)key.get(0)).equals("2f36935b06623c24")){
                    System.out.println("过滤后的颗数："+list.get(m).getSoc() +" "+list.get(m).getCount48v()+" "+list.get(m).getCount60v()+" 时间："+  list.get(m).getDate());
                }
            }

            for(int m=0;m<list.size()-1;m++) {
                if(list.get(m).getSoc() != list.get(m + 1).getSoc()){
                    result.setString(0, (String)key.get(0));//pid
                    result.setDatetime(1, list.get(m).getDate());//开始时间
                    result.setBigint(2, list.get(m).getSoc().longValue());//个数
                    result.setDatetime(3, list.get(m+1).getDate());//结束时间
                    result.setBigint(4, list.get(m+1).getSoc().longValue());
                    result.setBigint(5,list.get(m+1).getCount48v());
                    result.setBigint(6,list.get(m+1).getCount60v());
                    flag_out=true;
                    context.write(result);
                }


            }

            if(!flag_out){
                int count_out=0;
                for(int m=0;m<list.size()-1;m++) {
                    count_out++;

                    if((list.get(m).getSoc()==list.get(m+1).getSoc())){
                        if(count_out>20){
                            startDate = list.get(m).getDate();
                            startNum=list.get(m).getSoc();
                            endDate = list.get(m).getDate();
                            endNum=list.get(m).getSoc();
                            count48v=list.get(m).getCount48v();
                            count60v=list.get(m).getCount60v();

                            break;
                        }
                    }else{
                        count_out=0;
                    }
                }
                if(count_out>20) {
                    result.setString(0, (String) key.get(0));
                    result.setDatetime(1, startDate);
                    result.setBigint(2, startNum);
                    result.setDatetime(3, startDate);
                    result.setBigint(4, startNum);
                    result.setBigint(5, count48v);
                    result.setBigint(6, count60v);
                    context.write(result);
                }
            }
        }
    }



    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: WordCount <in_table> <out_table>");
            System.exit(2);
        }
        JobClient.runJob(ToolUtil.runJob(args,Cabinet_battery_chargingNumbers.TokenizerMapper.class,Cabinet_battery_chargingNumbers.SumReducer.class,
                SchemaUtils.fromString("pid:string"),SchemaUtils.fromString("time:datetime,number:bigint,count48v:bigint,count60v:bigint")));
    }












}
