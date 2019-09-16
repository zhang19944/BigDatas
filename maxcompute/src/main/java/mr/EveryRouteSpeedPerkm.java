package mr;

import com.aliyun.odps.data.Record;
import com.aliyun.odps.data.TableInfo;
import com.aliyun.odps.mapred.JobClient;
import com.aliyun.odps.mapred.MapperBase;
import com.aliyun.odps.mapred.ReducerBase;
import com.aliyun.odps.mapred.conf.JobConf;
import com.aliyun.odps.mapred.utils.InputUtils;
import com.aliyun.odps.mapred.utils.OutputUtils;
import com.aliyun.odps.mapred.utils.SchemaUtils;
import bean.distance.SegDistance;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class EveryRouteSpeedPerkm {
    public static class TokenizerMapper extends MapperBase {
        private Record word;
        private  Record one;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//这个是你要转成后的时间的格式
        @Override
        public void setup(TaskContext context) throws IOException {
            word = context.createMapOutputKeyRecord();
            one = context.createMapOutputValueRecord();
        }
        @Override
        public void map(long recordNum, Record record, TaskContext context)
                throws IOException {
                 if(record.getDouble("dist")==null||record.getBigint("diff")==0||((record.getDouble("dist"))/record.getBigint("diff"))>20.0)
                     return;
                 String sid=record.getString("sid");
                 Date  startTime=record.getDatetime("starttime");
                 Date  endTime=record.getDatetime("endtime");
                 Long seg=record.getBigint("seg");
                 Double dist=record.getDouble("dist");
//                 Date heartStartTime=record.getDatetime("time_from");
//                 Date heartEndTime=record.getDatetime("time_to");
                 Long diff=  record.getBigint("diff");
                 word.setString(0,sid);
                 word.setDatetime(1,startTime);
                 word.setDatetime(2,endTime);
                 one.setBigint(0,seg);
                 one.setDouble(1,dist);
//                 one.setDatetime(2,heartStartTime);
//                 one.setDatetime(3,heartEndTime);
                 one.setBigint(2,diff);
            context.write(word, one);

            }


    }
    public static class SumReducer extends ReducerBase {
        private Record result;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//这个是你要转成后的时间的格式

        @Override
        public void setup(TaskContext context) throws IOException {
            // 对于不同的输出需要创建不同的record，通过label来区分
            result = context.createOutputRecord();
        }
        public  double formatDouble1(double d) {
            return (double)Math.round(d*100)/100;
        }
        @Override
        public void reduce(Record key, Iterator<Record> values, TaskContext context)
                throws IOException {
            if(key.get(0)==null||key.get(1)==null){
                return;
            }
            String sid=key.getString(0);
            Date startTime=key.getDatetime(1);
            Date endTime=key.getDatetime(2);
            List<SegDistance> list=new ArrayList<SegDistance>();
            List<Double> speeds=new ArrayList<Double>();
            while(values.hasNext()){
                Record record=values.next();
                list.add(new SegDistance(record.getDouble(1),record.getBigint(0),record.getBigint(2)));

            }
            Collections.sort(list);
            Double distance=0.0;
            long costTime=0;
            for(int i=0;i<list.size();i++){

               distance+=list.get(i).getDist();
               costTime+=list.get(i).getDiff();
                if(distance>1000l){
                    if(key.getString(0).equals("574C5417EF01")){
                        System.out.println(distance+" "+costTime);
                    }
                    speeds.add((distance*3600)/(costTime*1000));
                    distance=0.0;
                    costTime=0l;
                }
            }
            result.set(0,key.get(0));
            result.set(1,key.get(1));
            result.set(2,key.get(2));
            StringBuffer sb=new StringBuffer();
            for (int i=0;i<speeds.size();i++){
                if(i==speeds.size()-1){
                    sb.append(formatDouble1(speeds.get(i)));
                }else {
                    sb.append(formatDouble1(speeds.get(i)) + ",");
                }
            }
            result.set(3,sb.toString());
          //  System.out.println(sb.toString());
            context.write(result);





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
        job.setMapperClass(EveryRouteSpeedPerkm.TokenizerMapper.class);
        job.setReducerClass(EveryRouteSpeedPerkm.SumReducer.class);
        job.setMapOutputKeySchema(SchemaUtils.fromString("sid:string,statTime:datetime,endTime:datetime"));
        job.setMapOutputValueSchema(SchemaUtils.fromString("seg:bigint,dist:double,diff:bigint"));
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
