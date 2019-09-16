package ashley.mrlogtype;

import ashley.AshleyDateUtils;
import ashley.beans.SystemUserBean;
import com.aliyun.odps.data.Record;
import com.aliyun.odps.mapred.*;
import com.aliyun.odps.mapred.utils.SchemaUtils;
import util.ToolUtil;
import java.io.IOException;
import java.util.*;

/**
 * Created by AshleyZHANG on 2019/6/27.
 */


/**
 * Created by AshleyZHANG on 2019/5/15.
 */
public class SystemUserRecord {
    public static class systemMapper extends MapperBase {

        private Record value;
        private Record key;

        @Override
        public void setup(Mapper.TaskContext context) throws IOException {

            value = context.createMapOutputValueRecord();
            key =context.createMapOutputKeyRecord();
        }

        //map方法，先定义要输出的实际表的数据
        @Override
        public void map(long recordNum, Record record, Mapper.TaskContext context)
                throws IOException {

            //日志时间
            key.setString(0,record.getString("user_id"));
            key.setString(1,record.getString("sn"));
            value.setString(0,record.getString("content"));
            value.setString(1,record.getString("create_time"));
            value.setString(2,record.getString("type"));

            context.write(key,value);
            }

    }

    public static class SumReducer extends ReducerBase {

        private Record result = null;
        AshleyDateUtils ashleyDateUtils = new AshleyDateUtils();

        @Override
        public void setup(Reducer.TaskContext context) throws IOException {
            result = context.createOutputRecord();

        }

        //相同的key进入一个reduce
        @Override
        public void reduce(Record key, Iterator<Record> values, Reducer.TaskContext context)
                throws IOException {
            String user_id = key.getString(0);
            String sn=key.getString(1);
            List<SystemUserBean> arraylist = new ArrayList<SystemUserBean>();
            while (values.hasNext()) {
                Record record = values.next();
                SystemUserBean systemUserBean = new SystemUserBean();

                systemUserBean.setContent(record.getString(0));
                systemUserBean.setCreate_time(record.getString(1));
                systemUserBean.setType(record.getString(2));

                arraylist.add(systemUserBean);
            }
            //按照create-time排序
            Collections.sort(arraylist);
            String[] str = {"登录", "退出登录"};
            int temp = 0;



            for (int i = 0; i < arraylist.size() - 1; i++) {
            /*    if (arraylist.get(i).getCreate_time() == arraylist.get(i + 1).getCreate_time()) {
                    return;
                }*/
                String string = arraylist.get(i).getContent();
                if (string.equals(str[temp])) {
                    String time1="";
                    String time2="";
                    //记录时间的
                    if (temp == 0) {
                        time1 = arraylist.get(i).getCreate_time();
                    }
                    if (temp == 1) {
                        time2 = arraylist.get(i).getCreate_time();
                    }

                    result.setString(0, user_id);
                    result.setString(1, sn);
                    result.setString(2, arraylist.get(i).getContent());
                    result.setString(3, arraylist.get(i).getCreate_time());
                    result.setString(4, time1);
                    result.setString(5, time2);
                    result.setString(6, arraylist.get(i).getType());
                    context.write(result);
                    temp++;
                    }
                if (temp > 1) {
                    temp = 0;
                }
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
        JobClient.runJob(ToolUtil.runJob(args, SystemUserRecord.systemMapper.class,SystemUserRecord.SumReducer.class,
                SchemaUtils.fromString("user_id:string,sn:string"),
                SchemaUtils.fromString("content:string,create_time:string,type:string")));
    }
}

