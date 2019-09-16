package ashley.mrlogtype;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ashion on 2019/9/6.
 */

public class ParseDemo {
    static String str = "[0B,45,00,00,E4,14,30,75,45,02,04,AD,01,10,14,0A,9B,C7,7D,80,16,64,00,78,00,45,45,43,44,00,00,00,00,06,00,00,00,C8,00,C1,00,25,09,36,0E,36,0E,36,0E,39,0E,36,0E,39,0E,37,0E,38,0E,34,0E,2E,0E,3A,0E,3A,0E,38,0E,42,0E,01,64,7E,19,30,75,45,00,00,1F,00,10,26,A8,7A,49,28,80,16,64,00,78,00,45,45,46,44,00,00,00,00,00,00,00,00,08,40,EF,0F,F1,0F,EE,0F,F1,0F,EE,0F,E8,0F,EF,0F,ED,0F,EC,0F,F0,0F,F2,0F,F1,0F,F1,0F,F1,0F,F0,0F,F1,0F,02,63,76,19,30,75,45,00,00,C9,00,10,23,50,1E,2C,2C,80,16,5B,00,78,00,45,44,45,44,00,00,00,00,00,00,00,00,08,40,E8,0F,EB,0F,ED,0F,EC,0F,E9,0F,E9,0F,EA,0F,EB,0F,E8,0F,EC,0F,E8,0F,E8,0F,E9,0F,EA,0F,EA,0F,EB,0F,03,63,7F,19,30,75,46,00,00,09,00,10,27,5C,C1,4F,AB,80,16,64,00,78,00,45,45,46,45,00,00,00,00,00,00,00,00,08,40,ED,0F,F0,0F,F0,0F,F0,0F,F0,0F,ED,0F,EE,0F,F0,0F,ED,0F,EF,0F,ED,0F,F0,0F,F0,0F,F0,0F,EF,0F,F7,0F,04,63,7D,19,2B,75,46,00,00,EF,00,10,1B,7C,EB,BB,88,80,16,64,00,78,00,46,45,47,45,00,00,00,00,00,00,00,00,9B,40,04,10,88,0F,FF,0F,FE,0F,00,10,00,10,C9,0F,01,10,FE,0F,F0,0F,FF,0F,B8,0F,FF,0F,FF,0F,01,10,EC,0F,05,64,77,19,30,75,45,00,00,32,00,10,24,54,E9,D4,AB,80,16,5C,00,78,00,45,45,45,45,00,00,00,00,00,00,00,00,08,40,E9,0F,EC,0F,EC,0F,EC,0F,EA,0F,E9,0F,E9,0F,EA,0F,E9,0F,E9,0F,EA,0F,EC,0F,EC,0F,EB,0F,EC,0F,EF,0F,08,63,20,19,30,75,46,00,00,26,01,10,1B,7D,3A,E9,AE,80,16,64,00,78,00,47,46,47,45,00,00,00,00,00,00,00,00,88,40,F3,0F,70,0F,FF,0F,C3,0E,01,10,00,10,FE,0F,00,10,7F,0F,FB,0F,7A,0F,11,0F,FE,0F,5C,0F,B9,0F,04,10,09,63,3A,19,30,75,48,00,00,29,01,10,1B,7C,EE,79,95,80,16,64,00,78,00,48,47,49,46,00,00,00,00,00,00,00,00,98,40,04,10,FD,0F,B4,0F,FE,0F,B6,0F,70,0F,C3,0F,CB,0F,AE,0F,FE,0F,BD,0F,FD,0F,C3,0F,FE,0F,51,0F,65,0F,0A,64,7A,19,30,75,45,00,00,96,01,10,14,4B,D9,B1,5E,80,16,59,00,78,00,45,46,46,45,00,00,00,00,00,00,00,00,08,40,EE,0F,EE,0F,ED,0F,E9,0F,ED,0F,ED,0F,EB,0F,EE,0F,EA,0F,E7,0F,EF,0F,ED,0F,EC,0F,ED,0F,EB,0F,F1,0F,0B,63,82,19,30,75,46,00,00,01,01,10,1B,9C,21,15,05,80,16,62,00,78,00,46,46,46,45,00,00,00,00,00,00,00,00,08,40,EF,0F,F2,0F,F2,0F,F1,0F,F2,0F,F1,0F,EF,0F,F2,0F,EF,0F,F2,0F,F2,0F,F2,0F,F1,0F,F2,0F,F3,0F,F3,0F,0C,64,78,19,30,75,46,00,00,23,00,10,22,C2,D4,53,92,80,16,64,00,78,00,46,45,46,45,00,00,00,00,00,00,00,00,08,40,EE,0F,EB,0F,EB,0F,EC,0F,EB,0F,EC,0F,EA,0F,EE,0F,E6,0F,EC,0F,EB,0F,EA,0F,E9,0F,EA,0F,E9,0F,F0,0F]";
    public static void main(String args[]) {

        byte data [] = hexString2Byte(str);
        if(data != null){
            System.out.println(getHexString(data));

            int minLength = 2;
            int num = byteToInt(data[0]);
            int singleLen = byteToInt(data[1]);
            if(data.length < minLength + num*singleLen){
                System.out.println("���ݳ���������");

            }else {
                List<BatteryInfo> batteries = new ArrayList<>();
                for(int i = 0; i < num; i++){
                    BatteryInfo info = new BatteryInfo();
                    byte[] infoData = new byte[singleLen];
                    System.arraycopy(data,minLength+i*singleLen, infoData, 0, singleLen);
                    info.parseData(infoData);
                    batteries.add(info);
                }

                System.out.println(batteries.size());

                System.out.println(batteries.get(10).getBv7());
            }
        }
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
    public static int byteToInt(byte b) {
        return (b & 0xFF);
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
    public static String getHexString(byte data[]){
        String hex = Integer.toHexString(data[0] & 0xFF).toUpperCase();
        if (hex.length() == 1) {
            hex = '0' + hex;
        }
        String str = "[" + hex;
        for(int i = 1; i < data.length; i++){
            hex = Integer.toHexString(data[i] & 0xFF).toUpperCase();
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            str += ","+hex;
        }
        str +="]";
        return  str;
    }
    static class BatteryInfo{
        public static final String INVALID_ID = "ffffffffffff";
        int port;    //unsigned
        int soc;     //unsigned����
        int voltage;//unsigned��ѹ
        short current;
        int temperature;//����¶�1
        int fault; //unsigned
        int damage; //unsigned
        int cycle; //unsigned
        String id; //unsigned 6λ
        int nominalVoltage; //unsigned
        short nominalCurrent;
        int designCapacity; //unsigned
        int soh;   //�����̶�
        int fgt;//�������¶�
        int bt2;//����¶�2
        int cmt;//���mos�¶�
        int ct;//�������¶�
        short df1;//����豸����1
        short df2;//����豸����2
        short of1;//������й���1
        short of2;//������й���2
        short bs;//�������״̬
        short bv0;//��0�ڵ�о��ѹ
        short bv1;//��1�ڵ�о��ѹ
        short bv2;//��2�ڵ�о��ѹ
        short bv3;//��3�ڵ�о��ѹ
        short bv4;//��4�ڵ�о��ѹ
        short bv5;//��5�ڵ�о��ѹ
        short bv6;//��6�ڵ�о��ѹ
        short bv7;//��7�ڵ�о��ѹ
        short bv8;//��8�ڵ�о��ѹ
        short bv9;//��9�ڵ�о��ѹ
        short bv10;//��10�ڵ�о��ѹ
        short bv11;//��11�ڵ�о��ѹ
        short bv12;//��12�ڵ�о��ѹ
        short bv13;//��12�ڵ�о��ѹ
        short bv14;//��14�ڵ�о��ѹ
        short bv15;//��15�ڵ�о��ѹ
        int reserved;//�����֤״̬������
        short tvsTemp;
        short maxVoltRate;
        short maxVoltRateInd;
        int maxTempRate;
        int maxTempRateInd;

        public short getTvsTemp() {
            return tvsTemp;
        }
        public short getMaxVoltRate() {
            return maxVoltRate;
        }

        public short getMaxVoltRateInd() {
            return maxVoltRateInd;
        }

        public int getMaxTempRate() {
            return maxTempRate;
        }

        public int getMaxTempRateInd() {
            return maxTempRateInd;
        }


        public void parseData(byte[] data){
            if(data!=null) {
                int length = data.length;
                byte[] byteShort = new byte[2];
                byte[] byteInt = new byte[4];
                byte[] byteLong = new byte[8];
                if (length > 0) {
                    port = byteToInt(data[0]);
                } else {
                    port = -1;
                }
                if (length > 1) {
                    soc = byteToInt(data[1]);
                }
                //小端
                if (length > 3) {
                    System.arraycopy(data, 2, byteShort, 0, 2);
                    voltage = bytesToInt(byteShort);
                }
                //小端
                if (length > 5) {
                    System.arraycopy(data, 4, byteShort, 0, 2);
                    current = (short) (bytesToInt(byteShort) - 30000);
                }
                if (length > 6) {
                    temperature = byteToInt(data[6]) - 40;
                }
                //按位解析
                if (length > 7) {
                    fault = byteToInt(data[7]);
                }
                //按位解析
                if (length > 8) {
                    damage = byteToInt(data[8]);
                }
                //小端
                if (length > 10) {
                    System.arraycopy(data, 9, byteShort, 0, 2);
                    cycle = bytesToInt(byteShort);
                }
                //小端
                if (length > 16) {
                    System.arraycopy(data, 11, byteLong, 0, 6);
                    id = byte2String(byteLong, 6);
                }
                //小端
                if (length > 18) {
                    System.arraycopy(data, 17, byteShort, 0, 2);
                    nominalVoltage = bytesToInt(byteShort);
                }
                if (length > 19) {
                    soh = byteToInt(data[19]);
                }
                if (length > 20) {
                    System.arraycopy(data, 19, byteShort, 0, 2);
                    nominalCurrent = (short) bytesToInt(byteShort);
                    reserved = byteToInt(data[20]);
                }
                //小端
                if (length > 22) {
                    System.arraycopy(data, 21, byteShort, 0, 2);
                    designCapacity = bytesToInt(byteShort);
                }
                if (length > 23) {
                    fgt = byteToInt(data[23]) - 40;
                }
                if (length > 24) {
                    bt2 = byteToInt(data[24]) - 40;
                }
                if (length > 25) {
                    cmt = byteToInt(data[25]) - 40;
                }
                if (length > 26) {
                    ct = byteToInt(data[26]) - 40;
                }
                //按位解析
                if (length > 28) {
                    System.arraycopy(data, 27, byteShort, 0, 2);
                    df1 = (short) bytesToInt(byteShort);
                }
                //按位解析
                if (length > 30) {
                    System.arraycopy(data, 29, byteShort, 0, 2);
                    df2 = (short) bytesToInt(byteShort);
                }//按位解析
                if (length > 32) {
                    System.arraycopy(data, 31, byteShort, 0, 2);
                    of1 = (short) bytesToInt(byteShort);
                }
                //按位解析
                if (length > 34) {
                    System.arraycopy(data, 33, byteShort, 0, 2);
                    of2 = (short) bytesToInt(byteShort);
                }
                //按位解析
                if (length > 36) {
                    System.arraycopy(data, 35, byteShort, 0, 2);
                    bs = (short) bytesToInt(byteShort);
                }
                //小端
                if (length > 38) {
                    System.arraycopy(data, 37, byteShort, 0, 2);
                    bv0 = (short) bytesToInt(byteShort);
                }
                //小端
                if (length > 40) {
                    System.arraycopy(data, 39, byteShort, 0, 2);
                    bv1 = (short) bytesToInt(byteShort);
                }
                //小端
                if (length > 42) {
                    System.arraycopy(data, 41, byteShort, 0, 2);
                    bv2 = (short) bytesToInt(byteShort);
                }
                //小端
                if (length > 44) {
                    System.arraycopy(data, 43, byteShort, 0, 2);
                    bv3 = (short) bytesToInt(byteShort);
                }
                //小端
                if (length > 46) {
                    System.arraycopy(data, 45, byteShort, 0, 2);
                    bv4 = (short) bytesToInt(byteShort);
                }
                //小端
                if (length > 48) {
                    System.arraycopy(data, 47, byteShort, 0, 2);
                    bv5 = (short) bytesToInt(byteShort);
                }
                //小端
                if (length > 50) {
                    System.arraycopy(data, 49, byteShort, 0, 2);
                    bv6 = (short) bytesToInt(byteShort);
                }
                //小端
                if (length > 52) {
                    System.arraycopy(data, 51, byteShort, 0, 2);
                    bv7 = (short) bytesToInt(byteShort);
                }
                if (length > 54) {
                    System.arraycopy(data, 53, byteShort, 0, 2);
                    bv8 = (short) bytesToInt(byteShort);
                }
                if (length > 56) {
                    System.arraycopy(data, 55, byteShort, 0, 2);
                    bv9 = (short) bytesToInt(byteShort);
                }
                if (length > 58) {
                    System.arraycopy(data, 57, byteShort, 0, 2);
                    bv10 = (short) bytesToInt(byteShort);
                }
                if (length > 60) {
                    System.arraycopy(data, 59, byteShort, 0, 2);
                    bv11 = (short) bytesToInt(byteShort);
                }
                if (length > 62) {
                    System.arraycopy(data, 61, byteShort, 0, 2);
                    bv12 = (short) bytesToInt(byteShort);
                }
                if (length > 64) {
                    System.arraycopy(data, 63, byteShort, 0, 2);
                    bv13 = (short) bytesToInt(byteShort);
                }
                if (length > 66) {
                    System.arraycopy(data, 65, byteShort, 0, 2);
                    bv14 = (short) bytesToInt(byteShort);
                }
                //小端
                if (length > 68) {
                    System.arraycopy(data, 67, byteShort, 0, 2);
                    bv15 = (short) bytesToInt(byteShort);
                }
                if (length>69){
                    tvsTemp = (short) byteToInt(data[68]);
                }
                //小端
                if (length>71){
                    System.arraycopy(data, 70, byteShort, 0, 2);
                    maxVoltRate= (short) bytesToInt(byteShort);
                }
                if (length>72){
                    maxVoltRateInd = (short) byteToInt(data[71]);
                }
                //小端
                if (length>74){
                    System.arraycopy(data, 73, byteShort, 0, 2);
                    maxTempRate= (short) bytesToInt(byteShort);
                }

                if (length>75){
                    maxTempRateInd = (short) byteToInt(data[74]);
                }
            }

        }

        public static String getInvalidId() {
            return INVALID_ID;
        }

        public int getPort() {
            return port;
        }

        public int getSoc() {
            return soc;
        }

        public int getVoltage() {
            return voltage;
        }

        public short getCurrent() {
            return current;
        }

        public int getTemperature() {
            return temperature;
        }

        public int getFault() {
            return fault;
        }

        public int getDamage() {
            return damage;
        }

        public int getCycle() {
            return cycle;
        }

        public String getId() {
            return id;
        }

        public int getNominalVoltage() {
            return nominalVoltage;
        }

        public short getNominalCurrent() {
            return nominalCurrent;
        }

        public int getDesignCapacity() {
            return designCapacity;
        }

        public int getSoh() {
            return soh;
        }

        public int getFgt() {
            return fgt;
        }

        public int getBt2() {
            return bt2;
        }

        public int getCmt() {
            return cmt;
        }

        public int getCt() {
            return ct;
        }

        public short getDf1() {
            return df1;
        }

        public short getDf2() {
            return df2;
        }

        public short getOf1() {
            return of1;
        }

        public short getOf2() {
            return of2;
        }

        public short getBs() {
            return bs;
        }

        public short getBv0() {
            return bv0;
        }

        public short getBv1() {
            return bv1;
        }

        public short getBv2() {
            return bv2;
        }

        public short getBv3() {
            return bv3;
        }

        public short getBv4() {
            return bv4;
        }

        public short getBv5() {
            return bv5;
        }

        public short getBv6() {
            return bv6;
        }

        public short getBv7() {
            return bv7;
        }

        public short getBv8() {
            return bv8;
        }

        public short getBv9() {
            return bv9;
        }

        public short getBv10() {
            return bv10;
        }

        public short getBv11() {
            return bv11;
        }

        public short getBv12() {
            return bv12;
        }

        public short getBv13() {
            return bv13;
        }

        public short getBv14() {
            return bv14;
        }

        public short getBv15() {
            return bv15;
        }

        public int getReserved() {
            return reserved;
        }

        public int byteToInt(byte b) {
            return (b & 0xFF);
        }

        //低位在前(小端)
        public int bytesToInt(byte[] b){
            int len = b.length;
            int result = 0;
            for(int i = 0; i < len; i++){
                result += (b[i]&0xff) << (8 * i);
            }
            return result;
        }
        //高位在前(大端)
        public int bytesToIntBig(byte[] b){
            int len = b.length;
            int result = 0;
            for(int i = 0; i < len; i++){
                result += (b[i]&0xff) << (8 * (len-i-1));
            }
            return result;
        }

        public String byte2String(byte[] buff, int size) {
            StringBuffer sbuf = new StringBuffer();
            for (int i = 0; i < size && i < buff.length; i++) {
                int tmp = buff[i] & 0XFF;
                String str = Integer.toHexString(tmp);
                if (str.length() == 1) {
                    sbuf.append("0" + str);
                } else {
                    sbuf.append(str);
                }
            }
            return sbuf.toString();
        }
    }
}
