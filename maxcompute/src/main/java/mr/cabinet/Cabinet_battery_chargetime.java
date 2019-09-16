package mr.cabinet;

import battery.*;
import bean.station_hb3.Station_hb3;
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

public class Cabinet_battery_chargetime {



        public static class TokenizerMapper extends MapperBase {

            private Record key;
            private Record value;
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss");//这个是你要转成后的时间的格式
            SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss");//这个是你要转成后的时间的格式

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
                for (int i=0;i<batteries.size();i++){
                    Battery battery= batteries.get(i);
                    if("ffffffffffff".equals( battery.getId())){
                        continue;
                    }
                    value.setBigint(0,(long) battery.getC());
                    value.setBigint(1,(long) battery.getSoc());
                    value.setBigint(2,(long) battery.getT());
                    try {
                        value.setDatetime(3,sdf1.parse(record.get("time1").toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    key.setString(0,record.get("pid").toString());

                    key.setString(1, battery.getId());

                    context.write(key,value);
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
                List<BattryCurrentAndSocBytime> list=new ArrayList();
                String pid=(String)key.get(0);
                String id=(String)key.get(1);
                while (values.hasNext()) {
                    Record val = values.next();
                    Date date=(Date)val.get(3);
                    Long soc =(Long)val.get(1);
                    Long c =(Long)val.get(0);
                    Long t =(Long)val.get(2);
                    BattryCurrentAndSocBytime battryCurrentAndSocBytime=new BattryCurrentAndSocBytime(c,soc, t,date);
                    list.add(battryCurrentAndSocBytime);

                }
                Collections.sort(list);

                if(pid.equals("100bc27ba280")){
                    for(BattryCurrentAndSocBytime dateSoc:list){
                        System.out.println("id: "+id+" pid:"+pid+" soc:" +dateSoc.getSoc() +" time:"+ dateSoc.getTime()+" c:"+dateSoc.getC()+" t:"+dateSoc.getT());
                    }
                }
                Date startTime=new  Date();
                Date endTime=new  Date();
                Long socStart=0l;
                Long socEnd=0l;
                Long startC=0l;
                Long endC=0l;
                Long startT=0l;
                Long endT=0l;
                int j=0;
                // boolean flag=false;
                //同一个电池在同一个柜子可能充点多次
                for(int m=j;m<list.size();m++) {
                    //判断开始充电时间
                    boolean flag=false;
                    boolean flag1=false;
                    for (int i = j; i < list.size(); i++) {
                        if(i==0){//第一位
                            if (list.get(i).getC()>=100) {
                                flag=true;
                                startTime = list.get(i).getTime();
                                socStart=list.get(i).getSoc();
                                startC=list.get(i).getC();
                                startT=list.get(i).getT();
//			                    if(id.equals("10155a27a1b8")){
//			                    System.out.println("startTime "+id+" "+pid+" " +startTime +" "+ list.get(i + 1).getSoc());
//			                    }
                                j = i + 1;
                                break;
                            }
                        }else
                        if(i+1<list.size()){//第二位开始
                            if (list.get(i).getC()>=100&&list.get(i-1).getC()<100&&list.get(i).getSoc()<80) {
                                flag=true;
                                startTime = list.get(i).getTime();
                                socStart=list.get(i).getSoc();
                                startC=list.get(i).getC();
                                startT=list.get(i).getT();
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

                            if (( list.get(k).getSoc() >=80)) {
                                flag1=true;
                                endTime = list.get(k).getTime();
                                socEnd=list.get(k).getSoc();
                                endC=list.get(k).getC();
                                endT=list.get(k).getT();
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
                        result.setBigint(7, startT);
                        result.setBigint(8, endT);
                        result.setBigint(9, startC);
                        result.setBigint(10, endC);
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
                outputs = args[1].split(",");
            } else {
                System.err.println("MultipleInOut in... out...");
                System.exit(1);
            }
            JobConf job = new JobConf();
            job.setMapperClass(Cabinet_battery_chargetime.TokenizerMapper.class);
            job.setReducerClass(Cabinet_battery_chargetime.SumReducer.class);
            job.setMapOutputKeySchema(SchemaUtils.fromString("id:string,pid:string"));
            job.setMapOutputValueSchema(SchemaUtils.fromString("c:bigint,soc:bigint,t:bigint,time:datetime"));
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

