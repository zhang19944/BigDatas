package battery;

import com.aliyun.odps.data.Record;
import com.aliyun.odps.data.TableInfo;
import com.aliyun.odps.mapred.JobClient;
import com.aliyun.odps.mapred.MapperBase;
import com.aliyun.odps.mapred.ReducerBase;
import com.aliyun.odps.mapred.conf.JobConf;
import com.aliyun.odps.mapred.utils.InputUtils;
import com.aliyun.odps.mapred.utils.OutputUtils;
import com.aliyun.odps.mapred.utils.SchemaUtils;
import util.DateUtil;

import java.io.IOException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Battery_cycle_soh {

    //Mapper类
        public static class TokenizerMapper extends MapperBase {

            private Record key;
            private Record value;

            private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            @Override
            public void setup(TaskContext context) throws IOException {
                value=context.createMapOutputValueRecord();
                key= context.createMapOutputKeyRecord();
            }

            @Override
            public void map(long recordNum, Record record, TaskContext context)
                    throws IOException {
                int count= record.getColumnCount();

                  //电池id
                  String id = record.get("id").toString();
                  //电池组荷电状态
                  double soc = (Double) record.get("soc");
                  //满冲电压
                  double tvolt = ((Long) record.get("bvolt1"))/1000.0;
                  //健康状态
                  double soh = (Double) record.get("soh");
                  //循环次数
                  long cycle = (Long) record.get("cycle");
                  //充满容量
                  long  fcap=(Long)record.get("fcap");
                  //10s最大允许充电电流
                  double csop=(Double) record.get("csop");
                  //搜集时间
                  Date collecttime = (Date) record.get("collecttime");
                  //电量计温度
                  double fuelt=(Double) record.get("fuelt");

                  String pt=record.get("pt").toString();


                  key.setString(0, id);
                  value.setDouble(0, tvolt);
                  value.setDouble(1, soc);
                  value.setDouble(2, soh);
                  value.setBigint(3, cycle);
                  value.setDatetime(4, collecttime);
                  value.setBigint(5, fcap);
                  value.setDouble(6, csop);
                  value.setDouble(7,fuelt);
                  value.setString(8,pt);
                //  value.setString(6,"sn");

                  context.write(key, value);

            }
               // word.set(0,record.get("id").toString());
        }


  //reducer类
        public static class SumReducer extends ReducerBase {
            private Record result;
            private Record result1;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//这个是你要转成后的时间的格式

            @Override
            public void setup(TaskContext context) throws IOException {
                // 对于不同的输出需要创建不同的record，通过label来区分
                result = context.createOutputRecord();
                //    result1=context.createOutputRecord("error");
            }

            public double formatDouble1(double d) {
                return (double) Math.round(d * 100) / 100;
            }

            @Override
            public void reduce(Record key, Iterator<Record> values, TaskContext context)
                    throws IOException {
                if (key.get(0) == null) {
                    return;
                }
                String id = key.getString(0);
                List<HardBatteryRealTimePartOnlyData> list = new ArrayList<HardBatteryRealTimePartOnlyData>();
                List<Double> speeds = new ArrayList<Double>();
                while (values.hasNext()) {
                    Record record = values.next();
                    HardBatteryRealTimePartOnlyData hardBatteryRealTimePartOnlyData = new HardBatteryRealTimePartOnlyData();

                  /*
                  *    key.setString(0,id);
                      value.setDouble(0,tvolt);
                       value.setDouble(1,soc);
                     value.setDouble(2,soh);
                     value.setBigint(3,cycle);
                     value.setDatetime(4,collecttime);

                  * */
                    hardBatteryRealTimePartOnlyData.setBid(key.getString(0));
                    hardBatteryRealTimePartOnlyData.setTvolt(record.getDouble(0));
                    hardBatteryRealTimePartOnlyData.setSoc(record.getDouble(1));
                    hardBatteryRealTimePartOnlyData.setSoh(record.getDouble(2));
                    hardBatteryRealTimePartOnlyData.setCycle(record.getBigint(3));
                    hardBatteryRealTimePartOnlyData.setCollecttime(record.getDatetime(4));
                    hardBatteryRealTimePartOnlyData.setFcap(record.getBigint(5));
                    hardBatteryRealTimePartOnlyData.setCsop(record.getDouble(6));
                    hardBatteryRealTimePartOnlyData.setFuelt(record.getDouble(7));

                    list.add(hardBatteryRealTimePartOnlyData);

                }
                Collections.sort(list);
//                if ("101c1d5b49f4".equals(id)) {
//                    for (int m = 0; m < list.size(); m++) {
//                        System.out.println("origin=" + list.get(m).getCycle() + " " + list.get(m).getCollecttime());
//                    }
//                }
                HardBatteryRealTimePartOnlyData[] array = list.toArray(new HardBatteryRealTimePartOnlyData[list.size()]);
                for (int n = 0; n < array.length; n++) {
                    //遍历数组，判断找出需删除元素的位置
                    //System.out.println(array[n].getSoc());
                    if (array.length >= 3 && n == array.length - 2 && array[n].getCycle() != -1) {
                        if ((array[n].getCycle() != array[n - 1].getCycle() || array[n].getCycle() != array[n + 1].getCycle()) && (
                                array[n].getCycle() != array[n - 1].getCycle() || array[n].getCycle() != array[n - 2].getCycle()
                        )) {

                            for (int k = n; k < array.length - 1; k++) {
                                array[k] = array[k + 1];
                            }
                            //数组最后一个元素赋为控制
                            HardBatteryRealTimePartOnlyData hardBatteryRealTimePartOnlyData = new HardBatteryRealTimePartOnlyData();
                            hardBatteryRealTimePartOnlyData.setCycle(-1);
                            array[array.length - 1] = hardBatteryRealTimePartOnlyData;
                            n--;

                        }
                    } else if (array.length >= 3 && n == array.length - 1 && array[n].getCycle() != -1) {
                        if ((
                                array[n].getCycle() != array[n - 1].getCycle() || array[n].getCycle() != array[n - 2].getCycle()
                        )) {


                            //数组最后一个元素赋为控制
                            HardBatteryRealTimePartOnlyData hardBatteryRealTimePartOnlyData = new HardBatteryRealTimePartOnlyData();
                            hardBatteryRealTimePartOnlyData.setCycle(-1);
                            array[array.length - 1] = hardBatteryRealTimePartOnlyData;
                            n--;

                        }

                    } else if (array[n].getCycle() != -1 && array.length >= 3) {
                        if (array[n].getCycle() != array[n + 1].getCycle() || array[n].getCycle() != array[n + 2].getCycle()) {
                            if (n == 1) {
                                if (array[n].getCycle() != array[n - 1].getCycle() || array[n].getCycle() != array[n + 1].getCycle()) {
                                    for (int k = n; k < array.length - 1; k++) {
                                        array[k] = array[k + 1];
                                    }
                                    //数组最后一个元素赋为控制
                                    HardBatteryRealTimePartOnlyData hardBatteryRealTimePartOnlyData = new HardBatteryRealTimePartOnlyData();
                                    hardBatteryRealTimePartOnlyData.setCycle(-1);
                                    array[array.length - 1] = hardBatteryRealTimePartOnlyData;
                                    n--;
                                }
                            } else if (n > 1) {
                                if ((array[n].getCycle() != array[n - 1].getCycle() || array[n].getCycle() != array[n - 2].getCycle()) &&
                                        (array[n].getCycle() != array[n - 1].getCycle() || array[n].getCycle() != array[n + 1].getCycle())) {
                                    for (int k = n; k < array.length - 1; k++) {
                                        array[k] = array[k + 1];
                                    }
                                    //数组最后一个元素赋为控制
                                    HardBatteryRealTimePartOnlyData hardBatteryRealTimePartOnlyData = new HardBatteryRealTimePartOnlyData();
                                    hardBatteryRealTimePartOnlyData.setCycle(-1);
                                    array[array.length - 1] = hardBatteryRealTimePartOnlyData;
                                    n--;
                                }
                            } else {
                                //把需删除元素后面的元素依次覆盖前面的元素
                                for (int k = n; k < array.length - 1; k++) {
                                    array[k] = array[k + 1];
                                }
                                //数组最后一个元素赋为控制
                                HardBatteryRealTimePartOnlyData hardBatteryRealTimePartOnlyData = new HardBatteryRealTimePartOnlyData();
                                hardBatteryRealTimePartOnlyData.setCycle(-1);
                                array[array.length - 1] = hardBatteryRealTimePartOnlyData;
                                n--;
                            }
                        }
                    }
                }

                if ("101c1d5b49f4".equals(id)) {
                    for (int m = 0; m < array.length; m++) {
                        System.out.println("array=" + array[m].getCycle() + " " + array[m].getCollecttime());
                    }
                }
                List list2 = Arrays.asList(array);
                List<HardBatteryRealTimePartOnlyData> hardBatteryRealTimePartOnlyDataList = new ArrayList(list2);
                Iterator<HardBatteryRealTimePartOnlyData> hardBatteryRealTimePartOnlyDataIterator = hardBatteryRealTimePartOnlyDataList.iterator();
                while (hardBatteryRealTimePartOnlyDataIterator.hasNext()) {
                    HardBatteryRealTimePartOnlyData hardBatteryRealTimePartOnlyData = hardBatteryRealTimePartOnlyDataIterator.next();
                    if (hardBatteryRealTimePartOnlyData.getCycle() == -1) {
                        hardBatteryRealTimePartOnlyDataIterator.remove();
                    }
                }
                String flagPT="";
                for (int j = 0; j < hardBatteryRealTimePartOnlyDataList.size() - 1; j++) {
                    Double tvolt1 = hardBatteryRealTimePartOnlyDataList.get(j).getTvolt();
                    Double soc1 = hardBatteryRealTimePartOnlyDataList.get(j).getSoc();
                    Double csop1=hardBatteryRealTimePartOnlyDataList.get(j).getCsop();
                    Double tvolt2 = hardBatteryRealTimePartOnlyDataList.get(j + 1).getTvolt();
                    Double soc2 = hardBatteryRealTimePartOnlyDataList.get(j + 1).getSoc();
                    Long cycle = hardBatteryRealTimePartOnlyDataList.get(j + 1).getCycle();
                    Date collectTime = hardBatteryRealTimePartOnlyDataList.get(j + 1).getCollecttime();
                    Double soh = hardBatteryRealTimePartOnlyDataList.get(j + 1).getSoh();
                    Long  fcap=hardBatteryRealTimePartOnlyDataList.get(j+1).getFcap();
                    Double temp1=hardBatteryRealTimePartOnlyDataList.get(j).getFuelt();
                    Double temp2=hardBatteryRealTimePartOnlyDataList.get(j+1).getFuelt();
                    String pt1=hardBatteryRealTimePartOnlyDataList.get(j).getPt();

                    if(temp1>=23.0) {
                        try {
                            if(flagPT!=null&&pt1.equals( DateUtil.findPreviousOrAfterDays(flagPT,1)))//如果温度小于18度后一天直接过滤{}
                            {
                                continue;
                            }
                        } catch (ParseException e) {
                           // e.printStackTrace();
                        }
                        //65.6,0.23  66.4,0.23  4.15  0.015  0.03
//                        if ((tvolt1 > 65.37 && tvolt1 < 65.83 && soc1 >= 99 && soc2 <= 55 && csop1 < 1.5) ||
//                                (soc1 == 100 && tvolt1 > 65.25 && tvolt1 < 65.95 && csop1 < 0.7 && soc2 <= 55)) {
                        if (collectTime.getTime()/1000<1565107200){
                            if ((tvolt1 >4.135  && tvolt1 <4.165  && soc1 >= 99 && soc2 <= 55 && csop1 < 1.5) ||
                                    (soc1 == 100 && tvolt1 > 4.12 && tvolt1 < 4.18  && soc2 <= 55)) { //&& csop1 < 0.7
                   /*         if ((tvolt1 >4.135  && tvolt1 <4.165  && soc1 >= 99 && soc2 <= 55 && csop1 < 1.5) ||
                                    (soc1 == 100 && tvolt1 > 4.12 && tvolt1 < 4.18  && soc2 <= 55)) {*/
                                if(temp2<23.0){
                                    continue;
                                }
                                result.setString(0, hardBatteryRealTimePartOnlyDataList.get(j).getBid());
                                result.setDouble(1, soh);
                                result.setBigint(2, cycle);
                                result.setDatetime(3, collectTime);
                                result.setBigint(4, fcap);
                                result.setDouble(5, temp1);
                                result.setDouble(6, temp2);
                                context.write(result);
                            }
                        }
                        if (collectTime.getTime()/1000>=1565107200) {

                            if ((tvolt1 > 4.085 && tvolt1 < 4.115 && soc1 >= 99 && soc2 <= 55 && csop1 < 1.5) ||
                                    (soc1 == 100 && tvolt1 > 4.07 && tvolt1 < 4.13 && soc2 <= 55)) { //&& csop1 < 0.7
                   /*         if ((tvolt1 >4.135  && tvolt1 <4.165  && soc1 >= 99 && soc2 <= 55 && csop1 < 1.5) ||
                                    (soc1 == 100 && tvolt1 > 4.12 && tvolt1 < 4.18  && soc2 <= 55)) {*/
                                if (temp2 < 23.0) {
                                    continue;
                                }
                                result.setString(0, hardBatteryRealTimePartOnlyDataList.get(j).getBid());
                                result.setDouble(1, soh);
                                result.setBigint(2, cycle);
                                result.setDatetime(3, collectTime);
                                result.setBigint(4, fcap);
                                result.setDouble(5, temp1);
                                result.setDouble(6, temp2);
                                context.write(result);
                            }
                        }
                    }else{
                        flagPT=pt1;
                        continue;
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


  //Driver类

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
            job.setMapperClass(battery.Battery_cycle_soh.TokenizerMapper.class);
            job.setReducerClass(battery.Battery_cycle_soh.SumReducer.class);
            job.setMapOutputKeySchema(SchemaUtils.fromString("id:string"));

                  /*
                  *    key.setString(0,id);
                      value.setDouble(0,tvolt);
                       value.setDouble(1,soc);
                     value.setDouble(2,soh);
                     value.setBigint(3,cycle);
                     value.setDatetime(4,collecttime);
                  * */
            job.setMapOutputValueSchema(SchemaUtils.fromString(
            "tvolt:double,soc:double,soh:double,cycle:bigint,collecttime:datetime,fcap:bigint,csop:double,fuelt:double,pt:string"
            ));
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

