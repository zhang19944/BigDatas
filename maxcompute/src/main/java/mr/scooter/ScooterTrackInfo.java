package mr.scooter;


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;

import com.aliyun.odps.data.Record;
import com.aliyun.odps.data.TableInfo;
import com.aliyun.odps.mapred.JobClient;
import com.aliyun.odps.mapred.MapperBase;
import com.aliyun.odps.mapred.conf.JobConf;
import com.aliyun.odps.mapred.utils.InputUtils;
import com.aliyun.odps.mapred.utils.OutputUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import scooter.Data;
import scooter.Scooter;

public class ScooterTrackInfo {


    public static class TokenizerMapper extends MapperBase {
        private Record one;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//这个是你要转成后的时间的格式

        @Override
        public void setup(TaskContext context) throws IOException {
            one=context.createOutputRecord();
        }
        private Integer status;
        private Integer costSoc;
        private Integer costTime;
        private Integer gcode;
        private Integer  ccode;
        private String sId;//车Mac地址
        private String  date;//心跳时间



        public  boolean isNumeric(String str){
            Pattern pattern = Pattern.compile("[0-9]*");
            return pattern.matcher(str).matches();
        }
        @Override
        public void map(long recordNum, Record record, TaskContext context)
                throws IOException {
            String str = record.get("content").toString();


            JsonObject obj = new JsonParser().parse(str).getAsJsonObject();
            Scooter scooter = new Gson().fromJson(obj,Scooter.class);

            List<Data> data = scooter.getData();
            for(int i=0;i<data.size();i++){
                one.set(0, scooter.getStatus());
                one.set(1,scooter.getCostSoc());
                one.set(2,scooter.getCostTime());
                one.set(3,Integer.parseInt(scooter.getCcode()));
                one.set(4,scooter.getSID());
                one.set(5,scooter.getDate());

                one.set(6,Double.parseDouble(data.get(i).getELocation().split(",")[0]));
                one.set(7,Double.parseDouble(data.get(i).getELocation().split(",")[1]));
                one.set(8,data.get(i).getSoc());
                one.set(9,data.get(i).getSPlaceName());
                //one.set(9,singleBatInfos[5].split(":")[1].trim().replaceAll("}","").replaceAll("]",""));
                one.set(10,data.get(i).getCostTime());
                one.set(11,data.get(i).getEPlaceName());
                try {
                    one.set(12,sdf.parse(sdf.format(data.get(i).getETime())));
                } catch (NumberFormatException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                try {
                    one.set(13,sdf.parse(sdf.format(data.get(i).getSTime())));
                } catch (NumberFormatException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                // System.out.println(singleDataInfos[7].split(":")[1].split(",")[0].replaceAll("\"", "").trim());
                one.set(14,Double.parseDouble(data.get(i).getSLocation().split(",")[0]));
                one.set(15,Double.parseDouble(data.get(i).getSLocation().split(",")[1]));
                one.set(16,scooter.getGcode());
                one.set(17,scooter.getUid());
                one.set(20,data.get(i).getEnergy());
                one.set(21,scooter.getCostEnergy());
                context.write(one);
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
        job.setMapperClass(ScooterTrackInfo.TokenizerMapper.class);
        job.setNumReduceTasks(0);
      //  job.setReducerClass(EveryRouteSpeedPerkm.SumReducer.class);
//        job.setMapOutputKeySchema(SchemaUtils.fromString("sid:string,statTime:datetime,endTime:datetime"));
//        job.setMapOutputValueSchema(SchemaUtils.fromString("seg:bigint,dist:double,diff:bigint"));
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
//    public static void main(String[] args) throws Exception {
//        if (args.length != 2) {
//            System.err.println("Usage: WordCount <in_table> <out_table>");
//            System.exit(2);
//        }
//        JobConf job = new JobConf();
//        job.setMapperClass(TokenizerMapper.class);
//        // job.setMapOutputValueSchema(SchemaUtils.fromString("hw:double,id:string,sw:string"));
//        InputUtils.addTable(TableInfo.builder().tableName(args[0]).build(), job);
//        OutputUtils.addTable(TableInfo.builder().tableName(args[1]).build(), job);
//        JobClient.runJob(job);
//    }







}

