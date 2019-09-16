package battery;


import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import mr.cabinet.Cabinet_hb3;
import util.CRCUtils;
import com.aliyun.odps.data.Record;
import com.aliyun.odps.data.TableInfo;
import com.aliyun.odps.mapred.JobClient;
import com.aliyun.odps.mapred.MapperBase;
import com.aliyun.odps.mapred.conf.JobConf;
import com.aliyun.odps.mapred.utils.InputUtils;
import com.aliyun.odps.mapred.utils.OutputUtils;
import util.ToolUtil;

public class Origin_BatteryLogData {



    public static class TokenizerMapper extends MapperBase {
        private Record one;
        private Record one1;
        private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CRCUtils crcUtils=new CRCUtils();
        Byte b1=0;
        Byte b2=0;

        @Override
        public void setup(TaskContext context) throws IOException {
            one=context.createOutputRecord();
          //  one1 = context.createOutputRecord("out1");


        }
        public static boolean isNumeric(String str){
            Pattern pattern = Pattern.compile("[0-9]*");
            return pattern.matcher(str).matches();
        }
        @Override
        public void map(long recordNum, Record record, TaskContext context)
                throws IOException {
            String str = record.get("partlog").toString();
            System.out.println(str);
            if(!str.contains(",")){
                return;
            }
            String partReadOnlyData= URLDecoder.decode(str, "UTF-8").replaceAll("\\[", "").replaceAll("]", "").toLowerCase();
            String[] strs=partReadOnlyData.split(",");
         //   System.out.println(strs.length);
            if(strs.length!=128)
               // return;

            System.out.println(partReadOnlyData);

            if((strs[3]+strs[2]+strs[1]+strs[0]).equals("ffffffff"))
                return;
            one.set(0,String.valueOf(Integer.parseInt(strs[3]+strs[2]+strs[1]+strs[0], 16)));
            if(!isNumeric(strs[4]+strs[5]+strs[6]+strs[7]+strs[8]+strs[9])){
              //  return;
                one.set(1,"1970-01-01 00:00:00");
                one.set(165,0);
            }else{
                String MM=String.valueOf(Integer.parseInt(strs[5], 10)).length()==2?String.valueOf(Integer.parseInt(strs[5], 10))
                        :"0"+String.valueOf(Integer.parseInt(strs[5], 10));
                String dd= String.valueOf(Integer.parseInt(strs[6], 10)).length()==2? String.valueOf(Integer.parseInt(strs[6], 10))
                        :"0"+ String.valueOf(Integer.parseInt(strs[6], 10));
                String hh=String.valueOf(Integer.parseInt(strs[7], 10)).length()==2?String.valueOf(Integer.parseInt(strs[7], 10))
                        :"0"+String.valueOf(Integer.parseInt(strs[7], 10));
                String mm=String.valueOf(Integer.parseInt(strs[8], 10)).length()==2?String.valueOf(Integer.parseInt(strs[8], 10))
                        :"0"+String.valueOf(Integer.parseInt(strs[8], 10));
                String ss=String.valueOf(Integer.parseInt(strs[9], 10)).length()==2?String.valueOf(Integer.parseInt(strs[9], 10))
                        :"0"+String.valueOf(Integer.parseInt(strs[9], 10));
                if(String.valueOf(Integer.parseInt(strs[4], 10)).length()<2||Integer.parseInt(MM)>12||Integer.parseInt(MM)<1){
                    one.set(165,0);
                }else {
                    one.set(165,1);
                }
                   // return;
//                if(Integer.parseInt(MM)>12||Integer.parseInt(MM)<1)
//                   return;
                one.set(1,"20"+String.valueOf(Integer.parseInt(strs[4], 10))+"-"+
                        MM+"-"+
                        dd+" "+
                        hh+":"+
                        mm+":"+ss);//userID
                String times="20"+String.valueOf(Integer.parseInt(strs[4], 10))+"-"+
                        MM+"-"+
                        dd+" "+
                        hh+":"+
                        mm+":"+ss;
                sdf.format(new Date());
                //try {
                  //  if(new Date().getTime()<sdf.parse(times).getTime()||sdf.parse(times).getTime()<sdf.parse("2017-01-01 00:00:00").getTime())
                    //    return;
                //} catch (ParseException e) {
                    //TODO Auto-generated catch block



            }

            one.set(2,  String.valueOf(Integer.parseInt(strs[10], 16)));
            one.set(3,String.valueOf(Integer.parseInt(strs[11], 16))+
                    String.valueOf(Integer.parseInt(strs[18], 16))+
                    String.valueOf(Integer.parseInt(strs[12], 16))+
                    String.valueOf(Integer.parseInt(strs[13], 16))+
                    String.valueOf(Integer.parseInt(strs[14], 16))+
                    String.valueOf(Integer.parseInt(strs[15], 16))+
                    String.valueOf(Integer.parseInt(strs[16], 16))+
                    String.valueOf(Integer.parseInt(strs[17], 16)));//userID
            String s8=Integer.toBinaryString(Integer.parseInt(strs[19]+strs[20], 16));//BMS状态
            for(int i=0;i<s8.length();i++){
                one.set(i+4,Integer.parseInt(s8.substring(s8.length()-1-i, s8.length()-i)));
            }
            if(s8.length()<16){
                for(int j=0;j<16-s8.length();j++){
                    one.set(s8.length()+4+j,0);
                }
            }



            one.set(20,Integer.parseInt(strs[21]+strs[22], 16)==65535?-100.0:(Integer.parseInt(strs[21]+strs[22], 16)/10.0));//电池组荷电状态
            one.set(21,Integer.parseInt(strs[23]+strs[24], 16)==65535?-100.0:(Integer.parseInt(strs[23]+strs[24], 16)/100.0));//总电压
            // one.set(19,(Integer.parseInt(strs[4]+strs[+15], 16)-300)/100.0);//总电流
            one.set(22,Integer.parseInt(strs[25]+strs[26], 16)==65535?-100.0:((Integer.parseInt(strs[25]+strs[26], 16)-30000)/100.0));//总电流
            //one.set(20,(Integer.parseInt(strs[6]+strs[+17], 16)-40)/10.0);//最高电池温度
            one.set(23,Integer.parseInt(strs[27]+strs[28], 16)==65535?-100.0:((Integer.parseInt(strs[27]+strs[28], 16)-400)/10.0));//最高电池温度
            // one.set(21,Integer.parseInt(strs[1]+strs[1+9], 16));//最高电池温度传感器编号
            one.set(24,Integer.parseInt(strs[29]+strs[30], 16)==65535?-100:Integer.parseInt(strs[29]+strs[30], 16));//最高电池温度传感器编号
            // one.set(22,(Integer.parseInt(strs[0]+strs[+21], 16)-40)/10.0);//最低电池温度
            one.set(25,Integer.parseInt(strs[31]+strs[32], 16)==65535?-100.0:((Integer.parseInt(strs[31]+strs[32], 16)-400)/10.0));//最低电池温度
            //  one.set(23,Integer.parseInt(strs[2]+strs[2+3], 16));//最低电池温度传感器编号
            one.set(26,Integer.parseInt(strs[33]+strs[34], 16)==65535?-100:Integer.parseInt(strs[33]+strs[34], 16));//最低电池温度传感器编号
            //  one.set(24,Integer.parseInt(strs[2]+strs[2+5], 16));//最高单体电压
            one.set(27,Integer.parseInt(strs[35]+strs[36], 16)==65535?-100:Integer.parseInt(strs[35]+strs[36], 16));//最高单体电压
            //  one.set(25,Integer.parseInt(strs[2]+strs[2+7], 16));//最高单体电压电池编号
            one.set(28,Integer.parseInt(strs[37]+strs[38], 16)==65535?-100:Integer.parseInt(strs[37]+strs[38], 16));//最高单体电压电池编号
            //   one.set(26,Integer.parseInt(strs[2]+strs[2+9], 16));//最低单体电压
            one.set(29,Integer.parseInt(strs[39]+strs[40], 16)==65535?-100:Integer.parseInt(strs[39]+strs[40], 16));//最低单体电压
            //  one.set(27,Integer.parseInt(strs[3]+strs[3+1], 16));//最低单体电压电池编号
            one.set(30,Integer.parseInt(strs[41]+strs[42], 16)==65535?-100:Integer.parseInt(strs[41]+strs[42], 16));//最低单体电压电池编号

            //   one.set(28,(Integer.parseInt(strs[2]+strs[+33], 16))/100.0);//10s最大允许放电电流
            one.set(31,Integer.parseInt(strs[43]+strs[44], 16)==65535?-100.0:((Integer.parseInt(strs[43]+strs[44], 16))/100.0));//10s最大允许放电电流
            //    one.set(29,(Integer.parseInt(strs[4]+strs[+35], 16))/100.0);//10s最大允许充电电流
            if((Integer.parseInt(strs[47]+strs[48], 16))/10.0<=0||(Integer.parseInt(strs[47]+strs[48], 16))/10.0>100||Integer.parseInt(strs[49]+strs[50], 16)>3000||record.get("id").toString().equals("ffffffffffff")||record.get("id").toString().equals("000000000000")){
              //  flag=false;
            }
            one.set(32,Integer.parseInt(strs[45]+strs[46], 16)==65535?-100.0:((Integer.parseInt(strs[45]+strs[46], 16))/100.0));//10s最大允许充电电流
            //    one.set(30,(Integer.parseInt(strs[6]+strs[+37], 16))/10.0);//健康状态
            one.set(33,Integer.parseInt(strs[47]+strs[48], 16)==65535?-100.0:((Integer.parseInt(strs[47]+strs[48], 16))/10.0));//健康状态
            //    one.set(31,Integer.parseInt(strs[3]+strs[3+9], 16));//循环次数
            one.set(34,Integer.parseInt(strs[49]+strs[50], 16)==65535?-100:Integer.parseInt(strs[49]+strs[50], 16));//循环次数
            //    one.set(32,Integer.parseInt(strs[4]+strs[4+1], 16));//剩余容量
            one.set(35,Integer.parseInt(strs[51]+strs[52], 16)==65535?-100:Integer.parseInt(strs[51]+strs[52], 16));//剩余容量
            //   one.set(33,Integer.parseInt(strs[4]+strs[4+3], 16));//充满容量
            one.set(36,Integer.parseInt(strs[53]+strs[54], 16)==65535?-100:Integer.parseInt(strs[53]+strs[54], 16));//充满容量
            //   one.set(34,Integer.parseInt(strs[4]+strs[4+5], 16));//充满时间
            one.set(37,Integer.parseInt(strs[55]+strs[56], 16)==65535?-100:Integer.parseInt(strs[55]+strs[56], 16));//充满时间
            //   one.set(35,(Integer.parseInt(strs[6]+strs[+47], 16))/10.0);//剩余能量
            one.set(38,Integer.parseInt(strs[57]+strs[58], 16)==65535?-100.0:((Integer.parseInt(strs[57]+strs[58], 16))/10.0));//剩余能量

            String s54=Integer.toBinaryString(Integer.parseInt(strs[59]+strs[60], 16));//设备故障字1(36-51)
            for(int i=0;i<s54.length();i++){
                one.set(i+39,Integer.parseInt(s54.substring(s54.length()-1-i, s54.length()-i)));
            }
            if(s54.length()<16){
                for(int j=0;j<16-s54.length();j++){
                    one.set(s54.length()+39+j,0);
                }
            }

            String s56=Integer.toBinaryString(Integer.parseInt(strs[61]+strs[62], 16));//设备故障字2(52-67)
            for(int i=0;i<s56.length();i++){
                one.set(i+55,Integer.parseInt(s56.substring(s56.length()-1-i, s56.length()-i)));
            }
            if(s56.length()<16){
                for(int j=0;j<16-s56.length();j++){
                    one.set(s56.length()+55+j,0);
                }
            }

            String s58=Integer.toBinaryString(Integer.parseInt(strs[63]+strs[64], 16));//运行故障字1(68-83)
            for(int i=0;i<s58.length();i++){
                one.set(i+71,Integer.parseInt(s58.substring(s58.length()-1-i, s58.length()-i)));
            }
            if(s58.length()<16){
                for(int j=0;j<16-s58.length();j++){
                    one.set(s58.length()+71+j,0);
                }
            }
            String s60=Integer.toBinaryString(Integer.parseInt(strs[65]+strs[66], 16));//运行故障字2(84-99)
            for(int i=0;i<s60.length();i++){
                one.set(i+87,Integer.parseInt(s60.substring(s60.length()-1-i, s60.length()-i)));
            }
            if(s60.length()<16){
                for(int j=0;j<16-s60.length();j++){
                    one.set(s60.length()+87+j,0);
                }
            }
            String s62=Integer.toBinaryString(Integer.parseInt(strs[67]+strs[68], 16));//运行告警字1(100-115)
            for(int i=0;i<s62.length();i++){
                one.set(i+103,Integer.parseInt(s62.substring(s62.length()-1-i, s62.length()-i)));
            }
            if(s62.length()<16){
                for(int j=0;j<16-s62.length();j++){
                    one.set(s62.length()+103+j,0);
                }
            }
            one.set(119,Integer.parseInt(strs[69]+strs[70], 16));//运行告警字2


            for(int k=71,m=0;k<83;k++,m++){  //117-122   2*6
                one.set(120+m,Integer.parseInt(strs[k]+strs[k+1], 16)==65535?-100.0:((Integer.parseInt(strs[k]+strs[k+1], 16)-400)/10.0));
                k=k+1;

            }

            for(int k=83,m=0;k<116;k++,m++){ //123-138   2*16
                one.set(126+m,Integer.parseInt(strs[k]+strs[k+1], 16)==65535?-100:Integer.parseInt(strs[k]+strs[k+1], 16));
                k=k+1;

            }
            String s106=Integer.toBinaryString(Integer.parseInt(strs[116]+strs[117], 16));//电池均衡详细状态 139-154
            for(int i=0;i<s106.length();i++){
                one.set(i+142,Integer.parseInt(s106.substring(s106.length()-1-i, s106.length()-i)));
            }
            if(s56.length()<16){
                for(int j=0;j<16-s106.length();j++){
                    one.set(s106.length()+142+j,0);
                }
            }

            for(int k=118,m=0;k<124;k++,m++){ //155-160  2*6
                one.set(158+m,Integer.parseInt(strs[k]+strs[k+1], 16));
                k=k+1;

            }
            long times=record.getBigint("collecttime");
            one.setDatetime(161,new Date(times));
            //one.set(161,record.getDatetime("collecttime"));//collectTime
            one.set(162,record.get("pid").toString());//pid
            one.set(163,record.get("id").toString());//id

            boolean flag= crcUtils.ComputeCrc(1, strs, 128, b1, b2);
            if(flag){//crc校验
                one.set(164,1);//id
            }else{
                one.set(164,0);
            }
            context.write(one);
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: WordCount <in_table> <out_table>");
            System.exit(2);
        }
        JobClient.runJob(ToolUtil.runJob(args,Origin_BatteryLogData.TokenizerMapper.class));
    }






}

