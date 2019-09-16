package ashley.mrlogtype;

import ashley.beans.partRunControlDataBean;
import com.aliyun.odps.data.Record;
import com.aliyun.odps.mapred.JobClient;
import com.aliyun.odps.mapred.Mapper;
import com.aliyun.odps.mapred.MapperBase;
import com.aliyun.odps.mapred.conf.JobConf;
import com.google.gson.Gson;
import util.ToolUtil;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by AshleyZHANG on 2019/5/20.
 */
public class logtype4 {
    public static class logTypeMapper extends MapperBase {

        private Record four;
        partRunControlDataBean bean4 = new partRunControlDataBean();

        private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        @Override
        public void setup(Mapper.TaskContext context) throws IOException {
            four = context.createOutputRecord();
        }
        public static boolean isNumeric(String str){
            Pattern pattern = Pattern.compile("[0-9]*");
            return pattern.matcher(str).matches();
        }
        //map方法，先定义要输出的实际表的数据
        @Override
        public void map(long recordNum, Record record, Mapper.TaskContext context)
                throws IOException {

            String jsonStrings = record.get("content").toString().trim();

            Gson gson = new Gson();
            bean4 = gson.fromJson(jsonStrings, partRunControlDataBean.class);


            if ((!(bean4.getType() == 4)) && jsonStrings.contains("list") ) {
                return;
            }

            //处理每一个字符串为单个字段
            String str1 = bean4.getPartRunControlData();

            if (str1==null){
                return;
            }
                String str1to = URLDecoder.decode(str1, "UTF-8").replaceAll("\\[", "").replaceAll("]", "");
                String[] str4array = str1to.split(",");

            if (str4array.length != 54)
                return;
            String s0 = Integer.toBinaryString(Integer.parseInt(str4array[0] + str4array[1], 16));//BMS状态
            for (int i = 0; i < s0.length(); i++) {
                four.set(i, Integer.parseInt(s0.substring(s0.length() - 1 - i, s0.length() - i)));
            }
            if (s0.length() < 16) {
                for (int j = 0; j < 16 - s0.length(); j++) {
                    four.set(s0.length() + j, 0);
                }
            }
            String bms = String.valueOf(Integer.parseInt(str4array[2], 16)) +
                    String.valueOf(Integer.parseInt(str4array[3], 16)) +
                    String.valueOf(Integer.parseInt(str4array[4], 16)) +
                    String.valueOf(Integer.parseInt(str4array[5], 16)) +
                    String.valueOf(Integer.parseInt(str4array[6], 16)) +
                    String.valueOf(Integer.parseInt(str4array[7], 16)) +
                    String.valueOf(Integer.parseInt(str4array[8], 16)) +
                    String.valueOf(Integer.parseInt(str4array[9], 16)) +
                    String.valueOf(Integer.parseInt(str4array[10], 16)) +
                    String.valueOf(Integer.parseInt(str4array[11], 16)) +
                    String.valueOf(Integer.parseInt(str4array[12], 16)) +
                    String.valueOf(Integer.parseInt(str4array[13], 16)) +
                    String.valueOf(Integer.parseInt(str4array[14], 16)) +
                    String.valueOf(Integer.parseInt(str4array[15], 16)) +
                    String.valueOf(Integer.parseInt(str4array[16], 16)) +
                    String.valueOf(Integer.parseInt(str4array[17], 16));
            four.set(16, bms);//bms
            String host = String.valueOf(Integer.parseInt(str4array[18], 16)) +
                    String.valueOf(Integer.parseInt(str4array[19], 16)) +
                    String.valueOf(Integer.parseInt(str4array[20], 16)) +
                    String.valueOf(Integer.parseInt(str4array[21], 16)) +
                    String.valueOf(Integer.parseInt(str4array[22], 16)) +
                    String.valueOf(Integer.parseInt(str4array[23], 16)) +
                    String.valueOf(Integer.parseInt(str4array[24], 16)) +
                    String.valueOf(Integer.parseInt(str4array[25], 16)) +
                    String.valueOf(Integer.parseInt(str4array[26], 16)) +
                    String.valueOf(Integer.parseInt(str4array[27], 16)) +
                    String.valueOf(Integer.parseInt(str4array[28], 16)) +
                    String.valueOf(Integer.parseInt(str4array[29], 16)) +
                    String.valueOf(Integer.parseInt(str4array[30], 16)) +
                    String.valueOf(Integer.parseInt(str4array[31], 16)) +
                    String.valueOf(Integer.parseInt(str4array[32], 16)) +
                    String.valueOf(Integer.parseInt(str4array[33], 16));
            four.set(17, host);//host

            four.set(18, ((Integer.parseInt(str4array[34] + str4array[35], 16) - 30000) / 100.0));//校准AFE电流增益
            four.set(19, Integer.parseInt(str4array[36] + str4array[37], 16));//校准第16节电池电压增益

            if (!isNumeric(str4array[38] + str4array[39] + str4array[40] + str4array[41] + str4array[42] + str4array[43])) {
                return;
                //four.set(1,"1970-01-01 00:00:00");
            } else {
                String MM = String.valueOf(Integer.parseInt(str4array[39], 10)).length() == 2 ? String.valueOf(Integer.parseInt(str4array[39], 10))
                        : "0" + String.valueOf(Integer.parseInt(str4array[39], 10));
                String dd = String.valueOf(Integer.parseInt(str4array[40], 10)).length() == 2 ? String.valueOf(Integer.parseInt(str4array[40], 10))
                        : "0" + String.valueOf(Integer.parseInt(str4array[40], 10));
                String hh = String.valueOf(Integer.parseInt(str4array[41], 10)).length() == 2 ? String.valueOf(Integer.parseInt(str4array[41], 10))
                        : "0" + String.valueOf(Integer.parseInt(str4array[41], 10));
                String mm = String.valueOf(Integer.parseInt(str4array[42], 10)).length() == 2 ? String.valueOf(Integer.parseInt(str4array[42], 10))
                        : "0" + String.valueOf(Integer.parseInt(str4array[42], 10));
                String ss = String.valueOf(Integer.parseInt(str4array[43], 10)).length() == 2 ? String.valueOf(Integer.parseInt(str4array[43], 10))
                        : "0" + String.valueOf(Integer.parseInt(str4array[43], 10));
                four.set(20, "20" + String.valueOf(Integer.parseInt(str4array[38], 10)) + "-" +
                        MM + "-" +
                        dd + " " +
                        hh + ":" +
                        mm + ":" + ss);//yyyymmdd hh:mm:ss
            }

            four.set(21, Integer.parseInt(str4array[44] + str4array[45], 16));//复位控制
            four.set(22, Integer.parseInt(str4array[46] + str4array[47], 16) / 4.0);//复位控制
            four.set(23, Integer.parseInt(str4array[48] + str4array[49], 16));//均衡控制（测试）
            four.set(24, Integer.parseInt(str4array[50] + str4array[51], 16));//测试控制

            //  four.set(25,Integer.parseInt(str4array[28]+str4array[29], 16));//测试结果
            String s36 = Integer.toBinaryString(Integer.parseInt(str4array[52] + str4array[53], 16));//测试结果
            for (int i = 0; i < s36.length(); i++) {
                four.set(i + 25, Integer.parseInt(s36.substring(s36.length() - 1 - i, s36.length() - i)));
            }
            if (s36.length() < 16) {
                for (int j = 0; j < 16 - s36.length(); j++) {
                    four.set(s36.length() + 25 + j, 0);
                }
            }
            four.set(41, bean4.getpId());
            four.setString(42, bean4.getbId());
            long times=record.getBigint("collecttime");
            four.setDatetime(43,new Date(times));
            four.set(44,bean4.getPort());
            context.write(four);

        }
    }

    public static void main(String[] args) throws Exception {
        //擦部署分别用逗号分隔
        if (args.length == 2) {
            System.out.println(args);
        } else {
            System.err.println("MultipleInOut in... out...");
            System.exit(1);
        }
        System.out.println(args);
        JobConf job = new JobConf();
        job.setNumReduceTasks(0);
        JobClient.runJob(ToolUtil.runJob(args, logtype4.logTypeMapper.class));
        System.out.println("finished");
    }
}
