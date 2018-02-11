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
    /**
     * 启动程序
     *
     * @param args
     * @throws SQLException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws SQLException, InterruptedException {
        //BcShrO8gVPAhutauLVVQYHdFdqmdIXfM
        //多个表
        String[] tables = {"USER_CBJF", "USER_GDZL", "USER_GZBX", "USER_TSJB", "USER_YKBZ_DY", "USER_YKBZ_GY", "USER_YYTFW"};
        for (String table : tables) {
            //获取表中所有的城市《数据去重》
            String[] citys = getCity(table);
            TCityLocationEntity2[] entity2s = new TCityLocationEntity2[citys.length];
            //得到每一个地点的经纬度，距离信息
            for (int i = 0; i < citys.length; i++) {
                //得到经纬度
                entity2s[i] = getLatAndLngByAddress(citys[i]);
                //得到距离
                getDist(entity2s[i]);
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
    public static String[] getCity(String table) throws SQLException {
        List<String> list = new ArrayList<>();
        Connection con = DataSource.getCon();
        PreparedStatement pre;
        ResultSet result;
        String sql;
        if (table.equals("USER_GZBX")) {
            sql = "SELECT DISTINCT ORG FROM USER_GZBX";
        } else {
            sql = "SELECT DISTINCT CITY FROM " + table;
        }
        pre = con.prepareStatement(sql);
        result = pre.executeQuery();
        while (result.next()) {
            list.add(result.getString(1));
        }
        result.close();
        pre.close();
        con.close();
        return list.toArray(new String[0]);
    }

    /**
     * 获取地点经纬度信息
     *
     * @param addr
     * @return
     */
    public static TCityLocationEntity2 getLatAndLngByAddress(String addr) {
        TCityLocationEntity2 entity = null;
        String address = "";
        String ak = "BcShrO8gVPAhutauLVVQYHdFdqmdIXfM";
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
                    //查询的地点名称
                    entity.setCity(addr);
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
     * 计算距离
     *
     * @param entity2
     */
    private static void getDist(TCityLocationEntity2 entity2) {

    }


    /**
     * 更新数据库
     *
     * @param entity2s
     * @param table
     * @throws SQLException
     */
    public static void updata(TCityLocationEntity2[] entity2s, String table) throws SQLException {
        System.out.println("更新数据库");
        int total = 0;
        Connection con = DataSource.getCon();
        PreparedStatement pre;
        String sql;
        System.out.println("共：" + entity2s.length);
        if (table.equals("USER_GZBX")) {
            sql = "UPDATE USER_GZBX SET ADDR_CODE = ? , LNG = ? , LAT = ? ,DIST_CT = ? WHERE ORG = ?";
        } else {
            sql = "UPDATE " + table + " SET ADDR_CODE = ? , LNG = ? , LAT = ? ,DIST_CT = ? WHERE CITY = ?";
        }
        for (TCityLocationEntity2 entity2 : entity2s) {
            pre = con.prepareStatement(sql);
            pre.setString(1, entity2.getCode());
            pre.setDouble(2, entity2.getLng());
            pre.setDouble(3, entity2.getLat());
            pre.setDouble(4, entity2.getDist());
            pre.setString(5, entity2.getCity());
            if (pre.executeUpdate() >= 1) total++;
            pre.close();
        }
        System.out.println("成功：" + total);
        con.close();
    }
}
