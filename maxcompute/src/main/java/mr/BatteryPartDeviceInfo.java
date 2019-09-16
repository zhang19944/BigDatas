package mr;

import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.aliyun.odps.data.Record;
import com.aliyun.odps.data.TableInfo;
import com.aliyun.odps.mapred.JobClient;
import com.aliyun.odps.mapred.MapperBase;
import com.aliyun.odps.mapred.conf.JobConf;
import com.aliyun.odps.mapred.utils.InputUtils;
import com.aliyun.odps.mapred.utils.OutputUtils;

public class BatteryPartDeviceInfo {

    public static class TokenizerMapper extends MapperBase {
        private Record one;
        private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        @Override
        public void setup(TaskContext context) throws IOException {
            one=context.createOutputRecord();
        }

        @Override
        public void map(long recordNum, Record record, TaskContext context)
                throws IOException {

            String str = record.get("partdeviceinfo").toString();
            String partReadOnlyData= URLDecoder.decode(str, "UTF-8").replaceAll("\\[", "").replaceAll("]", "");
            String[] strs=partReadOnlyData.split(",");
            // String[] strs=partDeviceInfos.split(",");
            one.set(25,(Date)record.get("collecttime"));
            one.set(26,record.get("pid").toString());
            one.set(27,record.get("id").toString());

            one.set(0,Integer.parseInt(strs[0]+strs[1], 16));//协议版本
            one.set(1,Double.parseDouble(Integer.parseInt(strs[2], 16)+"."+Integer.parseInt(strs[3], 16)));//硬件版本
            one.set(2,Double.parseDouble(Integer.parseInt(strs[4], 16)+"."+Integer.parseInt(strs[5], 16)));//Bootloader版本
            one.set(3,Double.parseDouble(Integer.parseInt(strs[6], 16)+"."+Integer.parseInt(strs[7], 16)));//固件版本-主次版本
            one.set(4,Double.parseDouble(Integer.parseInt(strs[8], 16)+"."+Integer.parseInt(strs[9], 16)));//固件版本-修正版本
            one.set(5,Integer.parseInt(strs[10]+strs[11]+strs[12]+strs[13], 16));//固件版本
            Integer battery14=Integer.parseInt(strs[14], 16);
            Integer battery15=Integer.parseInt(strs[15], 16);
            Integer battery16=Integer.parseInt(strs[16], 16);
            Integer battery17=Integer.parseInt(strs[17], 16);
            Integer battery18=Integer.parseInt(strs[18], 16);
            Integer battery19=Integer.parseInt(strs[19], 16);
            Integer battery20=Integer.parseInt(strs[20], 16);
            Integer battery21=Integer.parseInt(strs[21], 16);
            one.set(6,battery14.toString()+battery15.toString()+battery16.toString()+battery17.toString()+battery18.toString()+battery19.toString()+battery20.toString()+battery21.toString());//MCUN
            one.set(7,Integer.parseInt(strs[22]+strs[23], 16));//电池类型
            System.out.println(Integer.parseInt(strs[24]+strs[25], 16)/100.0);
            one.set(8,Integer.parseInt(strs[24]+strs[25], 16)/100.0);//额定电压
            one.set(9,Integer.parseInt(strs[26]+strs[27], 16));//额定容量
            Integer battery28=Integer.parseInt(strs[28], 16);
            Integer battery29=Integer.parseInt(strs[29], 16);
            Integer battery30=Integer.parseInt(strs[30], 16);
            Integer battery31=Integer.parseInt(strs[31], 16);
            Integer battery32=Integer.parseInt(strs[32], 16);
            Integer battery33=Integer.parseInt(strs[33], 16);
            Integer battery34=Integer.parseInt(strs[34], 16);
            Integer battery35=Integer.parseInt(strs[35], 16);
            one.set(10,battery28.toString()+battery29.toString()+battery30.toString()+battery31.toString()+battery32.toString()+battery33.toString()+battery34.toString()+battery35.toString());//sn
            one.set(11,Integer.parseInt(strs[36]+strs[37]+strs[38]+strs[39], 16));//最早记录号
            one.set(12,Integer.parseInt(strs[40]+strs[41]+strs[42]+strs[43], 16));//最近记录号
            Integer battery44=Integer.parseInt(strs[44]+strs[45], 16);
            String s40=Integer.toBinaryString(battery44);
            System.out.println(s40);
            if(s40.length()==1){
                one.set(13,Integer.parseInt(s40));//电池曾被拆卸
                one.set(14,0);//电池曾被碰撞

            }else{
                one.set(13,Integer.parseInt(s40.substring(s40.length()-1)));//电池曾被拆卸
                one.set(14,Integer.parseInt(s40.substring(s40.length()-2,s40.length()-1)));//电池曾被碰撞

            }
            one.set(15,Integer.parseInt(strs[46]+strs[47], 16)==65535?-100:Integer.parseInt(strs[46]+strs[47], 16));//历史最高单体电压
            one.set(16,Integer.parseInt(strs[48]+strs[49], 16)==65535?-100:Integer.parseInt(strs[48]+strs[49], 16));//历史最高单体电压编号
            one.set(17,Integer.parseInt(strs[50]+strs[51], 16)==65535?-100:Integer.parseInt(strs[50]+strs[51], 16));//历史最低单体电压
            one.set(18,Integer.parseInt(strs[52]+strs[53], 16)==65535?-100:Integer.parseInt(strs[52]+strs[53], 16));//历史最低单体电压编号
            one.set(19,Integer.parseInt(strs[54]+strs[55], 16)==65535?-100.0:Integer.parseInt(strs[54]+strs[55], 16)/10.0) ;//历史最高温度
            one.set(20,Integer.parseInt(strs[56]+strs[57], 16)==65535?-100:Integer.parseInt(strs[56]+strs[57], 16));//历史最高温度编号
            one.set(21,Integer.parseInt(strs[58]+strs[59], 16)==65535?-100.0:Integer.parseInt(strs[58]+strs[59], 16)/10.0);//历史最低温度
            one.set(22,Integer.parseInt(strs[60]+strs[61], 16)==65535?-100:Integer.parseInt(strs[60]+strs[61], 16));//历史最低温度编号
            one.set(23,Integer.parseInt(strs[62]+strs[63], 16)==65535?-100.0:Integer.parseInt(strs[62]+strs[63], 16)/100.0);//历史最大充电电流
            one.set(24,Integer.parseInt(strs[64]+strs[65], 16)==65535?-100.0:Integer.parseInt(strs[64]+strs[65], 16)/100.0);//历史最大放电电流
//	        for(int i=1;i<str.length;i++){
//	    	  String[] splitStr = str[i].split("=");
//	          one.set((i-1), splitStr[1]);
//	       }
            context.write(one);
        }

    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: WordCount <in_table> <out_table>");
            System.exit(2);
        }
        JobConf job = new JobConf();
        job.setMapperClass(TokenizerMapper.class);
        // job.setMapOutputValueSchema(SchemaUtils.fromString("hw:double,id:string,sw:string"));
        InputUtils.addTable(TableInfo.builder().tableName(args[0]).build(), job);
        OutputUtils.addTable(TableInfo.builder().tableName(args[1]).build(), job);
        JobClient.runJob(job);
    }



}
