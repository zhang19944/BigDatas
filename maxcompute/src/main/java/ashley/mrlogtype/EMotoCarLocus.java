package ashley.mrlogtype;

import ashley.beans.LogListBean;
import ashley.mrlogtype.logtype6;
import com.aliyun.odps.data.Record;
import com.aliyun.odps.mapred.JobClient;
import com.aliyun.odps.mapred.Mapper;
import com.aliyun.odps.mapred.MapperBase;
import com.aliyun.odps.mapred.conf.JobConf;
import util.ToolUtil;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * Created by AshleyZHANG on 2019/6/3.
 */
public class EMotoCarLocus {

    public static class logTypeMapper extends MapperBase {
        private Record one;
        //保留两位小数
        DecimalFormat df = new DecimalFormat("0.00");
        //时间格式化
        private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        @Override
        public void setup(Mapper.TaskContext context) throws IOException {
            one = context.createOutputRecord();
        }

        //map方法
        @Override
        public void map(long recordNum, Record record, Mapper.TaskContext context)
                throws IOException {






        }
    }


    public static void main(String[] args) throws Exception {
        //擦部署分别用逗号分隔
        if (args.length == 3) {
            System.out.println("开始执行");
        } else {
            System.err.println("MultipleInOut in... out...");
            System.exit(1);
        }
        JobConf job = new JobConf();
        job.setNumReduceTasks(0);
        JobClient.runJob(ToolUtil.runJob(args, logtype6.logTypeMapper.class));
        System.out.println("完成");
    }



}
