package mr;

import battery.Battery;
import battery.Datas;
import battery.DateSoc;
import battery.JsonRootBean;
import com.aliyun.odps.data.Record;
import com.aliyun.odps.data.TableInfo;
import com.aliyun.odps.mapred.JobClient;
import com.aliyun.odps.mapred.MapperBase;
import com.aliyun.odps.mapred.ReducerBase;
import com.aliyun.odps.mapred.conf.JobConf;
import com.aliyun.odps.mapred.utils.InputUtils;
import com.aliyun.odps.mapred.utils.OutputUtils;
import com.aliyun.odps.mapred.utils.SchemaUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Battery_chargetime_soc {
    public static class TokenizerMapper extends MapperBase {

        private Record word;
        private Record one;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//这个是你要转成后的时间的格式

        @Override
        public void setup(TaskContext context) throws IOException {
            word = context.createMapOutputKeyRecord();
            one = context.createMapOutputValueRecord();
        }

        @Override
        public void map(long recordNum, Record record, TaskContext context)
                throws IOException {
            String jsonString = record.get("content").toString();
            JsonObject obj = new JsonParser().parse(jsonString).getAsJsonObject();
            JsonRootBean jsonRootBean = new Gson().fromJson(obj,JsonRootBean.class);
            String pid=jsonRootBean.getPID();
            String time=jsonRootBean.getTime();
            Datas datas=jsonRootBean.getDatas();
            List<Battery> batterys = datas.getBattery();
            if(batterys.size()==0)
                return;
            for(Battery battery:batterys){
                String id = battery.getId();
                Integer soc=battery.getSoc();
                word.set(0, id);
                word.set(1, pid);
                one.setString(0,id);
                one.setString(1,pid);
                one.setBigint(3, soc.longValue());
                try {
                    one.setDatetime(2,sdf.parse(time) );
                } catch (ParseException e1) {

                    e1.printStackTrace();
                }
                context.write(word, one);

            }



        }



    }
    public static class SumReducer extends ReducerBase {

        private Record result = null;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//这个是你要转成后的时间的格式
        @Override
        public void setup(TaskContext context) throws IOException {
            result = context.createOutputRecord();
        }
        public    double formatDouble2l(double d) {
            return (double)Math.round(d*100)/100;
        }
        @Override
        public void reduce(Record key, Iterator<Record> values, TaskContext context)
                throws IOException {
            List<DateSoc> list=new ArrayList();
            String id=(String)key.get(0);
            String pid=(String)key.get(1);
            while (values.hasNext()) {
                Record val = values.next();
                Date date=(Date)val.get(2);
                Long soc =(Long)val.get(3);
                DateSoc dateSoc=new DateSoc(date, soc.intValue());
                list.add(dateSoc);

            }
            Collections.sort(list);
            if(pid.equals("9de1925b01a3b724")){
                for(DateSoc dateSoc:list){
                    System.out.println("startTime: "+id+" "+pid+" " +dateSoc.getSoc() +" "+ dateSoc.getDate());
                }
            }
            Date startTime=new  Date();
            Date endTime=new  Date();
            Integer socStart=0;
            Integer socEnd=0;
            int j=0;
            // boolean flag=false;
            //同一个电池在同一个柜子可能充点多次
            for(int m=j;m<list.size();m++) {
                //判断开始充电时间
                boolean flag=false;
                boolean flag1=false;
                for (int i = j; i < list.size(); i++) {
                    if(i+1<list.size()){
                       //if (list.get(i).getSoc() != list.get(i + 1).getSoc()&&list.get(i).getSoc()<90) {
                       if (list.get(i).getSoc()<80&&list.get(i).getSoc()!=0) {
                            flag=true;
                            startTime = list.get(i ).getDate();
                            socStart=list.get(i ).getSoc();
//			                    if(id.equals("10155a27a1b8")){
//			                    System.out.println("startTime "+id+" "+pid+" " +startTime +" "+ list.get(i + 1).getSoc());
//			                    }
                            j = i + 1;
                            break;
                        }
                    }
                }
                //判断结束充电时间
                if(flag){
                    for (int k = j; k < list.size(); k++) {
                        //list.get(k).getSoc() >list.get(k+1).getSoc()&&
                        //                                ||
                        if ((//(list.get(k).getDate().getTime()-list.get(k+1).getDate().getTime())>60*1000*10
                           //     ||k==(list.size()-2)
                                list.get(k).getSoc() >=80)&&list.get(k).getSoc()!=0) {
                            flag1=true;
                            endTime = list.get(k).getDate();
                            socEnd=list.get(k).getSoc();
//			                    if(id.equals("10155a27a1b8")){
//			                    System.out.println("endTime"+k+" "+endTime+" "+ list.get(k).getSoc());
//			                    }
                            j = k+1;
                            break;
                        }
                    }
                }
                if(flag&&flag1){//有开始时间和结束时间
                    result.setString(0,pid);
                    result.setString(1, id);
                    result.setBigint(2, socStart.longValue());
                    result.setBigint(3, socEnd.longValue());
                    result.setDatetime(4, startTime);
                    result.setDatetime(5, endTime);
                    result.setDouble(6,formatDouble2l((socEnd.longValue()- socStart.longValue())*60*1000.0/(endTime.getTime()-startTime.getTime())));
                    context.write(result);
                }

            }




        }

    }


    // 将分区字符串如"ds=1/pt=2"转为map的形式
    public static LinkedHashMap<String, String> convertPartSpecToMap(
            String partSpec) {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        if (partSpec != null && !partSpec.trim().isEmpty()) {
            String[] parts = partSpec.split("/");
            for (String part : parts) {
                String[] ss = part.split("=");
                if (ss.length != 2) {
                    throw new RuntimeException("ODPS-0730001: error part spec format: "
                            + partSpec);
                }
                map.put(ss[0], ss[1]);
            }
        }
        return map;
    }
    public static void main(String[] args) throws Exception {
        String[] inputs = null;
        String[] outputs = null;
        if (args.length == 2) {
            inputs = args[0].split(",");
            // System.out.println(inputs[0]+" "+inputs[1]);
            outputs = args[1].split(",");
            //  System.out.println(outputs[0]);
        } else {
            System.err.println("MultipleInOut in... out...");
            System.exit(1);
        }
        JobConf job = new JobConf();
        job.setMapperClass(Battery_chargetime_soc.TokenizerMapper.class);
        job.setReducerClass(Battery_chargetime_soc.SumReducer.class);
        job.setMapOutputKeySchema(SchemaUtils.fromString("id:string,pid:string"));
        job.setMapOutputValueSchema(SchemaUtils.fromString("id:string,pid:string,time:datetime,soc:bigint"));
        //解析用户的输入表字符串
        for (String in : inputs) {
            String[] ss = in.split("\\|");
            if (ss.length == 1) {
                InputUtils.addTable(TableInfo.builder().tableName(ss[0]).build(), job);
            } else if (ss.length == 2) {
                LinkedHashMap<String, String> map = convertPartSpecToMap(ss[1]);
                InputUtils.addTable(TableInfo.builder().tableName(ss[0]).partSpec(map).build(), job);
            } else {
                System.err.println("Style of input: " + in + " is not right");
                System.exit(1);
            }
        }
        //解析用户的输出表字符串
        for (String out : outputs) {
            String[] ss = out.split("\\|");
            if (ss.length == 1) {
                OutputUtils.addTable(TableInfo.builder().tableName(ss[0]).build(), job);
            } else if (ss.length == 2) {
                LinkedHashMap<String, String> map = convertPartSpecToMap(ss[1]);
                OutputUtils.addTable(TableInfo.builder().tableName(ss[0]).partSpec(map).build(), job);
            } else if (ss.length == 3) {
                if (ss[1].isEmpty()) {
                    LinkedHashMap<String, String> map = convertPartSpecToMap(ss[2]);
                    OutputUtils.addTable(TableInfo.builder().tableName(ss[0]).partSpec(map).build(), job);
                } else {
                    LinkedHashMap<String, String> map = convertPartSpecToMap(ss[1]);
                    OutputUtils.addTable(TableInfo.builder().tableName(ss[0]).partSpec(map)
                            .label(ss[2]).build(), job);
                }
            } else {
                System.err.println("Style of output: " + out + " is not right");
                System.exit(1);
            }
        }
        JobClient.runJob(job);
    }

}
