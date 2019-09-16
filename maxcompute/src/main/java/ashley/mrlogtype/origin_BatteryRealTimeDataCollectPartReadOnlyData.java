package ashley.mrlogtype;

import ashley.beans.partReadOnlyDataBean;
import battery.HardBatteryRealTimePartOnlyData;
import com.aliyun.odps.data.Record;
import com.aliyun.odps.data.TableInfo;
import com.aliyun.odps.mapred.JobClient;
import com.aliyun.odps.mapred.MapperBase;
import com.aliyun.odps.mapred.ReducerBase;
import com.aliyun.odps.mapred.conf.JobConf;
import com.aliyun.odps.mapred.utils.InputUtils;
import com.aliyun.odps.mapred.utils.OutputUtils;
import com.aliyun.odps.mapred.utils.SchemaUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

public class origin_BatteryRealTimeDataCollectPartReadOnlyData {
    public static class TokenizerMapper extends MapperBase {

        private Record one;
        private Record word;
        partReadOnlyDataBean bean3 = new partReadOnlyDataBean();

        private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        @Override
        public void setup(TaskContext context) throws IOException {
            one=context.createMapOutputValueRecord();
            word = context.createMapOutputKeyRecord();
        }

        @Override
        public void map(long recordNum, Record record, TaskContext context)
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
            String[] strs = str1to.split(",");

            if (strs.length!=122)
                return;

            one.set(0,String.valueOf(Integer.parseInt(strs[0], 16))+
                    String.valueOf(Integer.parseInt(strs[1], 16))+
                    String.valueOf(Integer.parseInt(strs[2], 16))+
                    String.valueOf(Integer.parseInt(strs[3], 16))+
                    String.valueOf(Integer.parseInt(strs[4], 16))+
                    String.valueOf(Integer.parseInt(strs[5], 16))+
                    String.valueOf(Integer.parseInt(strs[6], 16))+
                    String.valueOf(Integer.parseInt(strs[7], 16)));//userID
            String s8=Integer.toBinaryString(Integer.parseInt(strs[8]+strs[9], 16));//BMS状态
            for(int i=0;i<s8.length();i++){
                one.set(i+1,Integer.parseInt(s8.substring(s8.length()-1-i, s8.length()-i)));
            }
            if(s8.length()<16){
                for(int j=0;j<16-s8.length();j++){
                    one.set(s8.length()+1+j,0);
                }
            }

                one.set(167,true);

            if((Integer.parseInt(strs[10]+strs[11], 16)/10.0<0)||(Integer.parseInt(strs[10]+strs[11], 16)/10.0>100.0)){
                one.set(167,false);
            }
            //    return;
            one.set(17,Integer.parseInt(strs[10]+strs[11], 16)==65535?-100.0:(Integer.parseInt(strs[10]+strs[11], 16)/10.0));//电池组荷电状态

            if(((Integer.parseInt(strs[12]+strs[13], 16)/100.0)>300.0)||((Integer.parseInt(strs[12]+strs[13], 16)/100.0)<0))
            {
                one.set(167,false);
            }
            one.set(18,Integer.parseInt(strs[12]+strs[13], 16)==65535?-100.0:(Integer.parseInt(strs[12]+strs[13], 16)/100.0));//总电压

            if(((Integer.parseInt(strs[14]+strs[15], 16)-30000)/100.0>300.0)||((Integer.parseInt(strs[14]+strs[15], 16)-30000)/100.0<-300.0))
            {
                one.set(167,false);
            }
            one.set(19,Integer.parseInt(strs[14]+strs[15], 16)==65535?-100.0:((Integer.parseInt(strs[14]+strs[15], 16)-30000)/100.0));//总电流

            if(((Integer.parseInt(strs[16]+strs[17], 16)-400)/10.0<-40.0)||((Integer.parseInt(strs[16]+strs[17], 16)-400)/10.0>120.0))
            {
                one.set(167,false);
            }
            one.set(20,Integer.parseInt(strs[16]+strs[17], 16)==65535?-100.0:((Integer.parseInt(strs[16]+strs[17], 16)-400)/10.0));//最高电池温度

            if(Integer.parseInt(strs[18]+strs[19], 16)<1||Integer.parseInt(strs[18]+strs[19], 16)>2)
            {
                one.set(167,false);
            }
            one.set(21,Integer.parseInt(strs[18]+strs[19], 16)==65535?-100:Integer.parseInt(strs[18]+strs[19], 16));//最高电池温度传感器编号

            if(((Integer.parseInt(strs[20]+strs[21], 16)-400)/10.0<-40.0)||((Integer.parseInt(strs[20]+strs[21], 16)-400)/10.0>120.0))
            {
                one.set(167,false);
            }
            one.set(22,Integer.parseInt(strs[20]+strs[21], 16)==65535?-100.0:((Integer.parseInt(strs[20]+strs[21], 16)-400)/10.0));//最低电池温度

            if(Integer.parseInt(strs[22]+strs[23], 16)<1||Integer.parseInt(strs[22]+strs[23], 16)>2)
            {
                one.set(167,false);
            }
            one.set(23,Integer.parseInt(strs[22]+strs[23], 16)==65535?-100:Integer.parseInt(strs[22]+strs[23], 16));//最低电池温度传感器编号

            if(Integer.parseInt(strs[24]+strs[25], 16)>5000||Integer.parseInt(strs[24]+strs[25], 16)<0)
            {
                one.set(167,false);
            }
            one.set(24,Integer.parseInt(strs[24]+strs[25], 16)==65535?-100:Integer.parseInt(strs[24]+strs[25], 16));//最高单体电压

            if(Integer.parseInt(strs[26]+strs[27], 16)>16||Integer.parseInt(strs[26]+strs[27], 16)<1)
            {
                one.set(167,false);
            }
            one.set(25,Integer.parseInt(strs[26]+strs[27], 16)==65535?-100:Integer.parseInt(strs[26]+strs[27], 16));//最高单体电压电池编号

            if(Integer.parseInt(strs[28]+strs[29], 16)>5000||Integer.parseInt(strs[28]+strs[29], 16)<0)
            {
                one.set(167,false);
            }
            one.set(26,Integer.parseInt(strs[28]+strs[29], 16)==65535?-100:Integer.parseInt(strs[28]+strs[29], 16));//最低单体电压

            if(Integer.parseInt(strs[30]+strs[31], 16)>16||Integer.parseInt(strs[30]+strs[31], 16)<1)
            {
                one.set(167,false);
            }
            one.set(27,Integer.parseInt(strs[30]+strs[31], 16)==65535?-100:Integer.parseInt(strs[30]+strs[31], 16));//最低单体电压电池编号

            if(((Integer.parseInt(strs[32]+strs[33], 16))/100.0)<0||((Integer.parseInt(strs[32]+strs[33], 16))/100.0)>300.0)
            {
                one.set(167,false);
            }
            one.set(28,Integer.parseInt(strs[32]+strs[33], 16)==65535?-100.0:((Integer.parseInt(strs[32]+strs[33], 16))/100.0));//10s最大允许放电电流

            if(((Integer.parseInt(strs[34]+strs[35], 16))/100.0)<0||((Integer.parseInt(strs[34]+strs[35], 16))/100.0)>300.0)
            {
                one.set(167,false);
            }
            one.set(29,Integer.parseInt(strs[34]+strs[35], 16)==65535?-100.0:((Integer.parseInt(strs[34]+strs[35], 16))/100.0));//10s最大允许充电电流

            if((Integer.parseInt(strs[36]+strs[37], 16))/10.0<=0||(Integer.parseInt(strs[36]+strs[37], 16))/10.0>100||Integer.parseInt(strs[38]+strs[39], 16)>3000||bean3.getbId().equals("ffffffffffff")||bean3.getbId().equals("000000000000")){

                    one.set(167,false);
            }
            one.set(30,Integer.parseInt(strs[36]+strs[37], 16)==65535?-100.0:((Integer.parseInt(strs[36]+strs[37], 16))/10.0));//健康状态
            //    one.set(31,Integer.parseInt(strs[38]+strs[39], 16));//循环次数
            one.set(31,Integer.parseInt(strs[38]+strs[39], 16)==65535?-100:Integer.parseInt(strs[38]+strs[39], 16));//循环次数

            if(Integer.parseInt(strs[40]+strs[41], 16)>65000||Integer.parseInt(strs[40]+strs[41], 16)<0)
            {
                one.set(167,false);
            }
            one.set(32,Integer.parseInt(strs[40]+strs[41], 16)==65535?-100:Integer.parseInt(strs[40]+strs[41], 16));//剩余容量

            if(Integer.parseInt(strs[42]+strs[43], 16)>65000||Integer.parseInt(strs[42]+strs[43], 16)<0)
            {
                one.set(167,false);
            }
            one.set(33,Integer.parseInt(strs[42]+strs[43], 16)==65535?-100:Integer.parseInt(strs[42]+strs[43], 16));//充满容量

            if(Integer.parseInt(strs[44]+strs[45], 16)>65000||Integer.parseInt(strs[44]+strs[45], 16)<0)
            {
                one.set(167,false);
            }
            one.set(34,Integer.parseInt(strs[44]+strs[45], 16)==65535?-100:Integer.parseInt(strs[44]+strs[45], 16));//充满时间

            if(((Integer.parseInt(strs[46]+strs[47], 16))/10.0)>6500.0||((Integer.parseInt(strs[46]+strs[47], 16))/10.0)<0)
            {
                one.set(167,false);
            }
            one.set(35,Integer.parseInt(strs[46]+strs[47], 16)==65535?-100.0:((Integer.parseInt(strs[46]+strs[47], 16))/10.0));//剩余能量


            String s54=Integer.toBinaryString(Integer.parseInt(strs[48]+strs[49], 16));//设备故障字1(36-51)
            for(int i=0;i<s54.length();i++){
                one.set(i+36,Integer.parseInt(s54.substring(s54.length()-1-i, s54.length()-i)));
            }
            if(s54.length()<16){
                for(int j=0;j<16-s54.length();j++){
                    one.set(s54.length()+36+j,0);
                }
            }

            String s56=Integer.toBinaryString(Integer.parseInt(strs[50]+strs[51], 16));//设备故障字2(52-67)
            for(int i=0;i<s56.length();i++){
                one.set(i+52,Integer.parseInt(s56.substring(s56.length()-1-i, s56.length()-i)));
            }
            if(s56.length()<16){
                for(int j=0;j<16-s56.length();j++){
                    one.set(s56.length()+52+j,0);
                }
            }

