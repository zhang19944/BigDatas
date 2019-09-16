package ashley.mrlogtype;

import ashley.beans.BatteryInfoBean;
import com.aliyun.odps.data.Record;
import com.aliyun.odps.mapred.Mapper;
import com.aliyun.odps.mapred.MapperBase;
import com.aliyun.odps.mapred.Reducer;
import com.aliyun.odps.mapred.ReducerBase;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by AshleyZHANG on 2019/6/25.
 */
public class DuplicateRemove {
    public static class TokenizerMapper extends MapperBase {
        private Record key;
        private Record value;

        @Override
        public void setup(Mapper.TaskContext context) throws IOException {
            key = context.createMapOutputKeyRecord();
            value = context.createMapOutputValueRecord();
        }

        @Override
        public void map(long recordNum, Record record, Mapper.TaskContext context)
                throws IOException {


            key.setBigint(0, (Long) record.get("f"));
            key.set(1, record.get("pid").toString());
            key.set(2, record.get("pid_sn").toString());
            key.setBigint(3, (Long) record.get("p"));
            key.set(4, record.get("id_sn").toString());
            key.setDatetime(5, (Date) record.get("time"));
            key.setBigint(6, (Long) record.get("bv0"));
            key.setBigint(7, (Long) record.get("bv1"));
            key.setBigint(8, (Long) record.get("bv2"));
            key.setBigint(9, (Long) record.get("bv3"));
            key.setBigint(10, (Long) record.get("bv4"));
            key.setBigint(11, (Long) record.get("bv5"));
            key.setBigint(12, (Long) record.get("bv6"));
            key.setBigint(13, (Long) record.get("bv7"));
            key.setBigint(14, (Long) record.get("bv8"));
            key.setBigint(15, (Long) record.get("bv9"));
            key.setBigint(16, (Long) record.get("bv10"));
            key.setBigint(17, (Long) record.get("bv11"));
            key.setBigint(18, (Long) record.get("bv12"));
            key.setBigint(19, (Long) record.get("bv13"));
            key.setBigint(20, (Long) record.get("bv14"));
            key.setBigint(21, (Long) record.get("bv15"));
            key.setBigint(22, (Long) record.get("f_b2"));
            key.setString(23, record.get("id").toString());

            context.write(key, value);
        }
    }


    public static class SumReducer extends ReducerBase {
        private Record result = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//这个是你要转成后的时间的格式

        @Override
        public void setup(Reducer.TaskContext context) throws IOException {
            result = context.createOutputRecord();
        }

        //相同的key进入一个reduce
        @Override
        public void reduce(Record key, Iterator<Record> values, Reducer.TaskContext context)
                throws IOException {
            List<BatteryInfoBean> arraylist = new ArrayList();

            while (values.hasNext()) {


            }

        }
    }
}