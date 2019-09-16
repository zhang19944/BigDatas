package ashley.mrlogtype;

import com.aliyun.odps.OdpsException;
import com.aliyun.odps.data.Record;
import com.aliyun.odps.mapred.JobClient;
import com.aliyun.odps.mapred.Mapper;
import com.aliyun.odps.mapred.MapperBase;
import util.ToolUtil;
import java.io.IOException;
import java.text.SimpleDateFormat;
/**
 * Created by AshleyZHANG on 2019/8/23.
 */
public class CabinetElectricRead {
    public static class cabinetEl extends MapperBase {
        private Record out;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        @Override
        public void setup(Mapper.TaskContext context) throws IOException {
            out=context.createOutputRecord();
        }
        @Override
        public void map(long recordNum, Record record, Mapper.TaskContext context) throws IOException {
            
            String pid=record.getString("pid");
            String pid2=record.getString("pid2");


            double meterrecord;
            if (record.getDouble("meterrecord")==null){
                meterrecord=-1;
            }else{
                meterrecord=record.getDouble("meterrecord");
            }
            double meterrecord2;
            if (record.getDouble("meterrecord2")==null){
                meterrecord2=-1;
            }else {
                meterrecord2=record.getDouble("meterrecord2");
            }
            if (meterrecord2-meterrecord>800 && meterrecord!=-1){
                meterrecord2=-1;
            }

            out.setString(0, pid);
            out.setDouble(1, meterrecord);
            out.setString(2,pid2);
            out.setDouble(3, meterrecord2);

            context.write(out);
        }
    }
    public static void main(String[] args) throws OdpsException {

        if (args.length != 2) {
            System.err.println("参数不正确");
        }
        JobClient.runJob(ToolUtil.runJob(args, CabinetElectricRead.cabinetEl.class));
        System.out.println("finished");
    }
}
