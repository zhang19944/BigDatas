package ashley.banport;

import com.aliyun.odps.data.Record;
import com.aliyun.odps.mapred.JobClient;
import com.aliyun.odps.mapred.Mapper;
import com.aliyun.odps.mapred.MapperBase;
import com.aliyun.odps.mapred.conf.JobConf;
import util.ToolUtil;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by AshleyZHANG on 2019/7/2.
 */
public class LogBanPort {
    public static class logTypeMapper extends MapperBase {

        private Record one;
        private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        @Override
        public void setup(Mapper.TaskContext context) throws IOException {
            one = context.createOutputRecord();
        }

        //map方法，先定义要输出的实际表的数据
        @Override
        public void map(long recordNum, Record record, Mapper.TaskContext context)
                throws IOException {

            long id=record.getBigint("id");
            Date collecttime =record.getDatetime("create_time");
            String content=record.getString("content").trim();
            String pid =record.getString("pid");
            long port=record.getBigint("port");
            long status=record.getBigint("status");
            long type =record.getBigint("type");
            if(type!=14){return;}

            String[] ids=content.split("，");
            if(ids.length<2){
                return;
            }

            String[] bids=ids[1].split("=");
            if(bids.length<2){
                return;
            }

            String bid=bids[1];
            one.setBigint(0,id);
            one.setString(1, pid);
            one.setBigint(2,port+1);
            one.setString(3, bid);
            one.setDatetime(4, collecttime);
            one.setBigint(5, status);
            one.setBigint(6,type);
            one.setString(7,content);

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
        JobClient.runJob(ToolUtil.runJob(args, LogBanPort.logTypeMapper.class));
        System.out.println("完成");
    }
}