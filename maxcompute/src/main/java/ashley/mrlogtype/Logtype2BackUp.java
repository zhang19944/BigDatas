package ashley.mrlogtype;

import ashley.beans.partDeviceInfoBean;
import ashley.beans.partProtectionParameterBean;
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
public class Logtype2BackUp {
    public static class logTypeMapper2 extends MapperBase {

        private Record two;
        partProtectionParameterBean bean2 = new partProtectionParameterBean();

        private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        @Override
        public void setup(Mapper.TaskContext context) throws IOException {

            two = context.createOutputRecord();

        }

        @Override
        public void map(long recordNum, Record record, Mapper.TaskContext context)
                throws IOException {
            String jsonStrings = record.get("content").toString().trim();

            Gson gson = new Gson();
            bean2 = gson.fromJson(jsonStrings, partProtectionParameterBean.class);

            if (!(bean2.getType() ==2)&& jsonStrings.contains("list") ) {
                return;
            }

            String str2=bean2.getPartProtectionParameter();
            if (str2==null){
                return;
            }
            String str2to= URLDecoder.decode(str2, "UTF-8").replaceAll("\\[", "").replaceAll("]", "");
            String[] str2array=str2to.split(",");


            if (str2array.length!=120){ return; }

            if ((Integer.parseInt(str2array[0]+str2array[1],16)<2000)||(Integer.parseInt(str2array[0]+str2array[1],16)>4500)){
                return;
            }else{
                two.set(0, Integer.parseInt(str2array[0]+str2array[1],16));
            }
            if (((Integer.parseInt(str2array[2]+str2array[3],16)-1)<2000)||((Integer.parseInt(str2array[2]+str2array[3],16)-1)>4500)){
                return;
            }else {
                two.set(1, Integer.parseInt(str2array[2]+str2array[3],16));
            }
            if (((Integer.parseInt(str2array[4]+str2array[5],16)-2)<2000)||((Integer.parseInt(str2array[4]+str2array[5],16)-2)>4500)){
                return;
            }else {
                two.set(2, Integer.parseInt(str2array[4]+str2array[5],16));
            }
            //和 8，
            if (((Integer.parseInt(str2array[6]+str2array[7],16))/4<0.25)||((Integer.parseInt(str2array[6]+str2array[7],16))/4>30)){
                return;
            }else {
                two.set(3, Integer.parseInt(str2array[6]+str2array[7],16)/1.0);
            }
            if (((Integer.parseInt(str2array[8]+str2array[9],16)-4)<2000)||((Integer.parseInt(str2array[8]+str2array[9],16)-4)>4500)){
                return;
            }else {
                two.set(4, Integer.parseInt(str2array[8]+str2array[9],16));
            }

            two.set(5, Integer.parseInt(str2array[10]+str2array[11],16)/1.0);
            two.set(6, Integer.parseInt(str2array[12]+str2array[13],16)/1.0);

            if (((((double)Integer.parseInt(str2array[14]+str2array[15],16))/1000)<0.01)||(((double)Integer.parseInt(str2array[14]+str2array[15],16))/1000)>1){
                return;
            }else {
                two.set(7, Integer.parseInt(str2array[14]+str2array[15],16)/1.0);
            }
            if ((((Integer.parseInt(str2array[16]+str2array[17],16)-8))<2000)||((Integer.parseInt(str2array[16]+str2array[17],16)-8)>4500)){
                return;
            }else {
                two.set(8, Integer.parseInt(str2array[16]+str2array[17],16));
            }
            if (((Integer.parseInt(str2array[18]+str2array[19],16)-9)<2000)||((Integer.parseInt(str2array[18]+str2array[19],16)-9)>4500)){
                return;
            }else {
                two.set(9, Integer.parseInt(str2array[18]+str2array[19],16));
            }
            if (((Integer.parseInt(str2array[20]+str2array[21],16)-1000)/100<0.01)||((Integer.parseInt(str2array[20]+str2array[21],16)-1000)/100>300)){
                return;
            }else {
                two.set(10, Integer.parseInt(str2array[20]+str2array[21],16)/1.0);
            }
            if (((Integer.parseInt(str2array[22]+str2array[23],16))/4<0.25)||((Integer.parseInt(str2array[22]+str2array[23],16))/4>30)){
                return;
            }else {
                two.set(11, Integer.parseInt(str2array[22]+str2array[23],16)/1.0);
            }
            if (((Integer.parseInt(str2array[24]+str2array[25],16))/100<0.01)||((Integer.parseInt(str2array[24]+str2array[25],16))/100>300)){
                return;
            }else {
                two.set(12, Integer.parseInt(str2array[24]+str2array[25],16)/1.0);
            }
            if (((Integer.parseInt(str2array[26]+str2array[27],16))/4<0.25)||((Integer.parseInt(str2array[26]+str2array[27],16))/4>30)){
                return;
            }else {
                two.set(13, Integer.parseInt(str2array[26]+str2array[27],16)/1.0);
            }
            if (((Integer.parseInt(str2array[28]+str2array[29],16))/100<0.01)||((Integer.parseInt(str2array[28]+str2array[29],16))/100>300)){
                return;
            }else {
                two.set(14, Integer.parseInt(str2array[28]+str2array[29],16)/1.0);
            }
            if (((Integer.parseInt(str2array[30]+str2array[31],16))/100<0.01)||((Integer.parseInt(str2array[30]+str2array[31],16))/100>300)){
                return;
            }else {
                two.set(15, Integer.parseInt(str2array[30]+str2array[31],16)/1.0);
            }
            //16
            if (((Integer.parseInt(str2array[32]+str2array[33],16)-400)/10<-40)||((Integer.parseInt(str2array[32]+str2array[33],16)-400)/10>120)){
                return;
            }else {
                two.set(16, Integer.parseInt(str2array[32]+str2array[33],16)/1.0);
            }
            if (((Integer.parseInt(str2array[34]+str2array[35],16))/4<0.25)||((Integer.parseInt(str2array[34]+str2array[35],16))/4>30)){
                return;
            }else {
                two.set(17, Integer.parseInt(str2array[34]+str2array[35],16)/1.0);
            }
            if (((Integer.parseInt(str2array[36]+str2array[37],16)-400)/10<-40)||((Integer.parseInt(str2array[36]+str2array[37],16)-400)/10>120)){
                return;
            }else {
                two.set(18, Integer.parseInt(str2array[36]+str2array[37],16)/1.0);
            }
            if (((Integer.parseInt(str2array[38]+str2array[39],16)-400)/10<-40)||((Integer.parseInt(str2array[38]+str2array[39],16)-400)/10>120)){
                return;
            }else {
                two.set(19, Integer.parseInt(str2array[38]+str2array[39],16)/1.0);
            }
            if (((Integer.parseInt(str2array[40]+str2array[41],16))/4<0.25)||((Integer.parseInt(str2array[40]+str2array[41],16))/4>30)){
                return;
            }else {
                two.set(20, Integer.parseInt(str2array[40]+str2array[41],16)/1.0);
            }
            if (((Integer.parseInt(str2array[42]+str2array[43],16)-400)/10<-40)||((Integer.parseInt(str2array[42]+str2array[43],16)-400)/10>120)){
                return;
            }else {
                two.set(21, Integer.parseInt(str2array[42]+str2array[43],16)/1.0);
            }
            if (((Integer.parseInt(str2array[44]+str2array[45],16)-400)/10<-40)||((Integer.parseInt(str2array[44]+str2array[45],16)-400)/10>120)){
                return;
            }else {
                two.set(22, Integer.parseInt(str2array[44]+str2array[45],16)/1.0);
            }
            if (((Integer.parseInt(str2array[46]+str2array[47],16))/4<0.25)||((Integer.parseInt(str2array[46]+str2array[47],16))/4>30)){
                return;
            }else {
                two.set(23, Integer.parseInt(str2array[46]+str2array[47],16)/1.0);
            }
            if (((Integer.parseInt(str2array[48]+str2array[49],16)-400)/10<-40)||((Integer.parseInt(str2array[48]+str2array[49],16)-400)/10>120)){
                return;
            }else {
                two.set(24, Integer.parseInt(str2array[48]+str2array[49],16)/1.0);
            }
            if (((Integer.parseInt(str2array[50]+str2array[51],16)-400)/10<-40)||((Integer.parseInt(str2array[50]+str2array[51],16)-400)/10>120)){
                return;
            }else {
                two.set(25, Integer.parseInt(str2array[50]+str2array[51],16)/1.0);
            }
            if (((Integer.parseInt(str2array[52]+str2array[53],16))/4<0.25)||((Integer.parseInt(str2array[52]+str2array[53],16))/4>30)){
                return;
            }else {
                two.set(26, Integer.parseInt(str2array[52]+str2array[53],16)/1.0);
            }
            if (((Integer.parseInt(str2array[54]+str2array[55],16)-400)/10<-40)||((Integer.parseInt(str2array[54]+str2array[55],16)-400)/10>120)){
                return;
            }else {
                two.set(27, Integer.parseInt(str2array[54]+str2array[55],16)/1.0);
            }
            if (((Integer.parseInt(str2array[56]+str2array[57],16)-400)/10<-40)||((Integer.parseInt(str2array[56]+str2array[57],16)-400)/10>120)){
                return;
            }else {
                two.set(28, Integer.parseInt(str2array[56]+str2array[57],16)/1.0);
            }
            if (((Integer.parseInt(str2array[58]+str2array[59],16))/4<0.25)||((Integer.parseInt(str2array[58]+str2array[59],16))/4>30)){
                return;
            }else {
                two.set(29, Integer.parseInt(str2array[58]+str2array[59],16)/1.0);
            }
            if (((Integer.parseInt(str2array[60]+str2array[61],16)-400)/10<-40)||((Integer.parseInt(str2array[60]+str2array[61],16)-400)/10>120)){
                return;
            }else {
                two.set(30, Integer.parseInt(str2array[60]+str2array[61],16)/1.0);
            }
            if (((Integer.parseInt(str2array[62]+str2array[63],16)-400)/10<-40)||((Integer.parseInt(str2array[62]+str2array[63],16)-400)/10>120)){
                return;
            }else {
                two.set(31, Integer.parseInt(str2array[62]+str2array[63],16)/1.0);
            }
            if (((Integer.parseInt(str2array[64]+str2array[65],16))/4<0.25)||((Integer.parseInt(str2array[64]+str2array[65],16))/4>30)){
                return;
            }else {
                two.set(32, Integer.parseInt(str2array[64]+str2array[65],16)/1.0);
            }
            if (((Integer.parseInt(str2array[66]+str2array[67],16)-400)/10<-40)||((Integer.parseInt(str2array[66]+str2array[67],16)-400)/10>120)){
                return;
            }else {
                two.set(33, Integer.parseInt(str2array[66]+str2array[67],16)/1.0);
            }
            if ((Integer.parseInt(str2array[68]+str2array[69],16)<2000)||(Integer.parseInt(str2array[68]+str2array[69],16)>4500)){
                return;
            }else{
                two.set(34, Integer.parseInt(str2array[68]+str2array[69],16));
            }
            if (((Integer.parseInt(str2array[70]+str2array[71],16))/4<0.25)||((Integer.parseInt(str2array[70]+str2array[71],16))/4>30)){
                return;
            }else {
                two.set(35, Integer.parseInt(str2array[70]+str2array[71],16)/1.0);
            }
            if ((Integer.parseInt(str2array[72]+str2array[73],16)<2000)||(Integer.parseInt(str2array[72]+str2array[73],16)>4500)){
                return;
            }else{
                two.set(36, Integer.parseInt(str2array[72]+str2array[73],16));
            }
            if ((Integer.parseInt(str2array[74]+str2array[75],16)<2000)||(Integer.parseInt(str2array[74]+str2array[75],16)>4500)){
                return;
            }else{
                two.set(37, Integer.parseInt(str2array[74]+str2array[75],16));
            }
            if (((Integer.parseInt(str2array[76]+str2array[77],16))/4<0.25)||((Integer.parseInt(str2array[76]+str2array[77],16))/4>30)){
                return;
            }else {
                two.set(38, Integer.parseInt(str2array[76]+str2array[77],16)/1.0);
            }
            if ((Integer.parseInt(str2array[78]+str2array[79],16)<2000)||(Integer.parseInt(str2array[78]+str2array[79],16)>4500)){
                return;
            }else{
                two.set(39, Integer.parseInt(str2array[78]+str2array[79],16));
            }
            if (((Integer.parseInt(str2array[80]+str2array[81],16))/100<0.01)||((Integer.parseInt(str2array[80]+str2array[81],16))/100>300)){
                return;
            }else {
                two.set(40, Integer.parseInt(str2array[80]+str2array[81],16)/1.0);
            }
            if (((Integer.parseInt(str2array[82]+str2array[83],16))/4<0.25)||((Integer.parseInt(str2array[82]+str2array[83],16))/4>30)){
                return;
            }else {
                two.set(41, Integer.parseInt(str2array[82]+str2array[83],16)/1.0);
            }
            if (((Integer.parseInt(str2array[84]+str2array[85],16))/100<0.01)||((Integer.parseInt(str2array[84]+str2array[85],16))/100>300)){
                return;
            }else {
                two.set(42, Integer.parseInt(str2array[84]+str2array[85],16)/1.0);
            }
            if (((Integer.parseInt(str2array[86]+str2array[87],16))/100<0.01)||((Integer.parseInt(str2array[86]+str2array[87],16))/100>300)){
                return;
            }else {
                two.set(43, Integer.parseInt(str2array[86]+str2array[87],16)/1.0);
            }
            if (((Integer.parseInt(str2array[88]+str2array[89],16))/4<0.25)||((Integer.parseInt(str2array[88]+str2array[89],16))/4>30)){
                return;
            }else {
                two.set(44, Integer.parseInt(str2array[88]+str2array[89],16)/1.0);
            }
            if (((Integer.parseInt(str2array[90]+str2array[91],16))/100<0.01)||((Integer.parseInt(str2array[90]+str2array[91],16))/100>300)){
                return;
            }else {
                two.set(45, Integer.parseInt(str2array[90]+str2array[91],16)/1.0);
            }
            if (((Integer.parseInt(str2array[92]+str2array[93],16)-400)/10<-40)||((Integer.parseInt(str2array[92]+str2array[93],16)-400)/10>120)){
                return;
            }else {
                two.set(46, Integer.parseInt(str2array[92]+str2array[93],16)/1.0);
            }
            if (((Integer.parseInt(str2array[94]+str2array[95],16))/4<0.25)||((Integer.parseInt(str2array[94]+str2array[95],16))/4>30)){
                return;
            }else {
                two.set(47, Integer.parseInt(str2array[94]+str2array[95],16)/1.0);
            }
            if (((Integer.parseInt(str2array[96]+str2array[97],16)-400)/10<-40)||((Integer.parseInt(str2array[96]+str2array[97],16)-400)/10>120)){
                return;
            }else {
                two.set(48, Integer.parseInt(str2array[96]+str2array[97],16)/1.0);
            }
            if (((Integer.parseInt(str2array[98]+str2array[99],16)-400)/10<-40)||((Integer.parseInt(str2array[98]+str2array[99],16)-400)/10>120)){
                return;
            }else {
                two.set(49, Integer.parseInt(str2array[98]+str2array[99],16)/1.0);
            }
            if (((Integer.parseInt(str2array[100]+str2array[101],16))/4<0.25)||((Integer.parseInt(str2array[100]+str2array[101],16))/4>30)){
                return;
            }else {
                two.set(50, Integer.parseInt(str2array[100]+str2array[101],16)/1.0);
            }
            if (((Integer.parseInt(str2array[102]+str2array[103],16)-400)/10<-40)||((Integer.parseInt(str2array[102]+str2array[103],16)-400)/10>120)){
                return;
            }else {
                two.set(51, Integer.parseInt(str2array[102]+str2array[103],16)/1.0);
            }
            if (((Integer.parseInt(str2array[104]+str2array[105],16)-400)/10<-40)||((Integer.parseInt(str2array[104]+str2array[105],16)-400)/10>120)){
                return;
            }else {
                two.set(52, Integer.parseInt(str2array[104]+str2array[105],16)/1.0);
            }
            if (((Integer.parseInt(str2array[106]+str2array[107],16))/4<0.25)||((Integer.parseInt(str2array[106]+str2array[107],16))/4>30)){
                return;
            }else {
                two.set(53, Integer.parseInt(str2array[106]+str2array[107],16)/1.0);
            }
            if (((Integer.parseInt(str2array[108]+str2array[109],16)-400)/10<-40)||((Integer.parseInt(str2array[108]+str2array[109],16)-400)/10>120)){
                return;
            }else {
                two.set(54, Integer.parseInt(str2array[108]+str2array[109],16)/1.0);
            }
            if (((Integer.parseInt(str2array[110]+str2array[111],16)-400)/10<-40)||((Integer.parseInt(str2array[110]+str2array[111],16)-400)/10>120)){
                return;
            }else {
                two.set(55, Integer.parseInt(str2array[110]+str2array[111],16)/1.0);
            }
            if (((Integer.parseInt(str2array[112]+str2array[113],16))/4<0.25)||((Integer.parseInt(str2array[112]+str2array[113],16))/4>30)){
                return;
            }else {
                two.set(56, Integer.parseInt(str2array[112]+str2array[113],16)/1.0);
            }
            if (((Integer.parseInt(str2array[114]+str2array[115],16)-400)/10<-40)||((Integer.parseInt(str2array[114]+str2array[115],16)-400)/10>120)){
                return;
            }else {
                two.set(57, Integer.parseInt(str2array[114]+str2array[115],16)/1.0);
            }
            two.set(58, Integer.parseInt(str2array[116]+str2array[117],16));
            two.set(59, Integer.parseInt(str2array[118]+str2array[119],16));

            two.set(60, bean2.getpId());
            two.setString(61, bean2.getbId());
            long times=record.getBigint("collecttime");
            two.setDatetime(62,new Date(times));
            two.set(63,bean2.getPort());

            context.write(two);
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
        JobClient.runJob(ToolUtil.runJob(args, logtype2.logTypeMapper2.class));
        System.out.println("finished");
    }
}
