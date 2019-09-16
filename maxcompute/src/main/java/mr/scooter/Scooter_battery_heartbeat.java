package mr.scooter;

import battery.Bats;
import battery.ScooterBats;
import com.aliyun.odps.data.Record;
import com.aliyun.odps.mapred.JobClient;
import com.aliyun.odps.mapred.MapperBase;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import scooter.Scooter;
import util.ToolUtil;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Scooter_battery_heartbeat {


    public static class TokenizerMapper extends MapperBase {
        private Record one;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//这个是你要转成后的时间的格式

        @Override
        public void setup(TaskContext context) throws IOException {
            one=context.createOutputRecord();
        }

        @Override
        public void map(long recordNum, Record record, TaskContext context)
                throws IOException {
            String jsonString ="";
            if(record.get("bats").toString().contains("\"bats\":{")){
                jsonString = record.get("bats").toString();
            }else{
                jsonString = "{ \"bats\":"+record.get("bats").toString()+"}";
            }

            JsonObject obj = new JsonParser().parse(jsonString).getAsJsonObject();
            ScooterBats scooterBats = new Gson().fromJson(obj, ScooterBats.class);

            int odps_avg_soc= record.getBigint("soc").intValue();
            String odps_sid=record.getString("sid");
            int odps_maxopc=record.getBigint("maxopc").intValue();
            int odps_sum_current=record.getBigint("currents").intValue();
            String odps_location=record.getString("locations");
            Date time=record.getDatetime("time1");
            int odps_entirety_voltage=record.getBigint("voltage").intValue();

            List<Bats> list=scooterBats.getBats();
            if(list.size()==0)
                return;
            for(int i=0;i<list.size();i++){
                int odps_soc=list.get(i).getSoc();
                int odps_temperature= list.get(i).getTemperature();
                int odps_current= list.get(i).getCurrent();
                int odps_voltage=list.get(i).getVoltage();
                String odps_id=list.get(i).getId();
                int odps_cycle=list.get(i).getCycle();
                one.set(0, odps_avg_soc);
                one.setString(1, odps_sid);
                one.set(2, odps_maxopc);
                one.set(3, odps_sum_current);
                one.set(4, odps_soc);
                one.set(5,odps_temperature);
                one.set(6, odps_current);
                one.set(7, odps_voltage);
                one.setString(8, odps_id);
                one.set(9,  odps_cycle+"");
                one.set(10, Double.parseDouble(odps_location.split(",")[0]));
                one.set(11,Double.parseDouble(odps_location.split(",")[1]));
                one.setDatetime(12, time);
                one.set(13, odps_entirety_voltage);
                one.set(14,list.get(i).getNominalCurrent());
                one.set(15, list.get(i).getNominalVoltage());
                one.set(16, list.get(i).getPortNumber());
                one.set(17, list.get(i).getCapacity());
                if(null!=list.get(i).getFault()){
                    for(int k=0;k<list.get(i).getFault().length();k++){
                        one.set(k+18,Integer.parseInt(list.get(i).getFault().substring(k, k+1)));
                    }
                }
                if(null!=list.get(i).getDamage()){
                    for(int k=0;k<list.get(i).getDamage().length();k++){
                        one.set(k+26,Integer.parseInt(list.get(i).getDamage().substring(k, k+1)));
                    }
                }
                context.write(one);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: WordCount <in_table> <out_table>");
            System.exit(2);
        }
        JobClient.runJob(ToolUtil.runJob(args,Scooter_battery_heartbeat.TokenizerMapper.class));

    }
}
