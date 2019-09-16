package mr.cabinet;

import battery.Battery;
import bean.station_hb3.Station_hb3;
import com.aliyun.odps.data.Record;
import com.aliyun.odps.mapred.JobClient;
import com.aliyun.odps.mapred.MapperBase;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import util.ToolUtil;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Cabinet_hb3 {



    public static class TokenizerMapper extends MapperBase {
        private Record out;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//这个是你要转成后的时间的格式
        SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");//这个是你要转成后的时间的格式
        private Date dateCompare=new Date();
        @Override
        public void setup(TaskContext context) throws IOException {
            out=context.createOutputRecord();
           // one=context.createOutputRecord();

        }

        @Override
        public void map(long recordNum, Record record, TaskContext context)
                throws IOException {
            if(record.getBigint("type")!=3)
                return;
            String jsonString =record.get("datas").toString().trim();

            JsonObject obj = new JsonParser().parse(jsonString).getAsJsonObject();
            Station_hb3 station_hb3 = new Gson().fromJson(obj, Station_hb3.class);

            System.out.println(station_hb3);
            List<Battery> batteries=station_hb3.getBattery();
            if( record.get("pid").toString().equals("009d8c5a06f8c924")){
                try {
                    System.out.println(record.get("time1").toString()+" "+
                            sdf1.parse(record.get("time1").toString())
                    );
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

            for (int i=0;i<batteries.size();i++){
                Battery battery= batteries.get(i);
                out.setString(0, record.get("pid").toString());
                out.setBigint(1,(long) battery.getC());
                out.setBigint(2,(long) battery.getD());
                out.setBigint(3,(long) battery.getF());
                out.setBigint(4,(long) battery.getSoc());
                out.setBigint(5,(long) battery.getNv());
                out.setBigint(6,(long) battery.getCcl());
                out.setBigint(7,(long) battery.getSoh());
                out.setBigint(8,(long) battery.getP());
                out.setBigint(9,(long) battery.getT());
                out.setBigint(10,(long) battery.getNc());
                out.setBigint(11,(long) battery.getV());
                out.setString(12, battery.getId());
                out.setBigint(13,(long) battery.getDc());

                try {
                    out.setDatetime(14,sdf1.parse(record.get("time1").toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
               // out.setBigint(15, (Long)record.get("version"));

                out.setBigint(16,(long) battery.getBs());
                out.setBigint(17,(long) battery.getBt2());
                out.setBigint(18,(long) battery.getBv0());
                out.setBigint(19,(long) battery.getBv1());
                out.setBigint(20,(long) battery.getBv2());
                out.setBigint(21,(long) battery.getBv3());
                out.setBigint(22,(long) battery.getBv4());
                out.setBigint(23,(long) battery.getBv5());
                out.setBigint(24,(long) battery.getBv6());
                out.setBigint(25,(long) battery.getBv7());
                out.setBigint(26,(long) battery.getBv8());
                out.setBigint(27,(long) battery.getBv9());
                out.setBigint(28,(long) battery.getBv10());
                out.setBigint(29,(long) battery.getBv11());
                out.setBigint(30,(long) battery.getBv12());
                out.setBigint(31,(long) battery.getBv13());
                out.setBigint(32,(long) battery.getBv14());
                out.setBigint(33,(long) battery.getBv15());
                out.setBigint(34,(long) battery.getCmt());
                out.setBigint(35,(long) battery.getCt());
                out.setBigint(36,(long) battery.getDf1());
                out.setBigint(37,(long) battery.getDf2());
                out.setBigint(38,(long) battery.getFgt());
                out.setBigint(39,(long) battery.getOf1());
                out.setBigint(40,(long) battery.getOf2());

                context.write(out);
            }

        }

    }




    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: WordCount <in_table> <out_table>");
            System.exit(2);
        }
        JobClient.runJob(ToolUtil.runJob(args,Cabinet_hb3.TokenizerMapper.class));

    }
}

