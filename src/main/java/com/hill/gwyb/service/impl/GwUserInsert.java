package com.hill.gwyb.service.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class GwUserInsert {
    private Connection con = null;
    private HttpServletRequest request;

    public GwUserInsert(HttpServletRequest request) {
        this.request = request;
    }

    private String[] getSql(String zxName) {
        String[] sql = new String[]{"", ""};
        int zdCd = 0;
        switch (zxName) {
            case "USER_GDZL":
                sql[0] = "INSERT INTO USER_GDZL(XH,PROV,CITY,ORG,YHBH,YHMC,YDDZ,LXR,TEL,PHONE,YDLB,HTRL,DYDJ,CXBZ)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                sql[1] = "14";
                break;
            case "USER_YYTFW":
                sql[0] = "INSERT INTO USER_YYTFW(XH,PROV,CITY,ORG,YHBH,YHMC,YDDZ,LXR,TEL,PHONE,YDLB,DYDJ)VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
                sql[1] = "12";
                break;
            case "USER_CBJF":
                sql[0] = "INSERT INTO USER_CBJF(XH,PROV,CITY,ORG,YHBH,YHMC,HYFL,YDDZ,LXR,TEL,PHONE,YDLB,DYDJ,CXBZ)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                sql[1] = "14";
                break;
            case "USER_TSJB":
                sql[0] = "INSERT INTO USER_TSJB(XH,GDBH,FDSJ,JDSJ,GDZT,CITY,COUNTRY,PROV,CITY_ORG,ORG,SLGH,SLRY,YJRXM,YJRDZ,YJRDH,SLNR,YJJBLX,EJJBLX,CLNR,SFCS,BJSJ,HFMYD,HFSJ,HFZT,SFCDB,HBGDBH,SFTD,SFSS,SFGDGSZR,CXBZ,GLGDBH)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                sql[1] = "31";
                break;
            case "USER_GZBX":
                sql[0] = "INSERT INTO USER_GZBX(XH,GDBH,YHMC,YHBH,YDLB,YDDZ,LXR,TEL,CXBZ,PROV,CITY,COUNTRY,ORG,SLSJ,PFSJ,YJGZLX,EJGZLX,GZXX,GZBXJJD,SLNR,JDSJ,PDSJ,DDXCSJ,GZXFSJ,YJYY,EJYY,SBCQSX)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                sql[1] = "27";
                break;
            case "USER_YKBZ_DY":
                sql[0] = "INSERT INTO USER_YKBZ_DY(XH,PROV,CITY,ORG,YHBH,YHMC,HYFL,YDDZ,LXR,TEL,PHONE,YDLB,HTRL,DYDJ,CXBZ,YWLX,SLSJ,GDSJ,SDSJ)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                sql[1] = "19";
                break;
            case "USER_RGFW":
                sql[0] = "INSERT INTO USER_RGFW(YHBH,YHMC,TEL,PHONE,CITY,CXBZ,SEX,PROV,KHLB)VALUES(?,?,?,?,?,?,?,?,?)";
                sql[1] = "9";
                break;
            case "USER_ZZYY":
                sql[0] = "INSERT INTO USER_ZZYY(YHBH,YHMC,TEL,PHONE,PROV,CXBZ,SEX)VALUES(?,?,?,?,?,?,?)";
                sql[1] = "7";
                break;
            case "USER_95598WZ":
                sql[0] = "INSERT INTO USER_95598WZ(YHBH,YHMC,TEL,PHONE,YDDZ,CXBZ,SEX,PROV)VALUES(?,?,?,?,?,?,?,?)";
                sql[1] = "8";
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
            SessionFactory sessionFactory = (SessionFactory) WebApplicationContextUtils.getWebApplicationContext(request.getServletContext()).getBean("sessionFactory");
            Session session = sessionFactory.openSession();
            session.doWork(connection -> con = connection);
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
            session.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
