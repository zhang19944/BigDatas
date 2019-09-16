package mr.battery;

import com.aliyun.odps.data.Record;
import com.aliyun.odps.data.TableInfo;
import com.aliyun.odps.mapred.JobClient;
import com.aliyun.odps.mapred.MapperBase;
import com.aliyun.odps.mapred.conf.JobConf;
import com.aliyun.odps.mapred.utils.InputUtils;
import com.aliyun.odps.mapred.utils.OutputUtils;
import mr.scooter.Scooter_battery_heartbeat;
import util.ToolUtil;

import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

public class BatteryBMS_partRunControlData {




    public static class TokenizerMapper extends MapperBase {
        private Record one;
        private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        @Override
        public void setup(TaskContext context) throws IOException {
            one=context.createOutputRecord();
        }
        public static boolean isNumeric(String str){
            Pattern pattern = Pattern.compile("[0-9]*");
            return pattern.matcher(str).matches();
        }

        @Override
        public void map(long recordNum, Record record, TaskContext context)
                throws IOException {
            String str = record.get("partRunControlData").toString();
            String partReadOnlyData= URLDecoder.decode(str, "UTF-8").replaceAll("\\[", "").replaceAll("]", "");

            String[] strs=partReadOnlyData.split(",");
            if (strs.length!=54)
                return;
            String s0=Integer.toBinaryString(Integer.parseInt(strs[0]+strs[1], 16));//BMS状态
            for(int i=0;i<s0.length();i++){
                one.set(i,Integer.parseInt(s0.substring(s0.length()-1-i, s0.length()-i)));
            }
            if(s0.length()<16){
                for(int j=0;j<16-s0.length();j++){
                    one.set(s0.length()+j,0);
                }
            }
            one.set(16,String.valueOf(Integer.parseInt(strs[2], 16))+
                    String.valueOf(Integer.parseInt(strs[3], 16))+
                    String.valueOf(Integer.parseInt(strs[4], 16))+
                    String.valueOf(Integer.parseInt(strs[5], 16))+
                    String.valueOf(Integer.parseInt(strs[6], 16))+
                    String.valueOf(Integer.parseInt(strs[7], 16))+
                    String.valueOf(Integer.parseInt(strs[8], 16))+
                    String.valueOf(Integer.parseInt(strs[9], 16))+
                    String.valueOf(Integer.parseInt(strs[10], 16))+
                    String.valueOf(Integer.parseInt(strs[11], 16))+
                    String.valueOf(Integer.parseInt(strs[12], 16))+
                    String.valueOf(Integer.parseInt(strs[13], 16))+
                    String.valueOf(Integer.parseInt(strs[14], 16))+
                    String.valueOf(Integer.parseInt(strs[15], 16))+
                    String.valueOf(Integer.parseInt(strs[16], 16))+
                    String.valueOf(Integer.parseInt(strs[17], 16)));//bms
            one.set(17,String.valueOf(Integer.parseInt(strs[18], 16))+
                    String.valueOf(Integer.parseInt(strs[19], 16))+
                    String.valueOf(Integer.parseInt(strs[20], 16))+
                    String.valueOf(Integer.parseInt(strs[21], 16))+
                    String.valueOf(Integer.parseInt(strs[22], 16))+
                    String.valueOf(Integer.parseInt(strs[23], 16))+
                    String.valueOf(Integer.parseInt(strs[24], 16))+
                    String.valueOf(Integer.parseInt(strs[25], 16))+
                    String.valueOf(Integer.parseInt(strs[26], 16))+
                    String.valueOf(Integer.parseInt(strs[27], 16))+
                    String.valueOf(Integer.parseInt(strs[28], 16))+
                    String.valueOf(Integer.parseInt(strs[29], 16))+
                    String.valueOf(Integer.parseInt(strs[30], 16))+
                    String.valueOf(Integer.parseInt(strs[31], 16))+
                    String.valueOf(Integer.parseInt(strs[32], 16))+
                    String.valueOf(Integer.parseInt(strs[33], 16)));//host
            one.set(18,((Integer.parseInt(strs[34]+strs[35], 16)-30000)/100.0));//校准AFE电流增益
            one.set(19,Integer.parseInt(strs[36]+strs[37], 16));//校准第16节电池电压增益
            if(!isNumeric(strs[38]+strs[39]+strs[40]+strs[41]+strs[42]+strs[43])){
                return;
                //one.set(1,"1970-01-01 00:00:00");
            }else{
                String MM=String.valueOf(Integer.parseInt(strs[39], 10)).length()==2?String.valueOf(Integer.parseInt(strs[39], 10))
                        :"0"+String.valueOf(Integer.parseInt(strs[39], 10));
                String dd= String.valueOf(Integer.parseInt(strs[40], 10)).length()==2? String.valueOf(Integer.parseInt(strs[40], 10))
                        :"0"+ String.valueOf(Integer.parseInt(strs[40], 10));
                String hh=String.valueOf(Integer.parseInt(strs[41], 10)).length()==2?String.valueOf(Integer.parseInt(strs[41], 10))
                        :"0"+String.valueOf(Integer.parseInt(strs[41], 10));
                String mm=String.valueOf(Integer.parseInt(strs[42], 10)).length()==2?String.valueOf(Integer.parseInt(strs[42], 10))
                        :"0"+String.valueOf(Integer.parseInt(strs[42], 10));
                String ss=String.valueOf(Integer.parseInt(strs[43], 10)).length()==2?String.valueOf(Integer.parseInt(strs[43], 10))
                        :"0"+String.valueOf(Integer.parseInt(strs[43], 10));
                one.set(20,"20"+String.valueOf(Integer.parseInt(strs[38], 10))+"-"+
                        MM+"-"+
                        dd+" "+
                        hh+":"+
                        mm+":"+ss);//yyyymmdd hh:mm:ss


            }


            one.set(21,Integer.parseInt(strs[44]+strs[45], 16));//复位控制
            one.set(22,Integer.parseInt(strs[46]+strs[47], 16)/4.0);//复位控制
            one.set(23,Integer.parseInt(strs[48]+strs[49], 16));//均衡控制（测试）
            one.set(24,Integer.parseInt(strs[50]+strs[51], 16));//测试控制
            //  one.set(25,Integer.parseInt(strs[28]+strs[29], 16));//测试结果
            String s36=Integer.toBinaryString(Integer.parseInt(strs[52]+strs[53], 16));//测试结果
            for(int i=0;i<s36.length();i++){
                one.set(i+25,Integer.parseInt(s36.substring(s36.length()-1-i, s36.length()-i)));
            }
            if(s36.length()<16){
                for(int j=0;j<16-s36.length();j++){
                    one.set(s36.length()+25+j,0);
                }
            }
            one.set(41,record.get("pid").toString());//pid

            one.set(42,record.get("id").toString());//id

            context.write(one);
        }

    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: WordCount <in_table> <out_table>");
            System.exit(2);
        }
        JobClient.runJob(ToolUtil.runJob(args,BatteryBMS_partRunControlData.TokenizerMapper.class));
    }





}
