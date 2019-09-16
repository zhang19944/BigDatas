package ashley.mongo;

import ashley.AshleyDateUtils;
import com.aliyun.odps.OdpsException;
import com.aliyun.odps.data.Record;
import com.aliyun.odps.mapred.*;
import util.ToolUtil;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by AshleyZHANG on 2019/6/26.
 */
public class MongoPromotion {
    public static class promoMr extends MapperBase {
        private Record out;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        AshleyDateUtils ashleyDateUtils =new AshleyDateUtils();
        @Override
        public void setup(Mapper.TaskContext context) throws IOException {
            out=context.createOutputRecord();
        }
        @Override
        public void map(long recordNum, Record record, Mapper.TaskContext context) throws IOException {

            long timenow=System.currentTimeMillis();
            long status;

            String couponUseType = null;
            //优惠券的使用金额(两种，1.金额，2折扣)
            long discountMoney = 0;
            long discountType;
            status = record.getBigint("statu");
            //优惠券的有效期
            long coupontimestart=record.getBigint("starttime");
            long coupontimeend=record.getBigint("endtime");
            //判优惠券是否过期，断experitype的数值；
            long experitype =record.getBigint("coupon_expirytype");
            long createtime;
            if (record.getBigint("createtime")==null){
                createtime=0;
            }else{
                createtime=record.getBigint("createtime");
            }

            long days=record.getBigint("coupon_days")*86400000;

            //未使用：1.有效期内未使用，2.过期未使用（失效一种）
            if (status==0){
                    //天数：领取时间
                if (experitype==1){
                    if (timenow>coupontimeend){
                        couponUseType="过期未使用";
                    }else if ( createtime<coupontimeend && createtime>=coupontimestart && createtime+days>timenow){
                        couponUseType="有效期内未使用";
                    }else if (createtime<=coupontimestart && timenow<coupontimestart+days){
                        couponUseType="有效期内未使用";
                    }
                    //日期
                }if (experitype==2){
                    if (timenow>coupontimeend){
                        couponUseType="未使用导致失效";
                    }
                    if(timenow<coupontimeend){
                        couponUseType="有效期内未使用";
                    }
                }
            }
            //已失效：1.过期失效，status可能为0
            if (status==2){
                    couponUseType="过期未使用";
            }
            if (status==1){
                couponUseType="已使用";
                int count=0;
                count++;
            }

            //优惠券的使用金额(两种，1.金额，2折扣)
            discountType= record.getBigint("coupon_discounttype");
            if (discountType==1){
                discountMoney=record.getBigint("coupon_amount");
            }
            if (discountType==2){
                discountMoney=0;
            }
            out.setString(0, record.getString("couponid"));
            out.setString(1, record.getString("citycode"));
            out.setString(2,record.getString("cityname"));
            out.setString(3, record.getString("userid"));
            out.setString(4, record.getString("phone"));
            out.setString(5, record.getString("group_code"));
            out.setString(6,record.getString("group_name"));
            out.setString(7,record.getString("coupon_name"));
            out.setString(8,record.getString("coupon_desc"));
            if (record.getBigint("coupon_actstarttime")==null){
                out.setDatetime(9,null);
            }else {
                out.setDatetime(9, new Date(record.getBigint("coupon_actstarttime")));
            }
            if (record.getBigint("coupon_actendtime")==null){
                out.setDatetime(10,null);
            }else {
                out.setDatetime(10,new Date(record.getBigint("coupon_actendtime")));
            }
            out.setBigint(11, record.getBigint("coupon_circulation"));
            out.setBigint(12, record.getBigint("coupon_rectotal"));
            out.setBigint(13, record.getBigint("coupon_quantity"));
            out.setBigint(14, record.getBigint("coupon_expirytype"));
            out.setBigint(15,  record.getBigint("coupon_days"));
            out.setBigint(16, record.getBigint("coupon_discounttype"));
            out.setBigint(17, record.getBigint("coupon_amount"));
            out.setDouble(18, record.getDouble("coupon_discount"));

            if (record.getBigint("coupon_publishtime")==null){
                out.setDatetime(19,null);
            }else{
                out.setDatetime(19, new Date(record.getBigint("coupon_publishtime")));
            }
            out.setBigint(20, record.getBigint("statu"));
            if (record.getBigint("starttime")==null){
                out.setDatetime(21,null);
            }else{
                out.setDatetime(21, new Date(record.getBigint("starttime")) );
            }

            if (record.getBigint("endtime")==null){
                out.setDatetime(22,null);
            }else {
                out.setDatetime(22, new Date(record.getBigint("endtime")));
            }

            if (record.getBigint("usedtime")==null){
                out.setDatetime(23,null);
            }else {
                out.setDatetime(23,new Date(record.getBigint("usedtime")) );
            }

            out.setString(24,couponUseType);
            out.setBigint(25,discountMoney);

            if (record.getBigint("createtime")==null){
                out.setDatetime(26,null);
            }else {
                out.setDatetime(26,new Date(record.getBigint("createtime")));
            }
            out.setString(27,record.getString("coupon_rulecode"));
            context.write(out);
        }
    }
    public static void main(String[] args) throws OdpsException {

        if (args.length != 2) {
            System.err.println("参数不正确");
        }
        JobClient.runJob(ToolUtil.runJob(args, MongoPromotion.promoMr.class
                //SchemaUtils.fromString("couponid:string"),
                //SchemaUtils.fromString("citycode:string,cityname:string,userid:string,phone:string,group_code:string,group_name:string,coupon_desc:string,coupon_actstarttime:bigint,coupon_actendtime:bigint,coupon_circulation:bigint,coupon_rectotal:bigint,coupon_quantity:bigint,coupon_expirytype:bigint,coupon_days:bigint,coupon_discounttype:bigint,coupon_amount:bigint,coupon_discount:double,coupon_publishtime:bigint,statu:bigint,starttime:bigint,endtime:bigint,usedtime:bigint")
                ));
        System.out.println("finished");
    }
}
