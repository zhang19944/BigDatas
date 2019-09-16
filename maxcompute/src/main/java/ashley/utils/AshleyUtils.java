package ashley.utils;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * Created by AshleyZHANG on 2019/7/11.
 */
public class AshleyUtils {
    // 两点距离
    public static double latDistance(double latA, double logA, double latB, double logB) {
        int earthR = 6371000;
        double x = Math.cos(latA * Math.PI / 180) * Math.cos(latB * Math.PI / 180) * Math.cos((logA - logB) * Math.PI / 180);
        double y = Math.sin(latA * Math.PI / 180) * Math.sin(latB * Math.PI / 180);
        double s = x + y;
        if (s > 1)
            s = 1;
        if (s < -1)
            s = -1;
        double alpha = Math.acos(s);
        double distance = alpha * earthR;
        return distance;
    }

    //时间格式转化为时间戳，获得10位秒。
    public long timeToStamp(String s) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = simpleDateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long ts = date.getTime()/1000;
        return ts;
    }
    //获取时间的年，月，日，时分秒 截串
    public String evaluate(String s) {
        String year=s.substring(0, 4);
        String month=s.substring(4, 6);
        String day=s.substring(6, 8);
        String date=year+"-"+month+"-"+day+" "+"00:00:00";
        return date;
    }
    //判断这个数是否是16进制的数
    public boolean hex(String s){
        //String s="123bf";
        String regex="^[A-Fa-f0-9]+$";
        if(s.matches(regex)){
            System.out.println(s.toUpperCase()+"是16进制数");
        }else{
            System.out.println(s.toUpperCase()+"不是16进制数");
        }
        return s.matches(regex);
    }
    //获取到properties配置文件的值
    public static String getValue (String key) throws Exception {
        Properties properties = new Properties();
        String filePath=System.getProperty("user.dir")+"/src/main/resources/application.properties";
        BufferedReader bufferedReader =new BufferedReader(new FileReader(filePath));
        properties.load(bufferedReader);
        return properties.getProperty(key).trim();
    }

    //执行Linux命令
    public String exec(String host,String command) {
        //配置linux服务器信息
         /* private static String IP = "10.125.209.14";
            private static String USER = "kyapp";//  远程Linux服务器的用户名
            private static String PASSWORD = "ky123456";// 远程Linux服务器的登录密码
            private static int PORT = 22;
            private String Command = "mkdir bbbbb";*/

        String user="kyapp";
        String psw="ky123456";
        int port= 22;

        //创建缓冲字符流
        StringBuffer sb = new StringBuffer();
        Session session = null;
        ChannelExec openChannel = null;
        try {
            //创建JSch - SSH协议
            JSch jsch = new JSch();
            //获取连接信息
            session = jsch.getSession(user, host, port);
            //创建配置文件
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");//跳过公钥的询问
            session.setConfig(config);
            session.setPassword(psw);
            session.connect(5000);//设置连接的超时时间
            openChannel = (ChannelExec) session.openChannel("exec");
            openChannel.setCommand(command); //执行命令
            int exitStatus = openChannel.getExitStatus(); //退出状态为-1，直到通道关闭
            System.out.println(exitStatus);
            // 下面是得到输出的内容
            openChannel.connect();
            InputStream in = openChannel.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String buf = null;
            while ((buf = reader.readLine()) != null) {
                sb.append(buf + "\n");
            }
        } catch (JSchException | IOException e) {
            sb.append(e.getMessage() + "\n");
        } finally {
            if (openChannel != null && !openChannel.isClosed()) {
                openChannel.disconnect();
            }
            if (session != null && session.isConnected()) {
                session.disconnect();
            }
        }
        return sb.toString();
    }
}
