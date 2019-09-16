package ashley.mrlogtype;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.odps.data.Record;
import com.aliyun.odps.mapred.JobClient;
import com.aliyun.odps.mapred.Mapper;
import com.aliyun.odps.mapred.MapperBase;
import com.aliyun.odps.mapred.conf.JobConf;
import util.CRCUtils;
import util.ToolUtil;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import static org.apache.commons.lang.StringUtils.isNumeric;


/**
 * Created by AshleyZHANG on 2019/8/21.
 */
public class LogType11 {
    public static class logTypeMapper extends MapperBase {
        private Record one;


        private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CRCUtils crcUtils=new CRCUtils();
        Byte b1=0;
        Byte b2=0;
        //保留两位小数
        @Override
        public void setup(Mapper.TaskContext context) throws IOException {
            one = context.createOutputRecord();
        }

        //map方法
        @Override
        public void map(long recordNum, Record record, Mapper.TaskContext context)
                throws IOException {
            /**{
             "list": ["[01,0A,01,02,86,10,0B,BD,0E,C4,4E,12,01,00,00,19,05,30,11,22,37,80,00,00,04,1A,FF,FF,FF,01,00,03,03,84,19,2A,75,2B,02,A2,00,02,02,A0,00,01,0F,BE,00,0D,0F,B6,00,01,0B,B8,01,F4,03,C0,00,01,20,A1,24,75,FF,FF,13,0E,00,00,00,00,00,00,00,00,00,00,00,00,02,9B,02,9B,02,A0,02,72,02,A0,02,A2,0F,B6,0F,B7,0F,BB,0F,BB,0F,BC,0F,B8,0F,BB,0F,B9,0F,BB,0F,B9,0F,B9,0F,BB,0F,BE,0F,BD,0F,BB,0F,BE,00,00,01,3C,FC,7A,FF,87,00,00,00,D0,BD]",
             "[01,0B,01,02,86,30,25,8A,94,79,2C,07,01,00,00,19,06,06,09,56,50,08,00,00,00,08,FF,FF,FF,FF,00,0B,03,48,13,9E,70,6,02,C1,00,02,02,BE,00,03,0F,1E,00,01,0F,0F,00,0D,0B,B8,04,1A,03,E8,00,03,32,54,3B,F2,FF,FF,1D,64,00,00,00,00,00,00,00,00,00,00,00,00,02,EE,02,EE,02,BE,02,B9,02,BF,02,C1,0F,1E,0F,10,0F,1D,0F,10,0F,1B,0F,19,0F,11,0F,19,FF,FF,0F,1B,0F,10,0F,1E,0F,0F,FF,FF,0F,1E,FF,FF,00,00,00,00,00,00,00,00,00,03,00,68,56]",
             "[01,0C,01,02,86,10,0B,C4,0C,CA,22,55,5A,00,00,19,05,27,21,12,50,5,00,00,01,A2,FF,FF,FF,01,00,08,03,B6,19,73,75,30,02,A2,00,02,02,9F,00,01,0F,EB,00,04,0F,E6,00,06,0B,B8,01,F4,03,D4,00,04,23,2E,25,52,FF,FF,14,8B,00,00,00,00,00,00,00,00,00,00,00,00,02,A1,02,A1,02,A2,02,A1,02,9F,02,A2,0F,E7,0F,EA,0F,EA,0F,EB,0F,E9,0F,E6,0F,E8,0F,E8,0F,E7,0F,E7,0F,E7,0F,E9,0F,EA,0F,E9,0F,E7,0F,E9,00,00,FE,B4,FF,FB,FC,32,00,00,00,18,E7]"]
             "pId": "3134ae5b08987424",
             "type": 11
             }
             */
            String jsonString = record.get("content").toString().trim();
            if (!jsonString.contains("\"type\":11"))   return;
            JSONObject jsonObject = JSON.parseObject(jsonString);// json对象
            String pid=jsonObject.getString("pId");
            System.out.println("pid"+pid);
            String type=jsonObject.getString("type");
            System.out.println("type"+type);
            String list = jsonObject.getString("list");
            System.out.println("list"+list);
            JSONArray jsonArray = JSON.parseArray(list);// json数组对象

            for (int a =0;a<jsonArray.size();a++){
                String listStr =jsonArray.get(a).toString();
                System.out.println("listStr===="+listStr);

                String strs = URLDecoder.decode(listStr, "UTF-8").replaceAll("\\[", "").replaceAll("]", "");
                String[] str = strs.split(",");
                if (str.length!=139){return;}
                System.out.println(str.length);
                //数据解析
                //target
                one.set(0,String.valueOf(Integer.parseInt(str[0],16)));
                //port
                one.setString(1,String.valueOf(Integer.parseInt(str[1],16)));
                //error_code
                one.set(2,String.valueOf(Integer.parseInt(str[2],16)));
                //date_type
                one.set(3,String.valueOf(Integer.parseInt(str[3],16)));
                //date_length
                one.set(4,String.valueOf(Integer.parseInt(str[4],16)));
                //bid
                if (str[6] + str[5] + str[8] + str[7] + str[10] + str[9] == "FFFFFFFFFFFF") return;
                one.set(5,str[5]+str[6]+str[7]+str[8]+str[9]+str[10]);



                //开始
                if((str[14]+str[13]+str[12]+str[11]).equals("FFFFFFFF"))
                    return;
                //事件编号
                one.set(6,String.valueOf(Integer.parseInt(str[14]+str[13]+str[12]+str[11], 16)));
                //时间
                if (!isNumeric(str[15] + str[16] + str[17] + str[18] + str[19] + str[20])) {
                    //return;
                    one.set(7,"1970-01-01 00:00:00");
                    one.set(168,0);
                } else {
                    String MM = String.valueOf(Integer.parseInt(str[16], 10)).length() == 2 ? String.valueOf(Integer.parseInt(str[16], 10))
                            : "0" + String.valueOf(Integer.parseInt(str[16], 10));
                    String dd = String.valueOf(Integer.parseInt(str[17], 10)).length() == 2 ? String.valueOf(Integer.parseInt(str[17], 10))
                            : "0" + String.valueOf(Integer.parseInt(str[17], 10));
                    String hh = String.valueOf(Integer.parseInt(str[18], 10)).length() == 2 ? String.valueOf(Integer.parseInt(str[18], 10))
                            : "0" + String.valueOf(Integer.parseInt(str[18], 10));
                    String mm = String.valueOf(Integer.parseInt(str[19], 10)).length() == 2 ? String.valueOf(Integer.parseInt(str[19], 10))
                            : "0" + String.valueOf(Integer.parseInt(str[19], 10));
                    String ss = String.valueOf(Integer.parseInt(str[20], 10)).length() == 2 ? String.valueOf(Integer.parseInt(str[20], 10))
                            : "0" + String.valueOf(Integer.parseInt(str[20], 10));
                    if(String.valueOf(Integer.parseInt(str[15], 10)).length()<2||Integer.parseInt(MM)>12||Integer.parseInt(MM)<1){
                        one.set(168,0);
                    }else {
                        one.set(168,1);
                    }
                    one.set(7,"20"+String.valueOf(Integer.parseInt(str[15], 10))+"-"+
                            MM+"-"+
                            dd+" "+
                            hh+":"+
                            mm+":"+ss);//userID
                    String times="20"+String.valueOf(Integer.parseInt(str[15], 10))+"-"+
                            MM+"-"+
                            dd+" "+
                            hh+":"+
                            mm+":"+ss;
                    sdf.format(new Date());
                }
                //事件类型
                one.set(8,  String.valueOf(Integer.parseInt(str[21], 16)));
                //userID
                one.set(9,String.valueOf(Integer.parseInt(str[22]+str[23], 16))
                        +String.valueOf(Integer.parseInt(str[24]+str[25],16))
                        +String.valueOf(Integer.parseInt(str[26]+str[27],16))
                        +String.valueOf(Integer.parseInt(str[28]+str[29],16)));

                //10-25
                String s8=Integer.toBinaryString(Integer.parseInt(str[30]+str[31], 16));//BMS状态
                for(int i=0;i<s8.length();i++){
                    one.set(i+10,Integer.parseInt(s8.substring(s8.length()-1-i, s8.length()-i)));
                }
                if(s8.length()<16){
                    for(int j=0;j<16-s8.length();j++){
                        one.set(s8.length()+10+j,0);
                    }
                }

                one.setDouble(26,Integer.parseInt(str[32]+str[33], 16)==65535?-100.0:(Integer.parseInt(str[32]+str[33], 16)/10.0));//电池组荷电状态
                one.setDouble(27,Integer.parseInt(str[34]+str[35], 16)==65535?-100.0:(Integer.parseInt(str[34]+str[35], 16)/100.0));//总电压
                one.setDouble(28,Integer.parseInt(str[36]+str[37], 16)==65535?-100.0:((Integer.parseInt(str[36]+str[37], 16)-30000)/100.0));//总电流
                one.setDouble(29,Integer.parseInt(str[38]+str[39], 16)==65535?-100.0:((Integer.parseInt(str[38]+str[39], 16)-400)/10.0));//最高电池温度
                one.set(30, (Integer.parseInt(str[40]+str[41], 16)==65535?-100:Integer.parseInt(str[40]+str[41], 16)));//最高电池温度传感器编号
                one.setDouble(31,Integer.parseInt(str[42]+str[43], 16)==65535?-100.0:((Integer.parseInt(str[42]+str[43], 16)-400)/10.0));//最低电池温度
                one.set(32, (Integer.parseInt(str[44]+str[45], 16)==65535?-100:Integer.parseInt(str[44]+str[45], 16)));//最低电池温度传感器编号
                one.set(33,Integer.parseInt(str[46]+str[47], 16)==65535?-100:Integer.parseInt(str[46]+str[47], 16));//最高单体电压
                one.set(34,Integer.parseInt(str[48]+str[49], 16)==65535?-100:Integer.parseInt(str[48]+str[49], 16));//最高单体电压电池编号
                one.set(35,Integer.parseInt(str[50]+str[51], 16)==65535?-100:Integer.parseInt(str[50]+str[51], 16));//最低单体电压
                one.set(36,Integer.parseInt(str[52]+str[53], 16)==65535?-100:Integer.parseInt(str[52]+str[53], 16));//最低单体电压电池编号


                one.setDouble(37,Integer.parseInt(str[54]+str[55], 16)==65535?-100.0:((Integer.parseInt(str[54]+str[55], 16))/100.0));//10s最大允许放电电流
                if((Integer.parseInt(str[58]+str[59], 16))/10.0<=0||(Integer.parseInt(str[58]+str[59], 16))/10.0>100||Integer.parseInt(str[60]+str[61], 16)>3000){
                    //  flag=false;
                }

                one.setDouble(38,Integer.parseInt(str[56]+str[57], 16)==65535?-100.0:((Integer.parseInt(str[56]+str[57], 16))/100.0));//10s最大允许充电电流
                one.set(39,Integer.parseInt(str[58]+str[59], 16)==65535?-100.0:((Integer.parseInt(str[58]+str[59], 16))/10.0));//健康状态

                one.set(40, (Integer.parseInt(str[60]+str[61], 16)==65535?-100:Integer.parseInt(str[60]+str[61], 16)));//循环次数

                one.setBigint(41, Integer.parseInt(str[62]+str[63], 16)==65535? (long) -100.0 :Integer.parseInt(str[62]+str[63],16));//剩余容量

                one.set(42,Integer.parseInt(str[64]+str[65], 16)==65535?-100:Integer.parseInt(str[64]+str[65], 16));//充满容量
                one.set(43,Integer.parseInt(str[66]+str[67], 16)==65535?-100:Integer.parseInt(str[66]+str[67], 16));//充满时间
                one.set(44,(Integer.parseInt(str[68]+str[69], 16)==65535?-100.0:((Integer.parseInt(str[68]+str[69], 16))/10.0)));//剩余能量

                String s54=Integer.toBinaryString(Integer.parseInt(str[70]+str[71], 16));//设备故障字1(43-58)
                System.out.println(s54);
                for(int i=0;i<s54.length();i++){
                    one.set(i+45, Integer.parseInt(s54.substring(s54.length()-1-i, s54.length()-i)));
                }
                if(s54.length()<16){
                    for(int j=0;j<16-s54.length();j++){
                        one.set(s54.length()+45+j,0);
                    }
                }

                String s56=Integer.toBinaryString(Integer.parseInt(str[72]+str[73], 16));//设备故障字2(59-74)
                for(int i=0;i<s56.length();i++){
                    one.set(i+61,Integer.parseInt(s56.substring(s56.length()-1-i, s56.length()-i)));
                }
                if(s56.length()<16){
                    for(int j=0;j<16-s56.length();j++){
                        one.set(s56.length()+61+j,0);
                    }
                }
                String s58=Integer.toBinaryString(Integer.parseInt(str[74]+str[75], 16));//运行故障字1()
                for(int i=0;i<s58.length();i++){
                    one.set(i+77,Integer.parseInt(s58.substring(s58.length()-1-i, s58.length()-i)));
                }
                if(s58.length()<16){
                    for(int j=0;j<16-s58.length();j++){
                        one.set(s58.length()+77+j,0);
                    }
                }
                String s60=Integer.toBinaryString(Integer.parseInt(str[76]+str[77], 16));//运行故障字2(84-99)
                for(int i=0;i<s60.length();i++){
                    one.set(i+93,Integer.parseInt(s60.substring(s60.length()-1-i, s60.length()-i)));
                }
                if(s60.length()<16){
                    for(int j=0;j<16-s60.length();j++){
                        one.set(s60.length()+93+j,0);
                    }
                }
                String s62=Integer.toBinaryString(Integer.parseInt(str[78]+str[79], 16));//运行告警字1(100-115)
                for(int i=0;i<s62.length();i++){
                    one.set(i+109,Integer.parseInt(s62.substring(s62.length()-1-i, s62.length()-i)));
                }
                if(s62.length()<16){
                    for(int j=0;j<16-s62.length();j++){
                        one.set(s62.length()+109+j,0);
                    }
                }
                one.set(125,Integer.parseInt(str[80]+str[81], 16));//运行告警字2

                //double   82-  93
                for(int k=82,m=0;k<93;k++,m++){  //125-136   2*6   [92]-[93]
                    one.setDouble(126+m,(Integer.parseInt(str[k]+str[k+1], 16)==65535?-100.0:((Integer.parseInt(str[k]+str[k+1], 16)-400)/10.0)));
                    k=k+1;
                }

                for(int k=94,m=0;k<125;k++,m++){ //123-138   2*16   32
                    one.set(132+m,(Integer.parseInt(str[k]+str[k+1], 16)==65535?-100:Integer.parseInt(str[k]+str[k+1], 16)));
                    k=k+1;
                }
                String s106=Integer.toBinaryString(Integer.parseInt(str[127]+str[128], 16));//电池均衡详细状态 139-154
                for(int i=0;i<s106.length();i++){
                    one.set(i+148,Integer.parseInt(s106.substring(s106.length()-1-i, s106.length()-i)));
                }
                if(s56.length()<16){
                    for(int j=0;j<16-s106.length();j++){
                        one.set(s106.length()+148+j,0);
                    }
                }
                for(int k=128,m=0;k<135;k++,m++){ //162-167  2*6      129-134  6字节
                    one.set(164+m,Integer.parseInt(str[k]+str[k+1], 16));
                    k=k+1;
                }

                //168
                boolean flag= crcUtils.ComputeCrc(1, str, 139, b1, b2);
                if(flag){//crc校验
                    one.set(167,1);//id
                }else{
                    one.set(167,0);
                }

                one.setString(169,pid);
                one.setString(170,type);
                long times=record.getBigint("collecttime");
                one.setDatetime(171,new Date(times));


                System.out.println(one);
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
        JobClient.runJob(ToolUtil.runJob(args, LogType11.logTypeMapper.class));
        System.out.println("完成");
    }


}
