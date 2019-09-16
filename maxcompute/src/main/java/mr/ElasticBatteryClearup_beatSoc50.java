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
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import battery.Battery;
import battery.Datas;
import battery.DateSoc;
import battery.JsonRootBean;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ElasticBatteryClearup_beatSoc50 {




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
                DateSoc dateSoc=new DateSoc(date,soc.intValue());
                list.add(dateSoc);

            }
            Collections.sort(list);
            if(id.equals("10155a27a1b8")){
                for(DateSoc dateSoc:list){
                    System.out.println("startTime "+id+" "+pid+" " +dateSoc.getSoc() +" "+ dateSoc.getDate());
                }
            }
            Date startTime=new  Date();
            Date endTime=new  Date();
            Long socStart=0L;
            Long socEnd=0L;
            int j=0;
            // boolean flag=false;
            //同一个电池在同一个柜子可能充点多次
            for(int m=j;m<list.size();m++) {
                //判断开始充电时间
                boolean flag=false;
                boolean flag1=false;
                for (int i = j; i < list.size(); i++) {
                    if(i+1<list.size()){
                        if (list.get(i).getSoc() != list.get(i + 1).getSoc()&&list.get(i).getSoc()<60) {
                            flag=true;
                            startTime = list.get(i + 1).getDate();
                            socStart=list.get(i + 1).getSoc().longValue();
//			                    if(id.equals("10155a27a1b8")){
//			                    System.out.println("startTime "+id+" "+pid+" " +startTime +" "+ list.get(i + 1).getSoc());
//			                    }
                            j = i + 2;
                            break;
                        }
                    }
                }
                //判断结束充电时间
                if(flag){
                    for (int k = j; k < list.size(); k++) {
                        if (list.get(k).getSoc() >=60) {
                            flag1=true;
                            endTime = list.get(k).getDate();
                            socEnd=list.get(k).getSoc().longValue();
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
                    result.setBigint(2, socStart);
                    result.setBigint(3, socEnd);
                    result.setDatetime(4, startTime);
                    result.setDatetime(5, endTime);
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
        JobConf job = new JobConf();
        job.setMapperClass(TokenizerMapper.class);
        job.setReducerClass(SumReducer.class);
        job.setMapOutputKeySchema(SchemaUtils.fromString("id:string,pid:string"));
        job.setMapOutputValueSchema(SchemaUtils.fromString("id:string,pid:string,time:datetime,soc:bigint"));
        // job.setMapperClass(TokenizerMapper.class);
        InputUtils.addTable(TableInfo.builder().tableName(args[0]).build(), job);
        OutputUtils.addTable(TableInfo.builder().tableName(args[1]).build(), job);
        JobClient.runJob(job);
    }












}
