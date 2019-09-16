package mr.cabinet;

import battery.Battery;
import bean.station_hb3.SortBattery;
import bean.station_hb3.Station_hb3;
import com.aliyun.odps.data.Record;
import com.aliyun.odps.mapred.JobClient;
import com.aliyun.odps.mapred.MapperBase;
import com.aliyun.odps.mapred.ReducerBase;
import com.aliyun.odps.mapred.utils.SchemaUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import util.MapUtil;
import util.ToolUtil;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CabinetPortMonitor {


    public static class TokenizerMapper extends MapperBase {
        private Record key;
        private  Record  value;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//这个是你要转成后的时间的格式
       private Date dateCompare=new Date();
        @Override
        public void setup(TaskContext context) throws IOException {
            key=context.createMapOutputKeyRecord();
            value=context.createMapOutputValueRecord();

        }

        @Override
        public void map(long recordNum, Record record, TaskContext context)
                throws IOException {
            int count= record.getColumnCount();
            if(count==5){
                return;
            }else {
                if(record.getBigint("type")!=3)
                    return;
                String jsonString =record.get("datas").toString().trim();
                JsonObject obj = new JsonParser().parse(jsonString).getAsJsonObject();
                Station_hb3 station_hb3 = new Gson().fromJson(obj, Station_hb3.class);
                try {
                    if ((dateCompare.getTime() - (sdf.parse(record.get("time1").toString())).getTime()) > 60 * 60 * 1000) {
                        return;
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                List<Battery> batteries = station_hb3.getBattery();
                if (batteries.size() == 0)
                    return;
                for (int i = 0; i < batteries.size(); i++) {
                    Battery battery = batteries.get(i);
                    value.setBigint(0, (long) battery.getP());
                    value.setBigint(1, (long) battery.getF());
                    value.setBigint(2, (long) battery.getD());
                    value.setString(3, record.get("pid").toString());
                    try {
                        value.setDatetime(4, sdf.parse(record.get("time1").toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    value.setString(5, battery.getId());
                    key.set(0, record.get("pid").toString());
                    context.write(key, value);
                }

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
            List<SortBattery> list = new ArrayList();
            while (values.hasNext()) {
                Record val = values.next();
                //(Long f, Long d, String pid, Date time1, String id)
                Long p=val.getBigint(0);
                Long f = val.getBigint(1);
                Long d = val.getBigint(2);
                String pid = val.getString(3);
                Date time1 = val.getDatetime(4);
                String id = val.getString(5);
                SortBattery sortBattery = new SortBattery(p,f, d, pid, time1, id);
                list.add(sortBattery);

            }
            Collections.sort(list);
            Date date=list.get(list.size()-1).getTime1();
            for (int i=0;i<list.size();i++){
                if(date.getTime()!=list.get(i).getTime1().getTime()){
                    continue;
                }
                if("568d8c5a06f8c924".equals(list.get(i).getPid())){
                    System.out.println(list.get(i).toString());
                }
                if(list.get(i).getD()!=0){
                    result.setString(0, list.get(i).getPid());
                    result.setString(1, list.get(i).getId());
                    if(list.get(i).getD()==1){
                        result.setBigint(2,0l);//撞击错误不上报
                    }else {
                        result.setBigint(2, list.get(i).getD());//damage
                    }
                    result.setDatetime(3, list.get(i).getTime1());
                    result.setBigint(4, list.get(i).getP());
                    context.write(result);
                }
                if(list.get(i).getF()!=0){
                    result.setString(0, list.get(i).getPid());
                    result.setString(1, list.get(i).getId());
                    result.setBigint(2, list.get(i).getF()+1000);//fault
                    result.setDatetime(3, list.get(i).getTime1());
                    result.setBigint(4, list.get(i).getP());
                    context.write(result);
                }
                if(list.get(i).getD()==0&&list.get(i).getF()==0){
                    result.setString(0, list.get(i).getPid());
                    result.setString(1, list.get(i).getId());
                    result.setBigint(2, 0l);
                    result.setDatetime(3, list.get(i).getTime1());
                    result.setBigint(4, list.get(i).getP());
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
        JobClient.runJob(ToolUtil.runJob(args,CabinetPortMonitor.TokenizerMapper.class,CabinetPortMonitor.SumReducer.class,
                SchemaUtils.fromString("id:string"),SchemaUtils.fromString("p:bigint,f:bigint,d:bigint,pid:string,time1:datetime,id:string")));

    }
}
