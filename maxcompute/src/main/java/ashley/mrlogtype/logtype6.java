package ashley.mrlogtype;

import ashley.beans.LogBean;
import ashley.beans.LogListBean;
import ashley.utils.TempCaculate;
import com.aliyun.odps.data.Record;
import com.aliyun.odps.mapred.JobClient;
import com.aliyun.odps.mapred.Mapper;
import com.aliyun.odps.mapred.MapperBase;
import com.aliyun.odps.mapred.conf.JobConf;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang.math.NumberUtils;
import util.ToolUtil;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static ashley.utils.TempCaculate.TEMPER_CALC_B1;
import static ashley.utils.TempCaculate.TEMPER_CALC_B2;

public class logtype6{
    public static class logTypeMapper extends MapperBase {
        private Record one;
        LogListBean bean=new LogListBean();
        //保留两位小数
        DecimalFormat df = new DecimalFormat("0.00");

        private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        @Override
        public void setup(Mapper.TaskContext context) throws IOException {
            one = context.createOutputRecord();
        }

        //map方法
        @Override
        public void map(long recordNum, Record record, Mapper.TaskContext context)
                throws IOException {

            /**
             * ap和9在一起作为temp2
             * {"list":[{"content":"[
             * B7,05,D3,06,00,00,00,00,00,00,00,00,00,00,00,00,00,00,   18个*7+13=139
             * 00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,
             * 00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,
             * 00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,
             * 00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,
             * 00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,
             * 00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,
             * 00,00,00,00,00,00,00,00,00,00,00,00,00]","time":1558678789259}],"pId":"7849ae5b03623c24","type":6}
                89.90
             9+13*10

               F3,05,80,06,02,00,00,00,00,92,06,C0,00,00,00,00,00,00,
               00,9F,06,C0,00,00,00,00,00,00,00,76,06,00,00,00,00,00,
               00,00,00,64,06,C0,00,00,00,00,00,00,00,91,06,C0,00,00,
               00,00,00,00,00,87,06,C0,00,00,00,00,00,00,00,64,06,C0,
               00,00,00,00,00,00,00,79,06,C0,00,00,00,00,00,00,00,6F,
               06,C0,00,00,00,00,00,00,00,99,06,C0,00,00,00,00,00,00,
               02,70,06,C0,00,00,00,00,00,00,00,2A,06,C0,00,00,00,00,
               00,00,00,A4,06,C0,00,00,00,00,00,00,00]
             */
            String jsonString = record.get("content").toString().trim();
            if (!jsonString.contains("\"type\":6"))   return;
            JsonObject obj = new JsonParser().parse(jsonString).getAsJsonObject();
            System.out.println(obj);
            LogListBean loglistbean=new Gson().fromJson(obj,LogListBean.class);

            String pid=loglistbean.getpId();
            int type=Integer.parseInt(loglistbean.getType());

            if (type!=6){
                return;
            }
            List<LogBean> lists =loglistbean.getList();

            for (int a=0;a<lists.size();a++) {
                LogBean logbean = lists.get(a);
                String content = logbean.getContent();
                String strs = URLDecoder.decode(content, "UTF-8").replaceAll("\\[", "").replaceAll("]", "");
                String[] str = strs.split(",");

                if (str.length==139) {
                    // 数据处理逻辑
                    int temp1 = TempCaculate.calcTemper(Integer.parseInt(str[1] + str[0], 16), TEMPER_CALC_B1);
                    int temp2 = TempCaculate.calcTemper(Integer.parseInt(str[3] + str[2], 16), TEMPER_CALC_B1);


                    one.set(0, temp1 / 1.0);

                    one.set(1, temp2 / 1.0);
                    //Other Sta 2-9
                    String other = Integer.toBinaryString(Integer.parseInt(str[4], 16));
                    //2-9
                    for (int i = 0; i < other.length(); i++) {
                        one.set(i + 2, Integer.parseInt(other.substring(other.length() - 1 - i, other.length() - i)));
                    }
                    if (other.length() < 8) {
                        for (int j = 0; j < 8 - other.length(); j++) {
                            one.set(other.length() + 2 + j, 0);
                        }
                    }
                    //Sys_Pwr_Off_Cause 10-25
                    String s1 = Integer.toBinaryString(Integer.parseInt(str[6] + str[5], 16));
                    for (int i = 0; i < s1.length(); i++) {
                        one.set(i + 10, Integer.parseInt(s1.substring(s1.length() - 1 - i, s1.length() - i)));
                    }
                    if (s1.length() < 16) {
                        for (int j = 0; j < 16 - s1.length(); j++) {
                            one.set(s1.length() + 10 + j, 0);
                        }
                    }
                    //PMS_Reset26-41
                    String s2 = Integer.toBinaryString(Integer.parseInt(str[8] + str[7], 16));
                    for (int i = 0; i < s2.length(); i++) {
                        one.set(i + 26, Integer.parseInt(s2.substring(s2.length() - 1 - i, s2.length() - i)));
                    }
                    if (s2.length() < 16) {
                        for (int j = 0; j < 16 - s2.length(); j++) {
                            one.set(s2.length() + 26 + j, 0);
                        }
                    }
//电池故障
                    //PMS1_STA
//电池
                    for (int k = 0; k < 13; k++) {

                        int temp3 = TempCaculate.calcTemper(Integer.parseInt(str[10 + 10 * k] + str[9 + 10 * k], 16), TEMPER_CALC_B2);
                        one.set(42, temp3 / 1.0);
                        //充电状态1     43-50
                        String s3 = Integer.toBinaryString(Integer.parseInt(str[11 + 10 * k], 16));
                        for (int i = 0; i < s3.length(); i++) {
                            one.set(i + 43, Integer.parseInt(s3.substring(s3.length() - 1 - i, s3.length() - i)));
                        }
                        if (s3.length() < 8) {
                            for (int j = 0; j < 8 - s3.length(); j++) {
                                one.set(s3.length() + 43 + j, 0);
                            }
                        }
                        //充电状态2     51-58
                        String s4 = Integer.toBinaryString(Integer.parseInt(str[12 + 10 * k], 16));
                        for (int i = 0; i < s4.length(); i++) {
                            one.set(i + 51, Integer.parseInt(s4.substring(s4.length() - 1 - i, s4.length() - i)));
                        }
                        if (s4.length() < 8) {
                            for (int j = 0; j < 8 - s4.length(); j++) {
                                one.set(s4.length() + 51 + j, 0);
                            }
                        }
                        //主动检测电池故障  59-66
                        //温度的偏移
                        String s5 = Integer.toBinaryString(Integer.parseInt(str[13 + 10 * k], 16));
                        for (int i = 0; i < s5.length(); i++) {
                            one.set(i + 59, Integer.parseInt(s5.substring(s5.length() - 1 - i, s5.length() - i)));
                        }
                        if (s5.length() < 8) {
                            for (int j = 0; j < 8 - s5.length(); j++) {
                                one.set(s5.length() + 59 + j, 0);
                            }
                        }
                        //被动检测故障 67-98
                        String s6 = Long.toBinaryString(Long.parseLong(str[15 + 10 * k] + str[14 + 10 * k] + str[17 + 10 * k] + str[16 + 10 * k], 16));
                        for (int i = 0; i < s6.length(); i++) {
                            one.set(i + 67, Integer.parseInt(s6.substring(s6.length() - 1 - i, s6.length() - i)));
                        }
                        if (s6.length() < 32) {
                            for (int j = 0; j < 32 - s6.length(); j++) {
                                one.set(s6.length() + 67 + j, 0);
                            }
                        }
                        //其他检测故障        99-106
                        String regex = "^[A-Fa-f0-9]+$";
                        if (!str[18 + 10 * k].matches(regex)) return;
                        // if (!NumberUtils.isNumber(str[18+10*k])) return;
                        String s7 = Integer.toBinaryString(Integer.parseInt(str[18 + 10 * k], 16));
                        for (int i = 0; i < s7.length(); i++) {
                            one.set(i + 99, Long.parseLong(s7.substring(s7.length() - 1 - i, s7.length() - i)));
                        }
                        if (s7.length() < 8) {
                            for (int j = 0; j < 8 - s7.length(); j++) {
                                one.set(s7.length() + 99 + j, 0);
                            }
                        }
                        long time = Long.valueOf(logbean.getTime());
                        one.setDatetime(107, new Date(time));
                        one.setBigint(108, (long) type);
                        one.set(109, pid);
                        String battery = String.valueOf((k + 1));
                        one.set(110, battery);
                        context.write(one);
                    }
                }


                if (str.length==152) {
                    // 数据处理逻辑
                    int temp1 = TempCaculate.calcTemper(Integer.parseInt(str[1] + str[0], 16), TEMPER_CALC_B1);
                    int temp2 = TempCaculate.calcTemper(Integer.parseInt(str[3] + str[2], 16), TEMPER_CALC_B1);


                    one.set(0, temp1 / 1.0);

                    one.set(1, temp2 / 1.0);
                    //Other Sta 2-9
                    String other = Integer.toBinaryString(Integer.parseInt(str[4], 16));
                    //2-9
                    for (int i = 0; i < other.length(); i++) {
                        one.set(i + 2, Integer.parseInt(other.substring(other.length() - 1 - i, other.length() - i)));
                    }
                    if (other.length() < 8) {
                        for (int j = 0; j < 8 - other.length(); j++) {
                            one.set(other.length() + 2 + j, 0);
                        }
                    }
                    //Sys_Pwr_Off_Cause 10-25
                    String s1 = Integer.toBinaryString(Integer.parseInt(str[6] + str[5], 16));
                    for (int i = 0; i < s1.length(); i++) {
                        one.set(i + 10, Integer.parseInt(s1.substring(s1.length() - 1 - i, s1.length() - i)));
                    }
                    if (s1.length() < 16) {
                        for (int j = 0; j < 16 - s1.length(); j++) {
                            one.set(s1.length() + 10 + j, 0);
                        }
                    }
                    //PMS_Reset26-41
                    String s2 = Integer.toBinaryString(Integer.parseInt(str[8] + str[7], 16));
                    for (int i = 0; i < s2.length(); i++) {
                        one.set(i + 26, Integer.parseInt(s2.substring(s2.length() - 1 - i, s2.length() - i)));
                    }
                    if (s2.length() < 16) {
                        for (int j = 0; j < 16 - s2.length(); j++) {
                            one.set(s2.length() + 26 + j, 0);
                        }
                    }
//电池故障
                    //PMS1_STA
//电池
                    for (int k = 0; k < 13; k++) {

                        int temp3 = TempCaculate.calcTemper(Integer.parseInt(str[10 + 10 * k] + str[9 + 10 * k], 16), TEMPER_CALC_B2);
                        one.set(42, temp3 / 1.0);
                        //充电状态1     43-50
                        String s3 = Integer.toBinaryString(Integer.parseInt(str[11 + 10 * k], 16));
                        for (int i = 0; i < s3.length(); i++) {
                            one.set(i + 43, Integer.parseInt(s3.substring(s3.length() - 1 - i, s3.length() - i)));
                        }
                        if (s3.length() < 8) {
                            for (int j = 0; j < 8 - s3.length(); j++) {
                                one.set(s3.length() + 43 + j, 0);
                            }
                        }
                        //充电状态2     51-58
                        String s4 = Integer.toBinaryString(Integer.parseInt(str[12 + 10 * k], 16));
                        for (int i = 0; i < s4.length(); i++) {
                            one.set(i + 51, Integer.parseInt(s4.substring(s4.length() - 1 - i, s4.length() - i)));
                        }
                        if (s4.length() < 8) {
                            for (int j = 0; j < 8 - s4.length(); j++) {
                                one.set(s4.length() + 51 + j, 0);
                            }
                        }
                        //主动检测电池故障  59-66
                        //温度的偏移
                        String s5 = Integer.toBinaryString(Integer.parseInt(str[13 + 10 * k], 16));
                        for (int i = 0; i < s5.length(); i++) {
                            one.set(i + 59, Integer.parseInt(s5.substring(s5.length() - 1 - i, s5.length() - i)));
                        }
                        if (s5.length() < 8) {
                            for (int j = 0; j < 8 - s5.length(); j++) {
                                one.set(s5.length() + 59 + j, 0);
                            }
                        }
                        //被动检测故障 67-98
                        String s6 = Long.toBinaryString(Long.parseLong(str[15 + 10 * k] + str[14 + 10 * k] + str[17 + 10 * k] + str[16 + 10 * k], 16));
                        for (int i = 0; i < s6.length(); i++) {
                            one.set(i + 67, Integer.parseInt(s6.substring(s6.length() - 1 - i, s6.length() - i)));
                        }
                        if (s6.length() < 32) {
                            for (int j = 0; j < 32 - s6.length(); j++) {
                                one.set(s6.length() + 67 + j, 0);
                            }
                        }
                        //其他检测故障        99-106
                        String regex = "^[A-Fa-f0-9]+$";
                        if (!str[18 + 10 * k].matches(regex)) return;
                        // if (!NumberUtils.isNumber(str[18+10*k])) return;
                        String s7 = Integer.toBinaryString(Integer.parseInt(str[18 + 10 * k], 16));
                        for (int i = 0; i < s7.length(); i++) {
                            one.set(i + 99, Long.parseLong(s7.substring(s7.length() - 1 - i, s7.length() - i)));
                        }
                        if (s7.length() < 8) {
                            for (int j = 0; j < 8 - s7.length(); j++) {
                                one.set(s7.length() + 99 + j, 0);
                            }
                        }
                        long time = Long.valueOf(logbean.getTime());
                        one.setDatetime(107, new Date(time));
                        one.setBigint(108, (long) type);
                        one.set(109, pid);
                        String battery = String.valueOf((k + 1));
                        one.set(110, battery);

                        String s8 = String.valueOf(Integer.parseInt(str[139 + k], 16)-40);
                        one.setString(111, s8);

                        context.write(one);
                    }
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
        JobConf job = new JobConf();
        job.setNumReduceTasks(0);
        JobClient.runJob(ToolUtil.runJob(args, logtype6.logTypeMapper.class));
        System.out.println("完成");
    }
}
