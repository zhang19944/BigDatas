package ashley.batteryreplace;

import com.aliyun.odps.OdpsException;
import com.aliyun.odps.data.Record;
import com.aliyun.odps.mapred.JobClient;
import com.aliyun.odps.mapred.Mapper;
import com.aliyun.odps.mapred.MapperBase;
import com.aliyun.odps.mapred.conf.JobConf;
import util.ToolUtil;

import java.io.IOException;

/**
 * Created by AshleyZHANG on 2019/7/10.
 */
public class BatteryReplace {
    public static class batteryMap extends MapperBase {
        private Record out;
        @Override
        public void setup(Mapper.TaskContext context) throws IOException {
            out = context.createOutputRecord();
        }

        @Override
        public void map(long recordNum, Record record, Mapper.TaskContext context) throws IOException {

            String[] inport= record.getString("inport").split(",");
            String[] outport=record.getString("outport").split(",");

            if ( (!inport[0].equals("0")) && inport.length==outport.length) return;

            out.setString(0, record.getString("pid"));
            out.setBigint(1, record.getBigint("surein"));
            out.setBigint(2, record.getBigint("maybein"));
            out.setString(3, record.getString("origin"));
            out.setString(4, record.getString("inport"));
            out.setString(5, record.getString("outport"));
            out.setDatetime(6, record.getDatetime("times"));

            context.write(out);
        }
    }
    public static void main(String[] args) throws OdpsException {

        if (args.length != 2) {
            System.err.println("参数不正确");
        }
        JobConf job = new JobConf();
        job.setNumReduceTasks(0);
        JobClient.runJob(ToolUtil.runJob(args, BatteryReplace.batteryMap.class));
        System.out.println("finished");
    }
}

