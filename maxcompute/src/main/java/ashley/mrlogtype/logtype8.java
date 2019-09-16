package ashley.mrlogtype;

import ashley.beans.LogTypeEigthBean;
import com.aliyun.odps.data.Record;
import com.aliyun.odps.mapred.JobClient;
import com.aliyun.odps.mapred.Mapper;
import com.aliyun.odps.mapred.MapperBase;
import com.aliyun.odps.mapred.conf.JobConf;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import util.ToolUtil;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by AshleyZHANG on 2019/7/1.
 */
public class logtype8 {

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

            String jsonString = record.get("content").toString().trim();
            if (!jsonString.contains("\"type\":8"))   return;
            JsonObject obj = new JsonParser().parse(jsonString).getAsJsonObject();
            System.out.println(obj);
            LogTypeEigthBean bean8=new Gson().fromJson(obj,LogTypeEigthBean.class);
            long type=bean8.getType();
            String pid=bean8.getpId();
            List lists =bean8.getList();


            if ((type==8)) {
                String listsport=String.valueOf(lists);//[1.0,2.0,10.0]
                String str=URLDecoder.decode(listsport,"UTF-8").replaceAll("\\[", "").replaceAll("]","");
                String[] strs = str.split(",");
                List<Integer> list=new ArrayList<Integer>();
                for (int i=0;i<strs.length;i++){
                    double s=Double.parseDouble(strs[i]);
                    int m=(int) s ;
                    list.add(m);
                }

                System.out.println(strs);
                one.setString(0,pid);
                one.setString(1, String.valueOf(list));
                long times = record.getBigint("collecttime");
                one.setDatetime(2, new Date(times));
                one.setString(3, String.valueOf(type));
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
        JobClient.runJob(ToolUtil.runJob(args, logtype8.logTypeMapper.class));
        System.out.println("完成");
    }
}
