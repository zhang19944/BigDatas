package mr;

import com.aliyun.odps.data.Record;
import com.aliyun.odps.data.TableInfo;
import com.aliyun.odps.mapred.JobClient;
import com.aliyun.odps.mapred.MapperBase;
import com.aliyun.odps.mapred.ReducerBase;
import com.aliyun.odps.mapred.conf.JobConf;
import com.aliyun.odps.mapred.utils.InputUtils;
import com.aliyun.odps.mapred.utils.OutputUtils;
import util.DateUtil;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class Android_cabinetRecordClearup {





    public static class TokenizerMapper extends MapperBase {
        private Record one;
        private String output;//解析value值  value={[a1,b1,c1,d1],…}   a统计仓号，b空仓时间，c充电时间，d，满电不充时间
        private String[] values;//value值进行分割  a统计仓号，b空仓时间，c充电时间，d，满电不充时间
        private String pid; //柜子id
        private Date stopTime;  //柜子结束统计时间
        private Date  startTime;//柜子开始统计时间
        private String incodeInput;//编码格式的value值
        private Double  meterRecord;  //电表读数
        private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        private  LinkedHashMap<String,String> ptMap=new LinkedHashMap();
        private  String pt;

        @Override
        public void setup(TaskContext context) throws IOException {
            one=context.createOutputRecord();
            TableInfo tableInfo=context.getInputTableInfo();
            ptMap=tableInfo.getPartSpec();
            for (Map.Entry<String,String> map:
                ptMap.entrySet() ) {
                pt=map.getValue();
                System.out.println(pt);
            }
        }

        @Override
        public void map(long recordNum, Record record, TaskContext context)
                throws IOException {
            stopTime=(Date)record.get("endtime");
            pid=record.get("pid").toString();
            startTime=(Date)record.get("starttime");
            meterRecord=Double.parseDouble(record.get("meterrecord").toString());
            one.set(0, pid);

              //  if(meterRecord==-1.0||!DateUtil.SDF.format(stopTime).equals(DateUtil.findPreviousOrAfterDays(sdf.format(new Date()), -1))){
                if(meterRecord==-1.0||!DateUtil.SDF.format(stopTime).equals(pt)){
                    return;
                }

            one.set(2,stopTime);
            one.set(3,meterRecord);
            context.write(one);
        }
    }


    public static class SumReducer_refer extends ReducerBase {
        private Record result = null;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//这个是你要转成后的时间的格式
        @Override
        public void setup(TaskContext context) throws IOException {
            result = context.createOutputRecord();
        }

        @Override
        public void reduce(Record key, Iterator<Record> values, TaskContext context)
                throws IOException {

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
        job.setMapperClass(TokenizerMapper.class);
        job.setNumReduceTasks(0);
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
        //job.setMapOutputValueSchema(SchemaUtils.fromString("startSoc:string,onceFull:string,stopTime:string,id:string,pid:string,startTime:string,stopSoc:string"));
      //  InputUtils.addTable(TableInfo.builder().tableName(args[0]).build(), job);
      //  OutputUtils.addTable(TableInfo.builder().tableName(args[1]).build(), job);

        JobClient.runJob(job);
    }









}
