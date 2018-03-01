package com.hill.gwyb.api.maputil;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * 数据库连接类
 */
public class DataSource {
    public static Connection getCon() {
        Connection con = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String url = "jdbc:oracle:thin:@114.215.193.237:21521:orcl";
            String user = "guowang_data";
            String password = "guowanghill110";
            con = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }
}
