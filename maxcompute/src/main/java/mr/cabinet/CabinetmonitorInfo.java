package mr.cabinet;

import battery.Bats;
import battery.ScooterBats;
import bean.cabinet.CabinetMonitor;
import bean.cabinet.HeatDatas;
import com.aliyun.odps.data.Record;
import com.aliyun.odps.mapred.JobClient;
import com.aliyun.odps.mapred.MapperBase;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import mr.scooter.Scooter_battery_heartbeat;
import org.apache.commons.lang.StringUtils;
import util.ToolUtil;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CabinetmonitorInfo {


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
            String jsonString = record.get("info").toString();
            JsonObject obj = new JsonParser().parse(jsonString).getAsJsonObject();
            CabinetMonitor cabinetMonitor = new Gson().fromJson(obj, CabinetMonitor.class);
            List<HeatDatas> heatDatas=cabinetMonitor.getHeatDatas();
            for (int i=0;i<heatDatas.size();i++){
                //if(heatDatas.get(i).getIsValid()){
                   HeatDatas heatDatas1= heatDatas.get(i);
                  //  System.out.println("valid= "+heatDatas.get(i).getIsValid());
                   one.setString(0,cabinetMonitor.getPID());
                   one.setDouble(1,heatDatas1.getHeatCurrent()/10.0);
                   one.setDouble(2,heatDatas1.getHeatVoltage()/10.0);
                   one.setDouble(3,heatDatas1.getHeatPower()/10.0);
                   one.setDouble(4,heatDatas1.getTemperPMS()/10.0);
                   one.setString(5, StringUtils.join(heatDatas1.getTemperBatteries().toArray(),","));
                   one.setDouble(6,heatDatas1.getTemperCharger()/10.0);
                   one.setDouble(7,heatDatas1.getTemperCabin()/10.0);
                   one.setDouble(8,heatDatas1.getAmmeterVoltage()/10.0);
                   one.setDouble(9,heatDatas1.getAmmeterCurrent()/1000.0);
                   one.setBigint(10,(long)heatDatas1.getAmmeterPower());
                   one.setBigint(11,(long)heatDatas1.getEnableTemper());
                   one.setBigint(12,(long)heatDatas1.getDisableTemper());
                   one.setBoolean(13,heatDatas1.getIsAuto());
                   one.setBoolean(14,heatDatas1.getIsEnable());
                   one.setDatetime(15,new Date(heatDatas1.getRecordTime()));
                   one.setDatetime(16,new Date(heatDatas1.getMeterTime()));
                   one.setDatetime(17,record.getDatetime("collecttime"));
                   one.setBoolean(18,heatDatas1.getIsValid());
                 //   System.out.println(heatDatas1.toString());
                    context.write(one);
             //   }
            }



            }

    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: WordCount <in_table> <out_table>");
            System.exit(2);
        }
        JobClient.runJob(ToolUtil.runJob(args,CabinetmonitorInfo.TokenizerMapper.class));

    }
}
