package ashley.mrlogtype;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.odps.data.Record;
import com.aliyun.odps.mapred.JobClient;
import com.aliyun.odps.mapred.Mapper;
import com.aliyun.odps.mapred.MapperBase;
import com.aliyun.odps.mapred.conf.JobConf;
import util.ToolUtil;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by AshleyZHANG on 2019/5/29.
 */
public class logtype7 {
    public static class logTypeMapper extends MapperBase {
        private Record one;
        private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        @Override
        public void setup(Mapper.TaskContext context) throws IOException {
            one = context.createOutputRecord();
        }

        //map方法
        @Override
        public void map(long recordNum, Record record, Mapper.TaskContext context)
                throws IOException {
            String jsonString = record.get("content").toString().trim();
            System.out.println(jsonString);
            if (!jsonString.contains("\"type\":7"))   return;
            //解析数据
            JSONObject jsonObject =JSON.parseObject(jsonString);
            long type=jsonObject.getLong("type");
            String pid=jsonObject.getString("pId");
            String list=jsonObject.getString("list");
            JSONArray jsonArray = JSON.parseArray(list);// json数组对象
            System.out.println(jsonArray.size());
            //list中的content对象数组
            if(jsonArray.size()>0){
                Iterator<Object> it = jsonArray.iterator();
                while (it.hasNext()){
                    JSONObject arrayObj = (JSONObject) it.next();
                    String content = arrayObj.getString("content");
                    long time = arrayObj.getLong("time");
                    byte data [] = hexString2Byte(content);
                    int minLength = 2;
                    int num = byteToInt(data[0]);
                    int singleLen = byteToInt(data[1]);
                    if(data.length < minLength + num*singleLen){
                        System.out.println("数据有问题");
                    }else {
                        //ParseDemo.BatteryInfo对象映射
                        List<ParseDemo.BatteryInfo> batteries = new ArrayList<>();
                        for(int i = 0; i < num; i++){
                            ParseDemo.BatteryInfo info = new ParseDemo.BatteryInfo();
                            //info _byte
                            byte[] infoData = new byte[singleLen];
                            System.arraycopy(data,minLength+i*singleLen, infoData, 0, singleLen);
                            info.parseData(infoData);
                            batteries.add(info);
                            one.set(0,num);
                            one.set(1,singleLen);
                            one.setBigint(2, (long) info.getPort());
                            one.set(3,info.getSoc());
                            one.set(4,info.getVoltage());
                            one.setBigint(5, (long) info.getCurrent());
                            one.setBigint(6, (long) info.getTemperature());
                            one.setBigint(7, (long) info.getFault());
                            one.setBigint(8, (long) info.getDamage());
                            one.setBigint(9, (long) info.getCycle());
                            one.set(10,info.getId());
                            one.set(11,info.getNominalVoltage());
                            one.set(12,info.getSoh());
                            one.set(13,info.getReserved());
                            one.set(14,info.getDesignCapacity());
                            one.set(15,info.getFgt());
                            one.set(16,info.getBt2());
                            one.set(17,info.getCmt());
                            one.set(18,info.getCt());
                            one.setBigint(19, (long) ((info.getDf1() >> 15) & 0x1));
                            one.setBigint(20, (long) ((info.getDf1() >> 14) & 0x1));
                            one.setBigint(21, (long) ((info.getDf1() >> 13) & 0x1));
                            one.setBigint(22, (long) ((info.getDf1() >> 12) & 0x1));
                            one.setBigint(23, (long) ((info.getDf1() >> 11) & 0x1));
                            one.setBigint(24, (long) ((info.getDf1() >> 10) & 0x1));
                            one.setBigint(25, (long) ((info.getDf1() >> 9) & 0x1));
                            one.setBigint(26, (long) ((info.getDf1() >> 8) & 0x1));
                            one.setBigint(27, (long) ((info.getDf1() >> 7) & 0x1));
                            one.setBigint(28, (long) ((info.getDf1() >> 6) & 0x1));
                            one.setBigint(29, (long) ((info.getDf1() >> 5) & 0x1));
                            one.setBigint(30, (long) ((info.getDf1() >> 4) & 0x1));
                            one.setBigint(31, (long) ((info.getDf1() >> 3) & 0x1));
                            one.setBigint(32, (long) ((info.getDf1() >> 2) & 0x1));
                            one.setBigint(33, (long) ((info.getDf1() >> 1) & 0x1));
                            one.setBigint(34, (long) ((info.getDf1() >> 0) & 0x1));
                            one.setBigint(35, (long) ((info.getDf2() >> 15) & 0x1));
                            one.setBigint(36,(long)((info.getDf2() >> 14) & 0x1));
                            one.setBigint(37,(long)((info.getDf2() >> 13) & 0x1));
                            one.setBigint(38,(long)((info.getDf2() >> 12) & 0x1));
                            one.setBigint(39,(long)((info.getDf2() >> 11) & 0x1));
                            one.setBigint(40,(long)((info.getDf2() >> 10) & 0x1));
                            one.setBigint(41,(long)((info.getDf2() >> 9) & 0x1));
                            one.setBigint(42,(long)((info.getDf2() >> 8) & 0x1));
                            one.setBigint(43,(long)((info.getDf2() >> 7) & 0x1));
                            one.setBigint(44,(long)((info.getDf2() >> 6) & 0x1));
                            one.setBigint(45,(long)((info.getDf2() >> 5) & 0x1));
                            one.setBigint(46,(long)((info.getDf2() >> 4) & 0x1));
                            one.setBigint(47,(long)((info.getDf2() >> 3) & 0x1));
                            one.setBigint(48,(long)((info.getDf2() >> 2) & 0x1));
                            one.setBigint(49,(long)((info.getDf2() >> 1) & 0x1));
                            one.setBigint(50,(long)((info.getDf2() >> 0) & 0x1));
                            one.setBigint(51,(long)((info.getOf1() >> 15) & 0x1));
                            one.setBigint(52,(long)((info.getOf1() >> 14) & 0x1));
                            one.setBigint(53,(long)((info.getOf1() >> 13) & 0x1));
                            one.setBigint(54,(long)((info.getOf1() >> 12) & 0x1));
                            one.setBigint(55,(long)((info.getOf1() >> 11) & 0x1));
                            one.setBigint(56,(long)((info.getOf1() >> 10) & 0x1));
                            one.setBigint(57,(long)((info.getOf1() >> 9) & 0x1));
                            one.setBigint(58,(long)((info.getOf1() >> 8) & 0x1));
                            one.setBigint(59,(long)((info.getOf1() >> 7) & 0x1));
                            one.setBigint(60,(long)((info.getOf1() >> 6) & 0x1));
                            one.setBigint(61,(long)((info.getOf1() >> 5) & 0x1));
                            one.setBigint(62,(long)((info.getOf1() >> 4) & 0x1));
                            one.setBigint(63,(long)((info.getOf1() >> 3) & 0x1));
                            one.setBigint(64,(long)((info.getOf1() >> 2) & 0x1));
                            one.setBigint(65,(long)((info.getOf1() >> 1) & 0x1));
                            one.setBigint(66,(long)((info.getOf1() >> 0) & 0x1));
                            one.setBigint(67,(long)((info.getOf2() >> 15) & 0x1));
                            one.setBigint(68,(long)((info.getOf2() >> 14) & 0x1));
                            one.setBigint(69,(long)((info.getOf2() >> 13) & 0x1));
                            one.setBigint(70,(long)((info.getOf2() >> 12) & 0x1));
                            one.setBigint(71,(long)((info.getOf2() >> 11) & 0x1));
                            one.setBigint(72,(long)((info.getOf2() >> 10) & 0x1));
                            one.setBigint(73,(long)((info.getOf2() >> 9) & 0x1));
                            one.setBigint(74,(long)((info.getOf2() >> 8) & 0x1));
                            one.setBigint(75,(long)((info.getOf2() >> 7) & 0x1));
                            one.setBigint(76,(long)((info.getOf2() >> 6) & 0x1));
                            one.setBigint(77,(long)((info.getOf2() >> 5) & 0x1));
                            one.setBigint(78,(long)((info.getOf2() >> 4) & 0x1));
                            one.setBigint(79,(long)((info.getOf2() >> 3) & 0x1));
                            one.setBigint(80,(long)((info.getOf2() >> 2) & 0x1));
                            one.setBigint(81,(long)((info.getOf2() >> 1) & 0x1));
                            one.setBigint(82,(long)((info.getOf2() >> 0) & 0x1));
                            one.setBigint(83,(long)((info.getBs() >> 15) & 0x1));
                            one.setBigint(84,(long)((info.getBs() >> 14) & 0x1));
                            one.setBigint(85,(long)((info.getBs() >> 13) & 0x1));
                            one.setBigint(86,(long)((info.getBs() >> 12) & 0x1));
                            one.setBigint(87,(long)((info.getBs() >> 11) & 0x1));
                            one.setBigint(88,(long)((info.getBs() >> 10) & 0x1));
                            one.setBigint(89,(long)((info.getBs() >> 9) & 0x1));
                            one.setBigint(90,(long)((info.getBs() >> 8) & 0x1));
                            one.setBigint(91,(long)((info.getBs() >> 7) & 0x1));
                            one.setBigint(92,(long)((info.getBs() >> 6) & 0x1));
                            one.setBigint(93,(long)((info.getBs() >> 5) & 0x1));
                            one.setBigint(94,(long)((info.getBs() >> 4) & 0x1));
                            one.setBigint(95,(long)((info.getBs() >> 3) & 0x1));
                            one.setBigint(96,(long)((info.getBs() >> 2) & 0x1));
                            one.setBigint(97,(long)((info.getBs() >> 1) & 0x1));
                            one.setBigint(98,(long)((info.getBs() >> 0) & 0x1));
                            one.setBigint(99, (long) info.getBv0());
                            one.setBigint(100, (long) info.getBv1());
                            one.setBigint(101, (long) info.getBv2());
                            one.setBigint(102, (long) info.getBv3());
                            one.setBigint(103, (long) info.getBv4());
                            one.setBigint(104, (long) info.getBv5());
                            one.setBigint(105, (long) info.getBv6());
                            one.setBigint(106, (long) info.getBv7());
                            one.setBigint(107, (long) info.getBv8());
                            one.setBigint(108, (long) info.getBv9());
                            one.setBigint(109, (long) info.getBv10());
                            one.setBigint(110, (long) info.getBv11());
                            one.setBigint(111, (long) info.getBv12());
                            one.setBigint(112, (long) info.getBv13());
                            one.setBigint(113, (long) info.getBv14());
                            one.setBigint(114, (long) info.getBv15());
                            one.set(115,new Date(time));
                            one.set(116,type);
                            one.set(117,pid);
                            DecimalFormat df  = new DecimalFormat("######0.00000");
                            one.set(118,Double.valueOf(df.format(info.getVoltage())));
                            one.set(119,Double.valueOf(df.format(info.getCurrent())));
                            // one.set(7,info.getFault());
                            // one.set(8,info.getDamage());
                            one.setBigint(120, (long) ((info.getFault() >> 7) & 0x1));
                            one.setBigint(121, (long) ((info.getFault() >> 6) & 0x1));
                            one.setBigint(122, (long) ((info.getFault()>> 5) & 0x1));
                            one.setBigint(123, (long) ((info.getFault()>> 4) & 0x1));
                            one.setBigint(124, (long) ((info.getFault()>> 3) & 0x1));
                            one.setBigint(125, (long) ((info.getFault()>> 2) & 0x1));
                            one.setBigint(126, (long) ((info.getFault()>> 1) & 0x1));
                            one.setBigint(127, (long) ((info.getFault()>> 0) & 0x1));
                            one.setBigint(128, (long) ((info.getDamage() >> 7) & 0x1));
                            one.setBigint(129, (long) ((info.getDamage() >> 6) & 0x1));
                            one.setBigint(130, (long) ((info.getDamage() >> 5) & 0x1));
                            one.setBigint(131, (long) ((info.getDamage()>> 4) & 0x1));
                            one.setBigint(132, (long) ((info.getDamage()>> 3) & 0x1));
                            one.setBigint(133, (long) ((info.getDamage()>> 2) & 0x1));
                            one.setBigint(134, (long) ((info.getDamage()>> 1) & 0x1));
                            one.setBigint(135, (long) ((info.getDamage()>> 0) & 0x1));
                            one.setString(136, String.valueOf(info.getTvsTemp()));
                            one.setString(137, String.valueOf(info.getMaxVoltRate()));
                            one.setString(138, String.valueOf(info.getMaxVoltRateInd()));
                            one.setString(139, String.valueOf(info.getMaxTempRate()));
                            one.setString(140, String.valueOf(info.getMaxTempRateInd()));

                           // one.set(17,info.);
                            context.write(one);
                        }
                        //System.out.println(batteries.get(10).getBv7());
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
        JobClient.runJob(ToolUtil.runJob(args, logtype7.logTypeMapper.class));
        System.out.println("完成");
    }
    public static String byteToBit(byte b) {
        return "" +(byte)((b >> 7) & 0x1) +
                (byte)((b >> 6) & 0x1) +
                (byte)((b >> 5) & 0x1) +
                (byte)((b >> 4) & 0x1) +
                (byte)((b >> 3) & 0x1) +
                (byte)((b >> 2) & 0x1) +
                (byte)((b >> 1) & 0x1) +
                (byte)((b >> 0) & 0x1);
    }

    /**
     * 单个字节转化为Int数值
     * @param b
     * @return
     */
    public static int byteToInt(byte b) {
        return (b & 0xFF);
    }
    /**
     * 高位在前
     * 注意，函数中不会对字节数组长度进行判断，请自行保证传入参数的正确性。
     */
    public static int bytesToIntBig(byte[] b) {
        int len = b.length;
        int result = 0;
        for(int i=0; i<len; i++){
            result += (b[i] & 0xFF) << (8 * (len - i - 1));
        }
        return result;
    }

    /**
     * 低位在前
     */
    public static int bytesToInt(byte[] b){
        int len = b.length;
        int result = 0;
        for(int i = 0; i < len; i++){
            result += (b[i]&0xff) << (8 * i);
        }
        return result;
    }
    public static byte[] hexString2Byte(String str){
        str = str.replace("[","");
        str = str.replace("]","");
        str = str.replace(",","");
        try{
            byte[] result = new byte[str.length()/2];
            String temp;
            for(int i = 0; i < str.length()/2;i++){
                temp = str.substring(2*i, 2*i+2);
                result[i] =  (byte) Integer.parseInt(temp, 16);
            }
            return result;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}

