package util;

import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class GeoUtil {
    private static final String KEY = "8e523e0889f6ae0bca486daf238e585c";
    private static final String OUTPUT = "JSON";
    private static final String GET_LNG_LAT_URL = "https://restapi.amap.com/v3/geocode/geo";
    private static final String GET_ADDR_FROM_LNG_LAT = "https://restapi.amap.com/v3/geocode/regeo";
    private static final String EXTENSIONS_ALL = "all";
    //31.543297 120.417532 江苏省无锡市新吴区满天星文化艺术培训中心(梅村基地)梅村二胡文化园
    public static void main(String[] args) throws MalformedURLException {
        System.out.println( getAddrFromLngLat("31.542197","120.430834"));
    }
    /**
     *
     * @description 根据经纬度查地址
     * @param lng：经度，lat：纬度
     * @return 地址
     * @author jxp
     * @date 2017年7月12日
     */
    public static String getAddrFromLngLat(String lng, String lat) throws MalformedURLException {

        BufferedReader in = null;
        URL tirc = new URL(""+GET_ADDR_FROM_LNG_LAT+"?location=" + lat + "," + lng + "&key="+KEY+"");
        //System.out.println("url="+tirc);
        try {
          //  System.out.println("sb0");
            System.out.println(tirc.openStream());
            System.out.println(new InputStreamReader(tirc.openStream(), "UTF-8"));
            in = new BufferedReader(new InputStreamReader(tirc.openStream(), "UTF-8"));
          //  System.out.println("sb00");

            String res;
            StringBuilder sb = new StringBuilder("");
            System.out.println("sb1");
            while ((res = in.readLine()) != null) {
              //  System.out.println(res.trim());
                sb.append(res.trim());
            }
          //  System.out.println("sb2");
            String result = sb.toString();
          //  System.out.println("result="+result);
            // String result = HttpclientUtil.post(params, GET_ADDR_FROM_LNG_LAT);
            net.sf.json.JSONObject json = net.sf.json.JSONObject.fromObject(result);
            String status = json.getString("status");
            String address = null;
            if ("1".equals(status)) {
                JSONObject regeocode = JSONObject.fromObject(json.get("regeocode"));
                address = regeocode.getString("formatted_address");
            }
            return address;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
