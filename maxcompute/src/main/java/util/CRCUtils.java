package util;

public class CRCUtils {
    public   char  updateCrc(byte ch, char lpwCrc)
    {
    	  ch =(byte) (ch^(byte)((lpwCrc) & 0x00FF));
    	  ch = (byte)(ch^(ch<<4));
    	  int ch1=ch&0xff;
    	  lpwCrc = (char) ((char)(lpwCrc >> 8)^(char)((char)ch1 << 8)^(char)((char)ch1<<3)^((char)ch1>>4));
    	  return lpwCrc;
    }
    
   public  boolean ComputeCrc(int CRCType,String[] datas, int Length,byte TransmitFirst, byte TransmitSecond)
   {

	    char wCrc;
	    

	    if(Length == 0)
	    {
	        return false;
	    }
        wCrc = 0x6363; 
        for(int i=0;i<datas.length-2;i++){
        	wCrc=updateCrc((byte)(Integer.parseInt(datas[i],16)),wCrc);
        }
	    TransmitFirst = (byte) (wCrc & 0xFF);
	    TransmitSecond = (byte) ((wCrc >> 8) & 0xFF);
	    if((TransmitFirst==(byte)(Integer.parseInt(datas[137],16)))&&(TransmitSecond==(byte)(Integer.parseInt(datas[138],16)))){
	    	return true;
	    }

     	return false;
	}
   public static void main(String[] args) {
	 String strs="5E,00,00,00,18,07,09,20,08,39,80,00,00,00,0C,FF,FF,FF,01,00,0A,03,0C,17,F7,74,00,02,E2,00,02,02,DF,00,01,0F,12,00,07,0E,D3,00,0E,0B,B8,02,58,03,E8,00,02,27,1B,32,82,FF,FF,16,D6,00,00,00,00,00,00,00,00,00,00,00,00,02,FA,02,FA,02,DE,02,D1,02,DF,02,E2,0F,0B,0E,EB,0F,00,0F,0A,0F,04,0F,04,0F,12,0E,E6,0E,E8,0F,0C,0E,EB,0E,FA,0F,10,0E,D3,0E,DC,0F,12,00,00,00,00,00,00,00,00,00,03,00,E3,79";
     String[] strsSplit=strs.split(",");
     CRCUtils crcUtils=new CRCUtils();
     Byte b1=0;
     Byte b2=0;
     crcUtils.ComputeCrc(1, strsSplit, 128, b1, b2);
     System.out.println(b1+":"+b2);
   }

}
