package mr.cabinet;

import battery.Battery;
import battery.DayTime;
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

public class CabinetHBBetweenTime {

        public static class TokenizerMapper extends MapperBase {
            private Record key;
            private Record value;
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            @Override
            public void setup(TaskContext context) throws IOException {
                key= context.createMapOutputKeyRecord();
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

                key.setString(0, record.get("pid").toString());

                    try {
                        value.setDatetime(0,sdf.parse(record.get("time1").toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    context.write(key,value);
                }




            }


        public static class SumReducer extends ReducerBase {
            private Record result = null;
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            @Override
            public void setup(TaskContext context) throws IOException {
                result = context.createOutputRecord();
            }

            @Override
            public void reduce(Record key, Iterator<Record> values, TaskContext context)
                    throws IOException {
                List<DayTime> list=new ArrayList<DayTime>();
                // String id=(String)key.get(0);
                String pid=(String)key.get(0);
                while (values.hasNext()) {
                    Record val = values.next();
                    Date date=(Date)val.get(0);
                    DayTime dayTime=new DayTime(date);
                    list.add(dayTime);

                }
                Collections.sort(list);


                for(int m=0;m<list.size();m++){
                    if(m==0){
                        try {
                            if((   ((DayTime)list.get(m)).getDay().getTime()- sdf.parse((sdf.format(((DayTime)list.get(m)).getDay()).substring(0,sdf.format(((DayTime)list.get(m)).getDay()).indexOf(" "))+ " 00:00:00")).getTime()
                            )>=60*1000){
                                result.setString(0,pid);
                                result.setDatetime(2, ((DayTime)list.get(m)).getDay());
                                result.setDatetime(1, sdf.parse((sdf.format(((DayTime)list.get(m)).getDay()).substring(0,sdf.format(((DayTime)list.get(m)).getDay()).indexOf(" "))+ " 00:00:00"))
                                );
                                result.setDouble(3,
                                        (   ((DayTime)list.get(m)).getDay().getTime()- sdf.parse((sdf.format(((DayTime)list.get(m)).getDay()).substring(0,sdf.format(((DayTime)list.get(m)).getDay()).indexOf(" "))+ " 00:00:00")).getTime()
                                        )/(1000.0));
                                context.write(result);
                            }
                        } catch (ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    if(m==(list.size()-1)){
                        try {
                            //4*60*60*1000  60*60*
                            if((    sdf.parse((sdf.format(((DayTime)list.get(m)).getDay()).substring(0,sdf.format(((DayTime)list.get(m)).getDay()).indexOf(" "))+ " 23:59:59")).getTime() -
                                    ((DayTime)list.get(m)).getDay().getTime())>=60*1000){
                                result.setString(0,pid);
                                result.setDatetime(1, ((DayTime)list.get(m)).getDay());
                                result.setDatetime(2, sdf.parse((sdf.format(((DayTime)list.get(m)).getDay()).substring(0,sdf.format(((DayTime)list.get(m)).getDay()).indexOf(" "))+ " 23:59:59"))
                                );
                                result.setDouble(3,
                                        (    sdf.parse((sdf.format(((DayTime)list.get(m)).getDay()).substring(0,sdf.format(((DayTime)list.get(m)).getDay()).indexOf(" "))+ " 23:59:59")).getTime() -
                                                ((DayTime)list.get(m)).getDay().getTime())/(1000.0));
                                context.write(result);
                            }
                        } catch (ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }else{
                        if((((DayTime)list.get(m+1)).getDay().getTime() - ((DayTime)list.get(m)).getDay().getTime())>=60*1000){
                            result.setString(0,pid);
                            result.setDatetime(1, ((DayTime)list.get(m)).getDay());
                            result.setDatetime(2, ((DayTime)list.get(m+1)).getDay());
                            result.setDouble(3,
                                    (((DayTime)list.get(m+1)).getDay().getTime() -
                                            ((DayTime)list.get(m)).getDay().getTime())/(1000.0));
                            context.write(result);
                        }
                    }

                }





            }
        }




        public static void main(String[] args) throws Exception {
            if (args.length != 2) {
                System.err.println("Usage: WordCount <in_table> <out_table>");
                System.exit(2);
            }
            JobClient.runJob(ToolUtil.runJob(args,CabinetHBBetweenTime.TokenizerMapper.class,CabinetHBBetweenTime.SumReducer.class,
                    SchemaUtils.fromString("pid:string"),SchemaUtils.fromString("time:datetime")));

        }













    }

