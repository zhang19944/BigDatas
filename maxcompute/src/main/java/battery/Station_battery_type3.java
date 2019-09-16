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
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import mr.cabinet.CabinetmonitorInfo;
import util.ToolUtil;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Station_battery_type3 {




    public static class TokenizerMapper extends MapperBase {
        private Record word;
        private Record one;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//这个是你要转成后的时间的格式
        SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss");//这个是你要转成后的时间的格式

        @Override
        public void setup(TaskContext context) throws IOException {
           // word = context.createMapOutputKeyRecord();
            one = context.createOutputRecord();
        }

        @Override
        public void map(long recordNum, Record record, TaskContext context)
                throws IOException {
            String jsonString = record.get("content").toString();
            JsonObject obj = new JsonParser().parse(jsonString).getAsJsonObject();
            JsonRootBean jsonRootBean = new Gson().fromJson(obj,JsonRootBean.class);
            String pid=jsonRootBean.getPID();
            System.out.println(pid);
            String time=jsonRootBean.getTime();
            Datas datas=jsonRootBean.getDatas();
            List<Battery> batterys = datas.getBattery();
            if(batterys.size()==0)
                return;
            for(Battery battery:batterys){
                String id = battery.getId();
                Integer soc=battery.getSoc();
                one.setString(0,pid);

                one.setBigint(1,(long)battery.getC());

                one.setBigint(2,(long)battery.getD());

                one.setBigint(3,(long)battery.getF());

                one.setBigint(4,(long)battery.getSoc());

                one.setBigint(5,(long)battery.getNv());

                one.setBigint(6,(long)battery.getCcl());

                one.setBigint(7,(long)battery.getSoh());

                one.setBigint(8,(long)battery.getP());

                one.setBigint(9,(long)battery.getT());

                one.setBigint(10,(long)battery.getNc());

                one.setBigint(11,(long)battery.getV());

                one.setString(12,battery.getId());
                one.setBigint(13,(long)battery.getDc());
                //try {
                    one.setString(14,time);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
                 one.setBigint(15, (long)jsonRootBean.getType());
                one.setBigint(16, (long)jsonRootBean.getVersion());

                context.write( one);

            }



        }
    }




    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: WordCount <in_table> <out_table>");
            System.exit(2);
        }
        JobClient.runJob(ToolUtil.runJob(args,Station_battery_type3.TokenizerMapper.class));
    }












}
