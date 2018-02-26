package com.hill.gwyb.service.impl;

import com.hill.gwyb.api.GetConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class GwUserInsert {
    private Connection con = null;

    public GwUserInsert() {
        try {
            con = new GetConnection().getCon();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private String[] getSql(String zxName) {
        String[] sql = new String[]{"", ""};
        int zdCd = 0;
        switch (zxName) {
            case "1":
                sql[0] = "INSERT INTO USER_GDZL(XH,PROV,CITY,ORG,YHBH,YHMC,YDDZ,LXR,TEL,PHONE,YDLB,HTRL,DYDJ,CXBZ)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                sql[1] = "14";
                break;
            case "2":
                sql[0] = "INSERT INTO USER_YYTFW(XH,PROV,CITY,ORG,YHBH,YHMC,YDDZ,LXR,TEL,PHONE,YDLB,DYDJ)VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
                sql[1] = "12";
                break;
            case "3":
                sql[0] = "INSERT INTO USER_CBJF(XH,PROV,CITY,ORG,YHBH,YHMC,HYFL,YDDZ,LXR,TEL,PHONE,YDLB,DYDJ,CXBZ)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                sql[1] = "14";
                break;
            case "4":
                sql[0] = "INSERT INTO USER_TSJB(XH,GDBH,FDSJ,JDSJ,GDZT,CITY,COUNTRY,PROV,CITY_ORG,ORG,SLGH,SLRY,YJRXM,YJRDZ,YJRDH,SLNR,YJJBLX,EJJBLX,CLNR,SFCS,BJSJ,HFMYD,HFSJ,HFZT,SFCDB,HBGDBH,SFTD,SFSS,SFGDGSZR,CXBZ,GLGDBH)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                sql[1] = "31";
                break;
            case "5":
                sql[0] = "INSERT INTO USER_GZBX(XH,GDBH,YHMC,YHBH,YDLB,YDDZ,LXR,TEL,CXBZ,PROV,CITY,COUNTRY,ORG,SLSJ,PFSJ,YJGZLX,EJGZLX,GZXX,GZBXJJD,SLNR,JDSJ,PDSJ,DDXCSJ,GZXFSJ,YJYY,EJYY,SBCQSX)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                sql[1] = "27";
                break;
            case "6":
                sql[0] = "INSERT INTO USER_YKBZ_DY(XH,PROV,CITY,ORG,YHBH,YHMC,HYFL,YDDZ,LXR,TEL,PHONE,YDLB,HTRL,DYDJ,CXBZ,YWLX,SLSJ,GDSJ,SDSJ)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                sql[1] = "19";
                break;
            case "100":
                sql[0] = "INSERT INTO SURVEY_PHONE (CITY, COUNTRY, CUST_TYPE, PROV, SURVEY_RS, PHONE)VALUES(?,?,?,?,?,?)";
                sql[1] = "6";
                break;
            default:
                sql[0] = "INSERT INTO USER_YKBZ_GY(XH,PROV,CITY,ORG,YHBH,YHMC,HYFL,YDDZ,LXR,TEL,PHONE,YDLB,HTRL,DYDJ,CXBZ,YWLX,SLSJ,GDSJ,GDFADFKSSJ,GDFADFWCSJ,SJTZSHKSSJ,SJTZSHWCSJ,ZJJCKSSJ,ZJJCWCSJ,JGYSKSSJ,JGYSWCSJ,ZBJDKSSJ,ZBJDWCSJ,SDSJ)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                sql[1] = "29";
                break;
        }
        ;
        return sql;
    }

    /**
     * 插入答卷
     *
     * @param li
     */
    public void doInsert(List<List> li, String zxName) {
        try {
            if (null == con) {
                try {
                    con = new GetConnection().getCon();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            con.setAutoCommit(false);
            int len = li.size();
            String[] sql = getSql(zxName);
            int q = 0;
            int siz = 0;
            PreparedStatement pre = con.prepareStatement(sql[0]);
            for (int i = 0; i < len; i++) {
                siz = li.get(i).size();
                if (siz > Integer.parseInt(sql[1])) {
                    siz = Integer.parseInt(sql[1]);
                }
                for (int j = 0; j < siz; j++) {
                    pre.setString(j + 1, (li.get(i).get(j) + "").replace("'", ""));
                }
                for (int j = siz; j < Integer.parseInt(sql[1]); j++) {
                    pre.setString(j + 1, "");
                }
                pre.addBatch();
                q++;
                if (q > 5000) {
                    pre.executeBatch();
                    q = 0;
                }
                if (i % 200 == 0) {
                    con.commit();
                }
            }
            pre.executeBatch();
            con.commit();
            pre.close();
            con.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}