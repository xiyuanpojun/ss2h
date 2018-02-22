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
import java.util.ArrayList;
import java.util.List;

/**
 * json消息使用fastjson解析。
 */
public class GeocodingTools {
    //修改数据库用户档案表的LNG,LAT,DIST-CT属性
    public static void begin() throws SQLException {
        //多个表
        String[] tables = {"USER_CBJF", "USER_GDZL", "USER_GZBX", "USER_YKBZ_DY", "USER_YKBZ_GY", "USER_YYTFW", "USER_TSJB"};
        for (String table : tables) {
            //获取表中所有的地址去重
            TCityLocationEntity[] adress = getAdress(table);
            TCityLocationEntity2[] entity2s = new TCityLocationEntity2[adress.length];
            //得到每一个地点的经纬度，距离信息
            for (int i = 0; i < adress.length; i++) {
                //由于有些地址没有省市所以拼接完整地址
                entity2s[i] = getLatAndLngByAddress(adress[i].getProvince() + "-" + adress[i].getCity() + "-" + adress[i].getAdress());
                //获得市中心的经纬度
                TCityLocationEntity centralcity = getLatAndLngByAddress(adress[i].getProvince() + "-" + adress[i].getCity());
                centralcity.setCity(adress[i].getCity());
                //计算地址到市中心距离
                Double distance = MapUtil.getDistance(entity2s[i].getLng(), entity2s[i].getLat(), centralcity.getLng(), centralcity.getLat());
                //保存地址到市中心的距离(DIST-CT)
                entity2s[i].setAdress(adress[i].getAdress());
                entity2s[i].setDist(distance);
            }
            //更新数据库的内容
            updata(entity2s, table);
        }
    }

    /**
     * 获取数据库中的所有地点
     *
     * @param table
     * @return
     * @throws SQLException
     */
    private static TCityLocationEntity[] getAdress(String table) throws SQLException {
        List<TCityLocationEntity> cplist = new ArrayList<>();
        Connection con = DataSource.getCon();
        PreparedStatement pre;
        ResultSet result;
        String sql;
        if (table.equals("USER_GZBX")) {
            sql = "SELECT ORG,PROV,YDDZ FROM USER_GZBX WHERE ADDR_CODE IS NULL OR ADDR_CODE= '0'";
        } else if (table.equals("USER_TSJB")) {
            sql = "SELECT CITY,PROV,YJRDZ FROM " + table + " WHERE ADDR_CODE IS NULL OR ADDR_CODE= '0'";
        } else {
            sql = "SELECT CITY,PROV,YDDZ FROM " + table + " WHERE ADDR_CODE IS NULL OR ADDR_CODE= '0'";
        }
        pre = con.prepareStatement(sql);
        result = pre.executeQuery();
        while (result.next()) {
            TCityLocationEntity clentity = new TCityLocationEntity();
            clentity.setCity(result.getString(1));
            clentity.setProvince(result.getString(2));
            clentity.setAdress(result.getString(3));
            cplist.add(clentity);
            System.out.println("当前地址数" + cplist.size());
        }
        result.close();
        pre.close();
        con.close();
        return cplist.toArray(new TCityLocationEntity[0]);
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
        String ak = "bsrU3l7k7oG04yHw3BTQ8UAG5bokrfM0";
        try {
            address = java.net.URLEncoder.encode(addr, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        String url = String.format("http://api.map.baidu.com/geocoder/v2/?ak=" + ak + "&output=json&address=%s", address);
        URL myURL = null;
        URLConnection httpsConn;
        //进行转码
        try {
            myURL = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            assert myURL != null;
            httpsConn = myURL.openConnection();
            if (httpsConn != null) {
                InputStreamReader insr = new InputStreamReader(
                        httpsConn.getInputStream(), "UTF-8");
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
                    }
                }
                insr.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return entity;
    }

    /**
     * 更新数据库
     *
     * @param entity2s
     * @param table
     * @throws SQLException
     */
    private static void updata(TCityLocationEntity2[] entity2s, String table) throws SQLException {
        System.out.println("更新数据库");
        int total = 0;
        Connection con = DataSource.getCon();
        PreparedStatement pre;
        String sql;
        System.out.println("共：" + entity2s.length);
        if (table.equals("USER_TSJB")) {
            sql = "UPDATE USER_GZBX SET ADDR_CODE = ? , LNG = ? , LAT = ? ,DIST_CT = ? WHERE YJRDZ = ?";
        } else {
            sql = "UPDATE " + table + " SET ADDR_CODE = ? , LNG = ? , LAT = ? ,DIST_CT = ? WHERE YDDZ = ?";
        }
        for (TCityLocationEntity2 entity2 : entity2s) {
            pre = con.prepareStatement(sql);
            pre.setString(1, entity2.getCode());
            pre.setDouble(2, entity2.getLng());
            pre.setDouble(3, entity2.getLat());
            pre.setDouble(4, entity2.getDist());
            pre.setString(5, entity2.getAdress());
            if (pre.executeUpdate() >= 1) total++;
            pre.close();
        }
        System.out.println("成功：" + total);
        con.close();
    }

    //获取市经纬度
    private static TCityLocationEntity getLatAndLngByprovince(String province) throws SQLException {
        TCityLocationEntity clentity = new TCityLocationEntity();
        Connection conn = DataSource.getCon();
        PreparedStatement ps = conn.prepareStatement("SELECT CITY,LNG,LAT FROM T_CITY_LOCATION WHERE CITY = ?");
        ps.setString(1, province);
        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()) {
            clentity.setCity(resultSet.getString(1));
            clentity.setLng(resultSet.getDouble(2));
            clentity.setLat(resultSet.getDouble(3));
        }
        resultSet.close();
        ps.close();
        conn.close();
        return clentity;
    }
}

class test {
    /**
     * 启动程序
     *
     * @param args
     * @throws SQLException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws SQLException {
        GeocodingTools.begin();
    }
}