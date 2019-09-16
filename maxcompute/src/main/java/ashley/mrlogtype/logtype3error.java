package ashley.mrlogtype;


import ashley.beans.partReadOnlyDataBean;
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

/**
 * Created by AshleyZHANG on 2019/5/20.
 */
//partReadOnlyData
public class logtype3error {
    public static class logTypeMapper extends MapperBase {

        private Record three;
        partReadOnlyDataBean bean3 = new partReadOnlyDataBean();

        private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        @Override
        public void setup(Mapper.TaskContext context) throws IOException {

            three = context.createOutputRecord();
        }

        //map方法，先定义要输出的实际表的数据
        @Override
        public void map(long recordNum, Record record, Mapper.TaskContext context)
                throws IOException {

            String jsonStrings = record.get("content").toString().trim();
            Gson gson = new Gson();
            bean3 = gson.fromJson(jsonStrings, partReadOnlyDataBean.class);


            if (!(bean3.getType() == 3) && jsonStrings.contains("list")) {
                return;
            }
            String str1 = bean3.getPartReadOnlyData();

            if (str1 == null) {
                return;
            }
                String  str1to = URLDecoder.decode(str1, "UTF-8").replaceAll("\\[", "").replaceAll("]", "");
                String[] str3array = str1to.split(",");

            //处理每一个字符串为单个字段

            three.set(0, String.valueOf(Integer.parseInt(str3array[0], 16)) +
                    String.valueOf(Integer.parseInt(str3array[1], 16)) +
                    String.valueOf(Integer.parseInt(str3array[2], 16)) +
                    String.valueOf(Integer.parseInt(str3array[3], 16)) +
                    String.valueOf(Integer.parseInt(str3array[4], 16)) +
                    String.valueOf(Integer.parseInt(str3array[5], 16)) +
                    String.valueOf(Integer.parseInt(str3array[6], 16)) +
                    String.valueOf(Integer.parseInt(str3array[7], 16)));//userID

            String s8 = Integer.toBinaryString(Integer.parseInt(str3array[8] + str3array[9], 16));//BMS状态
            for (int i = 0; i < s8.length(); i++) {
                three.set(i + 1, Integer.parseInt(s8.substring(s8.length() - 1 - i, s8.length() - i)));
            }
            if (s8.length() < 16) {
                for (int j = 0; j < 16 - s8.length(); j++) {
                    three.set(s8.length() + 1 + j, 0);
                }
            }

            if ((Integer.parseInt(str3array[10] + str3array[11], 16) / 10.0 < 0) || (Integer.parseInt(str3array[10] + str3array[11], 16) / 10.0 > 100.0)) {
                return;
            }
            //    return;
            three.set(17, Integer.parseInt(str3array[10] + str3array[11], 16) == 65535 ? -100.0 : (Integer.parseInt(str3array[10] + str3array[11], 16) / 10.0));//电池组荷电状态

            if (((Integer.parseInt(str3array[12] + str3array[13], 16) / 100.0) > 300.0) || ((Integer.parseInt(str3array[12] + str3array[13], 16) / 100.0) < 0)) {
                return;
            }
            three.set(18, Integer.parseInt(str3array[12] + str3array[13], 16) == 65535 ? -100.0 : (Integer.parseInt(str3array[12] + str3array[13], 16) / 100.0));//总电压

            if (((Integer.parseInt(str3array[14] + str3array[15], 16) - 30000) / 100.0 > 300.0) || ((Integer.parseInt(str3array[14] + str3array[15], 16) - 30000) / 100.0 < -300.0)) {
                return;
            }
            three.set(19, Integer.parseInt(str3array[14] + str3array[15], 16) == 65535 ? -100.0 : ((Integer.parseInt(str3array[14] + str3array[15], 16) - 30000) / 100.0));//总电流


            if (((Integer.parseInt(str3array[16] + str3array[17], 16) - 400) / 10.0 < -40.0) || ((Integer.parseInt(str3array[16] + str3array[17], 16) - 400) / 10.0 > 120.0)) {
                return;
            }
            three.set(20, Integer.parseInt(str3array[16] + str3array[17], 16) == 65535 ? -100.0 : ((Integer.parseInt(str3array[16] + str3array[17], 16) - 400) / 10.0));//最高电池温度

            if (Integer.parseInt(str3array[18] + str3array[19], 16) < 1 || Integer.parseInt(str3array[18] + str3array[19], 16) > 2) {
                return;
            }
            three.set(21, Integer.parseInt(str3array[18] + str3array[19], 16) == 65535 ? -100 : Integer.parseInt(str3array[18] + str3array[19], 16));//最高电池温度传感器编号

            if (((Integer.parseInt(str3array[20] + str3array[21], 16) - 400) / 10.0 < -40.0) || ((Integer.parseInt(str3array[20] + str3array[21], 16) - 400) / 10.0 > 120.0)) {
                return;
            }
            three.set(22, Integer.parseInt(str3array[20] + str3array[21], 16) == 65535 ? -100.0 : ((Integer.parseInt(str3array[20] + str3array[21], 16) - 400) / 10.0));//最低电池温度

            if (Integer.parseInt(str3array[22] + str3array[23], 16) < 1 || Integer.parseInt(str3array[22] + str3array[23], 16) > 2) {
                return;
            }
            three.set(23, Integer.parseInt(str3array[22] + str3array[23], 16) == 65535 ? -100 : Integer.parseInt(str3array[22] + str3array[23], 16));//最低电池温度传感器编号

            if (Integer.parseInt(str3array[24] + str3array[25], 16) > 5000 || Integer.parseInt(str3array[24] + str3array[25], 16) < 0) {
                return;
            }
            three.set(24, Integer.parseInt(str3array[24] + str3array[25], 16) == 65535 ? -100 : Integer.parseInt(str3array[24] + str3array[25], 16));//最高单体电压

            if (Integer.parseInt(str3array[26] + str3array[27], 16) > 16 || Integer.parseInt(str3array[26] + str3array[27], 16) < 1) {
                return;
            }
            three.set(25, Integer.parseInt(str3array[26] + str3array[27], 16) == 65535 ? -100 : Integer.parseInt(str3array[26] + str3array[27], 16));//最高单体电压电池编号

            if (Integer.parseInt(str3array[28] + str3array[29], 16) > 5000 || Integer.parseInt(str3array[28] + str3array[29], 16) < 0) {
                return;

            }
            three.set(26, Integer.parseInt(str3array[28] + str3array[29], 16) == 65535 ? -100 : Integer.parseInt(str3array[28] + str3array[29], 16));//最低单体电压

            if (Integer.parseInt(str3array[30] + str3array[31], 16) > 16 || Integer.parseInt(str3array[30] + str3array[31], 16) < 1) {
                return;
            }
            three.set(27, Integer.parseInt(str3array[30] + str3array[31], 16) == 65535 ? -100 : Integer.parseInt(str3array[30] + str3array[31], 16));//最低单体电压电池编号

            if (((Integer.parseInt(str3array[32] + str3array[33], 16)) / 100.0) < 0 || ((Integer.parseInt(str3array[32] + str3array[33], 16)) / 100.0) > 300.0) {
                return;
            }
            three.set(28, Integer.parseInt(str3array[32] + str3array[33], 16) == 65535 ? -100.0 : ((Integer.parseInt(str3array[32] + str3array[33], 16)) / 100.0));//10s最大允许放电电流

            if (((Integer.parseInt(str3array[34] + str3array[35], 16)) / 100.0) < 0 || ((Integer.parseInt(str3array[34] + str3array[35], 16)) / 100.0) > 300.0) {
                return;
            }
            three.set(29, Integer.parseInt(str3array[34] + str3array[35], 16) == 65535 ? -100.0 : ((Integer.parseInt(str3array[34] + str3array[35], 16)) / 100.0));//10s最大允许充电电流

            if ((Integer.parseInt(str3array[36] + str3array[37], 16)) / 10.0 <= 0 || (Integer.parseInt(str3array[36] + str3array[37], 16)) / 10.0 > 100 || Integer.parseInt(str3array[38] + str3array[39], 16) > 3000 ) {
                return;
            }
            three.set(30, Integer.parseInt(str3array[36] + str3array[37], 16) == 65535 ? -100.0 : ((Integer.parseInt(str3array[36] + str3array[37], 16)) / 10.0));//健康状态

            //    three.set(31,Integer.parseInt(str3array[38]+str3array[39], 16));//循环次数
            three.set(31, Integer.parseInt(str3array[38] + str3array[39], 16) == 65535 ? -100 : Integer.parseInt(str3array[38] + str3array[39], 16));//循环次数
            //    three.set(31,Integer.parseInt(str3array[38]+str3array[39], 16));//循环次数

            if (Integer.parseInt(str3array[40] + str3array[41], 16) > 65000 || Integer.parseInt(str3array[40] + str3array[41], 16) < 0) {
                return;
            }
            three.set(32, Integer.parseInt(str3array[40] + str3array[41], 16) == 65535 ? -100 : Integer.parseInt(str3array[40] + str3array[41], 16));//剩余容量

            if (Integer.parseInt(str3array[42] + str3array[43], 16) > 65000 || Integer.parseInt(str3array[42] + str3array[43], 16) < 0) {
                return;
            }
            three.set(33, Integer.parseInt(str3array[42] + str3array[43], 16) == 65535 ? -100 : Integer.parseInt(str3array[42] + str3array[43], 16));//充满容量

            if (Integer.parseInt(str3array[44] + str3array[45], 16) > 65000 || Integer.parseInt(str3array[44] + str3array[45], 16) < 0) {
                return;
            }
            three.set(34, Integer.parseInt(str3array[44] + str3array[45], 16) == 65535 ? -100 : Integer.parseInt(str3array[44] + str3array[45], 16));//充满时间

            if (((Integer.parseInt(str3array[46] + str3array[47], 16)) / 10.0) > 6500.0 || ((Integer.parseInt(str3array[46] + str3array[47], 16)) / 10.0) < 0) {
                return;

            }
            three.set(35, Integer.parseInt(str3array[46] + str3array[47], 16) == 65535 ? -100.0 : ((Integer.parseInt(str3array[46] + str3array[47], 16)) / 10.0));//剩余能量


            String s54 = Integer.toBinaryString(Integer.parseInt(str3array[48] + str3array[49], 16));//设备故障字1(36-51)

            for (int i = 0; i < s54.length(); i++) {
                three.set(i + 36, Integer.parseInt(s54.substring(s54.length() - 1 - i, s54.length() - i)));

            }
            if (s54.length() < 16) {
                for (int j = 0; j < 16 - s54.length(); j++) {
                    three.set(s54.length() + 36 + j, 0);

                }
            }

            String s56 = Integer.toBinaryString(Integer.parseInt(str3array[50] + str3array[51], 16));//设备故障字2(52-67)
            for (int i = 0; i < s56.length(); i++) {
                three.set(i + 52, Integer.parseInt(s56.substring(s56.length() - 1 - i, s56.length() - i)));

            }
            if (s56.length() < 16) {
                for (int j = 0; j < 16 - s56.length(); j++) {
                    three.set(s56.length() + 52 + j, 0);

                }
            }

            String s58 = Integer.toBinaryString(Integer.parseInt(str3array[52] + str3array[53], 16));//运行故障字1(68-83)
            for (int i = 0; i < s58.length(); i++) {
                three.set(i + 68, Integer.parseInt(s58.substring(s58.length() - 1 - i, s58.length() - i)));

            }
            if (s58.length() < 16) {
                for (int j = 0; j < 16 - s58.length(); j++) {
                    three.set(s58.length() + 68 + j, 0);

                }
            }
            String s60 = Integer.toBinaryString(Integer.parseInt(str3array[54] + str3array[55], 16));//运行故障字2(84-99)
            for (int i = 0; i < s60.length(); i++) {
                three.set(i + 84, Integer.parseInt(s60.substring(s60.length() - 1 - i, s60.length() - i)));

            }
            if (s60.length() < 16) {
                for (int j = 0; j < 16 - s60.length(); j++) {
                    three.set(s60.length() + 84 + j, 0);

                }
            }
            String s62 = Integer.toBinaryString(Integer.parseInt(str3array[56] + str3array[57], 16));//运行告警字1(100-115)

            for (int i = 0; i < s62.length(); i++) {
                three.set(i + 100, Integer.parseInt(s62.substring(s62.length() - 1 - i, s62.length() - i)));

            }
            if (s62.length() < 16) {
                for (int j = 0; j < 16 - s62.length(); j++) {
                    three.set(s62.length() + 100 + j, 0);
                }
            }

            three.set(116, Integer.parseInt(str3array[58] + str3array[59], 16));//运行告警字2

            for (int k = 60, m = 0; k < 72; k++, m++) {  //117-122   2*6
                if (((Integer.parseInt(str3array[k] + str3array[k + 1], 16) - 400) / 10.0) > 120.0 || ((Integer.parseInt(str3array[k] + str3array[k + 1], 16) - 400) / 10.0) < -40.0)//温度
                {
                    return;
                }
                three.set(117 + m, Integer.parseInt(str3array[k] + str3array[k + 1], 16) == 65535 ? -100.0 : ((Integer.parseInt(str3array[k] + str3array[k + 1], 16) - 400) / 10.0));
                k = k + 1;
            }
//	        for(int k=72,m=0;k<104;k++,m++){ //123-138   2*16
//	        	three.set(123+m,Integer.parseInt(str3array[k]+str3array[k+1], 16));
//	        	k=k+1;
//	        }
            for (int k = 72, m = 0; k < 104; k++, m++) { //123-138   2*16
                if(Integer.parseInt(str3array[k]+str3array[k+1], 16)>5000||Integer.parseInt(str3array[k]+str3array[k+1], 16)<0)
                    return;
                three.set(123+m,Integer.parseInt(str3array[k]+str3array[k+1], 16)==65535?-100:Integer.parseInt(str3array[k]+str3array[k+1], 16));
                k=k+1;
                }
                String s106 = Integer.toBinaryString(Integer.parseInt(str3array[104] + str3array[105], 16));//电池均衡详细状态 139-154
                for (int i = 0; i < s106.length(); i++) {
                    three.set(i + 139, Integer.parseInt(s106.substring(s106.length() - 1 - i, s106.length() - i)));

                }
                if (s56.length() < 16) {
                    for (int j = 0; j < 16 - s106.length(); j++) {
                        three.set(s106.length() + 139 + j, 0);

                    }
                }

                for ( int k = 106, m = 0; k < 118; k++, m++) { //155-160  2*6
                    three.set(155 + m, Integer.parseInt(str3array[k] + str3array[k + 1], 16));
                    k = k + 1;
                }
                if ((Integer.parseInt(str3array[118] + str3array[119], 16)) / 100.0 > 300.0 || (Integer.parseInt(str3array[118] + str3array[119], 16)) / 100.0 < 0) {
                    return;
                }
                three.set(161, (Integer.parseInt(str3array[118] + str3array[119], 16)) / 100.0);//最大充电电压
                three.set(162, (Integer.parseInt(str3array[120] + str3array[121], 16)));//电量计电压

                long times=record.getBigint("collecttime");
                three.setDatetime(163,new Date(times));
                three.set(164, bean3.getpId());
                three.setString(165, bean3.getbId());
                three.set(166,bean3.getPort());

                //开始设置string解析的字段
                //开始设置string解析的字段
                context.write(three);
                System.out.println(three);
            }
        }


        public static void main(String[] args) throws Exception {
            //擦部署分别用逗号分隔
            if (args.length == 2) {

            } else {
                System.err.println("MultipleInOut in... out...");
                System.exit(1);
            }
            JobConf job = new JobConf();
            job.setNumReduceTasks(0);
            JobClient.runJob(ToolUtil.runJob(args, logtype3error.logTypeMapper.class));
            System.out.println("finished");
        }


}
