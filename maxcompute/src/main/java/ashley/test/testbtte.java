package ashley.test;

/**
 * Created by AshleyZHANG on 2019/9/16.
 */
public class testbtte {
    public static void main(String[] args) {
        byte b = (byte) 2;
        byte b2 = 3;
        System.out.println(b+"he"+b2);
        System.out.println( (byte)((b >> 7) & 0x1) +""+
                (byte)((b >> 6) & 0x1) +""+
                (byte)((b >> 5) & 0x1) +""+
                (byte)((b >> 4) & 0x1) +""+
                (byte)((b >> 3) & 0x1) +""+
                (byte)((b >> 2) & 0x1) +""+
                (byte)((b >> 1) & 0x1) +""+
                (byte)((b >> 0) & 0x1) +""+
                (byte)((b2 >> 7) & 0x1) +""+
                (byte)((b2 >> 6) & 0x1) +""+
                (byte)((b2 >> 5) & 0x1) +""+
                (byte)((b2 >> 4) & 0x1) +""+
                (byte)((b2 >> 3) & 0x1) +""+
                (byte)((b2 >> 2) & 0x1) +""+
                (byte)((b2 >> 1) & 0x1) +""+
                (byte)((b2 >> 0) & 0x1)) ;


        String s1="02";
        String s2="03";

        //倒序 排??
        String s106=Integer.toBinaryString(Integer.parseInt(s1+s2, 16));
        System.out.println(s106);
        for(int i=0;i<s106.length();i++){
            System.out.println(Integer.parseInt(s106.substring(s106.length()-1-i, s106.length()-i)));
        }
//        if(s56.length()<16){
//            for(int j=0;j<16-s106.length();j++){
//                one.set(s106.length()+142+j,0);
//            }
        String hex = Integer.toBinaryString(02);
        System.out.println(hex);

        byte[] bytes={02,03};
        testbtte testbtte=new testbtte();
        System.out.println(testbtte.bytesToInt(bytes));

        }



    public int bytesToInt(byte[] b){
        int len = b.length;
        int result = 0;
        for(int i = 0; i < len; i++){
            result += (b[i]&0xff) << (8 * i);
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
