package ashley.mrlogtype;

import ashley.AshleyDateUtils;
import ashley.beans.BatteryInfoBean;
import com.aliyun.odps.data.Record;
import com.aliyun.odps.mapred.*;
import com.aliyun.odps.mapred.conf.JobConf;
import com.aliyun.odps.mapred.utils.SchemaUtils;
import org.apache.commons.codec.language.bm.Lang;
import util.ToolUtil;
import java.io.IOException;
import java.security.Timestamp;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by AshleyZHANG on 2019/6/25.
 */
public class BatteryInfo {
    public static class TokenizerMapper extends MapperBase {
        private Record key;
        private Record value;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//这个是你要转成后的时间的格式

        @Override
        public void setup(Mapper.TaskContext context) throws IOException {
            key = context.createMapOutputKeyRecord();
            value = context.createMapOutputValueRecord();
        }

        @Override
        public void map(long recordNum, Record record, Mapper.TaskContext context)
                throws IOException {

            key.setString(0, record.get("id").toString());

            value.set(0, record.get("pid").toString());
            value.set(1, record.get("pid_sn").toString());
            value.setBigint(2, (Long) record.get("p"));
            value.setString(3, record.get("id").toString());
            value.set(4, record.get("id_sn").toString());
            value.setDatetime(5, (Date) record.get("time"));
            value.setBigint(6, (Long) record.get("bv0"));
            value.setBigint(7, (Long) record.get("bv1"));
            value.setBigint(8, (Long) record.get("bv2"));
            value.setBigint(9, (Long) record.get("bv3"));
            value.setBigint(10, (Long) record.get("bv4"));
            value.setBigint(11, (Long) record.get("bv5"));
            value.setBigint(12, (Long) record.get("bv6"));
            value.setBigint(13, (Long) record.get("bv7"));
            value.setBigint(14, (Long) record.get("bv8"));
            value.setBigint(15, (Long) record.get("bv9"));
            value.setBigint(16, (Long) record.get("bv10"));
            value.setBigint(17, (Long) record.get("bv11"));
            value.setBigint(18, (Long) record.get("bv12"));
            value.setBigint(19, (Long) record.get("bv13"));
            value.setBigint(20, (Long) record.get("bv14"));
            value.setBigint(21, (Long) record.get("bv15"));
            value.setBigint(22, (Long) record.get("f"));
            value.setBigint(23, (Long) record.get("f_b2"));
            value.setBigint(24, (Long) record.get("c"));


            context.write(key, value);
        }

    }


    public static long dateToTimestamp(String time) {


        SimpleDateFormat sf1 = new SimpleDateFormat("EEE MMM dd hh:mm:ss z yyyy", Locale.ENGLISH);
        Date date = null;
        try {
            date = sf1.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time2 = sf2.format(date);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date dates = simpleDateFormat.parse(time2);
            long ts = dates.getTime() / 1000;
            return ts;
        } catch (ParseException e) {
            return 0;
        }
    }

    public static class SumReducer extends ReducerBase {

        private Record result = null;

        @Override
        public void setup(Reducer.TaskContext context) throws IOException {
            result = context.createOutputRecord();
        }

