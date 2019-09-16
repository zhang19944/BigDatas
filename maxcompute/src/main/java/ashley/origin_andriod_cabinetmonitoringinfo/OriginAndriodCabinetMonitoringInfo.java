package ashley.origin_andriod_cabinetmonitoringinfo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.odps.mapred.JobClient;
import com.aliyun.odps.mapred.conf.JobConf;
import com.aliyun.odps.data.Record;
import com.aliyun.odps.mapred.Mapper;
import com.aliyun.odps.mapred.MapperBase;
import util.ToolUtil;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by AshleyZHANG on 2019/8/12.
 */
public class OriginAndriodCabinetMonitoringInfo {
    public static class monitorInfoMapper extends MapperBase{
        private Record one;
        private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        @Override
        public void setup(Mapper.TaskContext context) throws IOException {
            one = context.createOutputRecord();
        }
        @Override
        public void map(long recordNum, Record record, TaskContext context)
                throws IOException {
            Date collecttime = record.getDatetime("collecttime");
            String jsonStr = record.get("info").toString().trim();
            //Object jsonObj= JSON.toJSON(jsonStr);
            //Object jsonParse=JSON.parse(jsonStr);
            //String jsonString=JSON.toJSONString(jsonStr);
            JSONObject jsonObject= JSON.parseObject(jsonStr);

            String pid=jsonObject.getString("pID");

            String heatDatas=jsonObject.getString("heatDatas");
            JSONArray jsonArray=JSON.parseArray(heatDatas);
            // String arrayString=JSONArray.toJSONString(jsonArray);
            //创建对象
            int disableTemper;
            int heatCurrent;
            int heatVoltage;
            int heatPower;
            int temperPMS;
            int temperCharger;
            int temperCabin;
            int ammeterVoltage;
            int ammeterCurrent;
            int ammeterPower;
            int ammeterInstantTotalPower;
            int enableTemper;
            boolean isAuto;
            boolean isEnable;
            boolean isValid;
            long recordTime;
            long meterTime;
            String temperBatteries;
            //计数
            int count =0;
            Iterator<Object> it=jsonArray.iterator();


            while (it.hasNext()){
                JSONObject arrayObj =(JSONObject) it.next();

                heatCurrent= Integer.parseInt(arrayObj.getString("heatCurrent")) ;
                heatVoltage=Integer.parseInt(arrayObj.getString("heatVoltage"));
                heatPower=Integer.parseInt(arrayObj.getString("heatPower"));
                temperPMS=Integer.parseInt(arrayObj.getString("temperPMS"));
                temperBatteries=arrayObj.getString("temperBatteries");
                temperCharger=Integer.parseInt(arrayObj.getString("temperCharger"));
                temperCabin=Integer.parseInt(arrayObj.getString("temperCabin"));
                ammeterVoltage=Integer.parseInt(arrayObj.getString("ammeterVoltage"));
                ammeterCurrent=Integer.parseInt(arrayObj.getString("ammeterCurrent"));
                ammeterPower=Integer.parseInt(arrayObj.getString("ammeterPower"));
                if (arrayObj.toString().contains("ammeterInstantTotalPower")){
                    ammeterInstantTotalPower=Integer.parseInt(arrayObj.getString("ammeterInstantTotalPower"));
                }else{
                    ammeterInstantTotalPower=0;
                }
                enableTemper=Integer.parseInt(arrayObj.getString("enableTemper"));
                disableTemper=Integer.parseInt(arrayObj.getString("disableTemper"));
                isAuto= Boolean.parseBoolean(arrayObj.getString("isAuto"));
                isEnable= Boolean.parseBoolean(arrayObj.getString("isEnable"));
                isValid= Boolean.parseBoolean(arrayObj.getString("isValid"));
                recordTime=Long.parseLong(arrayObj.getString("recordTime"));
                meterTime=Long.parseLong(arrayObj.getString("meterTime"));
                count++;
                one.setString(0,pid);
                one.setDatetime(1,collecttime);
                one.setBigint(2, (long) heatCurrent);
                one.setBigint(3, (long) heatVoltage);
                one.setBigint(4, (long) heatPower);
                one.setBigint(5, (long) temperPMS);
                one.setString(6,temperBatteries);
                one.setBigint(7, (long) temperCharger);
                one.setBigint(8, (long) temperCabin);
                one.setBigint(9, (long) ammeterVoltage);
                one.setBigint(10, (long) ammeterCurrent);
                one.setBigint(11, (long) ammeterPower);
                one.setBigint(12, (long) ammeterInstantTotalPower);
                one.setBigint(13, (long) enableTemper);
                one.setBigint(14, (long) disableTemper);
                one.setBoolean(15,isAuto);
                one.setBoolean(16,isEnable);
                one.setBoolean(17, isValid);
                one.setDatetime(18,new Date(recordTime));
                one.setDatetime(19,new Date(meterTime) );
                context.write(one);
            }
        }
    }
    public static void main(String[] args) throws Exception {
        //擦部署分别用逗号分隔
        if (args.length == 2) {
            System.out.println("开始执行");
        } else {
            System.err.println("MultipleInOut in... out...");
            System.exit(1);
        }
        JobConf job = new JobConf();
        job.setNumReduceTasks(0);
        JobClient.runJob(ToolUtil.runJob(args, OriginAndriodCabinetMonitoringInfo.monitorInfoMapper.class));
        System.out.println("完成");
    }
}
