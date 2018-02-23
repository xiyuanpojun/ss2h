
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class GeocodingTools2 {
    public static void main(String[] args) throws Exception {
        //定时器执行在距离凌晨后
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2018-02-23 00:01:00");
        long time = date.getTime() - System.currentTimeMillis();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                try {
                    GeocodingTools2.distUpdate();
                } catch (SQLException | IOException e) {
                    e.printStackTrace();
                }
                this.cancel();
                timer.cancel();
            }
        }, time);
        System.out.println(time / 60 / 60 / 1000);
    }

    // 修改数据库用户档案表的LNG,LAT,DIST-CT属性
    public static void distUpdate() throws SQLException, IOException {
        // 多个表
        String[] tables = {"USER_CBJF", "USER_GDZL", "USER_GZBX", "USER_YKBZ_DY", "USER_YKBZ_GY", "USER_YYTFW", "USER_TSJB"};
        for (String table : tables) {
            // 修改地址
            update(table);
        }
    }
    /**
     * 获取数据库中的所有地点
     *
     * @param table
     * @return
     * @throws SQLException
     */
    private static void update(String table) throws SQLException, IOException {
        Connection con = DataSource.getCon();
        PreparedStatement pre, pre2;
        ResultSet result;
        String sql, sql2;
        switch (table) {
            case "USER_GZBX":
                sql = "SELECT ORG,PROV,YDDZ FROM USER_GZBX WHERE ADDR_CODE IS NULL OR ADDR_CODE= '0'";
                sql2 = "UPDATE USER_GZBX SET ADDR_CODE = ? , LNG = ? , LAT = ? ,DIST_CT = ? WHERE YDDZ = ?";
                break;
            case "USER_TSJB":
                sql = "SELECT CITY,PROV,YJRDZ FROM " + table + " WHERE ADDR_CODE IS NULL OR ADDR_CODE= '0'";
                sql2 = "UPDATE USER_GZBX SET ADDR_CODE = ? , LNG = ? , LAT = ? ,DIST_CT = ? WHERE YJRDZ = ?";
                break;
            default:
                sql = "SELECT CITY,PROV,YDDZ FROM " + table + " WHERE ADDR_CODE IS NULL OR ADDR_CODE= '0'";
                sql2 = "UPDATE " + table + " SET ADDR_CODE = ? , LNG = ? , LAT = ? ,DIST_CT = ? WHERE YDDZ = ?";
                break;
        }
        pre = con.prepareStatement(sql);
        result = pre.executeQuery();
        while (result.next()) {
            TCityLocationEntity2 entity2 = getLatAndLngByAddress(result.getString(2) + "-" + result.getString(1) + "-" + result.getString(3));
            // 获得市中心的经纬度
            TCityLocationEntity2 centralcity = getLatAndLngByAddress(result.getString(2) + "-" + result.getString(1));
            if (entity2 == null) {
                result.close();
                pre.close();
                con.close();
                System.out.println("请求配额已用完");
                return;
            }
            // 计算地址到市中心距离
            Double distance = MapUtil.getDistance(entity2.getLng(), entity2.getLat(), centralcity.getLng(), centralcity.getLat());
            pre2 = con.prepareStatement(sql2);
            pre2.setString(1, entity2.getCode());
            pre2.setDouble(2, entity2.getLng());
            pre2.setDouble(3, entity2.getLat());
            pre2.setDouble(4, distance);
            pre2.setString(5, result.getString(3));
            if (pre2.executeUpdate() >= 1) {
                System.out.println("更新成功" + entity2.toString());
            } else {
                System.out.println("更新失败" + entity2.toString());
            }
            pre2.close();
        }
        result.close();
        pre.close();
        con.close();
    }

    /**
     * 获取地点经纬度信息
     *
     * @param addr
     * @return
     */
    private static TCityLocationEntity2 getLatAndLngByAddress(String addr) {
        TCityLocationEntity2 entity = null;
        String address = "";
        String ak = "xh2XppvAc5uB36HDZHKTOMnV3gSUULTb";
        try {
            address = java.net.URLEncoder.encode(addr, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        String url = String.format("http://api.map.baidu.com/geocoder/v2/?ak=" + ak + "&output=json&address=%s",
                address);
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
                    JSONObject object = JSON.parseObject(data);
                    entity = new TCityLocationEntity2();
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
        return entity;
    }
}