        //相同的key进入一个reduce
        @Override
        public void reduce(Record key, Iterator<Record> values, TaskContext context)
                throws IOException {

            List<BatteryInfoBean> arraylist = new ArrayList<BatteryInfoBean>();

            //获取相同key的v,100条
            while (values.hasNext()) {

                Record record = values.next();
                BatteryInfoBean batteryInfoBean = new BatteryInfoBean();
                batteryInfoBean.setPid(record.getString(0));
                batteryInfoBean.setPid_sn(record.getString(1));
                batteryInfoBean.setP(record.getBigint(2));
                batteryInfoBean.setId(record.getString(3));
                batteryInfoBean.setId_sn(record.getString(4));
                batteryInfoBean.setTime(record.getDatetime(5));
                batteryInfoBean.setBv0(record.getBigint(6));
                batteryInfoBean.setBv1(record.getBigint(7));
                batteryInfoBean.setBv2(record.getBigint(8));
                batteryInfoBean.setBv3(record.getBigint(9));
                batteryInfoBean.setBv4(record.getBigint(10));
                batteryInfoBean.setBv5(record.getBigint(11));
                batteryInfoBean.setBv6(record.getBigint(12));
                batteryInfoBean.setBv7(record.getBigint(13));
                batteryInfoBean.setBv8(record.getBigint(14));
                batteryInfoBean.setBv9(record.getBigint(15));
                batteryInfoBean.setBv10(record.getBigint(16));
                batteryInfoBean.setBv11(record.getBigint(17));
                batteryInfoBean.setBv12(record.getBigint(18));
                batteryInfoBean.setBv13(record.getBigint(19));
                batteryInfoBean.setBv14(record.getBigint(20));
                batteryInfoBean.setBv15(record.getBigint(21));
                batteryInfoBean.setF(record.getBigint(22));
                batteryInfoBean.setF_b2(record.getBigint(23));
                batteryInfoBean.setC(record.getBigint(24));


                arraylist.add(batteryInfoBean);
                System.out.println(arraylist);

            }
            Collections.sort(arraylist);

            //对象数组
            BatteryInfoBean[] array = arraylist.toArray(new BatteryInfoBean[arraylist.size()]);


            //遍历数组，找时间在三十分钟内，且bv=0
            long time1;
            long time2;
            long time3;
            long time4;
            long time5;
            long time6;
            long time7;
            long time8;
            long time9;
            long time10;
            long time11;
            long time12;
            long time13;
            long time14;
            long time15;
            long time16;
            long time17;
            long time18;
            long time19;
            long time20;
            long time21;
            long time22;
            long time23;
            long time24;
            long time25;
            long time26;
            long time27;
            long time28;
            long time29;
            long time30;

            if (array.length / 30 > 1) {
                for (int i = 0; i < array.length; i++) {
                    time1 = dateToTimestamp(array[i].getTime().toString());
                    time2 = dateToTimestamp(array[i + 1].getTime().toString());
                    time3 = dateToTimestamp(array[i + 2].getTime().toString());
                    time4 = dateToTimestamp(array[i + 3].getTime().toString());
                    time5 = dateToTimestamp(array[i + 4].getTime().toString());
                    time6 = dateToTimestamp(array[i + 5].getTime().toString());
                    time7 = dateToTimestamp(array[i + 6].getTime().toString());
                    time8 = dateToTimestamp(array[i + 7].getTime().toString());
                    time9 = dateToTimestamp(array[i + 8].getTime().toString());
                    time10 = dateToTimestamp(array[i + 9].getTime().toString());
                    time11 = dateToTimestamp(array[i + 10].getTime().toString());
                    time12 = dateToTimestamp(array[i + 11].getTime().toString());
                    time13 = dateToTimestamp(array[i + 12].getTime().toString());
                    time14 = dateToTimestamp(array[i + 13].getTime().toString());
                    time15 = dateToTimestamp(array[i + 14].getTime().toString());
                    time16 = dateToTimestamp(array[i + 15].getTime().toString());
                    time17 = dateToTimestamp(array[i + 16].getTime().toString());
                    time18 = dateToTimestamp(array[i + 17].getTime().toString());
                    time19 = dateToTimestamp(array[i + 18].getTime().toString());
                    time20 = dateToTimestamp(array[i + 19].getTime().toString());
                    time21 = dateToTimestamp(array[i + 20].getTime().toString());
                    time22 = dateToTimestamp(array[i + 21].getTime().toString());
                    time23 = dateToTimestamp(array[i + 22].getTime().toString());
                    time24 = dateToTimestamp(array[i + 23].getTime().toString());
                    time25 = dateToTimestamp(array[i + 24].getTime().toString());
                    time26 = dateToTimestamp(array[i + 25].getTime().toString());
                    time27 = dateToTimestamp(array[i + 26].getTime().toString());
                    time28 = dateToTimestamp(array[i + 27].getTime().toString());
                    time29 = dateToTimestamp(array[i + 28].getTime().toString());
                    time30 = dateToTimestamp(array[i + 29].getTime().toString());
                }
            } else if (array.length / 30 > 0 || array.length / 30 < 1) {

                for (int i = 0; i < array.length % 30; i++) {
                    if (array.length % 30 < 1) return;
                    time1 = dateToTimestamp(array[i].getTime().toString());
                    if (array.length < 2) return;
                    time2 = dateToTimestamp(array[i + 1].getTime().toString());
                    if (array.length < 3) return;
                    time3 = dateToTimestamp(array[i + 2].getTime().toString());
                    if (array.length < 4) return;
                    time4 = dateToTimestamp(array[i + 3].getTime().toString());
                    if (array.length < 5) return;
                    time5 = dateToTimestamp(array[i + 4].getTime().toString());
                    if (array.length < 6) return;
                    time6 = dateToTimestamp(array[i + 5].getTime().toString());
                    if (array.length < 7) return;
                    time7 = dateToTimestamp(array[i + 6].getTime().toString());
                    if (array.length < 8) return;
                    time8 = dateToTimestamp(array[i + 7].getTime().toString());
                    if (array.length < 9) return;
                    time9 = dateToTimestamp(array[i + 8].getTime().toString());
                    if (array.length < 10) return;
                    time10 = dateToTimestamp(array[i + 9].getTime().toString());
                    if (array.length < 11) return;
                    time11 = dateToTimestamp(array[i + 10].getTime().toString());
                    if (array.length < 12) return;
                    time12 = dateToTimestamp(array[i + 11].getTime().toString());
                    if (array.length < 13) return;
                    time13 = dateToTimestamp(array[i + 12].getTime().toString());
                    if (array.length < 14) return;
                    time14 = dateToTimestamp(array[i + 13].getTime().toString());
                    if (array.length < 15) return;
                    time15 = dateToTimestamp(array[i + 14].getTime().toString());
                    if (array.length < 16) return;
                    time16 = dateToTimestamp(array[i + 15].getTime().toString());
                    if (array.length < 17) return;
                    time17 = dateToTimestamp(array[i + 16].getTime().toString());
                    if (array.length < 18) return;
                    time18 = dateToTimestamp(array[i + 17].getTime().toString());
                    if (array.length < 19) return;
                    time19 = dateToTimestamp(array[i + 18].getTime().toString());
                    if (array.length < 20) return;
                    time20 = dateToTimestamp(array[i + 19].getTime().toString());
                    if (array.length < 21) return;
                    time21 = dateToTimestamp(array[i + 20].getTime().toString());
                    if (array.length < 22) return;
                    time22 = dateToTimestamp(array[i + 21].getTime().toString());
                    if (array.length < 23) return;
                    time23 = dateToTimestamp(array[i + 22].getTime().toString());
                    if (array.length < 24) return;
                    time24 = dateToTimestamp(array[i + 23].getTime().toString());
                    if (array.length < 25) return;
                    time25 = dateToTimestamp(array[i + 24].getTime().toString());
                    if (array.length < 26) return;
                    time26 = dateToTimestamp(array[i + 25].getTime().toString());
                    if (array.length < 27) return;
                    time27 = dateToTimestamp(array[i + 26].getTime().toString());
                    if (array.length < 28) return;
                    time28 = dateToTimestamp(array[i + 27].getTime().toString());
                    if (array.length < 29) return;
                    time29 = dateToTimestamp(array[i + 28].getTime().toString());
                    if (array.length < 30) return;
                    time30 = dateToTimestamp(array[i + 29].getTime().toString());


                    List list2 = Arrays.asList(array);
                    List<BatteryInfoBean> batteryInfoBeanList = new ArrayList(list2);
                    // && array[i+1].getBv0()==0 && array[i+1].getF_b2()==1 每次增加一个
                    if (time2 - time1 >= 1800) {
                        if (array[i].getC() == 0 && array[i].getF_b2() == 1 && array[i + 1].getC() == 0 && array[i + 1].getF_b2() == 1) {
                            result.setString(0, batteryInfoBeanList.get(i).getPid());
                            result.setString(1, batteryInfoBeanList.get(i).getPid_sn());
                            result.setBigint(2, batteryInfoBeanList.get(i).getP());
                            result.setString(3, batteryInfoBeanList.get(i).getId());
                            result.setString(4, batteryInfoBeanList.get(i).getId_sn());
                            result.setDatetime(5, batteryInfoBeanList.get(i).getTime());
                            result.setBigint(6, batteryInfoBeanList.get(i).getBv0());
                            result.setBigint(7, batteryInfoBeanList.get(i).getBv1());
                            result.setBigint(8, batteryInfoBeanList.get(i).getBv2());
                            result.setBigint(9, batteryInfoBeanList.get(i).getBv3());
                            result.setBigint(10, batteryInfoBeanList.get(i).getBv4());
                            result.setBigint(12, batteryInfoBeanList.get(i).getBv5());
                            result.setBigint(13, batteryInfoBeanList.get(i).getBv6());
                            result.setBigint(14, batteryInfoBeanList.get(i).getBv7());
                            result.setBigint(15, batteryInfoBeanList.get(i).getBv8());
                            result.setBigint(16, batteryInfoBeanList.get(i).getBv9());
                            result.setBigint(17, batteryInfoBeanList.get(i).getBv10());
                            result.setBigint(18, batteryInfoBeanList.get(i).getBv11());
                            result.setBigint(19, batteryInfoBeanList.get(i).getBv12());
                            result.setBigint(20, batteryInfoBeanList.get(i).getBv13());
                            result.setBigint(21, batteryInfoBeanList.get(i).getBv14());
                            result.setBigint(22, batteryInfoBeanList.get(i).getBv15());
                            result.setBigint(23, batteryInfoBeanList.get(i).getF());
                            result.setBigint(24, batteryInfoBeanList.get(i).getF_b2());
                            result.setBigint(25, batteryInfoBeanList.get(i).getC());
                            context.write(result);
                        }
                    }
                    //1.符合时间，2.符合bv0=0,3.判断F_b2=1
                    if (time3 - time1 >= 1800) {
                        if (array[i].getC() == 0 && array[i].getF_b2() == 1 && array[i + 1].getC() == 0 && array[i + 1].getF_b2() == 1 && array[i + 2].getC() == 0 && array[i + 2].getF_b2() == 1) {

                            result.setString(0, batteryInfoBeanList.get(i).getPid());
                            result.setString(1, batteryInfoBeanList.get(i).getPid_sn());
                            result.setBigint(2, batteryInfoBeanList.get(i).getP());
                            result.setString(3, batteryInfoBeanList.get(i).getId());
                            result.setString(4, batteryInfoBeanList.get(i).getId_sn());
                            result.setDatetime(5, batteryInfoBeanList.get(i).getTime());
                            result.setBigint(6, batteryInfoBeanList.get(i).getBv0());
                            result.setBigint(7, batteryInfoBeanList.get(i).getBv1());
                            result.setBigint(8, batteryInfoBeanList.get(i).getBv2());
                            result.setBigint(9, batteryInfoBeanList.get(i).getBv3());
                            result.setBigint(10, batteryInfoBeanList.get(i).getBv4());
                            result.setBigint(12, batteryInfoBeanList.get(i).getBv5());
                            result.setBigint(13, batteryInfoBeanList.get(i).getBv6());
                            result.setBigint(14, batteryInfoBeanList.get(i).getBv7());
                            result.setBigint(15, batteryInfoBeanList.get(i).getBv8());
                            result.setBigint(16, batteryInfoBeanList.get(i).getBv9());
                            result.setBigint(17, batteryInfoBeanList.get(i).getBv10());
                            result.setBigint(18, batteryInfoBeanList.get(i).getBv11());
                            result.setBigint(19, batteryInfoBeanList.get(i).getBv12());
                            result.setBigint(20, batteryInfoBeanList.get(i).getBv13());
                            result.setBigint(21, batteryInfoBeanList.get(i).getBv14());
                            result.setBigint(22, batteryInfoBeanList.get(i).getBv15());
                            result.setBigint(23, batteryInfoBeanList.get(i).getF());
                            result.setBigint(24, batteryInfoBeanList.get(i).getF_b2());
                            result.setBigint(25, batteryInfoBeanList.get(i).getC());
                            context.write(result);
                        }
                    }
                    if (time4 - time1 >= 1800) {
                        if (array[i].getC() == 0 && array[i + 1].getC() == 0 && array[i + 2].getC() == 0 && array[i].getF_b2() == 1 && array[i + 1].getF_b2() == 1 && array[i + 2].getF_b2() == 1 && array[i + 3].getC() == 0 && array[i + 3].getF_b2() == 1) {
                            result.setString(0, batteryInfoBeanList.get(i).getPid());
                            result.setString(1, batteryInfoBeanList.get(i).getPid_sn());
                            result.setBigint(2, batteryInfoBeanList.get(i).getP());
                            result.setString(3, batteryInfoBeanList.get(i).getId());
                            result.setString(4, batteryInfoBeanList.get(i).getId_sn());
                            result.setDatetime(5, batteryInfoBeanList.get(i).getTime());
                            result.setBigint(6, batteryInfoBeanList.get(i).getBv0());
                            result.setBigint(7, batteryInfoBeanList.get(i).getBv1());
                            result.setBigint(8, batteryInfoBeanList.get(i).getBv2());
                            result.setBigint(9, batteryInfoBeanList.get(i).getBv3());
                            result.setBigint(10, batteryInfoBeanList.get(i).getBv4());
                            result.setBigint(12, batteryInfoBeanList.get(i).getBv5());
                            result.setBigint(13, batteryInfoBeanList.get(i).getBv6());
                            result.setBigint(14, batteryInfoBeanList.get(i).getBv7());
                            result.setBigint(15, batteryInfoBeanList.get(i).getBv8());
                            result.setBigint(16, batteryInfoBeanList.get(i).getBv9());
                            result.setBigint(17, batteryInfoBeanList.get(i).getBv10());
                            result.setBigint(18, batteryInfoBeanList.get(i).getBv11());
                            result.setBigint(19, batteryInfoBeanList.get(i).getBv12());
                            result.setBigint(20, batteryInfoBeanList.get(i).getBv13());
                            result.setBigint(21, batteryInfoBeanList.get(i).getBv14());
                            result.setBigint(22, batteryInfoBeanList.get(i).getBv15());
                            result.setBigint(23, batteryInfoBeanList.get(i).getF());
                            result.setBigint(24, batteryInfoBeanList.get(i).getF_b2());
                            result.setBigint(25, batteryInfoBeanList.get(i).getC());
                            context.write(result);
                        }
                    }
                    if (time5 - time1 >= 1800) {
                        if (array[i].getC() == 0 && array[i].getF_b2() == 1 && array[i + 1].getC() == 0 && array[i + 1].getF_b2() == 1 && array[i + 2].getC() == 0 && array[i + 2].getF_b2() == 1 && array[i + 3].getC() == 0 && array[i + 3].getF_b2() == 1 && array[i + 4].getC() == 0 && array[i + 4].getF_b2() == 1) {
                            result.setString(0, batteryInfoBeanList.get(i).getPid());
                            result.setString(1, batteryInfoBeanList.get(i).getPid_sn());
                            result.setBigint(2, batteryInfoBeanList.get(i).getP());
                            result.setString(3, batteryInfoBeanList.get(i).getId());
                            result.setString(4, batteryInfoBeanList.get(i).getId_sn());
                            result.setDatetime(5, batteryInfoBeanList.get(i).getTime());
                            result.setBigint(6, batteryInfoBeanList.get(i).getBv0());
                            result.setBigint(7, batteryInfoBeanList.get(i).getBv1());
                            result.setBigint(8, batteryInfoBeanList.get(i).getBv2());
                            result.setBigint(9, batteryInfoBeanList.get(i).getBv3());
                            result.setBigint(10, batteryInfoBeanList.get(i).getBv4());
                            result.setBigint(12, batteryInfoBeanList.get(i).getBv5());
                            result.setBigint(13, batteryInfoBeanList.get(i).getBv6());
                            result.setBigint(14, batteryInfoBeanList.get(i).getBv7());
                            result.setBigint(15, batteryInfoBeanList.get(i).getBv8());
                            result.setBigint(16, batteryInfoBeanList.get(i).getBv9());
                            result.setBigint(17, batteryInfoBeanList.get(i).getBv10());
                            result.setBigint(18, batteryInfoBeanList.get(i).getBv11());
                            result.setBigint(19, batteryInfoBeanList.get(i).getBv12());
                            result.setBigint(20, batteryInfoBeanList.get(i).getBv13());
                            result.setBigint(21, batteryInfoBeanList.get(i).getBv14());
                            result.setBigint(22, batteryInfoBeanList.get(i).getBv15());
                            result.setBigint(23, batteryInfoBeanList.get(i).getF());
                            result.setBigint(24, batteryInfoBeanList.get(i).getF_b2());
                            result.setBigint(25, batteryInfoBeanList.get(i).getC());
                            context.write(result);
                        }
                    }
                    if (time6 - time1 >= 1800) {
                        if (array[i].getC() == 0 && array[i].getF_b2() == 1 && array[i + 1].getC() == 0 && array[i + 1].getF_b2() == 1 && array[i + 2].getC() == 0 && array[i + 2].getF_b2() == 1 && array[i + 3].getC() == 0 && array[i + 3].getF_b2() == 1 && array[i + 4].getC() == 0 && array[i + 4].getF_b2() == 1 && array[i + 5].getC() == 0 && array[i + 5].getF_b2() == 1) {
                            result.setString(0, batteryInfoBeanList.get(i).getPid());
                            result.setString(1, batteryInfoBeanList.get(i).getPid_sn());
                            result.setBigint(2, batteryInfoBeanList.get(i).getP());
                            result.setString(3, batteryInfoBeanList.get(i).getId());
                            result.setString(4, batteryInfoBeanList.get(i).getId_sn());
                            result.setDatetime(5, batteryInfoBeanList.get(i).getTime());
                            result.setBigint(6, batteryInfoBeanList.get(i).getBv0());
                            result.setBigint(7, batteryInfoBeanList.get(i).getBv1());
                            result.setBigint(8, batteryInfoBeanList.get(i).getBv2());
                            result.setBigint(9, batteryInfoBeanList.get(i).getBv3());
                            result.setBigint(10, batteryInfoBeanList.get(i).getBv4());
                            result.setBigint(12, batteryInfoBeanList.get(i).getBv5());
                            result.setBigint(13, batteryInfoBeanList.get(i).getBv6());
                            result.setBigint(14, batteryInfoBeanList.get(i).getBv7());
                            result.setBigint(15, batteryInfoBeanList.get(i).getBv8());
                            result.setBigint(16, batteryInfoBeanList.get(i).getBv9());
                            result.setBigint(17, batteryInfoBeanList.get(i).getBv10());
                            result.setBigint(18, batteryInfoBeanList.get(i).getBv11());
                            result.setBigint(19, batteryInfoBeanList.get(i).getBv12());
                            result.setBigint(20, batteryInfoBeanList.get(i).getBv13());
                            result.setBigint(21, batteryInfoBeanList.get(i).getBv14());
                            result.setBigint(22, batteryInfoBeanList.get(i).getBv15());
                            result.setBigint(23, batteryInfoBeanList.get(i).getF());
                            result.setBigint(24, batteryInfoBeanList.get(i).getF_b2());
                            result.setBigint(25, batteryInfoBeanList.get(i).getC());
                            context.write(result);
                        }
                    }
                    if (time7 - time1 >= 1800) {
                        if (array[i].getC() == 0 && array[i].getF_b2() == 1 && array[i + 1].getC() == 0 && array[i + 1].getF_b2() == 1 && array[i + 2].getC() == 0 && array[i + 2].getF_b2() == 1 &&
                                array[i + 3].getC() == 0 && array[i + 3].getF_b2() == 1 && array[i + 4].getC() == 0 && array[i + 4].getF_b2() == 1 && array[i + 5].getC() == 0 && array[i + 5].getF_b2() == 1 &&
                                array[i + 6].getC() == 0 && array[i + 6].getF_b2() == 1) {
                            result.setString(0, batteryInfoBeanList.get(i).getPid());
                            result.setString(1, batteryInfoBeanList.get(i).getPid_sn());
                            result.setBigint(2, batteryInfoBeanList.get(i).getP());
                            result.setString(3, batteryInfoBeanList.get(i).getId());
                            result.setString(4, batteryInfoBeanList.get(i).getId_sn());
                            result.setDatetime(5, batteryInfoBeanList.get(i).getTime());
                            result.setBigint(6, batteryInfoBeanList.get(i).getBv0());
                            result.setBigint(7, batteryInfoBeanList.get(i).getBv1());
                            result.setBigint(8, batteryInfoBeanList.get(i).getBv2());
                            result.setBigint(9, batteryInfoBeanList.get(i).getBv3());
                            result.setBigint(10, batteryInfoBeanList.get(i).getBv4());
                            result.setBigint(12, batteryInfoBeanList.get(i).getBv5());
                            result.setBigint(13, batteryInfoBeanList.get(i).getBv6());
                            result.setBigint(14, batteryInfoBeanList.get(i).getBv7());
                            result.setBigint(15, batteryInfoBeanList.get(i).getBv8());
                            result.setBigint(16, batteryInfoBeanList.get(i).getBv9());
                            result.setBigint(17, batteryInfoBeanList.get(i).getBv10());
                            result.setBigint(18, batteryInfoBeanList.get(i).getBv11());
                            result.setBigint(19, batteryInfoBeanList.get(i).getBv12());
                            result.setBigint(20, batteryInfoBeanList.get(i).getBv13());
                            result.setBigint(21, batteryInfoBeanList.get(i).getBv14());
                            result.setBigint(22, batteryInfoBeanList.get(i).getBv15());
                            result.setBigint(23, batteryInfoBeanList.get(i).getF());
                            result.setBigint(24, batteryInfoBeanList.get(i).getF_b2());
                            result.setBigint(25, batteryInfoBeanList.get(i).getC());
                            context.write(result);
                        }
                    }
                    if (time8 - time1 >= 1800) {
                        if (array[i].getC() == 0 && array[i].getF_b2() == 1 && array[i + 1].getC() == 0 && array[i + 1].getF_b2() == 1 && array[i + 2].getC() == 0 && array[i + 2].getF_b2() == 1 &&
                                array[i + 3].getC() == 0 && array[i + 3].getF_b2() == 1 && array[i + 4].getC() == 0 && array[i + 4].getF_b2() == 1 && array[i + 5].getC() == 0 && array[i + 5].getF_b2() == 1 &&
                                array[i + 6].getC() == 0 && array[i + 6].getF_b2() == 1 && array[i + 7].getC() == 0 && array[i + 7].getF_b2() == 1) {
                            result.setString(0, batteryInfoBeanList.get(i).getPid());
                            result.setString(1, batteryInfoBeanList.get(i).getPid_sn());
                            result.setBigint(2, batteryInfoBeanList.get(i).getP());
                            result.setString(3, batteryInfoBeanList.get(i).getId());
                            result.setString(4, batteryInfoBeanList.get(i).getId_sn());
                            result.setDatetime(5, batteryInfoBeanList.get(i).getTime());
                            result.setBigint(6, batteryInfoBeanList.get(i).getBv0());
                            result.setBigint(7, batteryInfoBeanList.get(i).getBv1());
                            result.setBigint(8, batteryInfoBeanList.get(i).getBv2());
                            result.setBigint(9, batteryInfoBeanList.get(i).getBv3());
                            result.setBigint(10, batteryInfoBeanList.get(i).getBv4());
                            result.setBigint(12, batteryInfoBeanList.get(i).getBv5());
                            result.setBigint(13, batteryInfoBeanList.get(i).getBv6());
                            result.setBigint(14, batteryInfoBeanList.get(i).getBv7());
                            result.setBigint(15, batteryInfoBeanList.get(i).getBv8());
                            result.setBigint(16, batteryInfoBeanList.get(i).getBv9());
                            result.setBigint(17, batteryInfoBeanList.get(i).getBv10());
                            result.setBigint(18, batteryInfoBeanList.get(i).getBv11());
                            result.setBigint(19, batteryInfoBeanList.get(i).getBv12());
                            result.setBigint(20, batteryInfoBeanList.get(i).getBv13());
                            result.setBigint(21, batteryInfoBeanList.get(i).getBv14());
                            result.setBigint(22, batteryInfoBeanList.get(i).getBv15());
                            result.setBigint(23, batteryInfoBeanList.get(i).getF());
                            result.setBigint(24, batteryInfoBeanList.get(i).getF_b2());
                            result.setBigint(25, batteryInfoBeanList.get(i).getC());
                            context.write(result);
                        }
                    }
                    if (time9 - time1 >= 1800) {
                        if (array[i].getC() == 0 && array[i].getF_b2() == 1 && array[i + 1].getC() == 0 && array[i + 1].getF_b2() == 1 && array[i + 2].getC() == 0 && array[i + 2].getF_b2() == 1 &&
                                array[i + 3].getC() == 0 && array[i + 3].getF_b2() == 1 && array[i + 4].getC() == 0 && array[i + 4].getF_b2() == 1 && array[i + 5].getC() == 0 && array[i + 5].getF_b2() == 1 &&
                                array[i + 6].getC() == 0 && array[i + 6].getF_b2() == 1 && array[i + 7].getC() == 0 && array[i + 7].getF_b2() == 1 && array[i + 8].getC() == 0 && array[i + 8].getF_b2() == 1) {
                            result.setString(0, batteryInfoBeanList.get(i).getPid());
                            result.setString(1, batteryInfoBeanList.get(i).getPid_sn());
                            result.setBigint(2, batteryInfoBeanList.get(i).getP());
                            result.setString(3, batteryInfoBeanList.get(i).getId());
                            result.setString(4, batteryInfoBeanList.get(i).getId_sn());
                            result.setDatetime(5, batteryInfoBeanList.get(i).getTime());
                            result.setBigint(6, batteryInfoBeanList.get(i).getBv0());
                            result.setBigint(7, batteryInfoBeanList.get(i).getBv1());
                            result.setBigint(8, batteryInfoBeanList.get(i).getBv2());
                            result.setBigint(9, batteryInfoBeanList.get(i).getBv3());
                            result.setBigint(10, batteryInfoBeanList.get(i).getBv4());
                            result.setBigint(12, batteryInfoBeanList.get(i).getBv5());
                            result.setBigint(13, batteryInfoBeanList.get(i).getBv6());
                            result.setBigint(14, batteryInfoBeanList.get(i).getBv7());
                            result.setBigint(15, batteryInfoBeanList.get(i).getBv8());
                            result.setBigint(16, batteryInfoBeanList.get(i).getBv9());
                            result.setBigint(17, batteryInfoBeanList.get(i).getBv10());
                            result.setBigint(18, batteryInfoBeanList.get(i).getBv11());
                            result.setBigint(19, batteryInfoBeanList.get(i).getBv12());
                            result.setBigint(20, batteryInfoBeanList.get(i).getBv13());
                            result.setBigint(21, batteryInfoBeanList.get(i).getBv14());
                            result.setBigint(22, batteryInfoBeanList.get(i).getBv15());
                            result.setBigint(23, batteryInfoBeanList.get(i).getF());
                            result.setBigint(24, batteryInfoBeanList.get(i).getF_b2());
                            result.setBigint(25, batteryInfoBeanList.get(i).getC());
                            context.write(result);
                        }
                    }
                    if (time10 - time1 >= 1800) {
                        if (array[i].getC() == 0 && array[i].getF_b2() == 1 && array[i + 1].getC() == 0 && array[i + 1].getF_b2() == 1 && array[i + 2].getC() == 0 && array[i + 2].getF_b2() == 1 &&
                                array[i + 3].getC() == 0 && array[i + 3].getF_b2() == 1 && array[i + 4].getC() == 0 && array[i + 4].getF_b2() == 1 && array[i + 5].getC() == 0 && array[i + 5].getF_b2() == 1 &&
                                array[i + 6].getC() == 0 && array[i + 6].getF_b2() == 1 && array[i + 7].getC() == 0 && array[i + 7].getF_b2() == 1 && array[i + 8].getC() == 0 && array[i + 8].getF_b2() == 1 &&
                                array[i + 9].getC() == 0 && array[i + 9].getF_b2() == 1) {
                            result.setString(0, batteryInfoBeanList.get(i).getPid());
                            result.setString(1, batteryInfoBeanList.get(i).getPid_sn());
                            result.setBigint(2, batteryInfoBeanList.get(i).getP());
                            result.setString(3, batteryInfoBeanList.get(i).getId());
                            result.setString(4, batteryInfoBeanList.get(i).getId_sn());
                            result.setDatetime(5, batteryInfoBeanList.get(i).getTime());
                            result.setBigint(6, batteryInfoBeanList.get(i).getBv0());
                            result.setBigint(7, batteryInfoBeanList.get(i).getBv1());
                            result.setBigint(8, batteryInfoBeanList.get(i).getBv2());
                            result.setBigint(9, batteryInfoBeanList.get(i).getBv3());
                            result.setBigint(10, batteryInfoBeanList.get(i).getBv4());
                            result.setBigint(12, batteryInfoBeanList.get(i).getBv5());
                            result.setBigint(13, batteryInfoBeanList.get(i).getBv6());
                            result.setBigint(14, batteryInfoBeanList.get(i).getBv7());
                            result.setBigint(15, batteryInfoBeanList.get(i).getBv8());
                            result.setBigint(16, batteryInfoBeanList.get(i).getBv9());
                            result.setBigint(17, batteryInfoBeanList.get(i).getBv10());
                            result.setBigint(18, batteryInfoBeanList.get(i).getBv11());
                            result.setBigint(19, batteryInfoBeanList.get(i).getBv12());
                            result.setBigint(20, batteryInfoBeanList.get(i).getBv13());
                            result.setBigint(21, batteryInfoBeanList.get(i).getBv14());
                            result.setBigint(22, batteryInfoBeanList.get(i).getBv15());
                            result.setBigint(23, batteryInfoBeanList.get(i).getF());
                            result.setBigint(24, batteryInfoBeanList.get(i).getF_b2());
                            result.setBigint(25, batteryInfoBeanList.get(i).getC());
                            context.write(result);
                        }
                    }
                    if (time11 - time1 >= 1800) {
                        if (array[i].getC() == 0 && array[i].getF_b2() == 1 && array[i + 1].getC() == 0 && array[i + 1].getF_b2() == 1 && array[i + 2].getC() == 0 && array[i + 2].getF_b2() == 1 &&
                                array[i + 3].getC() == 0 && array[i + 3].getF_b2() == 1 && array[i + 4].getC() == 0 && array[i + 4].getF_b2() == 1 && array[i + 5].getC() == 0 && array[i + 5].getF_b2() == 1 &&
                                array[i + 6].getC() == 0 && array[i + 6].getF_b2() == 1 && array[i + 7].getC() == 0 && array[i + 7].getF_b2() == 1 && array[i + 8].getC() == 0 && array[i + 8].getF_b2() == 1 &&
                                array[i + 9].getC() == 0 && array[i + 9].getF_b2() == 1 && array[i + 10].getC() == 0 && array[i + 10].getF_b2() == 1) {
                            result.setString(0, batteryInfoBeanList.get(i).getPid());
                            result.setString(1, batteryInfoBeanList.get(i).getPid_sn());
                            result.setBigint(2, batteryInfoBeanList.get(i).getP());
                            result.setString(3, batteryInfoBeanList.get(i).getId());
                            result.setString(4, batteryInfoBeanList.get(i).getId_sn());
                            result.setDatetime(5, batteryInfoBeanList.get(i).getTime());
                            result.setBigint(6, batteryInfoBeanList.get(i).getBv0());
                            result.setBigint(7, batteryInfoBeanList.get(i).getBv1());
                            result.setBigint(8, batteryInfoBeanList.get(i).getBv2());
                            result.setBigint(9, batteryInfoBeanList.get(i).getBv3());
                            result.setBigint(10, batteryInfoBeanList.get(i).getBv4());
                            result.setBigint(12, batteryInfoBeanList.get(i).getBv5());
                            result.setBigint(13, batteryInfoBeanList.get(i).getBv6());
                            result.setBigint(14, batteryInfoBeanList.get(i).getBv7());
                            result.setBigint(15, batteryInfoBeanList.get(i).getBv8());
                            result.setBigint(16, batteryInfoBeanList.get(i).getBv9());
                            result.setBigint(17, batteryInfoBeanList.get(i).getBv10());
                            result.setBigint(18, batteryInfoBeanList.get(i).getBv11());
                            result.setBigint(19, batteryInfoBeanList.get(i).getBv12());
                            result.setBigint(20, batteryInfoBeanList.get(i).getBv13());
                            result.setBigint(21, batteryInfoBeanList.get(i).getBv14());
                            result.setBigint(22, batteryInfoBeanList.get(i).getBv15());
                            result.setBigint(23, batteryInfoBeanList.get(i).getF());
                            result.setBigint(24, batteryInfoBeanList.get(i).getF_b2());
                            result.setBigint(25, batteryInfoBeanList.get(i).getC());
                            context.write(result);
                        }
                    }
                    if (time12 - time1 >= 1800) {
                        if (array[i].getC() == 0 && array[i].getF_b2() == 1 && array[i + 1].getC() == 0 && array[i + 1].getF_b2() == 1 && array[i + 2].getC() == 0 && array[i + 2].getF_b2() == 1 &&
                                array[i + 3].getC() == 0 && array[i + 3].getF_b2() == 1 && array[i + 4].getC() == 0 && array[i + 4].getF_b2() == 1 && array[i + 5].getC() == 0 && array[i + 5].getF_b2() == 1 &&
                                array[i + 6].getC() == 0 && array[i + 6].getF_b2() == 1 && array[i + 7].getC() == 0 && array[i + 7].getF_b2() == 1 && array[i + 8].getC() == 0 && array[i + 8].getF_b2() == 1 &&
                                array[i + 9].getC() == 0 && array[i + 9].getF_b2() == 1 && array[i + 10].getC() == 0 && array[i + 10].getF_b2() == 1 && array[i + 11].getC() == 0 && array[i + 11].getF_b2() == 1) {
                            result.setString(0, batteryInfoBeanList.get(i).getPid());
                            result.setString(1, batteryInfoBeanList.get(i).getPid_sn());
                            result.setBigint(2, batteryInfoBeanList.get(i).getP());
                            result.setString(3, batteryInfoBeanList.get(i).getId());
                            result.setString(4, batteryInfoBeanList.get(i).getId_sn());
                            result.setDatetime(5, batteryInfoBeanList.get(i).getTime());
                            result.setBigint(6, batteryInfoBeanList.get(i).getBv0());
                            result.setBigint(7, batteryInfoBeanList.get(i).getBv1());
                            result.setBigint(8, batteryInfoBeanList.get(i).getBv2());
                            result.setBigint(9, batteryInfoBeanList.get(i).getBv3());
                            result.setBigint(10, batteryInfoBeanList.get(i).getBv4());
                            result.setBigint(12, batteryInfoBeanList.get(i).getBv5());
                            result.setBigint(13, batteryInfoBeanList.get(i).getBv6());
                            result.setBigint(14, batteryInfoBeanList.get(i).getBv7());
                            result.setBigint(15, batteryInfoBeanList.get(i).getBv8());
                            result.setBigint(16, batteryInfoBeanList.get(i).getBv9());
                            result.setBigint(17, batteryInfoBeanList.get(i).getBv10());
                            result.setBigint(18, batteryInfoBeanList.get(i).getBv11());
                            result.setBigint(19, batteryInfoBeanList.get(i).getBv12());
                            result.setBigint(20, batteryInfoBeanList.get(i).getBv13());
                            result.setBigint(21, batteryInfoBeanList.get(i).getBv14());
                            result.setBigint(22, batteryInfoBeanList.get(i).getBv15());
                            result.setBigint(23, batteryInfoBeanList.get(i).getF());
                            result.setBigint(24, batteryInfoBeanList.get(i).getF_b2());
                            result.setBigint(25, batteryInfoBeanList.get(i).getC());
                            context.write(result);
                        }
                    }
                    if (time13 - time1 >= 1800) {
                        if (array[i].getC() == 0 && array[i].getF_b2() == 1 && array[i + 1].getC() == 0 && array[i + 1].getF_b2() == 1 && array[i + 2].getC() == 0 && array[i + 2].getF_b2() == 1 &&
                                array[i + 3].getC() == 0 && array[i + 3].getF_b2() == 1 && array[i + 4].getC() == 0 && array[i + 4].getF_b2() == 1 && array[i + 5].getC() == 0 && array[i + 5].getF_b2() == 1 &&
                                array[i + 6].getC() == 0 && array[i + 6].getF_b2() == 1 && array[i + 7].getC() == 0 && array[i + 7].getF_b2() == 1 && array[i + 8].getC() == 0 && array[i + 8].getF_b2() == 1 &&
                                array[i + 9].getC() == 0 && array[i + 9].getF_b2() == 1 && array[i + 10].getC() == 0 && array[i + 10].getF_b2() == 1 && array[i + 11].getC() == 0 && array[i + 11].getF_b2() == 1 &&
                                array[i + 12].getC() == 0 && array[i + 12].getF_b2() == 1) {
                            result.setString(0, batteryInfoBeanList.get(i).getPid());
                            result.setString(1, batteryInfoBeanList.get(i).getPid_sn());
                            result.setBigint(2, batteryInfoBeanList.get(i).getP());
                            result.setString(3, batteryInfoBeanList.get(i).getId());
                            result.setString(4, batteryInfoBeanList.get(i).getId_sn());
                            result.setDatetime(5, batteryInfoBeanList.get(i).getTime());
                            result.setBigint(6, batteryInfoBeanList.get(i).getBv0());
                            result.setBigint(7, batteryInfoBeanList.get(i).getBv1());
                            result.setBigint(8, batteryInfoBeanList.get(i).getBv2());
                            result.setBigint(9, batteryInfoBeanList.get(i).getBv3());
                            result.setBigint(10, batteryInfoBeanList.get(i).getBv4());
                            result.setBigint(12, batteryInfoBeanList.get(i).getBv5());
                            result.setBigint(13, batteryInfoBeanList.get(i).getBv6());
                            result.setBigint(14, batteryInfoBeanList.get(i).getBv7());
                            result.setBigint(15, batteryInfoBeanList.get(i).getBv8());
                            result.setBigint(16, batteryInfoBeanList.get(i).getBv9());
                            result.setBigint(17, batteryInfoBeanList.get(i).getBv10());
                            result.setBigint(18, batteryInfoBeanList.get(i).getBv11());
                            result.setBigint(19, batteryInfoBeanList.get(i).getBv12());
                            result.setBigint(20, batteryInfoBeanList.get(i).getBv13());
                            result.setBigint(21, batteryInfoBeanList.get(i).getBv14());
                            result.setBigint(22, batteryInfoBeanList.get(i).getBv15());
                            result.setBigint(23, batteryInfoBeanList.get(i).getF());
                            result.setBigint(24, batteryInfoBeanList.get(i).getF_b2());
                            result.setBigint(25, batteryInfoBeanList.get(i).getC());
                            context.write(result);
                        }

                    }
                    if (time14 - time1 >= 1800) {
                        if (array[i].getC() == 0 && array[i].getF_b2() == 1 && array[i + 1].getC() == 0 && array[i + 1].getF_b2() == 1 && array[i + 2].getC() == 0 && array[i + 2].getF_b2() == 1 &&
                                array[i + 3].getC() == 0 && array[i + 3].getF_b2() == 1 && array[i + 4].getC() == 0 && array[i + 4].getF_b2() == 1 && array[i + 5].getC() == 0 && array[i + 5].getF_b2() == 1 &&
                                array[i + 6].getC() == 0 && array[i + 6].getF_b2() == 1 && array[i + 7].getC() == 0 && array[i + 7].getF_b2() == 1 && array[i + 8].getC() == 0 && array[i + 8].getF_b2() == 1 &&
                                array[i + 9].getC() == 0 && array[i + 9].getF_b2() == 1 && array[i + 10].getC() == 0 && array[i + 10].getF_b2() == 1 && array[i + 11].getC() == 0 && array[i + 11].getF_b2() == 1 &&
                                array[i + 12].getC() == 0 && array[i + 12].getF_b2() == 1 && array[i + 13].getC() == 0 && array[i + 13].getF_b2() == 1) {
                            result.setString(0, batteryInfoBeanList.get(i).getPid());
                            result.setString(1, batteryInfoBeanList.get(i).getPid_sn());
                            result.setBigint(2, batteryInfoBeanList.get(i).getP());
                            result.setString(3, batteryInfoBeanList.get(i).getId());
                            result.setString(4, batteryInfoBeanList.get(i).getId_sn());
                            result.setDatetime(5, batteryInfoBeanList.get(i).getTime());
                            result.setBigint(6, batteryInfoBeanList.get(i).getBv0());
                            result.setBigint(7, batteryInfoBeanList.get(i).getBv1());
                            result.setBigint(8, batteryInfoBeanList.get(i).getBv2());
                            result.setBigint(9, batteryInfoBeanList.get(i).getBv3());
                            result.setBigint(10, batteryInfoBeanList.get(i).getBv4());
                            result.setBigint(12, batteryInfoBeanList.get(i).getBv5());
                            result.setBigint(13, batteryInfoBeanList.get(i).getBv6());
                            result.setBigint(14, batteryInfoBeanList.get(i).getBv7());
                            result.setBigint(15, batteryInfoBeanList.get(i).getBv8());
                            result.setBigint(16, batteryInfoBeanList.get(i).getBv9());
                            result.setBigint(17, batteryInfoBeanList.get(i).getBv10());
                            result.setBigint(18, batteryInfoBeanList.get(i).getBv11());
                            result.setBigint(19, batteryInfoBeanList.get(i).getBv12());
                            result.setBigint(20, batteryInfoBeanList.get(i).getBv13());
                            result.setBigint(21, batteryInfoBeanList.get(i).getBv14());
                            result.setBigint(22, batteryInfoBeanList.get(i).getBv15());
                            result.setBigint(23, batteryInfoBeanList.get(i).getF());
                            result.setBigint(24, batteryInfoBeanList.get(i).getF_b2());
                            result.setBigint(25, batteryInfoBeanList.get(i).getC());
                            context.write(result);
                        }
                    }
                    if (time15 - time1 >= 1800) {
                        if (array[i].getC() == 0 && array[i].getF_b2() == 1 && array[i + 1].getC() == 0 && array[i + 1].getF_b2() == 1 && array[i + 2].getC() == 0 && array[i + 2].getF_b2() == 1 &&
                                array[i + 3].getC() == 0 && array[i + 3].getF_b2() == 1 && array[i + 4].getC() == 0 && array[i + 4].getF_b2() == 1 && array[i + 5].getC() == 0 && array[i + 5].getF_b2() == 1 &&
                                array[i + 6].getC() == 0 && array[i + 6].getF_b2() == 1 && array[i + 7].getC() == 0 && array[i + 7].getF_b2() == 1 && array[i + 8].getC() == 0 && array[i + 8].getF_b2() == 1 &&
                                array[i + 9].getC() == 0 && array[i + 9].getF_b2() == 1 && array[i + 10].getC() == 0 && array[i + 10].getF_b2() == 1 && array[i + 11].getC() == 0 && array[i + 11].getF_b2() == 1 &&
                                array[i + 12].getC() == 0 && array[i + 12].getF_b2() == 1 && array[i + 13].getC() == 0 && array[i + 13].getF_b2() == 1 && array[i + 14].getC() == 0 && array[i + 14].getF_b2() == 1) {
                            result.setString(0, batteryInfoBeanList.get(i).getPid());
                            result.setString(1, batteryInfoBeanList.get(i).getPid_sn());
                            result.setBigint(2, batteryInfoBeanList.get(i).getP());
                            result.setString(3, batteryInfoBeanList.get(i).getId());
                            result.setString(4, batteryInfoBeanList.get(i).getId_sn());
                            result.setDatetime(5, batteryInfoBeanList.get(i).getTime());
                            result.setBigint(6, batteryInfoBeanList.get(i).getBv0());
                            result.setBigint(7, batteryInfoBeanList.get(i).getBv1());
                            result.setBigint(8, batteryInfoBeanList.get(i).getBv2());
                            result.setBigint(9, batteryInfoBeanList.get(i).getBv3());
                            result.setBigint(10, batteryInfoBeanList.get(i).getBv4());
                            result.setBigint(12, batteryInfoBeanList.get(i).getBv5());
                            result.setBigint(13, batteryInfoBeanList.get(i).getBv6());
                            result.setBigint(14, batteryInfoBeanList.get(i).getBv7());
                            result.setBigint(15, batteryInfoBeanList.get(i).getBv8());
                            result.setBigint(16, batteryInfoBeanList.get(i).getBv9());
                            result.setBigint(17, batteryInfoBeanList.get(i).getBv10());
                            result.setBigint(18, batteryInfoBeanList.get(i).getBv11());
                            result.setBigint(19, batteryInfoBeanList.get(i).getBv12());
                            result.setBigint(20, batteryInfoBeanList.get(i).getBv13());
                            result.setBigint(21, batteryInfoBeanList.get(i).getBv14());
                            result.setBigint(22, batteryInfoBeanList.get(i).getBv15());
                            result.setBigint(23, batteryInfoBeanList.get(i).getF());
                            result.setBigint(24, batteryInfoBeanList.get(i).getF_b2());
                            result.setBigint(25, batteryInfoBeanList.get(i).getC());
                            context.write(result);
                        }
                    }
                    if (time16 - time1 >= 1800) {
                        if (array[i].getC() == 0 && array[i].getF_b2() == 1 && array[i + 1].getC() == 0 && array[i + 1].getF_b2() == 1 && array[i + 2].getC() == 0 && array[i + 2].getF_b2() == 1 &&
                                array[i + 3].getC() == 0 && array[i + 3].getF_b2() == 1 && array[i + 4].getC() == 0 && array[i + 4].getF_b2() == 1 && array[i + 5].getC() == 0 && array[i + 5].getF_b2() == 1 &&
                                array[i + 6].getC() == 0 && array[i + 6].getF_b2() == 1 && array[i + 7].getC() == 0 && array[i + 7].getF_b2() == 1 && array[i + 8].getC() == 0 && array[i + 8].getF_b2() == 1 &&
                                array[i + 9].getC() == 0 && array[i + 9].getF_b2() == 1 && array[i + 10].getC() == 0 && array[i + 10].getF_b2() == 1 && array[i + 11].getC() == 0 && array[i + 11].getF_b2() == 1 &&
                                array[i + 12].getC() == 0 && array[i + 12].getF_b2() == 1 && array[i + 13].getC() == 0 && array[i + 13].getF_b2() == 1 && array[i + 14].getC() == 0 && array[i + 14].getF_b2() == 1 &&
                                array[i + 15].getC() == 0 && array[i + 15].getF_b2() == 1) {
                            result.setString(0, batteryInfoBeanList.get(i).getPid());
                            result.setString(1, batteryInfoBeanList.get(i).getPid_sn());
                            result.setBigint(2, batteryInfoBeanList.get(i).getP());
                            result.setString(3, batteryInfoBeanList.get(i).getId());
                            result.setString(4, batteryInfoBeanList.get(i).getId_sn());
                            result.setDatetime(5, batteryInfoBeanList.get(i).getTime());
                            result.setBigint(6, batteryInfoBeanList.get(i).getBv0());
                            result.setBigint(7, batteryInfoBeanList.get(i).getBv1());
                            result.setBigint(8, batteryInfoBeanList.get(i).getBv2());
                            result.setBigint(9, batteryInfoBeanList.get(i).getBv3());
                            result.setBigint(10, batteryInfoBeanList.get(i).getBv4());
                            result.setBigint(12, batteryInfoBeanList.get(i).getBv5());
                            result.setBigint(13, batteryInfoBeanList.get(i).getBv6());
                            result.setBigint(14, batteryInfoBeanList.get(i).getBv7());
                            result.setBigint(15, batteryInfoBeanList.get(i).getBv8());
                            result.setBigint(16, batteryInfoBeanList.get(i).getBv9());
                            result.setBigint(17, batteryInfoBeanList.get(i).getBv10());
                            result.setBigint(18, batteryInfoBeanList.get(i).getBv11());
                            result.setBigint(19, batteryInfoBeanList.get(i).getBv12());
                            result.setBigint(20, batteryInfoBeanList.get(i).getBv13());
                            result.setBigint(21, batteryInfoBeanList.get(i).getBv14());
                            result.setBigint(22, batteryInfoBeanList.get(i).getBv15());
                            result.setBigint(23, batteryInfoBeanList.get(i).getF());
                            result.setBigint(24, batteryInfoBeanList.get(i).getF_b2());
                            result.setBigint(25, batteryInfoBeanList.get(i).getC());
                            context.write(result);
                        }
                    }
                    if (time17 - time1 >= 1800) {
                        if (array[i].getC() == 0 && array[i].getF_b2() == 1 && array[i + 1].getC() == 0 && array[i + 1].getF_b2() == 1 && array[i + 2].getC() == 0 && array[i + 2].getF_b2() == 1 &&
                                array[i + 3].getC() == 0 && array[i + 3].getF_b2() == 1 && array[i + 4].getC() == 0 && array[i + 4].getF_b2() == 1 && array[i + 5].getC() == 0 && array[i + 5].getF_b2() == 1 &&
                                array[i + 6].getC() == 0 && array[i + 6].getF_b2() == 1 && array[i + 7].getC() == 0 && array[i + 7].getF_b2() == 1 && array[i + 8].getC() == 0 && array[i + 8].getF_b2() == 1 &&
                                array[i + 9].getC() == 0 && array[i + 9].getF_b2() == 1 && array[i + 10].getC() == 0 && array[i + 10].getF_b2() == 1 && array[i + 11].getC() == 0 && array[i + 11].getF_b2() == 1 &&
                                array[i + 12].getC() == 0 && array[i + 12].getF_b2() == 1 && array[i + 13].getC() == 0 && array[i + 13].getF_b2() == 1 && array[i + 14].getC() == 0 && array[i + 14].getF_b2() == 1 &&
                                array[i + 15].getC() == 0 && array[i + 15].getF_b2() == 1 && array[i + 16].getC() == 0 && array[i + 16].getF_b2() == 1) {
                            result.setString(0, batteryInfoBeanList.get(i).getPid());
                            result.setString(1, batteryInfoBeanList.get(i).getPid_sn());
                            result.setBigint(2, batteryInfoBeanList.get(i).getP());
                            result.setString(3, batteryInfoBeanList.get(i).getId());
                            result.setString(4, batteryInfoBeanList.get(i).getId_sn());
                            result.setDatetime(5, batteryInfoBeanList.get(i).getTime());
                            result.setBigint(6, batteryInfoBeanList.get(i).getBv0());
                            result.setBigint(7, batteryInfoBeanList.get(i).getBv1());
                            result.setBigint(8, batteryInfoBeanList.get(i).getBv2());
                            result.setBigint(9, batteryInfoBeanList.get(i).getBv3());
                            result.setBigint(10, batteryInfoBeanList.get(i).getBv4());
                            result.setBigint(12, batteryInfoBeanList.get(i).getBv5());
                            result.setBigint(13, batteryInfoBeanList.get(i).getBv6());
                            result.setBigint(14, batteryInfoBeanList.get(i).getBv7());
                            result.setBigint(15, batteryInfoBeanList.get(i).getBv8());
                            result.setBigint(16, batteryInfoBeanList.get(i).getBv9());
                            result.setBigint(17, batteryInfoBeanList.get(i).getBv10());
                            result.setBigint(18, batteryInfoBeanList.get(i).getBv11());
                            result.setBigint(19, batteryInfoBeanList.get(i).getBv12());
                            result.setBigint(20, batteryInfoBeanList.get(i).getBv13());
                            result.setBigint(21, batteryInfoBeanList.get(i).getBv14());
                            result.setBigint(22, batteryInfoBeanList.get(i).getBv15());
                            result.setBigint(23, batteryInfoBeanList.get(i).getF());
                            result.setBigint(24, batteryInfoBeanList.get(i).getF_b2());
                            result.setBigint(25, batteryInfoBeanList.get(i).getC());
                            context.write(result);
                        }
                    }
                    if (time18 - time1 >= 1800) {
                        if (array[i].getC() == 0 && array[i].getF_b2() == 1 && array[i + 1].getC() == 0 && array[i + 1].getF_b2() == 1 && array[i + 2].getC() == 0 && array[i + 2].getF_b2() == 1 &&
                                array[i + 3].getC() == 0 && array[i + 3].getF_b2() == 1 && array[i + 4].getC() == 0 && array[i + 4].getF_b2() == 1 && array[i + 5].getC() == 0 && array[i + 5].getF_b2() == 1 &&
                                array[i + 6].getC() == 0 && array[i + 6].getF_b2() == 1 && array[i + 7].getC() == 0 && array[i + 7].getF_b2() == 1 && array[i + 8].getC() == 0 && array[i + 8].getF_b2() == 1 &&
                                array[i + 9].getC() == 0 && array[i + 9].getF_b2() == 1 && array[i + 10].getC() == 0 && array[i + 10].getF_b2() == 1 && array[i + 11].getC() == 0 && array[i + 11].getF_b2() == 1 &&
                                array[i + 12].getC() == 0 && array[i + 12].getF_b2() == 1 && array[i + 13].getC() == 0 && array[i + 13].getF_b2() == 1 && array[i + 14].getC() == 0 && array[i + 14].getF_b2() == 1 &&
                                array[i + 15].getC() == 0 && array[i + 15].getF_b2() == 1 && array[i + 16].getC() == 0 && array[i + 16].getF_b2() == 1 && array[i + 17].getC() == 0 && array[i + 17].getF_b2() == 1) {
                            result.setString(0, batteryInfoBeanList.get(i).getPid());
                            result.setString(1, batteryInfoBeanList.get(i).getPid_sn());
                            result.setBigint(2, batteryInfoBeanList.get(i).getP());
                            result.setString(3, batteryInfoBeanList.get(i).getId());
                            result.setString(4, batteryInfoBeanList.get(i).getId_sn());
                            result.setDatetime(5, batteryInfoBeanList.get(i).getTime());
                            result.setBigint(6, batteryInfoBeanList.get(i).getBv0());
                            result.setBigint(7, batteryInfoBeanList.get(i).getBv1());
                            result.setBigint(8, batteryInfoBeanList.get(i).getBv2());
                            result.setBigint(9, batteryInfoBeanList.get(i).getBv3());
                            result.setBigint(10, batteryInfoBeanList.get(i).getBv4());
                            result.setBigint(12, batteryInfoBeanList.get(i).getBv5());
                            result.setBigint(13, batteryInfoBeanList.get(i).getBv6());
                            result.setBigint(14, batteryInfoBeanList.get(i).getBv7());
                            result.setBigint(15, batteryInfoBeanList.get(i).getBv8());
                            result.setBigint(16, batteryInfoBeanList.get(i).getBv9());
                            result.setBigint(17, batteryInfoBeanList.get(i).getBv10());
                            result.setBigint(18, batteryInfoBeanList.get(i).getBv11());
                            result.setBigint(19, batteryInfoBeanList.get(i).getBv12());
                            result.setBigint(20, batteryInfoBeanList.get(i).getBv13());
                            result.setBigint(21, batteryInfoBeanList.get(i).getBv14());
                            result.setBigint(22, batteryInfoBeanList.get(i).getBv15());
                            result.setBigint(23, batteryInfoBeanList.get(i).getF());
                            result.setBigint(24, batteryInfoBeanList.get(i).getF_b2());
                            result.setBigint(25, batteryInfoBeanList.get(i).getC());
                            context.write(result);
                        }
                    }
                    if (time19 - time1 >= 1800) {
                        if (array[i].getC() == 0 && array[i].getF_b2() == 1 && array[i + 1].getC() == 0 && array[i + 1].getF_b2() == 1 && array[i + 2].getC() == 0 && array[i + 2].getF_b2() == 1 &&
                                array[i + 3].getC() == 0 && array[i + 3].getF_b2() == 1 && array[i + 4].getC() == 0 && array[i + 4].getF_b2() == 1 && array[i + 5].getC() == 0 && array[i + 5].getF_b2() == 1 &&
                                array[i + 6].getC() == 0 && array[i + 6].getF_b2() == 1 && array[i + 7].getC() == 0 && array[i + 7].getF_b2() == 1 && array[i + 8].getC() == 0 && array[i + 8].getF_b2() == 1 &&
                                array[i + 9].getC() == 0 && array[i + 9].getF_b2() == 1 && array[i + 10].getC() == 0 && array[i + 10].getF_b2() == 1 && array[i + 11].getC() == 0 && array[i + 11].getF_b2() == 1 &&
                                array[i + 12].getC() == 0 && array[i + 12].getF_b2() == 1 && array[i + 13].getC() == 0 && array[i + 13].getF_b2() == 1 && array[i + 14].getC() == 0 && array[i + 14].getF_b2() == 1 &&
                                array[i + 15].getC() == 0 && array[i + 15].getF_b2() == 1 && array[i + 16].getC() == 0 && array[i + 16].getF_b2() == 1 && array[i + 17].getC() == 0 && array[i + 17].getF_b2() == 1 &&
                                array[i + 18].getC() == 0 && array[i + 18].getF_b2() == 1) {
                            result.setString(0, batteryInfoBeanList.get(i).getPid());
                            result.setString(1, batteryInfoBeanList.get(i).getPid_sn());
                            result.setBigint(2, batteryInfoBeanList.get(i).getP());
                            result.setString(3, batteryInfoBeanList.get(i).getId());
                            result.setString(4, batteryInfoBeanList.get(i).getId_sn());
                            result.setDatetime(5, batteryInfoBeanList.get(i).getTime());
                            result.setBigint(6, batteryInfoBeanList.get(i).getBv0());
                            result.setBigint(7, batteryInfoBeanList.get(i).getBv1());
                            result.setBigint(8, batteryInfoBeanList.get(i).getBv2());
                            result.setBigint(9, batteryInfoBeanList.get(i).getBv3());
                            result.setBigint(10, batteryInfoBeanList.get(i).getBv4());
                            result.setBigint(12, batteryInfoBeanList.get(i).getBv5());
                            result.setBigint(13, batteryInfoBeanList.get(i).getBv6());
                            result.setBigint(14, batteryInfoBeanList.get(i).getBv7());
                            result.setBigint(15, batteryInfoBeanList.get(i).getBv8());
                            result.setBigint(16, batteryInfoBeanList.get(i).getBv9());
                            result.setBigint(17, batteryInfoBeanList.get(i).getBv10());
                            result.setBigint(18, batteryInfoBeanList.get(i).getBv11());
                            result.setBigint(19, batteryInfoBeanList.get(i).getBv12());
                            result.setBigint(20, batteryInfoBeanList.get(i).getBv13());
                            result.setBigint(21, batteryInfoBeanList.get(i).getBv14());
                            result.setBigint(22, batteryInfoBeanList.get(i).getBv15());
                            result.setBigint(23, batteryInfoBeanList.get(i).getF());
                            result.setBigint(24, batteryInfoBeanList.get(i).getF_b2());
                            result.setBigint(25, batteryInfoBeanList.get(i).getC());
                            context.write(result);
                        }
                    }
                    if (time20 - time1 >= 1800) {
                        if (array[i].getC() == 0 && array[i].getF_b2() == 1 && array[i + 1].getC() == 0 && array[i + 1].getF_b2() == 1 && array[i + 2].getC() == 0 && array[i + 2].getF_b2() == 1 &&
                                array[i + 3].getC() == 0 && array[i + 3].getF_b2() == 1 && array[i + 4].getC() == 0 && array[i + 4].getF_b2() == 1 && array[i + 5].getC() == 0 && array[i + 5].getF_b2() == 1 &&
                                array[i + 6].getC() == 0 && array[i + 6].getF_b2() == 1 && array[i + 7].getC() == 0 && array[i + 7].getF_b2() == 1 && array[i + 8].getC() == 0 && array[i + 8].getF_b2() == 1 &&
                                array[i + 9].getC() == 0 && array[i + 9].getF_b2() == 1 && array[i + 10].getC() == 0 && array[i + 10].getF_b2() == 1 && array[i + 11].getC() == 0 && array[i + 11].getF_b2() == 1 &&
                                array[i + 12].getC() == 0 && array[i + 12].getF_b2() == 1 && array[i + 13].getC() == 0 && array[i + 13].getF_b2() == 1 && array[i + 14].getC() == 0 && array[i + 14].getF_b2() == 1 &&
                                array[i + 15].getC() == 0 && array[i + 15].getF_b2() == 1 && array[i + 16].getC() == 0 && array[i + 16].getF_b2() == 1 && array[i + 17].getC() == 0 && array[i + 17].getF_b2() == 1 &&
                                array[i + 18].getC() == 0 && array[i + 18].getF_b2() == 1 && array[i + 19].getC() == 0 && array[i + 19].getF_b2() == 1) {
                            result.setString(0, batteryInfoBeanList.get(i).getPid());
                            result.setString(1, batteryInfoBeanList.get(i).getPid_sn());
                            result.setBigint(2, batteryInfoBeanList.get(i).getP());
                            result.setString(3, batteryInfoBeanList.get(i).getId());
                            result.setString(4, batteryInfoBeanList.get(i).getId_sn());
                            result.setDatetime(5, batteryInfoBeanList.get(i).getTime());
                            result.setBigint(6, batteryInfoBeanList.get(i).getBv0());
                            result.setBigint(7, batteryInfoBeanList.get(i).getBv1());
                            result.setBigint(8, batteryInfoBeanList.get(i).getBv2());
                            result.setBigint(9, batteryInfoBeanList.get(i).getBv3());
                            result.setBigint(10, batteryInfoBeanList.get(i).getBv4());
                            result.setBigint(12, batteryInfoBeanList.get(i).getBv5());
                            result.setBigint(13, batteryInfoBeanList.get(i).getBv6());
                            result.setBigint(14, batteryInfoBeanList.get(i).getBv7());
                            result.setBigint(15, batteryInfoBeanList.get(i).getBv8());
                            result.setBigint(16, batteryInfoBeanList.get(i).getBv9());
                            result.setBigint(17, batteryInfoBeanList.get(i).getBv10());
                            result.setBigint(18, batteryInfoBeanList.get(i).getBv11());
                            result.setBigint(19, batteryInfoBeanList.get(i).getBv12());
                            result.setBigint(20, batteryInfoBeanList.get(i).getBv13());
                            result.setBigint(21, batteryInfoBeanList.get(i).getBv14());
                            result.setBigint(22, batteryInfoBeanList.get(i).getBv15());
                            result.setBigint(23, batteryInfoBeanList.get(i).getF());
                            result.setBigint(24, batteryInfoBeanList.get(i).getF_b2());
                            result.setBigint(25, batteryInfoBeanList.get(i).getC());
                            context.write(result);
                        }
                    }
                    if (time21 - time1 >= 1800) {
                        if (array[i].getC() == 0 && array[i].getF_b2() == 1 && array[i + 1].getC() == 0 && array[i + 1].getF_b2() == 1 && array[i + 2].getC() == 0 && array[i + 2].getF_b2() == 1 &&
                                array[i + 3].getC() == 0 && array[i + 3].getF_b2() == 1 && array[i + 4].getC() == 0 && array[i + 4].getF_b2() == 1 && array[i + 5].getC() == 0 && array[i + 5].getF_b2() == 1 &&
                                array[i + 6].getC() == 0 && array[i + 6].getF_b2() == 1 && array[i + 7].getC() == 0 && array[i + 7].getF_b2() == 1 && array[i + 8].getC() == 0 && array[i + 8].getF_b2() == 1 &&
                                array[i + 9].getC() == 0 && array[i + 9].getF_b2() == 1 && array[i + 10].getC() == 0 && array[i + 10].getF_b2() == 1 && array[i + 11].getC() == 0 && array[i + 11].getF_b2() == 1 &&
                                array[i + 12].getC() == 0 && array[i + 12].getF_b2() == 1 && array[i + 13].getC() == 0 && array[i + 13].getF_b2() == 1 && array[i + 14].getC() == 0 && array[i + 14].getF_b2() == 1 &&
                                array[i + 15].getC() == 0 && array[i + 15].getF_b2() == 1 && array[i + 16].getC() == 0 && array[i + 16].getF_b2() == 1 && array[i + 17].getC() == 0 && array[i + 17].getF_b2() == 1 &&
                                array[i + 18].getC() == 0 && array[i + 18].getF_b2() == 1 && array[i + 19].getC() == 0 && array[i + 19].getF_b2() == 1 && array[i + 20].getC() == 0 && array[i + 20].getF_b2() == 1) {
                            result.setString(0, batteryInfoBeanList.get(i).getPid());
                            result.setString(1, batteryInfoBeanList.get(i).getPid_sn());
                            result.setBigint(2, batteryInfoBeanList.get(i).getP());
                            result.setString(3, batteryInfoBeanList.get(i).getId());
                            result.setString(4, batteryInfoBeanList.get(i).getId_sn());
                            result.setDatetime(5, batteryInfoBeanList.get(i).getTime());
                            result.setBigint(6, batteryInfoBeanList.get(i).getBv0());
                            result.setBigint(7, batteryInfoBeanList.get(i).getBv1());
                            result.setBigint(8, batteryInfoBeanList.get(i).getBv2());
                            result.setBigint(9, batteryInfoBeanList.get(i).getBv3());
                            result.setBigint(10, batteryInfoBeanList.get(i).getBv4());
                            result.setBigint(12, batteryInfoBeanList.get(i).getBv5());
                            result.setBigint(13, batteryInfoBeanList.get(i).getBv6());
                            result.setBigint(14, batteryInfoBeanList.get(i).getBv7());
                            result.setBigint(15, batteryInfoBeanList.get(i).getBv8());
                            result.setBigint(16, batteryInfoBeanList.get(i).getBv9());
                            result.setBigint(17, batteryInfoBeanList.get(i).getBv10());
                            result.setBigint(18, batteryInfoBeanList.get(i).getBv11());
                            result.setBigint(19, batteryInfoBeanList.get(i).getBv12());
                            result.setBigint(20, batteryInfoBeanList.get(i).getBv13());
                            result.setBigint(21, batteryInfoBeanList.get(i).getBv14());
                            result.setBigint(22, batteryInfoBeanList.get(i).getBv15());
                            result.setBigint(23, batteryInfoBeanList.get(i).getF());
                            result.setBigint(24, batteryInfoBeanList.get(i).getF_b2());
                            result.setBigint(25, batteryInfoBeanList.get(i).getC());
                            context.write(result);
                        }
                    }
                    if (time22 - time1 >= 1800) {
                        if (array[i].getC() == 0 && array[i].getF_b2() == 1 && array[i + 1].getC() == 0 && array[i + 1].getF_b2() == 1 && array[i + 2].getC() == 0 && array[i + 2].getF_b2() == 1 &&
                                array[i + 3].getC() == 0 && array[i + 3].getF_b2() == 1 && array[i + 4].getC() == 0 && array[i + 4].getF_b2() == 1 && array[i + 5].getC() == 0 && array[i + 5].getF_b2() == 1 &&
                                array[i + 6].getC() == 0 && array[i + 6].getF_b2() == 1 && array[i + 7].getC() == 0 && array[i + 7].getF_b2() == 1 && array[i + 8].getC() == 0 && array[i + 8].getF_b2() == 1 &&
                                array[i + 9].getC() == 0 && array[i + 9].getF_b2() == 1 && array[i + 10].getC() == 0 && array[i + 10].getF_b2() == 1 && array[i + 11].getC() == 0 && array[i + 11].getF_b2() == 1 &&
                                array[i + 12].getC() == 0 && array[i + 12].getF_b2() == 1 && array[i + 13].getC() == 0 && array[i + 13].getF_b2() == 1 && array[i + 14].getC() == 0 && array[i + 14].getF_b2() == 1 &&
                                array[i + 15].getC() == 0 && array[i + 15].getF_b2() == 1 && array[i + 16].getC() == 0 && array[i + 16].getF_b2() == 1 && array[i + 17].getC() == 0 && array[i + 17].getF_b2() == 1 &&
                                array[i + 18].getC() == 0 && array[i + 18].getF_b2() == 1 && array[i + 19].getC() == 0 && array[i + 19].getF_b2() == 1 && array[i + 20].getC() == 0 && array[i + 20].getF_b2() == 1 &&
                                array[i + 21].getC() == 0 && array[i + 21].getF_b2() == 1) {
                            result.setString(0, batteryInfoBeanList.get(i).getPid());
                            result.setString(1, batteryInfoBeanList.get(i).getPid_sn());
                            result.setBigint(2, batteryInfoBeanList.get(i).getP());
                            result.setString(3, batteryInfoBeanList.get(i).getId());
                            result.setString(4, batteryInfoBeanList.get(i).getId_sn());
                            result.setDatetime(5, batteryInfoBeanList.get(i).getTime());
                            result.setBigint(6, batteryInfoBeanList.get(i).getBv0());
                            result.setBigint(7, batteryInfoBeanList.get(i).getBv1());
                            result.setBigint(8, batteryInfoBeanList.get(i).getBv2());
                            result.setBigint(9, batteryInfoBeanList.get(i).getBv3());
                            result.setBigint(10, batteryInfoBeanList.get(i).getBv4());
                            result.setBigint(12, batteryInfoBeanList.get(i).getBv5());
                            result.setBigint(13, batteryInfoBeanList.get(i).getBv6());
                            result.setBigint(14, batteryInfoBeanList.get(i).getBv7());
                            result.setBigint(15, batteryInfoBeanList.get(i).getBv8());
                            result.setBigint(16, batteryInfoBeanList.get(i).getBv9());
                            result.setBigint(17, batteryInfoBeanList.get(i).getBv10());
                            result.setBigint(18, batteryInfoBeanList.get(i).getBv11());
                            result.setBigint(19, batteryInfoBeanList.get(i).getBv12());
                            result.setBigint(20, batteryInfoBeanList.get(i).getBv13());
                            result.setBigint(21, batteryInfoBeanList.get(i).getBv14());
                            result.setBigint(22, batteryInfoBeanList.get(i).getBv15());
                            result.setBigint(23, batteryInfoBeanList.get(i).getF());
                            result.setBigint(24, batteryInfoBeanList.get(i).getF_b2());
                            result.setBigint(25, batteryInfoBeanList.get(i).getC());
                            context.write(result);
                        }
                    }
                    if (time23 - time1 >= 1800) {
                        if (array[i].getC() == 0 && array[i].getF_b2() == 1 && array[i + 1].getC() == 0 && array[i + 1].getF_b2() == 1 && array[i + 2].getC() == 0 && array[i + 2].getF_b2() == 1 &&
                                array[i + 3].getC() == 0 && array[i + 3].getF_b2() == 1 && array[i + 4].getC() == 0 && array[i + 4].getF_b2() == 1 && array[i + 5].getC() == 0 && array[i + 5].getF_b2() == 1 &&
                                array[i + 6].getC() == 0 && array[i + 6].getF_b2() == 1 && array[i + 7].getC() == 0 && array[i + 7].getF_b2() == 1 && array[i + 8].getC() == 0 && array[i + 8].getF_b2() == 1 &&
                                array[i + 9].getC() == 0 && array[i + 9].getF_b2() == 1 && array[i + 10].getC() == 0 && array[i + 10].getF_b2() == 1 && array[i + 11].getC() == 0 && array[i + 11].getF_b2() == 1 &&
                                array[i + 12].getC() == 0 && array[i + 12].getF_b2() == 1 && array[i + 13].getC() == 0 && array[i + 13].getF_b2() == 1 && array[i + 14].getC() == 0 && array[i + 14].getF_b2() == 1 &&
                                array[i + 15].getC() == 0 && array[i + 15].getF_b2() == 1 && array[i + 16].getC() == 0 && array[i + 16].getF_b2() == 1 && array[i + 17].getC() == 0 && array[i + 17].getF_b2() == 1 &&
                                array[i + 18].getC() == 0 && array[i + 18].getF_b2() == 1 && array[i + 19].getC() == 0 && array[i + 19].getF_b2() == 1 && array[i + 20].getC() == 0 && array[i + 20].getF_b2() == 1 &&
                                array[i + 21].getC() == 0 && array[i + 21].getF_b2() == 1 && array[i + 22].getC() == 0 && array[i + 22].getF_b2() == 1) {
                            result.setString(0, batteryInfoBeanList.get(i).getPid());
                            result.setString(1, batteryInfoBeanList.get(i).getPid_sn());
                            result.setBigint(2, batteryInfoBeanList.get(i).getP());
                            result.setString(3, batteryInfoBeanList.get(i).getId());
                            result.setString(4, batteryInfoBeanList.get(i).getId_sn());
                            result.setDatetime(5, batteryInfoBeanList.get(i).getTime());
                            result.setBigint(6, batteryInfoBeanList.get(i).getBv0());
                            result.setBigint(7, batteryInfoBeanList.get(i).getBv1());
                            result.setBigint(8, batteryInfoBeanList.get(i).getBv2());
                            result.setBigint(9, batteryInfoBeanList.get(i).getBv3());
                            result.setBigint(10, batteryInfoBeanList.get(i).getBv4());
                            result.setBigint(12, batteryInfoBeanList.get(i).getBv5());
                            result.setBigint(13, batteryInfoBeanList.get(i).getBv6());
                            result.setBigint(14, batteryInfoBeanList.get(i).getBv7());
                            result.setBigint(15, batteryInfoBeanList.get(i).getBv8());
                            result.setBigint(16, batteryInfoBeanList.get(i).getBv9());
                            result.setBigint(17, batteryInfoBeanList.get(i).getBv10());
                            result.setBigint(18, batteryInfoBeanList.get(i).getBv11());
                            result.setBigint(19, batteryInfoBeanList.get(i).getBv12());
                            result.setBigint(20, batteryInfoBeanList.get(i).getBv13());
                            result.setBigint(21, batteryInfoBeanList.get(i).getBv14());
                            result.setBigint(22, batteryInfoBeanList.get(i).getBv15());
                            result.setBigint(23, batteryInfoBeanList.get(i).getF());
                            result.setBigint(24, batteryInfoBeanList.get(i).getF_b2());
                            result.setBigint(25, batteryInfoBeanList.get(i).getC());
                            context.write(result);
                        }
                    }
                    if (time24 - time1 >= 1800) {
                        if (array[i].getC() == 0 && array[i].getF_b2() == 1 && array[i + 1].getC() == 0 && array[i + 1].getF_b2() == 1 && array[i + 2].getC() == 0 && array[i + 2].getF_b2() == 1 &&
                                array[i + 3].getC() == 0 && array[i + 3].getF_b2() == 1 && array[i + 4].getC() == 0 && array[i + 4].getF_b2() == 1 && array[i + 5].getC() == 0 && array[i + 5].getF_b2() == 1 &&
                                array[i + 6].getC() == 0 && array[i + 6].getF_b2() == 1 && array[i + 7].getC() == 0 && array[i + 7].getF_b2() == 1 && array[i + 8].getC() == 0 && array[i + 8].getF_b2() == 1 &&
                                array[i + 9].getC() == 0 && array[i + 9].getF_b2() == 1 && array[i + 10].getC() == 0 && array[i + 10].getF_b2() == 1 && array[i + 11].getC() == 0 && array[i + 11].getF_b2() == 1 &&
                                array[i + 12].getC() == 0 && array[i + 12].getF_b2() == 1 && array[i + 13].getC() == 0 && array[i + 13].getF_b2() == 1 && array[i + 14].getC() == 0 && array[i + 14].getF_b2() == 1 &&
                                array[i + 15].getC() == 0 && array[i + 15].getF_b2() == 1 && array[i + 16].getC() == 0 && array[i + 16].getF_b2() == 1 && array[i + 17].getC() == 0 && array[i + 17].getF_b2() == 1 &&
                                array[i + 18].getC() == 0 && array[i + 18].getF_b2() == 1 && array[i + 19].getC() == 0 && array[i + 19].getF_b2() == 1 && array[i + 20].getC() == 0 && array[i + 20].getF_b2() == 1 &&
                                array[i + 21].getC() == 0 && array[i + 21].getF_b2() == 1 && array[i + 22].getC() == 0 && array[i + 22].getF_b2() == 1 && array[i + 23].getC() == 0 && array[i + 23].getF_b2() == 1) {
                            result.setString(0, batteryInfoBeanList.get(i).getPid());
                            result.setString(1, batteryInfoBeanList.get(i).getPid_sn());
                            result.setBigint(2, batteryInfoBeanList.get(i).getP());
                            result.setString(3, batteryInfoBeanList.get(i).getId());
                            result.setString(4, batteryInfoBeanList.get(i).getId_sn());
                            result.setDatetime(5, batteryInfoBeanList.get(i).getTime());
                            result.setBigint(6, batteryInfoBeanList.get(i).getBv0());
                            result.setBigint(7, batteryInfoBeanList.get(i).getBv1());
                            result.setBigint(8, batteryInfoBeanList.get(i).getBv2());
                            result.setBigint(9, batteryInfoBeanList.get(i).getBv3());
                            result.setBigint(10, batteryInfoBeanList.get(i).getBv4());
                            result.setBigint(12, batteryInfoBeanList.get(i).getBv5());
                            result.setBigint(13, batteryInfoBeanList.get(i).getBv6());
                            result.setBigint(14, batteryInfoBeanList.get(i).getBv7());
                            result.setBigint(15, batteryInfoBeanList.get(i).getBv8());
                            result.setBigint(16, batteryInfoBeanList.get(i).getBv9());
                            result.setBigint(17, batteryInfoBeanList.get(i).getBv10());
                            result.setBigint(18, batteryInfoBeanList.get(i).getBv11());
                            result.setBigint(19, batteryInfoBeanList.get(i).getBv12());
                            result.setBigint(20, batteryInfoBeanList.get(i).getBv13());
                            result.setBigint(21, batteryInfoBeanList.get(i).getBv14());
                            result.setBigint(22, batteryInfoBeanList.get(i).getBv15());
                            result.setBigint(23, batteryInfoBeanList.get(i).getF());
                            result.setBigint(24, batteryInfoBeanList.get(i).getF_b2());
                            result.setBigint(25, batteryInfoBeanList.get(i).getC());
                            context.write(result);
                        }
                    }
                    if (time25 - time1 >= 1800) {
                        if (array[i].getC() == 0 && array[i].getF_b2() == 1 && array[i + 1].getC() == 0 && array[i + 1].getF_b2() == 1 && array[i + 2].getC() == 0 && array[i + 2].getF_b2() == 1 &&
                                array[i + 3].getC() == 0 && array[i + 3].getF_b2() == 1 && array[i + 4].getC() == 0 && array[i + 4].getF_b2() == 1 && array[i + 5].getC() == 0 && array[i + 5].getF_b2() == 1 &&
                                array[i + 6].getC() == 0 && array[i + 6].getF_b2() == 1 && array[i + 7].getC() == 0 && array[i + 7].getF_b2() == 1 && array[i + 8].getC() == 0 && array[i + 8].getF_b2() == 1 &&
                                array[i + 9].getC() == 0 && array[i + 9].getF_b2() == 1 && array[i + 10].getC() == 0 && array[i + 10].getF_b2() == 1 && array[i + 11].getC() == 0 && array[i + 11].getF_b2() == 1 &&
                                array[i + 12].getC() == 0 && array[i + 12].getF_b2() == 1 && array[i + 13].getC() == 0 && array[i + 13].getF_b2() == 1 && array[i + 14].getC() == 0 && array[i + 14].getF_b2() == 1 &&
                                array[i + 15].getC() == 0 && array[i + 15].getF_b2() == 1 && array[i + 16].getC() == 0 && array[i + 16].getF_b2() == 1 && array[i + 17].getC() == 0 && array[i + 17].getF_b2() == 1 &&
                                array[i + 18].getC() == 0 && array[i + 18].getF_b2() == 1 && array[i + 19].getC() == 0 && array[i + 19].getF_b2() == 1 && array[i + 20].getC() == 0 && array[i + 20].getF_b2() == 1 &&
                                array[i + 21].getC() == 0 && array[i + 21].getF_b2() == 1 && array[i + 22].getC() == 0 && array[i + 22].getF_b2() == 1 && array[i + 23].getC() == 0 && array[i + 23].getF_b2() == 1 &&
                                array[i + 24].getC() == 0 && array[i + 24].getF_b2() == 1) {
                            result.setString(0, batteryInfoBeanList.get(i).getPid());
                            result.setString(1, batteryInfoBeanList.get(i).getPid_sn());
                            result.setBigint(2, batteryInfoBeanList.get(i).getP());
                            result.setString(3, batteryInfoBeanList.get(i).getId());
                            result.setString(4, batteryInfoBeanList.get(i).getId_sn());
                            result.setDatetime(5, batteryInfoBeanList.get(i).getTime());
                            result.setBigint(6, batteryInfoBeanList.get(i).getBv0());
                            result.setBigint(7, batteryInfoBeanList.get(i).getBv1());
                            result.setBigint(8, batteryInfoBeanList.get(i).getBv2());
                            result.setBigint(9, batteryInfoBeanList.get(i).getBv3());
                            result.setBigint(10, batteryInfoBeanList.get(i).getBv4());
                            result.setBigint(12, batteryInfoBeanList.get(i).getBv5());
                            result.setBigint(13, batteryInfoBeanList.get(i).getBv6());
                            result.setBigint(14, batteryInfoBeanList.get(i).getBv7());
                            result.setBigint(15, batteryInfoBeanList.get(i).getBv8());
                            result.setBigint(16, batteryInfoBeanList.get(i).getBv9());
                            result.setBigint(17, batteryInfoBeanList.get(i).getBv10());
                            result.setBigint(18, batteryInfoBeanList.get(i).getBv11());
                            result.setBigint(19, batteryInfoBeanList.get(i).getBv12());
                            result.setBigint(20, batteryInfoBeanList.get(i).getBv13());
                            result.setBigint(21, batteryInfoBeanList.get(i).getBv14());
                            result.setBigint(22, batteryInfoBeanList.get(i).getBv15());
                            result.setBigint(23, batteryInfoBeanList.get(i).getF());
                            result.setBigint(24, batteryInfoBeanList.get(i).getF_b2());
                            result.setBigint(25, batteryInfoBeanList.get(i).getC());
                            context.write(result);
                        }
                    }
                    if (time26 - time1 >= 1800) {
                        if (array[i].getC() == 0 && array[i].getF_b2() == 1 && array[i + 1].getC() == 0 && array[i + 1].getF_b2() == 1 && array[i + 2].getC() == 0 && array[i + 2].getF_b2() == 1 &&
                                array[i + 3].getC() == 0 && array[i + 3].getF_b2() == 1 && array[i + 4].getC() == 0 && array[i + 4].getF_b2() == 1 && array[i + 5].getC() == 0 && array[i + 5].getF_b2() == 1 &&
                                array[i + 6].getC() == 0 && array[i + 6].getF_b2() == 1 && array[i + 7].getC() == 0 && array[i + 7].getF_b2() == 1 && array[i + 8].getC() == 0 && array[i + 8].getF_b2() == 1 &&
                                array[i + 9].getC() == 0 && array[i + 9].getF_b2() == 1 && array[i + 10].getC() == 0 && array[i + 10].getF_b2() == 1 && array[i + 11].getC() == 0 && array[i + 11].getF_b2() == 1 &&
                                array[i + 12].getC() == 0 && array[i + 12].getF_b2() == 1 && array[i + 13].getC() == 0 && array[i + 13].getF_b2() == 1 && array[i + 14].getC() == 0 && array[i + 14].getF_b2() == 1 &&
                                array[i + 15].getC() == 0 && array[i + 15].getF_b2() == 1 && array[i + 16].getC() == 0 && array[i + 16].getF_b2() == 1 && array[i + 17].getC() == 0 && array[i + 17].getF_b2() == 1 &&
                                array[i + 18].getC() == 0 && array[i + 18].getF_b2() == 1 && array[i + 19].getC() == 0 && array[i + 19].getF_b2() == 1 && array[i + 20].getC() == 0 && array[i + 20].getF_b2() == 1 &&
                                array[i + 21].getC() == 0 && array[i + 21].getF_b2() == 1 && array[i + 22].getC() == 0 && array[i + 22].getF_b2() == 1 && array[i + 23].getC() == 0 && array[i + 23].getF_b2() == 1 &&
                                array[i + 24].getC() == 0 && array[i + 24].getF_b2() == 1 && array[i + 25].getC() == 0 && array[i + 25].getF_b2() == 1) {
                            result.setString(0, batteryInfoBeanList.get(i).getPid());
                            result.setString(1, batteryInfoBeanList.get(i).getPid_sn());
                            result.setBigint(2, batteryInfoBeanList.get(i).getP());
                            result.setString(3, batteryInfoBeanList.get(i).getId());
                            result.setString(4, batteryInfoBeanList.get(i).getId_sn());
                            result.setDatetime(5, batteryInfoBeanList.get(i).getTime());
                            result.setBigint(6, batteryInfoBeanList.get(i).getBv0());
                            result.setBigint(7, batteryInfoBeanList.get(i).getBv1());
                            result.setBigint(8, batteryInfoBeanList.get(i).getBv2());
                            result.setBigint(9, batteryInfoBeanList.get(i).getBv3());
                            result.setBigint(10, batteryInfoBeanList.get(i).getBv4());
                            result.setBigint(12, batteryInfoBeanList.get(i).getBv5());
                            result.setBigint(13, batteryInfoBeanList.get(i).getBv6());
                            result.setBigint(14, batteryInfoBeanList.get(i).getBv7());
                            result.setBigint(15, batteryInfoBeanList.get(i).getBv8());
                            result.setBigint(16, batteryInfoBeanList.get(i).getBv9());
                            result.setBigint(17, batteryInfoBeanList.get(i).getBv10());
                            result.setBigint(18, batteryInfoBeanList.get(i).getBv11());
                            result.setBigint(19, batteryInfoBeanList.get(i).getBv12());
                            result.setBigint(20, batteryInfoBeanList.get(i).getBv13());
                            result.setBigint(21, batteryInfoBeanList.get(i).getBv14());
                            result.setBigint(22, batteryInfoBeanList.get(i).getBv15());
                            result.setBigint(23, batteryInfoBeanList.get(i).getF());
                            result.setBigint(24, batteryInfoBeanList.get(i).getF_b2());
                            result.setBigint(25, batteryInfoBeanList.get(i).getC());
                            context.write(result);
                        }
                    }
                    if (time27 - time1 >= 1800) {
                        if (array[i].getC() == 0 && array[i].getF_b2() == 1 && array[i + 1].getC() == 0 && array[i + 1].getF_b2() == 1 && array[i + 2].getC() == 0 && array[i + 2].getF_b2() == 1 &&
                                array[i + 3].getC() == 0 && array[i + 3].getF_b2() == 1 && array[i + 4].getC() == 0 && array[i + 4].getF_b2() == 1 && array[i + 5].getC() == 0 && array[i + 5].getF_b2() == 1 &&
                                array[i + 6].getC() == 0 && array[i + 6].getF_b2() == 1 && array[i + 7].getC() == 0 && array[i + 7].getF_b2() == 1 && array[i + 8].getC() == 0 && array[i + 8].getF_b2() == 1 &&
                                array[i + 9].getC() == 0 && array[i + 9].getF_b2() == 1 && array[i + 10].getC() == 0 && array[i + 10].getF_b2() == 1 && array[i + 11].getC() == 0 && array[i + 11].getF_b2() == 1 &&
                                array[i + 12].getC() == 0 && array[i + 12].getF_b2() == 1 && array[i + 13].getC() == 0 && array[i + 13].getF_b2() == 1 && array[i + 14].getC() == 0 && array[i + 14].getF_b2() == 1 &&
                                array[i + 15].getC() == 0 && array[i + 15].getF_b2() == 1 && array[i + 16].getC() == 0 && array[i + 16].getF_b2() == 1 && array[i + 17].getC() == 0 && array[i + 17].getF_b2() == 1 &&
                                array[i + 18].getC() == 0 && array[i + 18].getF_b2() == 1 && array[i + 19].getC() == 0 && array[i + 19].getF_b2() == 1 && array[i + 20].getC() == 0 && array[i + 20].getF_b2() == 1 &&
                                array[i + 21].getC() == 0 && array[i + 21].getF_b2() == 1 && array[i + 22].getC() == 0 && array[i + 22].getF_b2() == 1 && array[i + 23].getC() == 0 && array[i + 23].getF_b2() == 1 &&
                                array[i + 24].getC() == 0 && array[i + 24].getF_b2() == 1 && array[i + 25].getC() == 0 && array[i + 25].getF_b2() == 1 && array[i + 26].getC() == 0 && array[i + 26].getF_b2() == 1) {
                            result.setString(0, batteryInfoBeanList.get(i).getPid());
                            result.setString(1, batteryInfoBeanList.get(i).getPid_sn());
                            result.setBigint(2, batteryInfoBeanList.get(i).getP());
                            result.setString(3, batteryInfoBeanList.get(i).getId());
                            result.setString(4, batteryInfoBeanList.get(i).getId_sn());
                            result.setDatetime(5, batteryInfoBeanList.get(i).getTime());
                            result.setBigint(6, batteryInfoBeanList.get(i).getBv0());
                            result.setBigint(7, batteryInfoBeanList.get(i).getBv1());
                            result.setBigint(8, batteryInfoBeanList.get(i).getBv2());
                            result.setBigint(9, batteryInfoBeanList.get(i).getBv3());
                            result.setBigint(10, batteryInfoBeanList.get(i).getBv4());
                            result.setBigint(12, batteryInfoBeanList.get(i).getBv5());
                            result.setBigint(13, batteryInfoBeanList.get(i).getBv6());
                            result.setBigint(14, batteryInfoBeanList.get(i).getBv7());
                            result.setBigint(15, batteryInfoBeanList.get(i).getBv8());
                            result.setBigint(16, batteryInfoBeanList.get(i).getBv9());
                            result.setBigint(17, batteryInfoBeanList.get(i).getBv10());
                            result.setBigint(18, batteryInfoBeanList.get(i).getBv11());
                            result.setBigint(19, batteryInfoBeanList.get(i).getBv12());
                            result.setBigint(20, batteryInfoBeanList.get(i).getBv13());
                            result.setBigint(21, batteryInfoBeanList.get(i).getBv14());
                            result.setBigint(22, batteryInfoBeanList.get(i).getBv15());
                            result.setBigint(23, batteryInfoBeanList.get(i).getF());
                            result.setBigint(24, batteryInfoBeanList.get(i).getF_b2());
                            result.setBigint(25, batteryInfoBeanList.get(i).getC());
                            context.write(result);
                        }
                    }
                    if (time28 - time1 >= 1800) {
                        if (array[i].getC() == 0 && array[i].getF_b2() == 1 && array[i + 1].getC() == 0 && array[i + 1].getF_b2() == 1 && array[i + 2].getC() == 0 && array[i + 2].getF_b2() == 1 &&
                                array[i + 3].getC() == 0 && array[i + 3].getF_b2() == 1 && array[i + 4].getC() == 0 && array[i + 4].getF_b2() == 1 && array[i + 5].getC() == 0 && array[i + 5].getF_b2() == 1 &&
                                array[i + 6].getC() == 0 && array[i + 6].getF_b2() == 1 && array[i + 7].getC() == 0 && array[i + 7].getF_b2() == 1 && array[i + 8].getC() == 0 && array[i + 8].getF_b2() == 1 &&
                                array[i + 9].getC() == 0 && array[i + 9].getF_b2() == 1 && array[i + 10].getC() == 0 && array[i + 10].getF_b2() == 1 && array[i + 11].getC() == 0 && array[i + 11].getF_b2() == 1 &&
                                array[i + 12].getC() == 0 && array[i + 12].getF_b2() == 1 && array[i + 13].getC() == 0 && array[i + 13].getF_b2() == 1 && array[i + 14].getC() == 0 && array[i + 14].getF_b2() == 1 &&
                                array[i + 15].getC() == 0 && array[i + 15].getF_b2() == 1 && array[i + 16].getC() == 0 && array[i + 16].getF_b2() == 1 && array[i + 17].getC() == 0 && array[i + 17].getF_b2() == 1 &&
                                array[i + 18].getC() == 0 && array[i + 18].getF_b2() == 1 && array[i + 19].getC() == 0 && array[i + 19].getF_b2() == 1 && array[i + 20].getC() == 0 && array[i + 20].getF_b2() == 1 &&
                                array[i + 21].getC() == 0 && array[i + 21].getF_b2() == 1 && array[i + 22].getC() == 0 && array[i + 22].getF_b2() == 1 && array[i + 23].getC() == 0 && array[i + 23].getF_b2() == 1 &&
                                array[i + 24].getC() == 0 && array[i + 24].getF_b2() == 1 && array[i + 25].getC() == 0 && array[i + 25].getF_b2() == 1 && array[i + 26].getC() == 0 && array[i + 26].getF_b2() == 1 &&
                                array[i + 27].getC() == 0 && array[i + 27].getF_b2() == 1) {
                            result.setString(0, batteryInfoBeanList.get(i).getPid());
                            result.setString(1, batteryInfoBeanList.get(i).getPid_sn());
                            result.setBigint(2, batteryInfoBeanList.get(i).getP());
                            result.setString(3, batteryInfoBeanList.get(i).getId());
                            result.setString(4, batteryInfoBeanList.get(i).getId_sn());
                            result.setDatetime(5, batteryInfoBeanList.get(i).getTime());
                            result.setBigint(6, batteryInfoBeanList.get(i).getBv0());
                            result.setBigint(7, batteryInfoBeanList.get(i).getBv1());
                            result.setBigint(8, batteryInfoBeanList.get(i).getBv2());
                            result.setBigint(9, batteryInfoBeanList.get(i).getBv3());
                            result.setBigint(10, batteryInfoBeanList.get(i).getBv4());
                            result.setBigint(12, batteryInfoBeanList.get(i).getBv5());
                            result.setBigint(13, batteryInfoBeanList.get(i).getBv6());
                            result.setBigint(14, batteryInfoBeanList.get(i).getBv7());
                            result.setBigint(15, batteryInfoBeanList.get(i).getBv8());
                            result.setBigint(16, batteryInfoBeanList.get(i).getBv9());
                            result.setBigint(17, batteryInfoBeanList.get(i).getBv10());
                            result.setBigint(18, batteryInfoBeanList.get(i).getBv11());
                            result.setBigint(19, batteryInfoBeanList.get(i).getBv12());
                            result.setBigint(20, batteryInfoBeanList.get(i).getBv13());
                            result.setBigint(21, batteryInfoBeanList.get(i).getBv14());
                            result.setBigint(22, batteryInfoBeanList.get(i).getBv15());
                            result.setBigint(23, batteryInfoBeanList.get(i).getF());
                            result.setBigint(24, batteryInfoBeanList.get(i).getF_b2());
                            result.setBigint(25, batteryInfoBeanList.get(i).getC());
                            context.write(result);
                        }
                    }
                    if (time29 - time1 >= 1800) {
                        if (array[i].getC() == 0 && array[i].getF_b2() == 1 && array[i + 1].getC() == 0 && array[i + 1].getF_b2() == 1 && array[i + 2].getC() == 0 && array[i + 2].getF_b2() == 1 &&
                                array[i + 3].getC() == 0 && array[i + 3].getF_b2() == 1 && array[i + 4].getC() == 0 && array[i + 4].getF_b2() == 1 && array[i + 5].getC() == 0 && array[i + 5].getF_b2() == 1 &&
                                array[i + 6].getC() == 0 && array[i + 6].getF_b2() == 1 && array[i + 7].getC() == 0 && array[i + 7].getF_b2() == 1 && array[i + 8].getC() == 0 && array[i + 8].getF_b2() == 1 &&
                                array[i + 9].getC() == 0 && array[i + 9].getF_b2() == 1 && array[i + 10].getC() == 0 && array[i + 10].getF_b2() == 1 && array[i + 11].getC() == 0 && array[i + 11].getF_b2() == 1 &&
                                array[i + 12].getC() == 0 && array[i + 12].getF_b2() == 1 && array[i + 13].getC() == 0 && array[i + 13].getF_b2() == 1 && array[i + 14].getC() == 0 && array[i + 14].getF_b2() == 1 &&
                                array[i + 15].getC() == 0 && array[i + 15].getF_b2() == 1 && array[i + 16].getC() == 0 && array[i + 16].getF_b2() == 1 && array[i + 17].getC() == 0 && array[i + 17].getF_b2() == 1 &&
                                array[i + 18].getC() == 0 && array[i + 18].getF_b2() == 1 && array[i + 19].getC() == 0 && array[i + 19].getF_b2() == 1 && array[i + 20].getC() == 0 && array[i + 20].getF_b2() == 1 &&
                                array[i + 21].getC() == 0 && array[i + 21].getF_b2() == 1 && array[i + 22].getC() == 0 && array[i + 22].getF_b2() == 1 && array[i + 23].getC() == 0 && array[i + 23].getF_b2() == 1 &&
                                array[i + 24].getC() == 0 && array[i + 24].getF_b2() == 1 && array[i + 25].getC() == 0 && array[i + 25].getF_b2() == 1 && array[i + 26].getC() == 0 && array[i + 26].getF_b2() == 1 &&
                                array[i + 27].getC() == 0 && array[i + 27].getF_b2() == 1 && array[i + 28].getC() == 0 && array[i + 28].getF_b2() == 1) {
                            result.setString(0, batteryInfoBeanList.get(i).getPid());
                            result.setString(1, batteryInfoBeanList.get(i).getPid_sn());
                            result.setBigint(2, batteryInfoBeanList.get(i).getP());
                            result.setString(3, batteryInfoBeanList.get(i).getId());
                            result.setString(4, batteryInfoBeanList.get(i).getId_sn());
                            result.setDatetime(5, batteryInfoBeanList.get(i).getTime());
                            result.setBigint(6, batteryInfoBeanList.get(i).getBv0());
                            result.setBigint(7, batteryInfoBeanList.get(i).getBv1());
                            result.setBigint(8, batteryInfoBeanList.get(i).getBv2());
                            result.setBigint(9, batteryInfoBeanList.get(i).getBv3());
                            result.setBigint(10, batteryInfoBeanList.get(i).getBv4());
                            result.setBigint(12, batteryInfoBeanList.get(i).getBv5());
                            result.setBigint(13, batteryInfoBeanList.get(i).getBv6());
                            result.setBigint(14, batteryInfoBeanList.get(i).getBv7());
                            result.setBigint(15, batteryInfoBeanList.get(i).getBv8());
                            result.setBigint(16, batteryInfoBeanList.get(i).getBv9());
                            result.setBigint(17, batteryInfoBeanList.get(i).getBv10());
                            result.setBigint(18, batteryInfoBeanList.get(i).getBv11());
                            result.setBigint(19, batteryInfoBeanList.get(i).getBv12());
                            result.setBigint(20, batteryInfoBeanList.get(i).getBv13());
                            result.setBigint(21, batteryInfoBeanList.get(i).getBv14());
                            result.setBigint(22, batteryInfoBeanList.get(i).getBv15());
                            result.setBigint(23, batteryInfoBeanList.get(i).getF());
                            result.setBigint(24, batteryInfoBeanList.get(i).getF_b2());
                            result.setBigint(25, batteryInfoBeanList.get(i).getC());
                            context.write(result);
                        }
                    }
                    if (time30 - time1 >= 1800) {
                        if (array[i].getC() == 0 && array[i].getF_b2() == 1 && array[i + 1].getC() == 0 && array[i + 1].getF_b2() == 1 && array[i + 2].getC() == 0 && array[i + 2].getF_b2() == 1 &&
                                array[i + 3].getC() == 0 && array[i + 3].getF_b2() == 1 && array[i + 4].getC() == 0 && array[i + 4].getF_b2() == 1 && array[i + 5].getC() == 0 && array[i + 5].getF_b2() == 1 &&
                                array[i + 6].getC() == 0 && array[i + 6].getF_b2() == 1 && array[i + 7].getC() == 0 && array[i + 7].getF_b2() == 1 && array[i + 8].getC() == 0 && array[i + 8].getF_b2() == 1 &&
                                array[i + 9].getC() == 0 && array[i + 9].getF_b2() == 1 && array[i + 10].getC() == 0 && array[i + 10].getF_b2() == 1 && array[i + 11].getC() == 0 && array[i + 11].getF_b2() == 1 &&
                                array[i + 12].getC() == 0 && array[i + 12].getF_b2() == 1 && array[i + 13].getC() == 0 && array[i + 13].getF_b2() == 1 && array[i + 14].getC() == 0 && array[i + 14].getF_b2() == 1 &&
                                array[i + 15].getC() == 0 && array[i + 15].getF_b2() == 1 && array[i + 16].getC() == 0 && array[i + 16].getF_b2() == 1 && array[i + 17].getC() == 0 && array[i + 17].getF_b2() == 1 &&
                                array[i + 18].getC() == 0 && array[i + 18].getF_b2() == 1 && array[i + 19].getC() == 0 && array[i + 19].getF_b2() == 1 && array[i + 20].getC() == 0 && array[i + 20].getF_b2() == 1 &&
                                array[i + 21].getC() == 0 && array[i + 21].getF_b2() == 1 && array[i + 22].getC() == 0 && array[i + 22].getF_b2() == 1 && array[i + 23].getC() == 0 && array[i + 23].getF_b2() == 1 &&
                                array[i + 24].getC() == 0 && array[i + 24].getF_b2() == 1 && array[i + 25].getC() == 0 && array[i + 25].getF_b2() == 1 && array[i + 26].getC() == 0 && array[i + 26].getF_b2() == 1 &&
                                array[i + 27].getC() == 0 && array[i + 27].getF_b2() == 1 && array[i + 28].getC() == 0 && array[i + 28].getF_b2() == 1 && array[i + 29].getC() == 0 && array[i + 29].getF_b2() == 1) {
                            result.setString(0, array[i].getPid());
                            result.setString(1, array[i].getPid_sn());
                            result.setBigint(2, array[i].getP());
                            result.setString(3, array[i].getId());
                            result.setString(4, array[i].getId_sn());
                            result.setDatetime(5, array[i].getTime());
                            result.setBigint(6, array[i].getBv0());
                            result.setBigint(7, array[i].getBv1());
                            result.setBigint(8, array[i].getBv2());
                            result.setBigint(9, array[i].getBv3());
                            result.setBigint(10, array[i].getBv4());
                            result.setBigint(12, array[i].getBv5());
                            result.setBigint(13, array[i].getBv6());
                            result.setBigint(14, array[i].getBv7());
                            result.setBigint(15, array[i].getBv8());
                            result.setBigint(16, array[i].getBv9());
                            result.setBigint(17, array[i].getBv10());
                            result.setBigint(18, array[i].getBv11());
                            result.setBigint(19, array[i].getBv12());
                            result.setBigint(20, array[i].getBv13());
                            result.setBigint(21, array[i].getBv14());
                            result.setBigint(22, array[i].getBv15());
                            result.setBigint(23, array[i].getF());
                            result.setBigint(24, array[i].getF_b2());
                            result.setBigint(25, batteryInfoBeanList.get(i).getC());
                            context.write(result);
                        }
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
            job.setNumReduceTasks(5);

            JobClient.runJob(ToolUtil.runJob(args, BatteryInfo.TokenizerMapper.class,BatteryInfo.SumReducer.class,
                    SchemaUtils.fromString("id:string"),
                    SchemaUtils.fromString("pid:string,pid_sn:string,p:bigint,id:string,id_sn:string,time:datetime,bv0:bigint,bv1:bigint,bv2:bigint,bv3:bigint,bv4:bigint,bv5:bigint,bv6:bigint,bv7:bigint,bv8:bigint,bv9:bigint,bv10:bigint,bv11:bigint,bv12:bigint,bv13:bigint,bv14:bigint,bv15:bigint,f:bigint,f_b2:bigint,c:bigint")));

        }

}
