package com.hill.gwyb.api.maputil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class GeocodingTools2 {
    public static Map<String, Object> getLatAndLngByAddress(String addr, String ak) {
        Map<String, Object> map = new HashMap<>();
        TCityLocationEntity2 entity = null;
        String address = "";
        try {
            address = java.net.URLEncoder.encode(addr, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        String url = String.format("http://api.map.baidu.com/geocoder/v2/?ak=" + ak + "&output=json&address=%s", address);
        URL myURL = null;
        URLConnection httpsConn;
        // 进行转码
        try {
            myURL = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            assert myURL != null;
            httpsConn = myURL.openConnection();
            if (httpsConn != null) {
                InputStreamReader insr = new InputStreamReader(httpsConn.getInputStream(), "UTF-8");
                BufferedReader br = new BufferedReader(insr);
                String data;
                if ((data = br.readLine()) != null) {
//                    System.out.println("百度地图api返回数据：" + data);
                    JSONObject object = JSON.parseObject(data);
                    entity = new TCityLocationEntity2();
                    map.put("status", object.getString("status"));
                    if (object.getString("status").equals("0")) {
                        entity.setCode("1");
                        object = object.getJSONObject("result").getJSONObject("location");
                        entity.setLng(object.getDouble("lng"));
                        entity.setLat(object.getDouble("lat"));
                    } else {
                        entity.setCode("0");
                        if (object.getString("status").equals("302")) {
                            entity = null;
                        }
                    }
                }
                insr.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        map.put("entity", entity);
        return map;
    }

    public static String getAddressByLocation(Double log, Double lat) {
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
            e.printStackTrace();
        }
        return res;
    }
}