            String s58=Integer.toBinaryString(Integer.parseInt(strs[52]+strs[53], 16));//运行故障字1(68-83)
            for(int i=0;i<s58.length();i++){
                one.set(i+68,Integer.parseInt(s58.substring(s58.length()-1-i, s58.length()-i)));
            }
            if(s58.length()<16){
                for(int j=0;j<16-s58.length();j++){
                    one.set(s58.length()+68+j,0);
                }
            }
            String s60=Integer.toBinaryString(Integer.parseInt(strs[54]+strs[55], 16));//运行故障字2(84-99)
            for(int i=0;i<s60.length();i++){
                one.set(i+84,Integer.parseInt(s60.substring(s60.length()-1-i, s60.length()-i)));
            }
            if(s60.length()<16){
                for(int j=0;j<16-s60.length();j++){
                    one.set(s60.length()+84+j,0);
                }
            }
            String s62=Integer.toBinaryString(Integer.parseInt(strs[56]+strs[57], 16));//运行告警字1(100-115)
            for(int i=0;i<s62.length();i++){
                one.set(i+100,Integer.parseInt(s62.substring(s62.length()-1-i, s62.length()-i)));
            }
            if(s62.length()<16){
                for(int j=0;j<16-s62.length();j++){
                    one.set(s62.length()+100+j,0);
                }
            }
            one.set(116,Integer.parseInt(strs[58]+strs[59], 16));//运行告警字2

//

            for(int k=60,m=0;k<72;k++,m++){  //117-122   2*6
                if(((Integer.parseInt(strs[k]+strs[k+1], 16)-400)/10.0)>120.0||((Integer.parseInt(strs[k]+strs[k+1], 16)-400)/10.0)<-40.0)//温度
                {
                    one.set(167,false);
                }
                one.set(117+m,Integer.parseInt(strs[k]+strs[k+1], 16)==65535?-100.0:((Integer.parseInt(strs[k]+strs[k+1], 16)-400)/10.0));
                k=k+1;

            }
//	        for(int k=72,m=0;k<104;k++,m++){ //123-138   2*16
//	        	one.set(123+m,Integer.parseInt(strs[k]+strs[k+1], 16));
//	        	k=k+1;
//
//	        }
            for(int k=72,m=0;k<104;k++,m++){ //123-138   2*16
                if((Integer.parseInt(strs[118]+strs[119], 16))/100.0==53.95){//13个串联
                    if ((Integer.parseInt(strs[k] + strs[k + 1], 16) > 5000 || Integer.parseInt(strs[k] + strs[k + 1], 16) < 0)&&k!=88&k!=98&k!=102) {
                        one.set(167, false);
                    }
                }else { //16个串联
                    if (Integer.parseInt(strs[k] + strs[k + 1], 16) > 5000 || Integer.parseInt(strs[k] + strs[k + 1], 16) < 0) {
                        one.set(167, false);
                    }
                }
                one.set(123+m,Integer.parseInt(strs[k]+strs[k+1], 16)==65535?-100:Integer.parseInt(strs[k]+strs[k+1], 16));
                k=k+1;

            }
            String s106=Integer.toBinaryString(Integer.parseInt(strs[104]+strs[105], 16));//电池均衡详细状态 139-154
            for(int i=0;i<s106.length();i++){
                one.set(i+139,Integer.parseInt(s106.substring(s106.length()-1-i, s106.length()-i)));
            }
            if(s56.length()<16){
                for(int j=0;j<16-s106.length();j++){
                    one.set(s106.length()+139+j,0);
                }
            }

            for(int k=106,m=0;k<118;k++,m++){ //155-160  2*6
                one.set(155+m,Integer.parseInt(strs[k]+strs[k+1], 16));
                k=k+1;

            }
            if((Integer.parseInt(strs[118]+strs[119], 16))/100.0>300.0||(Integer.parseInt(strs[118]+strs[119], 16))/100.0<0)
            {
                one.set(167,false);
            }
            one.set(161,(Integer.parseInt(strs[118]+strs[119], 16))/100.0);//最大充电电压
            one.set(162,(Integer.parseInt(strs[120]+strs[121], 16)));//电量计电压
            long times=record.getBigint("collecttime");
            one.setDatetime(163,new Date(times));
            one.set(164, bean3.getpId());;//pid

