package ashley;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by AshleyZHANG on 2019/5/10.
 */
public class AshleyDateUtils{

    public String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }
    public long timeToStamp(String s){
        String res;
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

    public static void main(String[] args) {
        AshleyDateUtils ashleyDateUtils=new AshleyDateUtils();

        System.out.println(ashleyDateUtils.timeToStamp("2019-06-25 00:00:00"));
    }

}



