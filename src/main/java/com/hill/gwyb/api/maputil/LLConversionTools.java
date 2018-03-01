package com.hill.gwyb.api.maputil;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public final class LLConversionTools {
    private LLConversionTools() {
    }

    public static List<TCityLocationEntity> conversion() throws IOException {
        StringBuilder sb = new StringBuilder();
        String str;
        BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/BaiduMap_cityCenter.txt"));
        while ((str = reader.readLine()) != null) {
            sb.append(str);
        }
        reader.close();
        str = sb.toString();
        //开始拼装字符串，使之成为一个数组。
        str = str.replace("{municipalities:", "");
        str = str.replace("],provinces:[", ",");
        str = str.replace(",cities:[", "},");
        str = str.replace("]}", "");
        str = str.replace("],other:[", ",");
        str = str.replace("};", "}]");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        List<TCityLocationEntityTemp> list = mapper.readValue(str, new TypeReference<List<TCityLocationEntityTemp>>() {
        });
        List<TCityLocationEntity> result = new ArrayList<>();
        String[] address;
        for (TCityLocationEntityTemp temp : list) {
            TCityLocationEntity entity = new TCityLocationEntity();
            entity.setCity(temp.getN());
            address = temp.getG().split(",", 2);
            entity.setLng(Double.valueOf(address[0]));//经度
            entity.setLat(Double.valueOf(address[1].split("\\|", 2)[0]));//纬度
            result.add(entity);
        }
        return result;
    }

    public static void main(String[] args) throws IOException {
        List<TCityLocationEntity> list = LLConversionTools.conversion();
        System.out.println(list.size());
        Connection con = null;
        PreparedStatement pre = null;
        ResultSet result = null;
        int total;
        int index = 0;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String url = "jdbc:oracle:thin:@114.215.193.237:21521:orcl";
            String user = "guowang_data";
            String password = "guowanghill110";
            String sql;
            con = DriverManager.getConnection(url, user, password);
            for (TCityLocationEntity entity : list) {
                sql = "INSERT INTO T_CITY_LOCATION (CITY, LNG, LAT) VALUES (?,?,?)";
                pre = con.prepareStatement(sql);
                pre.setString(1, entity.getCity());
                pre.setDouble(2, entity.getLng());
                pre.setDouble(3, entity.getLat());
                total = pre.executeUpdate();
                if (total == 1) {
                    index++;
                    System.out.println("插入成功" + entity.toString());
                } else {
                    System.out.println("插入失败" + entity.toString());
                }
                try {
                    pre.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("共插入" + index + "数据");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class TCityLocationEntityTemp {
    private String n;
    private String g;

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public String getG() {
        return g;
    }

    public void setG(String g) {
        this.g = g;
    }

    @Override
    public String toString() {
        return "TCityLocationEntityTemp{" +
                "n='" + n + '\'' +
                ", g='" + g + '\'' +
                '}';
    }
}
