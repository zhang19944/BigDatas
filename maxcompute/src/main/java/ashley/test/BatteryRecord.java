package ashley.test;

import com.aliyun.odps.data.Record;
import com.aliyun.odps.mapred.JobClient;
import com.aliyun.odps.mapred.Mapper;
import com.aliyun.odps.mapred.MapperBase;
import com.aliyun.odps.mapred.conf.JobConf;
import util.ToolUtil;
import java.io.IOException;


/**
 * Created by AshleyZHANG on 2019/8/28.
 */
public class BatteryRecord {

    public static class logTypeMapper extends MapperBase {

        private Record one;
        @Override
        public void setup(Mapper.TaskContext context) throws IOException {
            one = context.createOutputRecord();
        }
        //map方法，先定义要输出的实际表的数据
        @Override
        public void map(long recordNum, Record record, Mapper.TaskContext context)
                throws IOException {

            String bid=record.getString("odps_bid");
            String sn=record.getString("sn");
            String cycle=record.getString("cycle");
            String name=record.getString("name");
            String address=record.getString("address");
            String time=record.getString("time");

            one.setString(0,bid);
            one.setString(1,sn);
            one.setString(2,cycle);
            one.setString(3,time);
            if (name ==null && address !=null){
                one.setString(4,address);
            }else {
                one.setString(4,name);
            }
            context.write(one);
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
        JobClient.runJob(ToolUtil.runJob(args, ashley.test.BatteryRecord.logTypeMapper.class));
        System.out.println("完成");
    }
}