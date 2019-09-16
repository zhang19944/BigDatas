package mr;

import battery.DateSoc;
import com.aliyun.odps.data.Record;
import com.aliyun.odps.data.TableInfo;
import com.aliyun.odps.mapred.JobClient;
import com.aliyun.odps.mapred.MapperBase;
import com.aliyun.odps.mapred.ReducerBase;
import com.aliyun.odps.mapred.conf.JobConf;
import com.aliyun.odps.mapred.utils.InputUtils;
import com.aliyun.odps.mapred.utils.OutputUtils;
import com.aliyun.odps.mapred.utils.SchemaUtils;
import temperature.Temperature;


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class Maxcompute_tables_batteries_temperature {

    public static class TokenizerMapper extends MapperBase {
        private Record word;
        private  Record one;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//这个是你要转成后的时间的格式
        @Override
        public void setup(TaskContext context) throws IOException {
           // word = context.	createOutputKeyRecord();
            word = context.createMapOutputKeyRecord();
            one = context.createMapOutputValueRecord();
            //one=context.createOutputRecord();
//            word = context.createMapOutputKeyRecord();
//            one = context.createMapOutputValueRecord();
        }
        @Override
        public void map(long recordNum, Record record, TaskContext context)
                throws IOException {
            int count= record.getColumnCount();
            if(count<166){


            if(record.get("odps_pid")!=null&&record.get("odps_create_time")!=null&&record.get("odps_b_in")!=null){
                word.set(0,(String)record.getString("odps_b_in"));
                word.set(1,(String)record.getString("odps_pid"));
                one.setString(0,sdf.format(record.getDatetime("odps_create_time")));
                one.setString(1,1000+"");//in
                context.write(word, one);
            }
            if(record.get("odps_pid")!=null&&record.get("odps_create_time")!=null&&record.get("odps_b_out")!=null){
                word.set(0,record.getString("odps_b_out"));
                word.set(1,record.getString("odps_pid"));
                //one.setDatetime(0,record.getDatetime("odps_create_time"));
                //one.setDouble(1,2000.0);//out
                one.setString(0,sdf.format(record.getDatetime("odps_create_time")));
                one.setString(1,2000+"");//out
                context.write(word, one);
            }
            }else{
            if(record.get("id")!=null&&record.get("pid")!=null&&record.get("collecttime")!=null&&record.get("btemp1")!=null){
                word.set(0,record.getString("id"));
                word.set(1,record.getString("pid"));
                one.setString(0,sdf.format(record.getDatetime("collecttime")));
                one.setString(1,record.getDouble("btemp1").toString());

                context.write(word,one);

            }
            }

        }
    }
    public static class SumReducer extends ReducerBase {
        private Record result;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//这个是你要转成后的时间的格式

        @Override
        public void setup(TaskContext context) throws IOException {
            // 对于不同的输出需要创建不同的record，通过label来区分
            result = context.createOutputRecord();
//      result1 = context.createOutputRecord("out1");
//      result2 = context.createOutputRecord("out2");
        }
        @Override
        public void reduce(Record key, Iterator<Record> values, TaskContext context)
                throws IOException {
            if(key.get(0)==null||key.get(1)==null){
                return;
            }
            String bid=(String)key.get(0);
            String pid=(String)key.get(1);
            List<DateSoc> listDate=new ArrayList<DateSoc>();
            List<Temperature> tempratures=new ArrayList<Temperature>();
            while(values.hasNext()){
                Record record=values.next();
//                System.out.println(record.getDouble(1));
//                if(((Double)record.getDouble(1)==1000) ||((Double)record.getDouble(1)==2000.0)){
//                    listDate.add(new DateSoc(record.getDatetime(0), record.getDouble(1)));//relation表
//                }else{
//                    tempratures.add(new Temperature((Date)record.getDatetime(0),(Double)record.getDouble(1)));//temperature表
//                }

                try {

                if(record.getString(1).equals("1000") ||(record.getString(1).equals("2000"))){

                        listDate.add(new DateSoc(sdf.parse(record.getString(0)),Integer.parseInt(record.getString(1))));//relation表

                }else{
                    tempratures.add(new Temperature(sdf.parse(record.getString(0)),Double.parseDouble(record.getString(1))));//temperature表
                }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
            Collections.sort(listDate);
            Collections.sort(tempratures);
//          for(int z=0;z<tempratures.size();z++){
//              System.out.println("tempratures="+key.get(0)+" "+key.get(1)+" "+tempratures.get(z).getDate()+" "+tempratures.get(z).getTemperature());
//          }
       //     System.out.println("===================================");
            int k=0;
            boolean flag=false;

            for(int i=0;i<listDate.size();i++){
//                if(key.get(0).equals("1008fc493eee")&&key.get(1).equals("b1f3e35a0370cb24")){
//                    System.out.println("hahhha i= "+i);
//                }
              //  System.out.println(listDate.size());
                if(listDate.get(i).getSoc()==1000){//循环进电池
                    Date dateIn= listDate.get(i).getDate();
                    Double tempIn=0.0;
                    Date dateTempIn=new Date();
                   // System.out.println(key.get(0)+" "+key.get(1));
                    for(int n=k;n<tempratures.size();n++){//电池实时表数据，找出进电池温度
                        Date dateTemp1=tempratures.get(n).getDate();
                        if(dateIn.compareTo(dateTemp1)<0){
                            k=n+1;
                            tempIn=tempratures.get(n).getTemperature();
                            dateTempIn=dateTemp1;
                             System.out.println(key.get(0)+" "+key.get(1));

//                            if(key.get(0).equals("1008fc493eee")&&key.get(1).equals("b1f3e35a0370cb24")){
//                                System.out.println("n= "+n);
//                            }


                            flag=true;
                            //      System.out.println("1out="+dateTempIn+" "+tempIn);
                            break;
                        }

                    }


                    for(int j=i+1;j<listDate.size()&&flag;j++){//循环出电池
                        if(listDate.get(j).getSoc()==2000){
                            Date dateOut= listDate.get(j).getDate();
                            Double tempOut=0.0;
                            for(int m=k;m<tempratures.size()-1;m++){//电池实时表数据，找出出电池温度
                                Date dateTemp1=tempratures.get(m).getDate();
                                Date dateTemp2=tempratures.get(m+1).getDate();
                                if(dateOut.compareTo(dateTemp2)>0&&(m==tempratures.size()-2)){
                                    k=m+1;
                                    i=j;
                                    tempOut=tempratures.get(m+1).getTemperature();
                                    result.set(0,(String)key.get(1));
                                    result.set(1,(String)key.get(0));
                                    result.set(2,dateTempIn);//in_time
                                    result.set(3,dateTemp2);//out_time
                                    result.set(4,tempIn);//in_temp
                                    result.set(5,tempOut);//out_temp
//                                    if(key.get(0).equals("1008fc493eee")&&key.get(1).equals("b1f3e35a0370cb24")) {
//                                        System.out.println("outM1= " +m+" " + (String) key.get(1) + " " + (String) key.get(0) + " " + dateTempIn + " " + dateTemp2 + " " + tempIn + " " + tempOut);
//                                    }
                                    flag=false;
                                    context.write(result);
                                    break;
                                }else if(dateOut.compareTo(dateTemp1)>0&&dateOut.compareTo(dateTemp2)<0){
                                    k=m+1;
                                    i=j;
                                    tempOut=tempratures.get(m).getTemperature();
                                    result.set(0,(String)key.get(1));
                                    result.set(1,(String)key.get(0));
                                    result.set(2,dateTempIn);//in_time
                                    result.set(3,dateTemp1);//out_time
                                    result.set(4,tempIn);//in_temp
                                    result.set(5,tempOut);//out_temp
//                                    if(key.get(0).equals("1008fc493eee")&&key.get(1).equals("b1f3e35a0370cb24")) {
//                                        System.out.println("outM2= " +m+" " +  (String) key.get(1) + " " + (String) key.get(0) + " " + dateTempIn + " " + dateTemp2 + " " + tempIn + " " + tempOut);
//                                    }
                                    flag=false;
                                    context.write(result);
                                    break;
                                }

                            }

                        }
                    }
                }
            }
//    	for(int m=0;m<listDate.size();m++){
//    		Date dateRelation=listDate.get(m);
//    		for(int n=0;n<tempratures.size()-1;n++){
//    			Date dateTemp1=tempratures.get(n).getDate();
//    			Date dateTemp2=tempratures.get(n+1).getDate();
//    			if(dateRelation.compareTo(dateTemp1)>0&&dateRelation.compareTo(dateTemp2)<0){
//
//    			}
//    		}
//    	}

//      long count = 0;
//      while (values.hasNext()) {
//        Record val = values.next();
//        count += (Long) val.get(0);
//      }
//      long mod = count % 3;
//      if (mod == 0) {
//        result.set(0, key.get(0));
//        result.set(1, count);
//        //不指定label，输出的默认(default)输出
//        context.write(result);
//      } else if (mod == 1) {
//        result1.set(0, key.get(0));
//        result1.set(1, count);
//        context.write(result1, "out1");
//      } else {
//        result2.set(0, key.get(0));
//        result2.set(1, count);
//        context.write(result2, "out2");
//      }
        }
//    @Override
//    public void cleanup(TaskContext context) throws IOException {
//      Record result = context.createOutputRecord();
//      result.set(0, "default");
//      result.set(1, 1L);
//      context.write(result);
//      Record result1 = context.createOutputRecord("out1");
//      result1.set(0, "out1");
//      result1.set(1, 1L);
//      context.write(result1, "out1");
//      Record result2 = context.createOutputRecord("out2");
//      result2.set(0, "out2");
//      result2.set(1, 1L);
//      context.write(result2, "out2");
//    }
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
            System.out.println(inputs[0]+" "+inputs[1]);
            outputs = args[1].split(",");
            System.out.println(outputs[0]);
        } else {
            System.err.println("MultipleInOut in... out...");
            System.exit(1);
        }
        JobConf job = new JobConf();
        job.setMapperClass(TokenizerMapper.class);
        job.setReducerClass(SumReducer.class);
        job.setMapOutputKeySchema(SchemaUtils.fromString("id:string,pid:string"));
        job.setMapOutputValueSchema(SchemaUtils.fromString("pid:string,bid:string,in_time:datetime,out_time:datetime,temperature_in:double,temperaure_out:double"));
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
