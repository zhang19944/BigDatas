package mr.test;

import com.aliyun.odps.OdpsException;
import com.aliyun.odps.data.Record;
import com.aliyun.odps.mapred.JobClient;
import com.aliyun.odps.mapred.MapperBase;
import com.aliyun.odps.mapred.conf.JobConf;
import com.google.gson.Gson;
import util.ToolUtil;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;


/**
 * Created by AshleyZHANG on 2019/5/7.
 */
public class StationMrLog {

    public static class StationMr extends MapperBase {

        private Record out;

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        @Override
        public void setup(TaskContext context) throws IOException {

            out = context.createOutputRecord();
        }

        @Override
        public void map(long recordNum, Record record, TaskContext context) throws IOException {
            if (record.getBigint("type") != 2)
                return;

            //处理data下的数据
            String jsonString = record.get("datas").toString().trim();
            Gson gson = new Gson();
            StationBean bean = gson.fromJson(jsonString, StationBean.class);
            System.out.println(bean);

            out.setBigint(0, (long) bean.getNum());
            out.setString(1, bean.getpID());
            out.setDouble(2, bean.getLongitude());
            out.setBigint(3, (long) bean.getValid());
            out.setDouble(4, bean.getLatitude());
            out.setBigint(5, (long) bean.getValid48());
            out.setBigint(6, (long) bean.getEmpty());
            try {
                out.setDatetime(7, sdf.parse(record.get("time1").toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
           context.write(out);
        }
    }
    public static void main(String[] args) throws OdpsException {

        if (args.length != 2) {
            System.err.println("参数不正确");
        }
        JobConf job = new JobConf();
        job.setNumReduceTasks(0);
        JobClient.runJob(ToolUtil.runJob(args,StationMr.class));
        System.out.println("finished");
    }
}