            one.setString(165, bean3.getbId());//id
            one.setBigint(166, (long) bean3.getPort());
//                        if((Integer.parseInt(strs[10]+strs[11], 16)/10.0<0)||(Integer.parseInt(strs[10]+strs[11], 16)/10.0>100.0)||
//                    ((Integer.parseInt(strs[12]+strs[13], 16)/100.0)>300.0)||((Integer.parseInt(strs[12]+strs[13], 16)/100.0)<0) ||
//                    ((Integer.parseInt(strs[14]+strs[15], 16)-30000)/100.0>300.0)||((Integer.parseInt(strs[14]+strs[15], 16)-30000)/100.0<-300.0) ||
//                    ((Integer.parseInt(strs[16]+strs[17], 16)-400)/10.0<-40.0)||((Integer.parseInt(strs[16]+strs[17], 16)-400)/10.0>120.0)||
//                    (Integer.parseInt(strs[18]+strs[19], 16)<1||Integer.parseInt(strs[18]+strs[19], 16)>2)||
//                    ((Integer.parseInt(strs[20]+strs[21], 16)-400)/10.0<-40.0)||((Integer.parseInt(strs[20]+strs[21], 16)-400)/10.0>120.0) ||
//                    (Integer.parseInt(strs[22]+strs[23], 16)<1||Integer.parseInt(strs[22]+strs[23], 16)>2)||
//                    (Integer.parseInt(strs[24]+strs[25], 16)>5000||Integer.parseInt(strs[24]+strs[25], 16)<0)||
//                    (Integer.parseInt(strs[26]+strs[27], 16)>16||Integer.parseInt(strs[26]+strs[27], 16)<1)||
//                    (Integer.parseInt(strs[28]+strs[29], 16)>5000||Integer.parseInt(strs[28]+strs[29], 16)<0)||
//                    (Integer.parseInt(strs[30]+strs[31], 16)>16||Integer.parseInt(strs[30]+strs[31], 16)<1)||
//                    (((Integer.parseInt(strs[32]+strs[33], 16))/100.0)<0||((Integer.parseInt(strs[32]+strs[33], 16))/100.0)>300.0)||
//                    (((Integer.parseInt(strs[34]+strs[35], 16))/100.0)<0||((Integer.parseInt(strs[34]+strs[35], 16))/100.0)>300.0)||
//                    ((Integer.parseInt(strs[36]+strs[37], 16))/10.0<=0||(Integer.parseInt(strs[36]+strs[37], 16))/10.0>100||Integer.parseInt(strs[38]+strs[39], 16)>3000||record.get("id").toString().equals("ffffffffffff")||record.get("id").toString().equals("000000000000"))||
//                    (Integer.parseInt(strs[40]+strs[41], 16)>65000||Integer.parseInt(strs[40]+strs[41], 16)<0)||
//                    (Integer.parseInt(strs[42]+strs[43], 16)>65000||Integer.parseInt(strs[42]+strs[43], 16)<0)||
//                    (Integer.parseInt(strs[44]+strs[45], 16)>65000||Integer.parseInt(strs[44]+strs[45], 16)<0)||
//                    (((Integer.parseInt(strs[46]+strs[47], 16))/10.0)>6500.0||((Integer.parseInt(strs[46]+strs[47], 16))/10.0)<0)||
//                    (((Integer.parseInt(strs[60]+strs[61], 16)-400)/10.0)>120.0||((Integer.parseInt(strs[60]+strs[61], 16)-400)/10.0)<-40.0)||
//                    (((Integer.parseInt(strs[62]+strs[63], 16)-400)/10.0)>120.0||((Integer.parseInt(strs[62]+strs[63], 16)-400)/10.0)<-40.0)||
//                    (((Integer.parseInt(strs[64]+strs[65], 16)-400)/10.0)>120.0||((Integer.parseInt(strs[64]+strs[65], 16)-400)/10.0)<-40.0)||
//                    (((Integer.parseInt(strs[66]+strs[67], 16)-400)/10.0)>120.0||((Integer.parseInt(strs[66]+strs[67], 16)-400)/10.0)<-40.0)||
//                    (((Integer.parseInt(strs[68]+strs[69], 16)-400)/10.0)>120.0||((Integer.parseInt(strs[68]+strs[69], 16)-400)/10.0)<-40.0)||
//                    (((Integer.parseInt(strs[70]+strs[71], 16)-400)/10.0)>120.0||((Integer.parseInt(strs[70]+strs[71], 16)-400)/10.0)<-40.0)||
//                    (Integer.parseInt(strs[72]+strs[73], 16)>5000||Integer.parseInt(strs[72]+strs[73], 16)<0)||
//                    (Integer.parseInt(strs[74]+strs[75], 16)>5000||Integer.parseInt(strs[74]+strs[75], 16)<0)||
//                    (Integer.parseInt(strs[76]+strs[77], 16)>5000||Integer.parseInt(strs[76]+strs[77], 16)<0)||
//                    (Integer.parseInt(strs[78]+strs[79], 16)>5000||Integer.parseInt(strs[78]+strs[79], 16)<0)||
//                    (Integer.parseInt(strs[80]+strs[81], 16)>5000||Integer.parseInt(strs[80]+strs[81], 16)<0)||
//                    (Integer.parseInt(strs[82]+strs[83], 16)>5000||Integer.parseInt(strs[82]+strs[83], 16)<0)||
//                    (Integer.parseInt(strs[84]+strs[85], 16)>5000||Integer.parseInt(strs[84]+strs[85], 16)<0)||
//                    (Integer.parseInt(strs[86]+strs[87], 16)>5000||Integer.parseInt(strs[86]+strs[87], 16)<0)||
//                    (Integer.parseInt(strs[88]+strs[89], 16)>5000||Integer.parseInt(strs[88]+strs[89], 16)<0)||
//                    (Integer.parseInt(strs[90]+strs[91], 16)>5000||Integer.parseInt(strs[90]+strs[91], 16)<0)||
//                    (Integer.parseInt(strs[92]+strs[93], 16)>5000||Integer.parseInt(strs[92]+strs[93], 16)<0)||
//                    (Integer.parseInt(strs[94]+strs[95], 16)>5000||Integer.parseInt(strs[94]+strs[95], 16)<0)||
//                    (Integer.parseInt(strs[96]+strs[97], 16)>5000||Integer.parseInt(strs[96]+strs[97], 16)<0)||
//                    (Integer.parseInt(strs[98]+strs[99], 16)>5000||Integer.parseInt(strs[98]+strs[99], 16)<0)||
//                    (Integer.parseInt(strs[100]+strs[101], 16)>5000||Integer.parseInt(strs[100]+strs[101], 16)<0)||
//                    (Integer.parseInt(strs[102]+strs[103], 16)>5000||Integer.parseInt(strs[102]+strs[103], 16)<0)||
//                    (Integer.parseInt(strs[118]+strs[119], 16))/100.0>300.0||(Integer.parseInt(strs[118]+strs[119], 16))/100.0<0
//                    ){
//
//
//            }
            word.set(0,bean3.getbId());
            context.write(word,one);
        }




    }
    public static class SumReducer extends ReducerBase {
        private Record result;
        private Record result1;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//这个是你要转成后的时间的格式

        @Override
        public void setup(TaskContext context) throws IOException {
            // 对于不同的输出需要创建不同的record，通过label来区分
            result = context.createOutputRecord();
            result1=context.createOutputRecord("error");
        }
        public  double formatDouble1(double d) {
            return (double)Math.round(d*100)/100;
        }
        @Override
        public void reduce(Record key, Iterator<Record> values, TaskContext context)
                throws IOException {
            if(key.get(0)==null){
                return;
            }
            String id=key.getString(0);
            List<HardBatteryRealTimePartOnlyData> list=new ArrayList<HardBatteryRealTimePartOnlyData>();
            List<Double> speeds=new ArrayList<Double>();
            while(values.hasNext()){
                Record record=values.next();
                HardBatteryRealTimePartOnlyData hardBatteryRealTimePartOnlyData=new HardBatteryRealTimePartOnlyData();


                hardBatteryRealTimePartOnlyData.setUserid(record.getString(0));


                hardBatteryRealTimePartOnlyData.setState_0(record.getBigint(1));
                hardBatteryRealTimePartOnlyData.setState_1(record.getBigint(2));
                hardBatteryRealTimePartOnlyData.setState_2(record.getBigint(3));
                hardBatteryRealTimePartOnlyData.setState_3(record.getBigint(4));
                hardBatteryRealTimePartOnlyData.setState_4(record.getBigint(5));
                hardBatteryRealTimePartOnlyData.setState_5(record.getBigint(6));
                hardBatteryRealTimePartOnlyData.setState_6(record.getBigint(7));
                hardBatteryRealTimePartOnlyData.setState_7(record.getBigint(8));
                hardBatteryRealTimePartOnlyData.setState_8(record.getBigint(9));
                hardBatteryRealTimePartOnlyData.setState_9(record.getBigint(10));
                hardBatteryRealTimePartOnlyData.setState_10(record.getBigint(11));
                hardBatteryRealTimePartOnlyData.setState_11(record.getBigint(12));
                hardBatteryRealTimePartOnlyData.setState_12(record.getBigint(13));
                hardBatteryRealTimePartOnlyData.setState_13(record.getBigint(14));
                hardBatteryRealTimePartOnlyData.setState_14(record.getBigint(15));
                hardBatteryRealTimePartOnlyData.setState_15(record.getBigint(16));


                hardBatteryRealTimePartOnlyData.setSoc(record.getDouble(17));
                hardBatteryRealTimePartOnlyData.setTvolt(record.getDouble(18));
                hardBatteryRealTimePartOnlyData.setTcurr(record.getDouble(19));
                hardBatteryRealTimePartOnlyData.setHtemp(record.getDouble(20));
                hardBatteryRealTimePartOnlyData.setHtnum(record.getBigint(21));
                hardBatteryRealTimePartOnlyData.setLtemp(record.getDouble(22));
                hardBatteryRealTimePartOnlyData.setLtnum(record.getBigint(23));
                hardBatteryRealTimePartOnlyData.setHvolt(record.getBigint(24));
                hardBatteryRealTimePartOnlyData.setHvnum(record.getBigint(25));
                hardBatteryRealTimePartOnlyData.setLvolt(record.getBigint(26));
                hardBatteryRealTimePartOnlyData.setLvnum(record.getBigint(27));
                hardBatteryRealTimePartOnlyData.setDsop(record.getDouble(28));
                hardBatteryRealTimePartOnlyData.setCsop(record.getDouble(29));
                hardBatteryRealTimePartOnlyData.setSoh(record.getDouble(30));
                hardBatteryRealTimePartOnlyData.setCycle(record.getBigint(31));
                hardBatteryRealTimePartOnlyData.setRcap(record.getBigint(32));
                hardBatteryRealTimePartOnlyData.setFcap(record.getBigint(33));
                hardBatteryRealTimePartOnlyData.setFctime(record.getBigint(34));
                hardBatteryRealTimePartOnlyData.setRpow(record.getDouble(35));


                hardBatteryRealTimePartOnlyData.setDevft1_0(record.getBigint(36));
                hardBatteryRealTimePartOnlyData.setDevft1_1(record.getBigint(37));
                hardBatteryRealTimePartOnlyData.setDevft1_2(record.getBigint(38));
                hardBatteryRealTimePartOnlyData.setDevft1_3(record.getBigint(39));
                hardBatteryRealTimePartOnlyData.setDevft1_4(record.getBigint(40));
                hardBatteryRealTimePartOnlyData.setDevft1_5(record.getBigint(41));
                hardBatteryRealTimePartOnlyData.setDevft1_6(record.getBigint(42));
                hardBatteryRealTimePartOnlyData.setDevft1_7(record.getBigint(43));
                hardBatteryRealTimePartOnlyData.setDevft1_8(record.getBigint(44));
                hardBatteryRealTimePartOnlyData.setDevft1_9(record.getBigint(45));
                hardBatteryRealTimePartOnlyData.setDevft1_10(record.getBigint(46));
                hardBatteryRealTimePartOnlyData.setDevft1_11(record.getBigint(47));
                hardBatteryRealTimePartOnlyData.setDevft1_12(record.getBigint(48));
                hardBatteryRealTimePartOnlyData.setDevft1_13(record.getBigint(49));
                hardBatteryRealTimePartOnlyData.setDevft1_14(record.getBigint(50));
                hardBatteryRealTimePartOnlyData.setDevft1_15(record.getBigint(51));


                hardBatteryRealTimePartOnlyData.setDevft2_0(record.getBigint(52));
                hardBatteryRealTimePartOnlyData.setDevft2_1(record.getBigint(53));
                hardBatteryRealTimePartOnlyData.setDevft2_2(record.getBigint(54));
                hardBatteryRealTimePartOnlyData.setDevft2_3(record.getBigint(55));
                hardBatteryRealTimePartOnlyData.setDevft2_4(record.getBigint(56));
                hardBatteryRealTimePartOnlyData.setDevft2_5(record.getBigint(57));
                hardBatteryRealTimePartOnlyData.setDevft2_6(record.getBigint(58));
                hardBatteryRealTimePartOnlyData.setDevft2_7(record.getBigint(59));
                hardBatteryRealTimePartOnlyData.setDevft2_8(record.getBigint(60));
                hardBatteryRealTimePartOnlyData.setDevft2_9(record.getBigint(61));
                hardBatteryRealTimePartOnlyData.setDevft2_10(record.getBigint(62));
                hardBatteryRealTimePartOnlyData.setDevft2_11(record.getBigint(63));
                hardBatteryRealTimePartOnlyData.setDevft2_12(record.getBigint(64));
                hardBatteryRealTimePartOnlyData.setDevft2_13(record.getBigint(65));
                hardBatteryRealTimePartOnlyData.setDevft2_14(record.getBigint(66));
                hardBatteryRealTimePartOnlyData.setDevft2_15(record.getBigint(67));


                hardBatteryRealTimePartOnlyData.setOpft1_0(record.getBigint(68));
                hardBatteryRealTimePartOnlyData.setOpft1_1(record.getBigint(69));
                hardBatteryRealTimePartOnlyData.setOpft1_2(record.getBigint(70));
                hardBatteryRealTimePartOnlyData.setOpft1_3(record.getBigint(71));
                hardBatteryRealTimePartOnlyData.setOpft1_4(record.getBigint(72));
                hardBatteryRealTimePartOnlyData.setOpft1_5(record.getBigint(73));
                hardBatteryRealTimePartOnlyData.setOpft1_6(record.getBigint(74));
                hardBatteryRealTimePartOnlyData.setOpft1_7(record.getBigint(75));
                hardBatteryRealTimePartOnlyData.setOpft1_8(record.getBigint(76));
                hardBatteryRealTimePartOnlyData.setOpft1_9(record.getBigint(77));
                hardBatteryRealTimePartOnlyData.setOpft1_10(record.getBigint(78));
                hardBatteryRealTimePartOnlyData.setOpft1_11(record.getBigint(79));
                hardBatteryRealTimePartOnlyData.setOpft1_12(record.getBigint(80));
                hardBatteryRealTimePartOnlyData.setOpft1_13(record.getBigint(81));
                hardBatteryRealTimePartOnlyData.setOpft1_14(record.getBigint(82));
                hardBatteryRealTimePartOnlyData.setOpft1_15(record.getBigint(83));


                hardBatteryRealTimePartOnlyData.setOpft2_0(record.getBigint(84));
                hardBatteryRealTimePartOnlyData.setOpft2_1(record.getBigint(85));
                hardBatteryRealTimePartOnlyData.setOpft2_2(record.getBigint(86));
                hardBatteryRealTimePartOnlyData.setOpft2_3(record.getBigint(87));
                hardBatteryRealTimePartOnlyData.setOpft2_4(record.getBigint(88));
                hardBatteryRealTimePartOnlyData.setOpft2_5(record.getBigint(89));
                hardBatteryRealTimePartOnlyData.setOpft2_6(record.getBigint(90));
                hardBatteryRealTimePartOnlyData.setOpft2_7(record.getBigint(91));
                hardBatteryRealTimePartOnlyData.setOpft2_8(record.getBigint(92));
                hardBatteryRealTimePartOnlyData.setOpft2_9(record.getBigint(93));
                hardBatteryRealTimePartOnlyData.setOpft2_10(record.getBigint(94));
                hardBatteryRealTimePartOnlyData.setOpft2_11(record.getBigint(95));
                hardBatteryRealTimePartOnlyData.setOpft2_12(record.getBigint(96));
                hardBatteryRealTimePartOnlyData.setOpft2_13(record.getBigint(97));
                hardBatteryRealTimePartOnlyData.setOpft2_14(record.getBigint(98));
                hardBatteryRealTimePartOnlyData.setOpft2_15(record.getBigint(99));

                hardBatteryRealTimePartOnlyData.setOpwarn_0(record.getBigint(100));
                hardBatteryRealTimePartOnlyData.setOpwarn_1(record.getBigint(101));
                hardBatteryRealTimePartOnlyData.setOpwarn_2(record.getBigint(102));
                hardBatteryRealTimePartOnlyData.setOpwarn_3(record.getBigint(103));
                hardBatteryRealTimePartOnlyData.setOpwarn_4(record.getBigint(104));
                hardBatteryRealTimePartOnlyData.setOpwarn_5(record.getBigint(105));
                hardBatteryRealTimePartOnlyData.setOpwarn_6(record.getBigint(106));
                hardBatteryRealTimePartOnlyData.setOpwarn_7(record.getBigint(107));
                hardBatteryRealTimePartOnlyData.setOpwarn_8(record.getBigint(108));
                hardBatteryRealTimePartOnlyData.setOpwarn_9(record.getBigint(109));
                hardBatteryRealTimePartOnlyData.setOpwarn_10(record.getBigint(110));
                hardBatteryRealTimePartOnlyData.setOpwarn_11(record.getBigint(111));
                hardBatteryRealTimePartOnlyData.setOpwarn_12(record.getBigint(112));
                hardBatteryRealTimePartOnlyData.setOpwarn_13(record.getBigint(113));
                hardBatteryRealTimePartOnlyData.setOpwarn_14(record.getBigint(114));
                hardBatteryRealTimePartOnlyData.setOpwarn_15(record.getBigint(115));

                hardBatteryRealTimePartOnlyData.setOpwarn2(record.getBigint(116));

                hardBatteryRealTimePartOnlyData.setCmost(record.getDouble(117));
                hardBatteryRealTimePartOnlyData.setDmost(record.getDouble(118));
                hardBatteryRealTimePartOnlyData.setFuelt(record.getDouble(119));
                hardBatteryRealTimePartOnlyData.setCont(record.getDouble(120));
                hardBatteryRealTimePartOnlyData.setBtemp1(record.getDouble(121));
                hardBatteryRealTimePartOnlyData.setBtemp2(record.getDouble(122));

                hardBatteryRealTimePartOnlyData.setBvolt1(record.getBigint(123));
                hardBatteryRealTimePartOnlyData.setBvolt2(record.getBigint(124));
                hardBatteryRealTimePartOnlyData.setBvolt3(record.getBigint(125));
                hardBatteryRealTimePartOnlyData.setBvolt4(record.getBigint(126));
                hardBatteryRealTimePartOnlyData.setBvolt5(record.getBigint(127));
                hardBatteryRealTimePartOnlyData.setBvolt6(record.getBigint(128));
                hardBatteryRealTimePartOnlyData.setBvolt7(record.getBigint(129));
                hardBatteryRealTimePartOnlyData.setBvolt8(record.getBigint(130));
                hardBatteryRealTimePartOnlyData.setBvolt9(record.getBigint(131));
                hardBatteryRealTimePartOnlyData.setBvolt10(record.getBigint(132));
                hardBatteryRealTimePartOnlyData.setBvolt11(record.getBigint(133));
                hardBatteryRealTimePartOnlyData.setBvolt12(record.getBigint(134));
                hardBatteryRealTimePartOnlyData.setBvolt13(record.getBigint(135));
                hardBatteryRealTimePartOnlyData.setBvolt14(record.getBigint(136));
                hardBatteryRealTimePartOnlyData.setBvolt15(record.getBigint(137));
                hardBatteryRealTimePartOnlyData.setBvolt16(record.getBigint(138));


                hardBatteryRealTimePartOnlyData.setBalasta_0(record.getBigint(139));
                hardBatteryRealTimePartOnlyData.setBalasta_1(record.getBigint(140));
                hardBatteryRealTimePartOnlyData.setBalasta_2(record.getBigint(141));
                hardBatteryRealTimePartOnlyData.setBalasta_3(record.getBigint(142));
                hardBatteryRealTimePartOnlyData.setBalasta_4(record.getBigint(143));
                hardBatteryRealTimePartOnlyData.setBalasta_5(record.getBigint(144));
                hardBatteryRealTimePartOnlyData.setBalasta_6(record.getBigint(145));
                hardBatteryRealTimePartOnlyData.setBalasta_7(record.getBigint(146));
                hardBatteryRealTimePartOnlyData.setBalasta_8(record.getBigint(147));
                hardBatteryRealTimePartOnlyData.setBalasta_9(record.getBigint(148));
                hardBatteryRealTimePartOnlyData.setBalasta_10(record.getBigint(149));
                hardBatteryRealTimePartOnlyData.setBalasta_11(record.getBigint(150));
                hardBatteryRealTimePartOnlyData.setBalasta_12(record.getBigint(151));
                hardBatteryRealTimePartOnlyData.setBalasta_13(record.getBigint(152));
                hardBatteryRealTimePartOnlyData.setBalasta_14(record.getBigint(153));
                hardBatteryRealTimePartOnlyData.setBalasta_15(record.getBigint(154));
                hardBatteryRealTimePartOnlyData.setAccelerated_speed_x(record.getBigint(155));
                hardBatteryRealTimePartOnlyData.setAccelerated_speed_y(record.getBigint(156));
                hardBatteryRealTimePartOnlyData.setAccelerated_speed_z(record.getBigint(157));
                hardBatteryRealTimePartOnlyData.setMcu3v3(record.getBigint(158));
                hardBatteryRealTimePartOnlyData.setPre_electric_infer_vol(record.getBigint(159));
                hardBatteryRealTimePartOnlyData.setElectric_quantity_ma(record.getBigint(160));
                hardBatteryRealTimePartOnlyData.setMax_charge_electric(record.getDouble(161));
                hardBatteryRealTimePartOnlyData.setElectric_meter_charge(record.getBigint(162));
                hardBatteryRealTimePartOnlyData.setCollecttime(record.getDatetime(163));
                hardBatteryRealTimePartOnlyData.setPid(record.getString(164));
                hardBatteryRealTimePartOnlyData.setBid(record.getString(165));
                hardBatteryRealTimePartOnlyData.setPort(record.getBigint(166));
                hardBatteryRealTimePartOnlyData.setFlag(record.getBoolean(167));


                list.add(hardBatteryRealTimePartOnlyData);

            }
            Collections.sort(list);
            if("101c1d5b49f4".equals(id)){
                for(int m=0;m<list.size();m++){
                    System.out.println("origin="+list.get(m).getCycle()+" "+list.get(m).getCollecttime());
                }
            }
            HardBatteryRealTimePartOnlyData[] array = list.toArray(new HardBatteryRealTimePartOnlyData[list.size()] );
//            for(int n = 0;n<array.length-1;n++){
//                //遍历数组，判断找出需删除元素的位置
//
//                if((array[n].getCycle()>array[n+1].getCycle()&&(array[n+1].getCycle()!=-1))||(
//                        (array[n].getCycle()+6)<array[n+1].getCycle()&&(array[n+1].getCycle()!=-1)
//                        )){
//                    for (int m=n+1;m<array.length-1;m++){
//                        array[m]=array[m+1];
//                    }
//                    HardBatteryRealTimePartOnlyData hardBatteryRealTimePartOnlyData=new HardBatteryRealTimePartOnlyData();
//                    hardBatteryRealTimePartOnlyData.setCycle(-1);
//                    array[array.length-1]=hardBatteryRealTimePartOnlyData;
//                    n--;
//                }
//            }




            for(int n = 0;n<array.length;n++) {
                //遍历数组，判断找出需删除元素的位置
                //System.out.println(array[n].getSoc());
                if (array.length>=3&&n == array.length - 2 && array[n].getCycle() != -1) {
                    if ((array[n].getCycle() != array[n - 1].getCycle() || array[n].getCycle() != array[n + 1].getCycle()) && (
                            array[n].getCycle() != array[n - 1].getCycle() || array[n].getCycle() != array[n - 2].getCycle()
                    )) {

                        for (int k = n; k < array.length - 1; k++) {
                            array[k] = array[k + 1];
                        }
                        //数组最后一个元素赋为控制
                        HardBatteryRealTimePartOnlyData hardBatteryRealTimePartOnlyData = new HardBatteryRealTimePartOnlyData();
                        hardBatteryRealTimePartOnlyData.setCycle(-1);
                        array[array.length - 1] = hardBatteryRealTimePartOnlyData;
                        n--;

                    }
                } else if (array.length>=3&&n == array.length - 1 && array[n].getCycle() != -1) {
                    if ((
                            array[n].getCycle() != array[n - 1].getCycle() || array[n].getCycle() != array[n - 2].getCycle()
                    )) {


                        //数组最后一个元素赋为控制
                        HardBatteryRealTimePartOnlyData hardBatteryRealTimePartOnlyData = new HardBatteryRealTimePartOnlyData();
                        hardBatteryRealTimePartOnlyData.setCycle(-1);
                        array[array.length - 1] = hardBatteryRealTimePartOnlyData;
                        n--;

                    }

                } else if (array[n].getCycle() != -1&&array.length>=3) {
                    if (array[n].getCycle() != array[n + 1].getCycle() || array[n].getCycle() != array[n + 2].getCycle()) {
                        if (n == 1) {
                            if (array[n].getCycle() != array[n - 1].getCycle() || array[n].getCycle() != array[n + 1].getCycle()) {
                                for (int k = n; k < array.length - 1; k++) {
                                    array[k] = array[k + 1];
                                }
                                //数组最后一个元素赋为控制
                                HardBatteryRealTimePartOnlyData hardBatteryRealTimePartOnlyData = new HardBatteryRealTimePartOnlyData();
                                hardBatteryRealTimePartOnlyData.setCycle(-1);
                                array[array.length - 1] = hardBatteryRealTimePartOnlyData;
                                n--;
                            }
                        } else if (n > 1) {
                            if ((array[n].getCycle() != array[n - 1].getCycle() || array[n].getCycle() != array[n - 2].getCycle()) &&
                                    (array[n].getCycle() != array[n - 1].getCycle() || array[n].getCycle() != array[n + 1].getCycle())) {
                                for (int k = n; k < array.length - 1; k++) {
                                    array[k] = array[k + 1];
                                }
                                //数组最后一个元素赋为控制
                                HardBatteryRealTimePartOnlyData hardBatteryRealTimePartOnlyData = new HardBatteryRealTimePartOnlyData();
                                hardBatteryRealTimePartOnlyData.setCycle(-1);
                                array[array.length - 1] = hardBatteryRealTimePartOnlyData;
                                n--;
                            }
                        } else {
                            //把需删除元素后面的元素依次覆盖前面的元素
                            for (int k = n; k < array.length - 1; k++) {
                                array[k] = array[k + 1];
                            }
                            //数组最后一个元素赋为控制
                            HardBatteryRealTimePartOnlyData hardBatteryRealTimePartOnlyData = new HardBatteryRealTimePartOnlyData();
                            hardBatteryRealTimePartOnlyData.setCycle(-1);
                            array[array.length - 1] = hardBatteryRealTimePartOnlyData;
                            n--;
                        }
                    }
                }
            }





            if("101c1d5b49f4".equals(id)){
                for(int m=0;m<array.length;m++){
                    System.out.println("array="+array[m].getCycle()+" "+array[m].getCollecttime());
                }
            }
            List list2=Arrays.asList(array);
            List<HardBatteryRealTimePartOnlyData> hardBatteryRealTimePartOnlyDataList = new ArrayList(list2);
            Iterator<HardBatteryRealTimePartOnlyData> hardBatteryRealTimePartOnlyDataIterator = hardBatteryRealTimePartOnlyDataList.iterator();
            while (hardBatteryRealTimePartOnlyDataIterator.hasNext()){
                HardBatteryRealTimePartOnlyData  hardBatteryRealTimePartOnlyData=hardBatteryRealTimePartOnlyDataIterator.next();
                if(hardBatteryRealTimePartOnlyData.getCycle()==-1){
                    hardBatteryRealTimePartOnlyDataIterator.remove();
                }
            }

            for (int j=0;j<hardBatteryRealTimePartOnlyDataList.size();j++){
                if (hardBatteryRealTimePartOnlyDataList.get(j).getFlag()) {
                    result.set(0, hardBatteryRealTimePartOnlyDataList.get(j).getUserid());

                    result.set(1, hardBatteryRealTimePartOnlyDataList.get(j).getState_0());
                    result.set(2, hardBatteryRealTimePartOnlyDataList.get(j).getState_1());
                    result.set(3, hardBatteryRealTimePartOnlyDataList.get(j).getState_2());
                    result.set(4, hardBatteryRealTimePartOnlyDataList.get(j).getState_3());
                    result.set(5, hardBatteryRealTimePartOnlyDataList.get(j).getState_4());
                    result.set(6, hardBatteryRealTimePartOnlyDataList.get(j).getState_5());
                    result.set(7, hardBatteryRealTimePartOnlyDataList.get(j).getState_6());
                    result.set(8, hardBatteryRealTimePartOnlyDataList.get(j).getState_7());
                    result.set(9, hardBatteryRealTimePartOnlyDataList.get(j).getState_8());
                    result.set(10, hardBatteryRealTimePartOnlyDataList.get(j).getState_9());
                    result.set(11, hardBatteryRealTimePartOnlyDataList.get(j).getState_10());
                    result.set(12, hardBatteryRealTimePartOnlyDataList.get(j).getState_11());
                    result.set(13, hardBatteryRealTimePartOnlyDataList.get(j).getState_12());
                    result.set(14, hardBatteryRealTimePartOnlyDataList.get(j).getState_13());
                    result.set(15, hardBatteryRealTimePartOnlyDataList.get(j).getState_14());
                    result.set(16, hardBatteryRealTimePartOnlyDataList.get(j).getState_15());

                    result.set(17, hardBatteryRealTimePartOnlyDataList.get(j).getSoc());
                    result.set(18, hardBatteryRealTimePartOnlyDataList.get(j).getTvolt());
                    result.set(19, hardBatteryRealTimePartOnlyDataList.get(j).getTcurr());
                    result.set(20, hardBatteryRealTimePartOnlyDataList.get(j).getHtemp());
                    result.set(21, hardBatteryRealTimePartOnlyDataList.get(j).getHtnum());
                    result.set(22, hardBatteryRealTimePartOnlyDataList.get(j).getLtemp());
                    result.set(23, hardBatteryRealTimePartOnlyDataList.get(j).getLtnum());
                    result.set(24, hardBatteryRealTimePartOnlyDataList.get(j).getHvolt());
                    result.set(25, hardBatteryRealTimePartOnlyDataList.get(j).getHvnum());
                    result.set(26, hardBatteryRealTimePartOnlyDataList.get(j).getLvolt());
                    result.set(27, hardBatteryRealTimePartOnlyDataList.get(j).getLvnum());
                    result.set(28, hardBatteryRealTimePartOnlyDataList.get(j).getDsop());
                    result.set(29, hardBatteryRealTimePartOnlyDataList.get(j).getCsop());
                    result.set(30, hardBatteryRealTimePartOnlyDataList.get(j).getSoh());
                    result.set(31, hardBatteryRealTimePartOnlyDataList.get(j).getCycle());
                    result.set(32, hardBatteryRealTimePartOnlyDataList.get(j).getRcap());
                    result.set(33, hardBatteryRealTimePartOnlyDataList.get(j).getFcap());
                    result.set(34, hardBatteryRealTimePartOnlyDataList.get(j).getFctime());
                    result.set(35, hardBatteryRealTimePartOnlyDataList.get(j).getRpow());

                    result.set(36, hardBatteryRealTimePartOnlyDataList.get(j).getDevft1_0());
                    result.set(37, hardBatteryRealTimePartOnlyDataList.get(j).getDevft1_1());
                    result.set(38, hardBatteryRealTimePartOnlyDataList.get(j).getDevft1_2());
                    result.set(39, hardBatteryRealTimePartOnlyDataList.get(j).getDevft1_3());
                    result.set(40, hardBatteryRealTimePartOnlyDataList.get(j).getDevft1_4());
                    result.set(41, hardBatteryRealTimePartOnlyDataList.get(j).getDevft1_5());
                    result.set(42, hardBatteryRealTimePartOnlyDataList.get(j).getDevft1_6());
                    result.set(43, hardBatteryRealTimePartOnlyDataList.get(j).getDevft1_7());
                    result.set(44, hardBatteryRealTimePartOnlyDataList.get(j).getDevft1_8());
                    result.set(45, hardBatteryRealTimePartOnlyDataList.get(j).getDevft1_9());
                    result.set(46, hardBatteryRealTimePartOnlyDataList.get(j).getDevft1_10());
                    result.set(47, hardBatteryRealTimePartOnlyDataList.get(j).getDevft1_11());
                    result.set(48, hardBatteryRealTimePartOnlyDataList.get(j).getDevft1_12());
                    result.set(49, hardBatteryRealTimePartOnlyDataList.get(j).getDevft1_13());
                    result.set(50, hardBatteryRealTimePartOnlyDataList.get(j).getDevft1_14());
                    result.set(51, hardBatteryRealTimePartOnlyDataList.get(j).getDevft1_15());

                    result.set(52, hardBatteryRealTimePartOnlyDataList.get(j).getDevft2_0());
                    result.set(53, hardBatteryRealTimePartOnlyDataList.get(j).getDevft2_1());
                    result.set(54, hardBatteryRealTimePartOnlyDataList.get(j).getDevft2_2());
                    result.set(55, hardBatteryRealTimePartOnlyDataList.get(j).getDevft2_3());
                    result.set(56, hardBatteryRealTimePartOnlyDataList.get(j).getDevft2_4());
                    result.set(57, hardBatteryRealTimePartOnlyDataList.get(j).getDevft2_5());
                    result.set(58, hardBatteryRealTimePartOnlyDataList.get(j).getDevft2_6());
                    result.set(59, hardBatteryRealTimePartOnlyDataList.get(j).getDevft2_7());
                    result.set(60, hardBatteryRealTimePartOnlyDataList.get(j).getDevft2_8());
                    result.set(61, hardBatteryRealTimePartOnlyDataList.get(j).getDevft2_9());
                    result.set(62, hardBatteryRealTimePartOnlyDataList.get(j).getDevft2_10());
                    result.set(63, hardBatteryRealTimePartOnlyDataList.get(j).getDevft2_11());
                    result.set(64, hardBatteryRealTimePartOnlyDataList.get(j).getDevft2_12());
                    result.set(65, hardBatteryRealTimePartOnlyDataList.get(j).getDevft2_13());
                    result.set(66, hardBatteryRealTimePartOnlyDataList.get(j).getDevft2_14());
                    result.set(67, hardBatteryRealTimePartOnlyDataList.get(j).getDevft2_15());

                    result.set(68, hardBatteryRealTimePartOnlyDataList.get(j).getOpft1_0());
                    result.set(69, hardBatteryRealTimePartOnlyDataList.get(j).getOpft1_1());
                    result.set(70, hardBatteryRealTimePartOnlyDataList.get(j).getOpft1_2());
                    result.set(71, hardBatteryRealTimePartOnlyDataList.get(j).getOpft1_3());
                    result.set(72, hardBatteryRealTimePartOnlyDataList.get(j).getOpft1_4());
                    result.set(73, hardBatteryRealTimePartOnlyDataList.get(j).getOpft1_5());
                    result.set(74, hardBatteryRealTimePartOnlyDataList.get(j).getOpft1_6());
                    result.set(75, hardBatteryRealTimePartOnlyDataList.get(j).getOpft1_7());
                    result.set(76, hardBatteryRealTimePartOnlyDataList.get(j).getOpft1_8());
                    result.set(77, hardBatteryRealTimePartOnlyDataList.get(j).getOpft1_9());
                    result.set(78, hardBatteryRealTimePartOnlyDataList.get(j).getOpft1_10());
                    result.set(79, hardBatteryRealTimePartOnlyDataList.get(j).getOpft1_11());
                    result.set(80, hardBatteryRealTimePartOnlyDataList.get(j).getOpft1_12());
                    result.set(81, hardBatteryRealTimePartOnlyDataList.get(j).getOpft1_13());
                    result.set(82, hardBatteryRealTimePartOnlyDataList.get(j).getOpft1_14());
                    result.set(83, hardBatteryRealTimePartOnlyDataList.get(j).getOpft1_15());

                    result.set(84, hardBatteryRealTimePartOnlyDataList.get(j).getOpft2_0());
                    result.set(85, hardBatteryRealTimePartOnlyDataList.get(j).getOpft2_1());
                    result.set(86, hardBatteryRealTimePartOnlyDataList.get(j).getOpft2_2());
                    result.set(87, hardBatteryRealTimePartOnlyDataList.get(j).getOpft2_3());
                    result.set(88, hardBatteryRealTimePartOnlyDataList.get(j).getOpft2_4());
                    result.set(89, hardBatteryRealTimePartOnlyDataList.get(j).getOpft2_5());
                    result.set(90, hardBatteryRealTimePartOnlyDataList.get(j).getOpft2_6());
                    result.set(91, hardBatteryRealTimePartOnlyDataList.get(j).getOpft2_7());
                    result.set(92, hardBatteryRealTimePartOnlyDataList.get(j).getOpft2_8());
                    result.set(93, hardBatteryRealTimePartOnlyDataList.get(j).getOpft2_9());
                    result.set(94, hardBatteryRealTimePartOnlyDataList.get(j).getOpft2_10());
                    result.set(95, hardBatteryRealTimePartOnlyDataList.get(j).getOpft2_11());
                    result.set(96, hardBatteryRealTimePartOnlyDataList.get(j).getOpft2_12());
                    result.set(97, hardBatteryRealTimePartOnlyDataList.get(j).getOpft2_13());
                    result.set(98, hardBatteryRealTimePartOnlyDataList.get(j).getOpft2_14());
                    result.set(99, hardBatteryRealTimePartOnlyDataList.get(j).getOpft2_15());

                    result.set(100, hardBatteryRealTimePartOnlyDataList.get(j).getOpwarn_0());
                    result.set(101, hardBatteryRealTimePartOnlyDataList.get(j).getOpwarn_1());
                    result.set(102, hardBatteryRealTimePartOnlyDataList.get(j).getOpwarn_2());
                    result.set(103, hardBatteryRealTimePartOnlyDataList.get(j).getOpwarn_3());
                    result.set(104, hardBatteryRealTimePartOnlyDataList.get(j).getOpwarn_4());
                    result.set(105, hardBatteryRealTimePartOnlyDataList.get(j).getOpwarn_5());
                    result.set(106, hardBatteryRealTimePartOnlyDataList.get(j).getOpwarn_6());
                    result.set(107, hardBatteryRealTimePartOnlyDataList.get(j).getOpwarn_7());
                    result.set(108, hardBatteryRealTimePartOnlyDataList.get(j).getOpwarn_8());
                    result.set(109, hardBatteryRealTimePartOnlyDataList.get(j).getOpwarn_9());
                    result.set(110, hardBatteryRealTimePartOnlyDataList.get(j).getOpwarn_10());
                    result.set(111, hardBatteryRealTimePartOnlyDataList.get(j).getOpwarn_11());
                    result.set(112, hardBatteryRealTimePartOnlyDataList.get(j).getOpwarn_12());
                    result.set(113, hardBatteryRealTimePartOnlyDataList.get(j).getOpwarn_13());
                    result.set(114, hardBatteryRealTimePartOnlyDataList.get(j).getOpwarn_14());
                    result.set(115, hardBatteryRealTimePartOnlyDataList.get(j).getOpwarn_15());

                    result.set(116, hardBatteryRealTimePartOnlyDataList.get(j).getOpwarn2());

                    result.set(117, hardBatteryRealTimePartOnlyDataList.get(j).getCmost());
                    result.set(118, hardBatteryRealTimePartOnlyDataList.get(j).getDmost());
                    result.set(119, hardBatteryRealTimePartOnlyDataList.get(j).getFuelt());
                    result.set(120, hardBatteryRealTimePartOnlyDataList.get(j).getCont());
                    result.set(121, hardBatteryRealTimePartOnlyDataList.get(j).getBtemp1());
                    result.set(122, hardBatteryRealTimePartOnlyDataList.get(j).getBtemp2());

                    result.set(123, hardBatteryRealTimePartOnlyDataList.get(j).getBvolt1());
                    result.set(124, hardBatteryRealTimePartOnlyDataList.get(j).getBvolt2());
                    result.set(125, hardBatteryRealTimePartOnlyDataList.get(j).getBvolt3());
                    result.set(126, hardBatteryRealTimePartOnlyDataList.get(j).getBvolt4());
                    result.set(127, hardBatteryRealTimePartOnlyDataList.get(j).getBvolt5());
                    result.set(128, hardBatteryRealTimePartOnlyDataList.get(j).getBvolt6());
                    result.set(129, hardBatteryRealTimePartOnlyDataList.get(j).getBvolt7());
                    result.set(130, hardBatteryRealTimePartOnlyDataList.get(j).getBvolt8());
                    result.set(131, hardBatteryRealTimePartOnlyDataList.get(j).getBvolt9());
                    result.set(132, hardBatteryRealTimePartOnlyDataList.get(j).getBvolt10());
                    result.set(133, hardBatteryRealTimePartOnlyDataList.get(j).getBvolt11());
                    result.set(134, hardBatteryRealTimePartOnlyDataList.get(j).getBvolt12());
                    result.set(135, hardBatteryRealTimePartOnlyDataList.get(j).getBvolt13());
                    result.set(136, hardBatteryRealTimePartOnlyDataList.get(j).getBvolt14());
                    result.set(137, hardBatteryRealTimePartOnlyDataList.get(j).getBvolt15());
                    result.set(138, hardBatteryRealTimePartOnlyDataList.get(j).getBvolt16());

                    result.set(139, hardBatteryRealTimePartOnlyDataList.get(j).getBalasta_0());
                    result.set(140, hardBatteryRealTimePartOnlyDataList.get(j).getBalasta_1());
                    result.set(141, hardBatteryRealTimePartOnlyDataList.get(j).getBalasta_2());
                    result.set(142, hardBatteryRealTimePartOnlyDataList.get(j).getBalasta_3());
                    result.set(143, hardBatteryRealTimePartOnlyDataList.get(j).getBalasta_4());
                    result.set(144, hardBatteryRealTimePartOnlyDataList.get(j).getBalasta_5());
                    result.set(145, hardBatteryRealTimePartOnlyDataList.get(j).getBalasta_6());
                    result.set(146, hardBatteryRealTimePartOnlyDataList.get(j).getBalasta_7());
                    result.set(147, hardBatteryRealTimePartOnlyDataList.get(j).getBalasta_8());
                    result.set(148, hardBatteryRealTimePartOnlyDataList.get(j).getBalasta_9());
                    result.set(149, hardBatteryRealTimePartOnlyDataList.get(j).getBalasta_10());
                    result.set(150, hardBatteryRealTimePartOnlyDataList.get(j).getBalasta_11());
                    result.set(151, hardBatteryRealTimePartOnlyDataList.get(j).getBalasta_12());
                    result.set(152, hardBatteryRealTimePartOnlyDataList.get(j).getBalasta_13());
                    result.set(153, hardBatteryRealTimePartOnlyDataList.get(j).getBalasta_14());
                    result.set(154, hardBatteryRealTimePartOnlyDataList.get(j).getBalasta_15());
                    result.set(155, hardBatteryRealTimePartOnlyDataList.get(j).getAccelerated_speed_x());
                    result.set(156, hardBatteryRealTimePartOnlyDataList.get(j).getAccelerated_speed_y());
                    result.set(157, hardBatteryRealTimePartOnlyDataList.get(j).getAccelerated_speed_z());
                    result.set(158, hardBatteryRealTimePartOnlyDataList.get(j).getMcu3v3());
                    result.set(159, hardBatteryRealTimePartOnlyDataList.get(j).getPre_electric_infer_vol());
                    result.set(160, hardBatteryRealTimePartOnlyDataList.get(j).getElectric_quantity_ma());
                    result.set(161, hardBatteryRealTimePartOnlyDataList.get(j).getMax_charge_electric());
                    result.set(162, hardBatteryRealTimePartOnlyDataList.get(j).getElectric_meter_charge());
                    result.set(163, hardBatteryRealTimePartOnlyDataList.get(j).getCollecttime());
                    result.set(164, hardBatteryRealTimePartOnlyDataList.get(j).getPid());
                    result.set(165, hardBatteryRealTimePartOnlyDataList.get(j).getBid());
                    result.set(166, hardBatteryRealTimePartOnlyDataList.get(j).getPort());
                    context.write(result);
                }else{
                    result1.set(0, hardBatteryRealTimePartOnlyDataList.get(j).getUserid());

                    result1.set(1, hardBatteryRealTimePartOnlyDataList.get(j).getState_0());
                    result1.set(2, hardBatteryRealTimePartOnlyDataList.get(j).getState_1());
                    result1.set(3, hardBatteryRealTimePartOnlyDataList.get(j).getState_2());
                    result1.set(4, hardBatteryRealTimePartOnlyDataList.get(j).getState_3());
                    result1.set(5, hardBatteryRealTimePartOnlyDataList.get(j).getState_4());
                    result1.set(6, hardBatteryRealTimePartOnlyDataList.get(j).getState_5());
                    result1.set(7, hardBatteryRealTimePartOnlyDataList.get(j).getState_6());
                    result1.set(8, hardBatteryRealTimePartOnlyDataList.get(j).getState_7());
                    result1.set(9, hardBatteryRealTimePartOnlyDataList.get(j).getState_8());
                    result1.set(10, hardBatteryRealTimePartOnlyDataList.get(j).getState_9());
                    result1.set(11, hardBatteryRealTimePartOnlyDataList.get(j).getState_10());
                    result1.set(12, hardBatteryRealTimePartOnlyDataList.get(j).getState_11());
                    result1.set(13, hardBatteryRealTimePartOnlyDataList.get(j).getState_12());
                    result1.set(14, hardBatteryRealTimePartOnlyDataList.get(j).getState_13());
                    result1.set(15, hardBatteryRealTimePartOnlyDataList.get(j).getState_14());
                    result1.set(16, hardBatteryRealTimePartOnlyDataList.get(j).getState_15());

                    result1.set(17, hardBatteryRealTimePartOnlyDataList.get(j).getSoc());
                    result1.set(18, hardBatteryRealTimePartOnlyDataList.get(j).getTvolt());
                    result1.set(19, hardBatteryRealTimePartOnlyDataList.get(j).getTcurr());
                    result1.set(20, hardBatteryRealTimePartOnlyDataList.get(j).getHtemp());
                    result1.set(21, hardBatteryRealTimePartOnlyDataList.get(j).getHtnum());
                    result1.set(22, hardBatteryRealTimePartOnlyDataList.get(j).getLtemp());
                    result1.set(23, hardBatteryRealTimePartOnlyDataList.get(j).getLtnum());
                    result1.set(24, hardBatteryRealTimePartOnlyDataList.get(j).getHvolt());
                    result1.set(25, hardBatteryRealTimePartOnlyDataList.get(j).getHvnum());
                    result1.set(26, hardBatteryRealTimePartOnlyDataList.get(j).getLvolt());
                    result1.set(27, hardBatteryRealTimePartOnlyDataList.get(j).getLvnum());
                    result1.set(28, hardBatteryRealTimePartOnlyDataList.get(j).getDsop());
                    result1.set(29, hardBatteryRealTimePartOnlyDataList.get(j).getCsop());
                    result1.set(30, hardBatteryRealTimePartOnlyDataList.get(j).getSoh());
                    result1.set(31, hardBatteryRealTimePartOnlyDataList.get(j).getCycle());
                    result1.set(32, hardBatteryRealTimePartOnlyDataList.get(j).getRcap());
                    result1.set(33, hardBatteryRealTimePartOnlyDataList.get(j).getFcap());
                    result1.set(34, hardBatteryRealTimePartOnlyDataList.get(j).getFctime());
                    result1.set(35, hardBatteryRealTimePartOnlyDataList.get(j).getRpow());

                    result1.set(36, hardBatteryRealTimePartOnlyDataList.get(j).getDevft1_0());
                    result1.set(37, hardBatteryRealTimePartOnlyDataList.get(j).getDevft1_1());
                    result1.set(38, hardBatteryRealTimePartOnlyDataList.get(j).getDevft1_2());
                    result1.set(39, hardBatteryRealTimePartOnlyDataList.get(j).getDevft1_3());
                    result1.set(40, hardBatteryRealTimePartOnlyDataList.get(j).getDevft1_4());
                    result1.set(41, hardBatteryRealTimePartOnlyDataList.get(j).getDevft1_5());
                    result1.set(42, hardBatteryRealTimePartOnlyDataList.get(j).getDevft1_6());
                    result1.set(43, hardBatteryRealTimePartOnlyDataList.get(j).getDevft1_7());
                    result1.set(44, hardBatteryRealTimePartOnlyDataList.get(j).getDevft1_8());
                    result1.set(45, hardBatteryRealTimePartOnlyDataList.get(j).getDevft1_9());
                    result1.set(46, hardBatteryRealTimePartOnlyDataList.get(j).getDevft1_10());
                    result1.set(47, hardBatteryRealTimePartOnlyDataList.get(j).getDevft1_11());
                    result1.set(48, hardBatteryRealTimePartOnlyDataList.get(j).getDevft1_12());
                    result1.set(49, hardBatteryRealTimePartOnlyDataList.get(j).getDevft1_13());
                    result1.set(50, hardBatteryRealTimePartOnlyDataList.get(j).getDevft1_14());
                    result1.set(51, hardBatteryRealTimePartOnlyDataList.get(j).getDevft1_15());

                    result1.set(52, hardBatteryRealTimePartOnlyDataList.get(j).getDevft2_0());
                    result1.set(53, hardBatteryRealTimePartOnlyDataList.get(j).getDevft2_1());
                    result1.set(54, hardBatteryRealTimePartOnlyDataList.get(j).getDevft2_2());
                    result1.set(55, hardBatteryRealTimePartOnlyDataList.get(j).getDevft2_3());
                    result1.set(56, hardBatteryRealTimePartOnlyDataList.get(j).getDevft2_4());
                    result1.set(57, hardBatteryRealTimePartOnlyDataList.get(j).getDevft2_5());
                    result1.set(58, hardBatteryRealTimePartOnlyDataList.get(j).getDevft2_6());
                    result1.set(59, hardBatteryRealTimePartOnlyDataList.get(j).getDevft2_7());
                    result1.set(60, hardBatteryRealTimePartOnlyDataList.get(j).getDevft2_8());
                    result1.set(61, hardBatteryRealTimePartOnlyDataList.get(j).getDevft2_9());
                    result1.set(62, hardBatteryRealTimePartOnlyDataList.get(j).getDevft2_10());
                    result1.set(63, hardBatteryRealTimePartOnlyDataList.get(j).getDevft2_11());
                    result1.set(64, hardBatteryRealTimePartOnlyDataList.get(j).getDevft2_12());
                    result1.set(65, hardBatteryRealTimePartOnlyDataList.get(j).getDevft2_13());
                    result1.set(66, hardBatteryRealTimePartOnlyDataList.get(j).getDevft2_14());
                    result1.set(67, hardBatteryRealTimePartOnlyDataList.get(j).getDevft2_15());

                    result1.set(68, hardBatteryRealTimePartOnlyDataList.get(j).getOpft1_0());
                    result1.set(69, hardBatteryRealTimePartOnlyDataList.get(j).getOpft1_1());
                    result1.set(70, hardBatteryRealTimePartOnlyDataList.get(j).getOpft1_2());
                    result1.set(71, hardBatteryRealTimePartOnlyDataList.get(j).getOpft1_3());
                    result1.set(72, hardBatteryRealTimePartOnlyDataList.get(j).getOpft1_4());
                    result1.set(73, hardBatteryRealTimePartOnlyDataList.get(j).getOpft1_5());
                    result1.set(74, hardBatteryRealTimePartOnlyDataList.get(j).getOpft1_6());
                    result1.set(75, hardBatteryRealTimePartOnlyDataList.get(j).getOpft1_7());
                    result1.set(76, hardBatteryRealTimePartOnlyDataList.get(j).getOpft1_8());
                    result1.set(77, hardBatteryRealTimePartOnlyDataList.get(j).getOpft1_9());
                    result1.set(78, hardBatteryRealTimePartOnlyDataList.get(j).getOpft1_10());
                    result1.set(79, hardBatteryRealTimePartOnlyDataList.get(j).getOpft1_11());
                    result1.set(80, hardBatteryRealTimePartOnlyDataList.get(j).getOpft1_12());
                    result1.set(81, hardBatteryRealTimePartOnlyDataList.get(j).getOpft1_13());
                    result1.set(82, hardBatteryRealTimePartOnlyDataList.get(j).getOpft1_14());
                    result1.set(83, hardBatteryRealTimePartOnlyDataList.get(j).getOpft1_15());
                    result1.set(84, hardBatteryRealTimePartOnlyDataList.get(j).getOpft2_0());
                    result1.set(85, hardBatteryRealTimePartOnlyDataList.get(j).getOpft2_1());
                    result1.set(86, hardBatteryRealTimePartOnlyDataList.get(j).getOpft2_2());
                    result1.set(87, hardBatteryRealTimePartOnlyDataList.get(j).getOpft2_3());
                    result1.set(88, hardBatteryRealTimePartOnlyDataList.get(j).getOpft2_4());
                    result1.set(89, hardBatteryRealTimePartOnlyDataList.get(j).getOpft2_5());
                    result1.set(90, hardBatteryRealTimePartOnlyDataList.get(j).getOpft2_6());
                    result1.set(91, hardBatteryRealTimePartOnlyDataList.get(j).getOpft2_7());
                    result1.set(92, hardBatteryRealTimePartOnlyDataList.get(j).getOpft2_8());
                    result1.set(93, hardBatteryRealTimePartOnlyDataList.get(j).getOpft2_9());
                    result1.set(94, hardBatteryRealTimePartOnlyDataList.get(j).getOpft2_10());
                    result1.set(95, hardBatteryRealTimePartOnlyDataList.get(j).getOpft2_11());
                    result1.set(96, hardBatteryRealTimePartOnlyDataList.get(j).getOpft2_12());
                    result1.set(97, hardBatteryRealTimePartOnlyDataList.get(j).getOpft2_13());
                    result1.set(98, hardBatteryRealTimePartOnlyDataList.get(j).getOpft2_14());
                    result1.set(99, hardBatteryRealTimePartOnlyDataList.get(j).getOpft2_15());

                    result1.set(100, hardBatteryRealTimePartOnlyDataList.get(j).getOpwarn_0());
                    result1.set(101, hardBatteryRealTimePartOnlyDataList.get(j).getOpwarn_1());
                    result1.set(102, hardBatteryRealTimePartOnlyDataList.get(j).getOpwarn_2());
                    result1.set(103, hardBatteryRealTimePartOnlyDataList.get(j).getOpwarn_3());
                    result1.set(104, hardBatteryRealTimePartOnlyDataList.get(j).getOpwarn_4());
                    result1.set(105, hardBatteryRealTimePartOnlyDataList.get(j).getOpwarn_5());
                    result1.set(106, hardBatteryRealTimePartOnlyDataList.get(j).getOpwarn_6());
                    result1.set(107, hardBatteryRealTimePartOnlyDataList.get(j).getOpwarn_7());
                    result1.set(108, hardBatteryRealTimePartOnlyDataList.get(j).getOpwarn_8());
                    result1.set(109, hardBatteryRealTimePartOnlyDataList.get(j).getOpwarn_9());
                    result1.set(110, hardBatteryRealTimePartOnlyDataList.get(j).getOpwarn_10());
                    result1.set(111, hardBatteryRealTimePartOnlyDataList.get(j).getOpwarn_11());
                    result1.set(112, hardBatteryRealTimePartOnlyDataList.get(j).getOpwarn_12());
                    result1.set(113, hardBatteryRealTimePartOnlyDataList.get(j).getOpwarn_13());
                    result1.set(114, hardBatteryRealTimePartOnlyDataList.get(j).getOpwarn_14());
                    result1.set(115, hardBatteryRealTimePartOnlyDataList.get(j).getOpwarn_15());

                    result1.set(116, hardBatteryRealTimePartOnlyDataList.get(j).getOpwarn2());

                    result1.set(117, hardBatteryRealTimePartOnlyDataList.get(j).getCmost());
                    result1.set(118, hardBatteryRealTimePartOnlyDataList.get(j).getDmost());
                    result1.set(119, hardBatteryRealTimePartOnlyDataList.get(j).getFuelt());
                    result1.set(120, hardBatteryRealTimePartOnlyDataList.get(j).getCont());
                    result1.set(121, hardBatteryRealTimePartOnlyDataList.get(j).getBtemp1());
                    result1.set(122, hardBatteryRealTimePartOnlyDataList.get(j).getBtemp2());

                    result1.set(123, hardBatteryRealTimePartOnlyDataList.get(j).getBvolt1());
                    result1.set(124, hardBatteryRealTimePartOnlyDataList.get(j).getBvolt2());
                    result1.set(125, hardBatteryRealTimePartOnlyDataList.get(j).getBvolt3());
                    result1.set(126, hardBatteryRealTimePartOnlyDataList.get(j).getBvolt4());
                    result1.set(127, hardBatteryRealTimePartOnlyDataList.get(j).getBvolt5());
                    result1.set(128, hardBatteryRealTimePartOnlyDataList.get(j).getBvolt6());
                    result1.set(129, hardBatteryRealTimePartOnlyDataList.get(j).getBvolt7());
                    result1.set(130, hardBatteryRealTimePartOnlyDataList.get(j).getBvolt8());
                    result1.set(131, hardBatteryRealTimePartOnlyDataList.get(j).getBvolt9());
                    result1.set(132, hardBatteryRealTimePartOnlyDataList.get(j).getBvolt10());
                    result1.set(133, hardBatteryRealTimePartOnlyDataList.get(j).getBvolt11());
                    result1.set(134, hardBatteryRealTimePartOnlyDataList.get(j).getBvolt12());
                    result1.set(135, hardBatteryRealTimePartOnlyDataList.get(j).getBvolt13());
                    result1.set(136, hardBatteryRealTimePartOnlyDataList.get(j).getBvolt14());
                    result1.set(137, hardBatteryRealTimePartOnlyDataList.get(j).getBvolt15());
                    result1.set(138, hardBatteryRealTimePartOnlyDataList.get(j).getBvolt16());

                    result1.set(139, hardBatteryRealTimePartOnlyDataList.get(j).getBalasta_0());
                    result1.set(140, hardBatteryRealTimePartOnlyDataList.get(j).getBalasta_1());
                    result1.set(141, hardBatteryRealTimePartOnlyDataList.get(j).getBalasta_2());
                    result1.set(142, hardBatteryRealTimePartOnlyDataList.get(j).getBalasta_3());
                    result1.set(143, hardBatteryRealTimePartOnlyDataList.get(j).getBalasta_4());
                    result1.set(144, hardBatteryRealTimePartOnlyDataList.get(j).getBalasta_5());
                    result1.set(145, hardBatteryRealTimePartOnlyDataList.get(j).getBalasta_6());
                    result1.set(146, hardBatteryRealTimePartOnlyDataList.get(j).getBalasta_7());
                    result1.set(147, hardBatteryRealTimePartOnlyDataList.get(j).getBalasta_8());
                    result1.set(148, hardBatteryRealTimePartOnlyDataList.get(j).getBalasta_9());
                    result1.set(149, hardBatteryRealTimePartOnlyDataList.get(j).getBalasta_10());
                    result1.set(150, hardBatteryRealTimePartOnlyDataList.get(j).getBalasta_11());
                    result1.set(151, hardBatteryRealTimePartOnlyDataList.get(j).getBalasta_12());
                    result1.set(152, hardBatteryRealTimePartOnlyDataList.get(j).getBalasta_13());
                    result1.set(153, hardBatteryRealTimePartOnlyDataList.get(j).getBalasta_14());
                    result1.set(154, hardBatteryRealTimePartOnlyDataList.get(j).getBalasta_15());
                    result1.set(155, hardBatteryRealTimePartOnlyDataList.get(j).getAccelerated_speed_x());
                    result1.set(156, hardBatteryRealTimePartOnlyDataList.get(j).getAccelerated_speed_y());
                    result1.set(157, hardBatteryRealTimePartOnlyDataList.get(j).getAccelerated_speed_z());
                    result1.set(158, hardBatteryRealTimePartOnlyDataList.get(j).getMcu3v3());
                    result1.set(159, hardBatteryRealTimePartOnlyDataList.get(j).getPre_electric_infer_vol());
                    result1.set(160, hardBatteryRealTimePartOnlyDataList.get(j).getElectric_quantity_ma());
                    result1.set(161, hardBatteryRealTimePartOnlyDataList.get(j).getMax_charge_electric());
                    result1.set(162, hardBatteryRealTimePartOnlyDataList.get(j).getElectric_meter_charge());
                    result1.set(163, hardBatteryRealTimePartOnlyDataList.get(j).getCollecttime());
                    result1.set(164, hardBatteryRealTimePartOnlyDataList.get(j).getPid());
                    result1.set(165, hardBatteryRealTimePartOnlyDataList.get(j).getBid());
                    result1.set(166, hardBatteryRealTimePartOnlyDataList.get(j).getPort());
                    context.write(result1,"error");
                }
            }
        }

    }
    // 将分区字符串如"ds=1/pt=2"转为map的形式
    public static LinkedHashMap<String, String> convertPartSpecToMap(
            String partSpec) {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        if (partSpec != null && !partSpec.trim().isEmpty()) {
            String[] parts = partSpec.split("/");
            for (String part : parts) {
                String[] ss = part.split("=");
                if (ss.length != 2) {
                    throw new RuntimeException("ODPS-0730001: error part spec format: "
                            + partSpec);
                }
                map.put(ss[0], ss[1]);
            }
        }
        return map;
    }
    public static void main(String[] args) throws Exception {
        String[] inputs = null;
        String[] outputs = null;
        if (args.length == 2) {
            inputs = args[0].split(",");
            // System.out.println(inputs[0]+" "+inputs[1]);
            outputs = args[1].split(",");
            //  System.out.println(outputs[0]);
        } else {
            System.err.println("MultipleInOut in... out...");
            System.exit(1);
        }
        JobConf job = new JobConf();
        job.setMapperClass(origin_BatteryRealTimeDataCollectPartReadOnlyData.TokenizerMapper.class);
        job.setReducerClass(origin_BatteryRealTimeDataCollectPartReadOnlyData.SumReducer.class);
        job.setMapOutputKeySchema(SchemaUtils.fromString("id:string"));
        job.setMapOutputValueSchema(SchemaUtils.fromString(
                "\tuserid:string                            ,\tstate_0 :bigint                          ,\tstate_1 :bigint                          ,\tstate_2 :bigint                          ,\tstate_3 :bigint                          ,\tstate_4 :bigint                          ,\tstate_5 :bigint                          ,\tstate_6 :bigint                          ,\tstate_7 :bigint                          ,\tstate_8 :bigint                          ,\tstate_9 :bigint                          ,\tstate_10:bigint                          ,\tstate_11:bigint                          ,\tstate_12:bigint                          ,\tstate_13:bigint                          ,\tstate_14:bigint                          ,\tstate_15:bigint                          ,\tsoc:double                               ,\ttvolt:double                             ,\ttcurr:double                             ,\thtemp:double                             ,\t htnum:bigint                            ,\tltemp:double                             ,\t ltnum:bigint                            ,\t hvolt:bigint                            ,\t hvnum:bigint                            ,\t lvolt:bigint                            ,\t lvnum:bigint                            ,\tdsop  :double                            ,\tcsop  :double                            ,\tsoh   :double                            ,\t cycle :bigint                           ,\t rcap  :bigint                           ,\t fcap  :bigint                           ,\t fctime:bigint                           ,\trpow :double                             ,\tdevft1_0:bigint                          ,\tdevft1_1:bigint                          ,\tdevft1_2:bigint                          ,\tdevft1_3:bigint                          ,\tdevft1_4:bigint                          ,\tdevft1_5:bigint                          ,\tdevft1_6:bigint                          ,\tdevft1_7:bigint                          ,\tdevft1_8:bigint                          ,\tdevft1_9:bigint                          ,\tdevft1_10 :bigint                        ,\tdevft1_11 :bigint                        ,\tdevft1_12 :bigint                        ,\tdevft1_13 :bigint                        ,\tdevft1_14 :bigint                        ,\tdevft1_15 :bigint                        ,\tdevft2_0  :bigint                        ,\tdevft2_1  :bigint                        ,\tdevft2_2  :bigint                        ,\tdevft2_3  :bigint                        ,\tdevft2_4  :bigint                        ,\tdevft2_5  :bigint                        ,\tdevft2_6  :bigint                        ,\tdevft2_7  :bigint                        ,\tdevft2_8  :bigint                        ,\tdevft2_9  :bigint                        ,\tdevft2_10 :bigint                        ,\tdevft2_11 :bigint                        ,\tdevft2_12 :bigint                        ,\tdevft2_13 :bigint                        ,\tdevft2_14 :bigint                        ,\tdevft2_15 :bigint                        ,\topft1_0 :bigint                          ,\topft1_1 :bigint                          ,\topft1_2 :bigint                          ,\topft1_3 :bigint                          ,\topft1_4 :bigint                          ,\topft1_5 :bigint                          ,\topft1_6 :bigint                          ,\topft1_7 :bigint                          ,\topft1_8 :bigint                          ,\topft1_9 :bigint                          ,\t opft1_10 :bigint                        ,\t opft1_11 :bigint                        ,\t opft1_12 :bigint                        ,\t opft1_13 :bigint                        ,\t opft1_14 :bigint                        ,\t opft1_15 :bigint                        ,\t  opft2_0 :bigint                        ,\t  opft2_1 :bigint                        ,\t  opft2_2 :bigint                        ,\t  opft2_3 :bigint                        ,\t  opft2_4 :bigint                        ,\t  opft2_5 :bigint                        ,\t  opft2_6 :bigint                        ,\t  opft2_7 :bigint                        ,\t  opft2_8 :bigint                        ,\t  opft2_9 :bigint                        ,\t opft2_10 :bigint                        ,\t opft2_11 :bigint                        ,\t opft2_12 :bigint                        ,\t opft2_13 :bigint                        ,\t opft2_14 :bigint                        ,\t opft2_15 :bigint                        ,\t opwarn_0 :bigint                        ,\t opwarn_1 :bigint                        ,\t opwarn_2 :bigint                        ,\t opwarn_3 :bigint                        ,\t opwarn_4 :bigint                        ,\t opwarn_5 :bigint                        ,\t opwarn_6 :bigint                        ,\t opwarn_7 :bigint                        ,\t opwarn_8 :bigint                        ,\t opwarn_9 :bigint                        ,\t  opwarn_10: bigint                      ,\t  opwarn_11: bigint                      ,\t  opwarn_12: bigint                      ,\t  opwarn_13: bigint                      ,\t  opwarn_14: bigint                      ,\t  opwarn_15: bigint                      ,\topwarn2 : bigint                         ,\t  cmost :double                          ,\t  dmost :double                          ,\t  fuelt :double                          ,\t  cont  :double                          ,\t btemp1 :double                          ,\t btemp2 :double                          ,\tbvolt1    :bigint                        ,\tbvolt2    :bigint                        ,\tbvolt3    :bigint                        ,\tbvolt4    :bigint                        ,\tbvolt5    :bigint                        ,\tbvolt6    :bigint                        ,\tbvolt7    :bigint                        ,\tbvolt8    :bigint                        ,\tbvolt9    :bigint                        ,\tbvolt10   :bigint                        ,\tbvolt11   :bigint                        ,\tbvolt12   :bigint                        ,\tbvolt13   :bigint                        ,\tbvolt14   :bigint                        ,\tbvolt15   :bigint                        ,\tbvolt16   :bigint                        ,\tbalasta_0 :bigint                        ,\tbalasta_1 :bigint                        ,\tbalasta_2 :bigint                        ,\tbalasta_3 :bigint                        ,\tbalasta_4 :bigint                        ,\tbalasta_5 :bigint                        ,\tbalasta_6 :bigint                        ,\tbalasta_7 :bigint                        ,\tbalasta_8 :bigint                        ,\tbalasta_9 :bigint                        ,\tbalasta_10:bigint                        ,\tbalasta_11:bigint                        ,\tbalasta_12:bigint                        ,\tbalasta_13:bigint                        ,\tbalasta_14:bigint                        ,\tbalasta_15:bigint                        ,\taccelerated_speed_x  :bigint             ,\taccelerated_speed_y  :bigint             ,\taccelerated_speed_z  :bigint             ,\tmcu3v3: bigint                           ,\tpre_electric_infer_vol: bigint           ,\telectric_quantity_ma: bigint             ,\t max_charge_electric: double             ,\telectric_meter_charge: bigint            ,\tcollecttime: datetime                        ,\tpid: string        ,\tid: string         ,\tport: bigint        ,\tflag:Boolean                                            "
        ));
        for (String in : inputs) {
            String[] ss = in.split("\\|");
            if (ss.length == 1) {
                InputUtils.addTable(TableInfo.builder().tableName(ss[0]).build(), job);
            } else if (ss.length == 2) {
                LinkedHashMap<String, String> map = convertPartSpecToMap(ss[1]);
                InputUtils.addTable(TableInfo.builder().tableName(ss[0]).partSpec(map).build(), job);
            } else {
                System.err.println("Style of input: " + in + " is not right");
                System.exit(1);
            }
        }
        //解析用户的输出表字符串
        for (String out : outputs) {
            String[] ss = out.split("\\|");
            if (ss.length == 1) {
                OutputUtils.addTable(TableInfo.builder().tableName(ss[0]).build(), job);
            } else if (ss.length == 2) {
                LinkedHashMap<String, String> map = convertPartSpecToMap(ss[1]);
                OutputUtils.addTable(TableInfo.builder().tableName(ss[0]).partSpec(map).build(), job);
            } else if (ss.length == 3) {
                if (ss[1].isEmpty()) {
                    LinkedHashMap<String, String> map = convertPartSpecToMap(ss[2]);
                    OutputUtils.addTable(TableInfo.builder().tableName(ss[0]).partSpec(map).build(), job);
                } else {
                    LinkedHashMap<String, String> map = convertPartSpecToMap(ss[1]);
                    OutputUtils.addTable(TableInfo.builder().tableName(ss[0]).partSpec(map)
                            .label(ss[2]).build(), job);
                }
            } else {
                System.err.println("Style of output: " + out + " is not right");
                System.exit(1);
            }
        }
        JobClient.runJob(job);
    }

}
