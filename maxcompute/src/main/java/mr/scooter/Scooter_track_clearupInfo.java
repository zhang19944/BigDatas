package mr.scooter;

import battery.DateSoc;
import bean.track.Track;
import bean.track.TrackCompare;
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
import mr.Android_BatteryRealTimeDataCollectPartReadOnlyData;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class Scooter_track_clearupInfo {


    public static class TokenizerMapper extends MapperBase {
        private Record one;
        private Record word;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//这个是你要转成后的时间的格式

        @Override
        public void setup(TaskContext context) throws IOException {
            one=context.createOutputRecord();
            word=context.createMapOutputKeyRecord();
        }

        public  boolean isNumeric(String str){
            Pattern pattern = Pattern.compile("[0-9]*");
            return pattern.matcher(str).matches();
        }
        @Override
        public void map(long recordNum, Record record, TaskContext context)
                throws IOException {
            String str = record.get("datas").toString();
            JsonObject obj = new JsonParser().parse(str).getAsJsonObject();
            Track track = new Gson().fromJson(obj,Track.class);
            TrackCompare trackCompare=new TrackCompare();


                one.setBigint(0,record.getBigint("costsoc"));
                one.setBigint(1,record.getBigint("costtime"));
                one.setBigint(2,record.getBigint("ccode"));
                one.setString(3,record.getString("sid"));
                one.setString(4,track.getELocation());

              //  one.set(5,Double.parseDouble(track.getELocation().split(",")[1]));
                one.setBigint(5,(long)track.getSoc());
                one.setString(6,track.getSPlaceName());
                one.setBigint(7,(long)track.getCostTime());
                one.setString(8,track.getEPlaceName());
                // one.set(9,sdf.parse(sdf.format(track.getETime())));
                one.setBigint(9,track.getETime());
                one.setBigint(10,track.getSTime());
                one.setString(11,track.getSLocation());
               // one.set(13,Double.parseDouble(track.getSLocation().split(",")[1]));
                one.setBigint(12,record.getBigint("gcode"));
                one.setBigint(13,record.getBigint("uid"));
                one.setDouble(14,track.getEnergy());
                word.setString(0,record.getString("sid"));
                //word.setBigint(0,record.getBigint("uid"));
             //   word.set(1,track.getSTime());

                context.write(word,one);

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
            List<TrackCompare> list=new ArrayList();
//            String id=(String)key.get(0);
//            String pid=(String)key.get(1);
            while (values.hasNext()) {
                Record val = values.next();
                TrackCompare tc=new TrackCompare();
                tc.setCostsoc( val.getBigint(0).intValue());
                tc.setCosttime( val.getBigint(1).intValue());
                tc.setCcode( val.getBigint(2).intValue());
                tc.setSid( val.getString(3));
                tc.setELocation(val.getString(4));
                tc.setSoc(val.getBigint(5).intValue());
                tc.setSPlaceName(val.getString(6));
                tc.setCosttime(val.getBigint(7).intValue());
                tc.setEPlaceName(val.getString(8));
                tc.setETime(val.getBigint(9));
                tc.setSTime(val.getBigint(10));
                tc.setSLocation(val.getString(11));
                tc.setGcode(val.getBigint(12));
                tc.setUid(val.getBigint(13));
                tc.setEnergy(val.getDouble(14));
                list.add(tc);

            }
            System.out.println("===================================================");
            Collections.sort(list);
            List<TrackCompare> list1=new ArrayList();
            Map<Long,TrackCompare> map=new HashMap<Long, TrackCompare>();
            for (int i=0;i<list.size()-1;i++){
                System.out.println("uid= "+list.get(i).getUid()  +" stime:"+list.get(i).getSTime()+" etime: "+list.get(i).getETime()+
                        " location:"+list.get(i).getELocation());
                if(list.get(i).getSTime()==list.get(i+1).getSTime()){
                         if(!map.containsKey(list.get(i).getSTime())){
                             map.put(list.get(i).getSTime(),list.get(i));
                         }
                    } else{
                        if(!map.containsKey(list.get(i).getSTime())) {
                             list1.add(list.get(i));
                        }
                        if((i+1==(list.size()-1))&&!map.containsKey(list.get(i+1).getSTime())) {
                             list1.add(list.get(i+1));
                        }

                   }
                }
            Iterator<Map.Entry<Long,TrackCompare>> iterator=map.entrySet().iterator();
            while (iterator.hasNext()){
                    Map.Entry<Long,TrackCompare> longTrackCompareEntry=  iterator.next();
                    list1.add(longTrackCompareEntry.getValue());
                }
            int costTime=0;
            int costSoc=0;
            double costEnergy=0.0;
            for (int i=0;i<list1.size();i++){
                        costTime+=(((list1.get(i).getETime()-list1.get(i).getSTime())/1000));
                        costSoc+=list1.get(i).getSoc();
                        costEnergy+=list1.get(i).getEnergy();

               }
            for (int i=0;i<list1.size();i++){
                result.set(0,costSoc);
                result.set(1,costTime);
                result.set(2,list1.get(i).getCcode());
                result.set(3,list1.get(i).getSid());
                result.set(4,Double.parseDouble(list1.get(i).getELocation().split(",")[0]));
                result.set(5,Double.parseDouble(list1.get(i).getELocation().split(",")[1]));
                result.set(6, list1.get(i).getSoc());
                result.set(7, list1.get(i).getSPlaceName());
                result.set(8,(((list1.get(i).getETime()-list1.get(i).getSTime())/1000)));
                result.set(9, list1.get(i).getEPlaceName());
                try {
                    result.set(10, sdf.parse(sdf.format(new Date(list1.get(i).getETime()))));
                    result.set(11,sdf.parse(sdf.format(new Date(list1.get(i).getSTime()))));

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                result.set(12,Double.parseDouble(list1.get(i).getSLocation().split(",")[0]));
                result.set(13,Double.parseDouble(list1.get(i).getSLocation().split(",")[1]));
                result.set(14,list1.get(i).getCcode());
                result.set(15,list1.get(i).getUid());
                result.set(16,list1.get(i).getEnergy());
                result.set(17,costEnergy);
                context.write(result);
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
        job.setMapperClass(Scooter_track_clearupInfo.TokenizerMapper.class);
        job.setReducerClass(Scooter_track_clearupInfo.SumReducer.class);
        job.setMapOutputKeySchema(SchemaUtils.fromString("uuid:string"));
        job.setMapOutputValueSchema(SchemaUtils.fromString("Costsoc:bigint           ,\n" +
                        "  Costtime:bigint          ,\n" +
                        "  Ccode:bigint             ,\n" +
                        "  Sid:string            ,\n"+
                        "  eLocation:string      ,\n"+
                        "  Soc:bigint               ,\n" +
                        "  SPlaceName:string     ,\n" +
                        "  Costime:bigint          ,\n" +
                        "  EPlaceName:string     ,\n" +
                        "  ETime:bigint             ,\n" +
                        "  STime:bigint             ,\n" +
                        "  sLocation:string      ,\n" +
                        "  Gcode:bigint             ,\n" +
                        "  Uid:bigint               ,\n"+
                        "  Energy:double         "
                    ));
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
