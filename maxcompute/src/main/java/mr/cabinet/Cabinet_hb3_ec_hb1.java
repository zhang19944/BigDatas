package mr.cabinet;

import battery.Bats;
import battery.Battery;
import battery.ScooterBats;
import bean.station_hb3.SortBattery;
import bean.station_hb3.Station_hb3;
import bean.track.TrackEchb;
import com.aliyun.odps.data.Record;
import com.aliyun.odps.mapred.JobClient;
import com.aliyun.odps.mapred.MapperBase;
import com.aliyun.odps.mapred.Reducer;
import com.aliyun.odps.mapred.ReducerBase;
import com.aliyun.odps.mapred.utils.SchemaUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import mr.cabinet.bean.BIDTime;
import util.DistanceUtil;
import util.ListUtil;
import util.StringUtil;
import util.ToolUtil;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Cabinet_hb3_ec_hb1 {



    public static class TokenizerMapper extends MapperBase {

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//这个是你要转成后的时间的格式
        SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");//这个是你要转成后的时间的格式
        private Date dateCompare=new Date();
        private Record key;
        private  Record  value;
        @Override
        public void setup(TaskContext context) throws IOException {
            key=context.createMapOutputKeyRecord();
            value=context.createMapOutputValueRecord();
        }

        @Override
        public void map(long recordNum, Record record, TaskContext context)
                throws IOException {
            int count= record.getColumnCount();
            if(count==14){//t_station
                Long cityCode =record.getBigint("city_code");
                String pid =record.getString("pid");
                double latitude=record.getDouble("latitude");
                double longitude=record.getDouble("longitude");
                key.setString(0,pid);
                value.setString(0,latitude+","+longitude+","+cityCode);
                value.setBigint(1,0l);
                context.write(key,value);
            }else if(count==4){// type=3
                try {
                    if(record.getBigint("type")!=3||new Date().getTime()-sdf.parse(record.getString("time1")).getTime()>3600*100)
                        return;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String jsonString =record.get("datas").toString().trim();
                JsonObject obj = new JsonParser().parse(jsonString).getAsJsonObject();
                Station_hb3 station_hb3 = new Gson().fromJson(obj, Station_hb3.class);

                List<Battery> batteries=station_hb3.getBattery();
                key.setString(0, record.get("pid").toString());
                String batteriess="";
                for (int i=0;i<batteries.size();i++){
                      Battery battery= batteries.get(i);
                      if(i==(batteries.size()-1)){
                          batteriess+=battery.getId();
                      }else {
                          batteriess += (battery.getId() + "|");
                      }

                }
                if(batteries.size()>0){
                        value.setString(0, batteriess+","+record.get("time1").toString());
                        value.setBigint(1,1l);
                        context.write(key,value);
                }
            }else  if(count==8){//ec_hb
                Date time=record.getDatetime("time1");
                if(new Date().getTime()-time.getTime()>3600*1000)
                    return;
                String jsonString ="";
                if(record.get("bats").toString().contains("\"bats\":{")){
                    jsonString = record.get("bats").toString();
                }else{
                    jsonString = "{ \"bats\":"+record.get("bats").toString()+"}";
                }
                JsonObject obj = new JsonParser().parse(jsonString).getAsJsonObject();
                ScooterBats scooterBats = new Gson().fromJson(obj, ScooterBats.class);
                String odps_sid=record.getString("sid");
                String odps_location=record.getString("locations");
                if(Double.parseDouble(odps_location.split(",")[0])==0.0||Double.parseDouble(odps_location.split(",")[1])==0.0)
                    return;
                Double lat= Double.parseDouble(odps_location.split(",")[0]);
                Double lon=Double.parseDouble(odps_location.split(",")[1]);
                key.setString(0,odps_sid);

                if(scooterBats.getBats().size()==1){
                    value.setString(0,lat+","+lon+","+scooterBats.getBats().get(0).getId()+","+sdf.format(time));

                }else if(scooterBats.getBats().size()==2){
                    value.setString(0,lat+","+lon+","+scooterBats.getBats().get(0).getId()+"|"+scooterBats.getBats().get(1).getId()+","+sdf.format(time));

                }
                if(scooterBats.getBats().size()>0) {
                    value.setBigint(1, 2l);
                    context.write(key, value);
                }
            }else if(count==9) {//scooter_iccid
                record.getString("sid");
                record.getBigint("ccode");
                key.setString(0,record.getString("sid"));
                value.setString(0,record.getBigint("ccode")+"");
                value.setBigint(1,3l);
                context.write(key,value);

            }

        }

    }


    public static class SumReducer extends ReducerBase {
        private Record result = null;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//这个是你要转成后的时间的格式
        SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");//这个是你要转成后的时间的格式

        @Override
        public void setup(Reducer.TaskContext context) throws IOException {
            result = context.createOutputRecord();
        }

        @Override
        public void reduce(Record key, Iterator<Record> values, Reducer.TaskContext context)
                throws IOException {

            String keyID=(String)key.get(0);
            List<BIDTime> bidTimes=new ArrayList<BIDTime>();
            String code=0+"";
            String location="";
            while (values.hasNext()) {

                Record val = values.next();
                String value=val.getString(0);

                if(val.getBigint(1)==0)//t_station
                {
                       location =value.split(",")[0]+","+value.split(",")[1];
                       code=value.split(",")[2];
                }else if(val.getBigint(1)==1){//type3
                    String id=value.split(",")[0];
                    try {
                        Date time=sdf.parse(value.split(",")[1]);
                        bidTimes.add(new BIDTime(time,id));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }else if(val.getBigint(1)==2){//type3
                    String id=value.split(",")[0]+","+value.split(",")[1]+","+value.split(",")[2];
                    try {
                        Date time=sdf.parse(value.split(",")[3]);
                        bidTimes.add(new BIDTime(time,id));

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }else if(val.getBigint(1)==3){
                    code=value;
                }


            }
            if(bidTimes.size()>0){
                Collections.sort(bidTimes);

               String bid=bidTimes.get(bidTimes.size()-1).getBid();
               Date date=bidTimes.get(bidTimes.size()-1).getDate();
               if(!bid.contains(",")){//柜子
                     result.setString(0,code);
                     result.setString(1,location);
                     result.setString(2,keyID);
                     result.setString(3,bid);

               }else {//车
                   result.setString(0,code);
                   result.setString(1,bid.split(",")[0]+","+bid.split(",")[1]);
                   result.setString(2,keyID);
                   result.setString(3,bid.split(",")[2]);

               }
               context.write(result);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: WordCount <in_table> <out_table>");
            System.exit(2);
        }
        JobClient.runJob(ToolUtil.runJob(args,Cabinet_hb3_ec_hb1.TokenizerMapper.class,Cabinet_hb3_ec_hb1.SumReducer.class,
                SchemaUtils.fromString("id:string"),SchemaUtils.fromString("id:string,p:bigint")));


    }
}

