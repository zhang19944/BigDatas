package mr.scooter;

import battery.Bats;
import battery.ScooterBats;
import bean.track.Track;
import bean.track.TrackCompare;
import bean.track.TrackEchb;
import com.aliyun.odps.data.Record;
import com.aliyun.odps.data.TableInfo;
import com.aliyun.odps.mapred.*;
import com.aliyun.odps.mapred.conf.JobConf;
import com.aliyun.odps.mapred.utils.InputUtils;
import com.aliyun.odps.mapred.utils.OutputUtils;
import com.aliyun.odps.mapred.utils.SchemaUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import util.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class Scooter_track_from_ec_hb {


    public static class TokenizerMapper extends MapperBase {
        private Record one;
        private Record word;
        private Date dateCompare=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//这个是你要转成后的时间的格式

        @Override
        public void setup(Mapper.TaskContext context) throws IOException {
            one=context.createOutputRecord();
            word=context.createMapOutputKeyRecord();
        }

        public  boolean isNumeric(String str){
            Pattern pattern = Pattern.compile("[0-9]*");
            return pattern.matcher(str).matches();
        }
        @Override
        public void map(long recordNum, Record record, Mapper.TaskContext context)
                throws IOException {
            Date time=record.getDatetime("time1");

            String jsonString = "{ \"bats\":"+record.get("bats").toString()+"}";
            JsonObject obj = new JsonParser().parse(jsonString).getAsJsonObject();
            ScooterBats scooterBats = new Gson().fromJson(obj, ScooterBats.class);
            String odps_sid=record.getString("sid");
            String odps_location=record.getString("locations");
            Long currents=record.getBigint("currents");
            Long socs=record.getBigint("soc");
            List<Bats> list=scooterBats.getBats();
            word.setString(0, odps_sid);
            one.setDatetime(0,time );
            one.setString(1,jsonString);
            one.setDouble(2, Double.parseDouble(odps_location.split(",")[0]));
            one.setDouble(3,Double.parseDouble(odps_location.split(",")[1]));
            one.setBigint(4,currents);
            one.setBigint(5,socs);
            context.write(word,one);
        }

    }



    public static class SumReducer extends ReducerBase {
        private Record result = null;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//这个是你要转成后的时间的格式
        @Override
        public void setup(Reducer.TaskContext context) throws IOException {
            result = context.createOutputRecord();
        }

        @Override
        public void reduce(Record key, Iterator<Record> values, Reducer.TaskContext context)
                throws IOException {
            List<TrackEchb> list=new ArrayList();
            String sid=(String)key.get(0);
           // System.out.println("sid="+sid);
            while (values.hasNext()) {

                Record val = values.next();
                if(val.getDouble(3)==0.0||val.getDouble(3)==0.0){
                    continue;
                }
                TrackEchb tc=new TrackEchb();
                JsonObject obj = new JsonParser().parse(val.get(1).toString()).getAsJsonObject();
                ScooterBats scooterBats = new Gson().fromJson(obj, ScooterBats.class);
                tc.setSid( sid);
                tc.setList( scooterBats.getBats());
                tc.setTime(val.getDatetime(0));
                tc.setLatitude(val.getDouble(3));
                tc.setLongitude(val.getDouble(2));
                tc.setCurrents(val.getBigint(4));
                tc.setSum_soc(val.getBigint(5));
                list.add(tc);

            }
            Collections.sort(list);
            int count=0;
            int total=0;

            List<TrackEchb> list1=new ArrayList();
            TrackEchb trackEchb_first=new TrackEchb();

            if(sid.equals("574C546C750A")){

                for(int k=0;k<list.size()-1;k++){
                    String bid="";
                    String soc="";
                    for(int n=0;n<list.get(k).getList().size();n++){
                        bid+=list.get(k).getList().get(n).getId()+" ";
                        soc+=String.valueOf(list.get(k).getList().get(n).getSoc())+", ";
                    }


                        System.out.println("574C546C750A: "+sdf.format(list.get(k).getTime())+" "+list.get(k).getList().size()
                                +" "+(list.get(k+1).getTime().getTime()-list.get(k).getTime().getTime())+" "
                                +soc
                                +" "+bid);


                }
            }
            TrackEchb trackEchb_sencod;
            Map<TrackEchb,TrackEchb> map=new HashMap<TrackEchb, TrackEchb>();
          //  if(m<list.size()-1){
            int m=0;
            boolean flag=false;
           // boolean flag2=false;
              for(int i=m;i<list.size();i++){
                  total=0;
                  count=0;
                  Date first=new Date(new Date().getTime()+1000*24*3600);
                  Date second=new Date(new Date().getTime()+1000*24*3600);
                 //电池总数
                  for(int k=m;k<list.size()-1;k++){
                      if(list.get(k).getList().size()==list.get(k+1).getList().size()&&list.get(k).getList().size()!=0&&(list.get(k).getTime().getTime()-list.get(k+1).getTime().getTime())<=5*60*1000){
                          if(first.compareTo(new Date())>0){
                              first=list.get(k).getTime();
                          }
                          count++;
                          if(count>=6){
                              second=list.get(k).getTime();
                          }
                      }else{
                          first=new Date(new Date().getTime()+1000*24*3600);
                          second=new Date(new Date().getTime()+1000*24*3600);
                          count=0;
                      }
                      if(count>=6&&((second.getTime()-first.getTime())>=5*60*1000)){
                          total=list.get(k).getList().size();
                          break;
                      }

                  }
                  if(total==0)
                      break;
                  flag=false;
                  //先循环期初电池
                  for(int j=m;j<list.size();j++){
                      //&&list.get(j).getLatitude()!=0.0&&list.get(j).getLongitude()!=0.0
                      if(list.get(j).getList().size()==total) {
                          trackEchb_first = list.get(j);
                          if(trackEchb_first.getSid().equals("574C546C750A")){
                              System.out.println("574C546C750A first: "+sdf.format(trackEchb_first.getTime())+" ");
                          }
                          m=j;
                          flag=true;
                          break;
                      }
                  }

                  //结束位置
                  if(flag){
                  for(int k=m;k<list.size()-1;k++){
                      if(total==1){

                      if(((list.get(k+1).getTime().getTime()-list.get(k).getTime().getTime())>(5*60*1000l)&&list.get(k).getList().size()==total)||
                              (list.get(k).getList().size()==total&&list.get(k+1).getList().size()!=total)
                              ||((k==list.size()-2)&&list.get(k).getList().size()==total)
                              ||(list.get(k).getList().size()==total&&list.get(k).getList().get(0).getId().equals(trackEchb_first.getList().get(0).getId())
                                 &&list.get(k+1).getList().size()==total&&!list.get(k+1).getList().get(0).getId().equals(trackEchb_first.getList().get(0).getId())   )){
                          trackEchb_sencod=list.get(k);
                          m=k+1;

                          map.put(trackEchb_first,trackEchb_sencod);
                          break;
                      }
                      }
                      if(total==2){
                          if((((list.get(k+1).getTime().getTime()-list.get(k).getTime().getTime())>(5*60*1000l)&&list.get(k).getList().size()==total)||
                                  (list.get(k).getList().size()==total&&list.get(k+1).getList().size()!=total)
                                  ||((k==list.size()-2)&&list.get(k).getList().size()==total)
                                  ||(list.get(k).getList().size()==total&&list.get(k).getList().get(0).getId().equals(trackEchb_first.getList().get(0).getId())
                                   &&list.get(k).getList().get(1).getId().equals(trackEchb_first.getList().get(1).getId())
                                  &&list.get(k+1).getList().size()==total&&(!list.get(k+1).getList().get(0).getId().equals(trackEchb_first.getList().get(0).getId())||
                                     !list.get(k+1).getList().get(1).getId().equals(trackEchb_first.getList().get(1).getId()) )  ))){
                              trackEchb_sencod=list.get(k);
                              m=k+1;

                              map.put(trackEchb_first,trackEchb_sencod);
                              break;
                          }
                      }
                  }
                  }

              }


                for (Map.Entry<TrackEchb,TrackEchb> entry:
                        map.entrySet()) {
                  if(entry.getKey().getSid().equals("574C546C750A")){
                      System.out.println("574C546C750A: "+sdf.format(entry.getKey().getTime())+" "+sdf.format(entry.getValue().getTime().getTime())+" ");
                  }
                    if((entry.getKey().getTime().getTime()+5*60*1000)<entry.getValue().getTime().getTime()){
                        TrackEchb sTrackEchb=entry.getKey();

                        TrackEchb eTrackEchb=entry.getValue();
                        //耗时
                        long costTime=(eTrackEchb.getTime().getTime()-sTrackEchb.getTime().getTime())/1000;

                        //开始时间
                        Date sDate=sTrackEchb.getTime();
                        //结束时间
                        Date eDate=eTrackEchb.getTime();
                        //车的sid
                        String sid1=sTrackEchb.getSid();
                        //开始经度
                        Double sLat=sTrackEchb.getLatitude();
                        //结束经度
                        Double eLat=eTrackEchb.getLatitude();
                        //开始纬度
                        Double sLon=sTrackEchb.getLongitude();
                        //结束纬度
                        Double eLon=eTrackEchb.getLongitude();
                        //开始电流
                        Long  sCurrent=sTrackEchb.getCurrents();
                        //结束电流
                        Long  eCurrent=sTrackEchb.getCurrents();
                        //开始地点


                        String sAddr="";
                        int sVoltage0=0;
                        int eVoltage0=0;
                        int sSoc0=0;
                        int eSoc0=0;
                        int sVoltage1=0;
                        int eVoltage1=0;
                        int sSoc1=0;
                        int eSoc1=0;
                        List<Integer> list2=new ArrayList<Integer>();
                        if(sTrackEchb.getList().size()>1){
                           list2= ListUtil.isAvail(list,sTrackEchb.getTime(),eTrackEchb.getTime(),2);
                        }else{
                            list2= ListUtil.isAvail(list,sTrackEchb.getTime(),eTrackEchb.getTime(),1);
                        }

                        if(sTrackEchb.getList().size()>0){
                            sAddr= sTrackEchb.getList().get(0).getId();
                            sVoltage0=sTrackEchb.getList().get(0).getVoltage();
                            eVoltage0=eTrackEchb.getList().get(0).getVoltage();
                            if (list2==null||list2.get(0)==null||StringUtil.isEmpty(list2.get(0))){
                                sSoc0=sTrackEchb.getList().get(0).getSoc();
                            }else {
                                sSoc0 = list2.get(0);
                            }
                            eSoc0=eTrackEchb.getList().get(0).getSoc();
                        }
                        String eAddr ="";
                        if(sTrackEchb.getList().size()>1) {
                             eAddr = sTrackEchb.getList().get(1).getId();//GeoUtil.getAddrFromLngLat(eLon.toString(),eLat.toString());
                            sVoltage1=sTrackEchb.getList().get(1).getVoltage();
                            eVoltage1=eTrackEchb.getList().get(1).getVoltage();
                            if ( list2==null||list2.get(1)==null||StringUtil.isEmpty(list2.get(1))){
                                sSoc1=sTrackEchb.getList().get(1).getSoc();;
                            }else{
                                sSoc1 = list2.get(1);
                            }

                            eSoc1=eTrackEchb.getList().get(1).getSoc();
                        }

                        String sBid="";
                        String eBid="";

                        //耗电量
                        int totalSoc=(sSoc0+sSoc1-eSoc0-eSoc1);
                        if(totalSoc<0&&totalSoc>-5){
                            totalSoc=0;
                        }
                        double enery=totalSoc/20000.0;
                        //距离
                        double totalDistance=0;
                        for (int y=0;y<list.size()-1;y++){
                            if((list.get(y).getTime().compareTo(sDate)>=0)
                                    &&(list.get(y+1).getTime().compareTo(eDate)<=0)) {

                                totalDistance += DistanceUtil.LantitudeLongitudeDist(list.get(y).getLongitude(), list.get(y).getLatitude(), list.get(y + 1).getLongitude(), list.get(y + 1).getLatitude());
                            }
                        }




                        result.setBigint(0,costTime);
                        result.setDatetime(1,sDate);
                        result.setDatetime(2,eDate);
                        result.setString(3,sid1);
                        result.setDouble(4,sLat);
                        result.setDouble(5,sLon);
                        result.setDouble(6,eLat);
                        result.setDouble(7,eLon);
                        result.setBigint(8,Long.valueOf(totalSoc));
                        result.setString(9,sAddr);
                        result.setString(10,eAddr);
                        result.setBigint(11,Long.valueOf(sSoc0));
                        result.setBigint(12,Long.valueOf(eSoc0));
                        result.setBigint(13,Long.valueOf(sSoc1));
                        result.setBigint(14,Long.valueOf(eSoc1));
                        result.setBigint(15,Long.valueOf(sVoltage0));
                        result.setBigint(16,Long.valueOf(eVoltage0));
                        result.setBigint(17,Long.valueOf(sVoltage1));
                        result.setBigint(18,Long.valueOf(eVoltage1));
                        result.setDouble(19,Double.valueOf(enery));
                        result.setDouble(20,Double.valueOf(totalDistance));
                        result.setBigint(21,sCurrent);
                        result.setBigint(22,eCurrent);
                        context.write(result);

                    }
                }
         //   }










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
        job.setMapperClass(Scooter_track_from_ec_hb.TokenizerMapper.class);
        job.setReducerClass(Scooter_track_from_ec_hb.SumReducer.class);
        job.setMapOutputKeySchema(SchemaUtils.fromString("uuid:string"));

      /*  one.set(2, Double.parseDouble(odps_location.split(",")[0]));
            one.set(3,Double.parseDouble(odps_location.split(",")[1]));

            List<Bats> list=scooterBats.getBats();
            word.setString(0, odps_sid);
            one.set(0,time );
            one.set(1,list);*/
        job.setMapOutputValueSchema(SchemaUtils.fromString("time:datetime           ,\n" +
                "  obj:string          ,\n" +
                "  lat:double             ,\n" +
                "  lon:double            ,\n"+
                "  sCur:bigint            ,\n"+
                "  eCur:bigint            ,\n"+
                "socs:bigint"
        ));
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
