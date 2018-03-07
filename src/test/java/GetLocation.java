
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.net.URL;



public class GetLocation {
    public static void main(String[] args) {
//        String rel="{\"queryLocation\":[39.3657152161418,116.78308356294202],\"addrList\":[{\"type\":\"poi\",\"status\":1,\"name\":\"马神庙村\",\"id\":\"ANB013C155HB\",\"admCode\":\"131002\",\"admName\":\"河北省,廊坊市,安次区,\",\"addr\":\"\",\"nearestPoint\":[116.78545,39.36153],\"distance\":480.513}]}";
//        JSONObject json=JSONObject.parseObject(rel);
//        JSONArray jray=json.getJSONArray("addrList");
//        json=jray.getJSONObject(0);
//        if("1".equals(json.getString("status"))){
//            System.out.println(json.getString("admName").split(",",3)[0]);
//        }
        System.out.println(getAdd(121.52296694600652,31.219266513984053));
    }

    public static String getAdd(Double log, Double lat) {
        //lat 小  log  大
        //参数解释: 纬度,经度 type 001 (100代表道路，010代表POI，001代表门址，111可以同时显示前三项)
        String urlString = "http://gc.ditu.aliyun.com/regeocoding?l=" + lat + "," + log + "&type=010";
        String res = "";
        try {
            URL url = new URL(urlString);
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                res += line + "\n";
            }
            in.close();
        } catch (Exception e) {
            System.out.println("error in wapaction,and e is " + e.getMessage());
        }
        return res;
    }

}
