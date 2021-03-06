package com.hill.gwyb.dao.impl;

import com.hill.gwyb.api.maputil.GeocodingTools2;
import com.hill.gwyb.api.maputil.MapUtil;
import com.hill.gwyb.api.maputil.TCityLocationEntity2;
import com.hill.gwyb.dao.ISurveyDao;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
public class SurveyDaoImpl implements ISurveyDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public String getCityList(String orgid) throws SQLException {
        Session session = sessionFactory.openSession();
        String sql = "SELECT ORGID,ORGNAME FROM T_ORG t WHERE P_ORGID =?1";
        Query query = session.createSQLQuery(sql)
                .setParameter("1", orgid)
                .addScalar("ORGID", StandardBasicTypes.STRING)
                .addScalar("ORGNAME", StandardBasicTypes.STRING);
        JSONArray json = new JSONArray();
        for (Object o : query.list()) {
            Object[] objects = (Object[]) o;
            String ORGID = (String) objects[0];
            String ORGNAME = (String) objects[1];
            JSONObject jo = new JSONObject();
            jo.put("ORGID", ORGID);
            jo.put("ORGNAME", ORGNAME);
            json.add(jo);
        }
        session.close();
        return "{\"dataList\":" + json.toJSONString() + "}";
    }

    @Override
    public String getSurveyType(String rowv) throws SQLException {
        Session session = sessionFactory.openSession();
        String sql = null;
        if (null == rowv) {
            sql = "SELECT t.survey_type,t.tab FROM T_SURVEY_TYPE t ";
        } else {
            sql = "SELECT t.survey_type,t.tab FROM T_SURVEY_TYPE t WHERE t.YB_XS='" + rowv + "'";
        }
        Query query = session.createSQLQuery(sql)
                .addScalar("survey_type", StandardBasicTypes.STRING)
                .addScalar("tab", StandardBasicTypes.STRING);
        JSONArray json = new JSONArray();
        for (Object o : query.list()) {
            Object[] objects = (Object[]) o;
            JSONObject jo = new JSONObject();
            jo.put("survey_type", (String) objects[0]);
            jo.put("tab", (String) objects[1]);
            json.add(jo);
        }
        session.close();
        return "{\"dataList\":" + json.toJSONString() + "}";
    }

    @Override
    public String getCustType(String type) throws SQLException {
        Session session = sessionFactory.openSession();
        String sql = "SELECT PID,PNAME FROM T_P_CODE t WHERE T.PTYPE=?1";
        Query query = session.createSQLQuery(sql)
                .setParameter("1", type)
                .addScalar("PID", StandardBasicTypes.STRING)
                .addScalar("PNAME", StandardBasicTypes.STRING);
        JSONArray json = new JSONArray();
        for (Object o : query.list()) {
            Object[] objects = (Object[]) o;
            JSONObject jo = new JSONObject();
            jo.put("PID", (String) objects[0]);
            jo.put("PNAME", (String) objects[1]);
            json.add(jo);
        }
        session.close();
        return "{\"dataList\":" + json.toJSONString() + "}";
    }

    @Override
    public String getSurveyCol(String tab) throws SQLException {
        String sql = "SELECT COL,COL_NAME FROM T_SURVEY_COL t,T_SURVEY_TYPE S WHERE T.TYPE_ID=S.TYPE_ID AND S.TAB=?1";
        Session session = sessionFactory.openSession();
        Query query = session.createSQLQuery(sql)
                .setParameter("1", tab)
                .addScalar("COL", StandardBasicTypes.STRING)
                .addScalar("COL_NAME", StandardBasicTypes.STRING);
        JSONArray json = new JSONArray();
        for (Object o : query.list()) {
            Object[] objects = (Object[]) o;
            JSONObject jo = new JSONObject();
            jo.put("COL", (String) objects[0]);
            jo.put("COL_NAME", (String) objects[1]);
            json.add(jo);
        }
        session.close();
        return "{\"dataList\":" + json.toJSONString() + "}";
    }

    @Override
    public String getSurveyData(String tab, String orgid, String tick, String city, String address, int dist) throws SQLException {
        //获取省份名称
        String orgname = getOrgname(orgid);
        //获取城市名称
        String cityname = getOrgname(city);
//        System.out.println("省份：" + orgname + "城市：" + cityname + "搜索地址：" + address + "搜索半径：" + dist);
        String arsql = "";
        boolean flag = false;
        TCityLocationEntity2 cityentity = new TCityLocationEntity2();
        if (!"".equals(address)) {
            flag = true;
            //声明ak
            String ak = "xh2XppvAc5uB36HDZHKTOMnV3gSUULTb";
            if (!"".equals(cityname) && !address.contains(cityname)) address = cityname + address;
            if (!address.contains(orgname)) address = orgname + address;

            Map<String, Object> map1 = GeocodingTools2.getLatAndLngByAddress(address, ak);
            cityentity = (TCityLocationEntity2) map1.get("entity");

            //获取地址附近坐标范围
            double[] locaion = MapUtil.GetAround(cityentity.getLat(), cityentity.getLng(), dist);
//            System.out.println("拼接后搜索地址：" + address);
            //minLat, minLng, maxLat, maxLng;
//            System.out.println("距离范围，最小经度：" + locaion[1] + "，最大经度：" + locaion[3] + "，最小为纬度：" + locaion[0] + "，最大纬度：" + locaion[2]);
            arsql = " AND LNG BETWEEN " + locaion[1] + " AND " + locaion[3]
                    + " AND LAT BETWEEN " + locaion[0] + " AND " + locaion[2];
        }
        JSONArray json = new JSONArray();
        Session session = sessionFactory.openSession();
        String sql = "SELECT S.SHOW_NUM,T.COL FROM T_SURVEY_TYPE S,T_SURVEY_COL t WHERE S.TYPE_ID=T.TYPE_ID AND S.TAB=?1";
        Query query = session.createSQLQuery(sql)
                .setParameter("1", tab)
                .addScalar("SHOW_NUM", StandardBasicTypes.INTEGER)
                .addScalar("COL", StandardBasicTypes.STRING);
        List colObj = query.list();
        StringBuilder col = new StringBuilder();
        int showNumber = 0;
        for (Object o : colObj) {
            Object[] objects = (Object[]) o;
            if (showNumber == 0) {
                showNumber = Integer.parseInt(objects[0].toString());
            }
            col.append(",");
            col.append((String) objects[1]);
        }
        SQLQuery sq = null;
        if ("".equals(address)) {
            if ("".equals(orgid)) {
                sql = "SELECT ROWVAL,LNG,LAT" + col.toString() + " FROM (SELECT A.ROWID ROWVAL,LNG,LAT" + col.toString() + ",ROW_NUMBER() OVER(ORDER BY DBMS_RANDOM.RANDOM) RN FROM " + tab + " A "
                        + "WHERE NOT EXISTS(SELECT 1 FROM T_SURVEY_INVITE F WHERE F.TAB=?1 AND F.ROWVAL=A.ROWID AND F.IN_FLAG=1)"
                        + tick + " ) Q WHERE Q.RN<=" + showNumber;
                sq = session.createSQLQuery(sql)
                        .setParameter("1", tab);
            } else {
                sql = "SELECT ROWVAL,LNG,LAT" + col.toString() + " FROM(SELECT A.ROWID ROWVAL,LNG,LAT" + col.toString() + ",ROW_NUMBER() OVER(ORDER BY DBMS_RANDOM.RANDOM) RN FROM " + tab + " A,T_ORG B "
                        + "WHERE PROV LIKE '%'||B.ORGNAME||'%'" + tick + " AND B.ORGID=?1"
                        + " AND NOT EXISTS(SELECT 1 FROM T_SURVEY_INVITE F WHERE F.TAB=?2 AND F.ROWVAL=A.ROWID AND F.IN_FLAG=1) ) Q WHERE Q.RN<=" + showNumber;
                sq = session.createSQLQuery(sql)
                        .setParameter("1", orgid)
                        .setParameter("2", tab);
            }
        } else {
            if ("".equals(orgid)) {
                sql = "SELECT A.ROWID ROWVAL,LNG,LAT" + col.toString() + " FROM " + tab + " A "
                        + "WHERE NOT EXISTS(SELECT 1 FROM T_SURVEY_INVITE F WHERE F.TAB=?1 AND F.ROWVAL=A.ROWID AND F.IN_FLAG=1)"
                        + tick
                        + arsql + " ORDER BY DBMS_RANDOM.RANDOM";
                sq = session.createSQLQuery(sql)
                        .setParameter("1", tab);
            } else {
                sql = "SELECT A.ROWID ROWVAL,LNG,LAT" + col.toString() + " FROM " + tab + " A,T_ORG B "
                        + "WHERE PROV LIKE '%'||B.ORGNAME||'%'" + tick + " AND B.ORGID=?1"
                        + " AND NOT EXISTS(SELECT 1 FROM T_SURVEY_INVITE F WHERE F.TAB=?2 AND F.ROWVAL=A.ROWID AND F.IN_FLAG=1)"
                        + arsql + " ORDER BY DBMS_RANDOM.RANDOM";
                sq = session.createSQLQuery(sql)
                        .setParameter("1", orgid)
                        .setParameter("2", tab);
            }
        }
        sq.addScalar("ROWVAL", StandardBasicTypes.STRING)
                .addScalar("LNG", StandardBasicTypes.DOUBLE)
                .addScalar("LAT", StandardBasicTypes.DOUBLE);
        for (Object o : colObj) {
            Object[] objects = (Object[]) o;
            sq.addScalar((String) objects[1], StandardBasicTypes.STRING);
        }
        List<Object> list = sq.list();
        int k = 0;
        for (Object o : list) {
            JSONObject jo = new JSONObject();
            Object[] objects = (Object[]) o;
            jo.put("ROWVAL", (String) objects[0]);
            Double lng = (Double) objects[1];
            Double lat = (Double) objects[2];
            jo.put("LNG", lng);
            jo.put("LAT", lat);
            if (flag) {
                //获取拿出的数据与搜索的地址距离
                Double aadistance = MapUtil.getDistance(lng, lat, cityentity.getLng(), cityentity.getLat());
                //满足半径距离
                if (aadistance <= dist) {
                    for (int i = 0; i < colObj.size(); i++) {
                        Object[] colO = (Object[]) colObj.get(i);
                        jo.put((String) colO[1], (String) objects[i + 3]);
                    }
                    json.add(jo);
                    k++;
                }
            } else {
                for (int i = 0; i < colObj.size(); i++) {
                    Object[] colO = (Object[]) colObj.get(i);
                    jo.put((String) colO[1], (String) objects[i + 3]);
                }
                json.add(jo);
                k++;
            }
            if (k >= showNumber) {
                break;
            }
        }
        session.close();
        return "{\"code\":0,\"msg\":\"\",\"count\":100,\"data\":" + json.toJSONString() + "}";
    }

    @Override
    public String getSurveyInvData(String tab, String tick, String userid, Integer start, Integer end) throws SQLException {
        JSONArray json = new JSONArray();
        Session session = sessionFactory.openSession();
        String sql = "SELECT T.COL FROM T_SURVEY_TYPE S,T_SURVEY_COL t WHERE S.TYPE_ID=T.TYPE_ID AND S.TAB=?1";
        Query query = session.createSQLQuery(sql)
                .setParameter("1", tab)
                .addScalar("COL", StandardBasicTypes.STRING);
        List colObj = query.list();
        StringBuilder col = new StringBuilder();
        String orderCol = "YDDZ";
        if (tab.equals("USER_TSJB")) {
            orderCol = "YJRDZ";
        }
        for (Object o : colObj) {
            col.append((String) o);
            col.append(",");
        }
        sql = "SELECT ROWVAL,SF_DIS,TOTAL,LNG,LAT," + col + "ROWXH FROM(SELECT A.ROWID ROWVAL,A.LNG,A.LAT,CASE WHEN F.DISTRI IS NULL OR F.DISTRI='2' THEN '未分配' ELSE (SELECT S_USER_NAME FROM T_SURVEY_USER WHERE S_USER_ID=F.DIS_RM) END SF_DIS,count(1) over() total," + col + "ROWNUM rowxh FROM " + tab + " A,T_SURVEY_INVITE F "
                + "WHERE F.TAB=?1 AND F.USERID=?2 AND F.ROWVAL=A.ROWID AND F.IN_FLAG=1 " + tick
                + " ORDER BY " + orderCol + ") T WHERE T.ROWXH>" + start + " AND T.ROWXH <=" + end;
        SQLQuery sq = session.createSQLQuery(sql)
                .setParameter("1", tab)
                .setParameter("2", userid)
                .addScalar("ROWVAL", StandardBasicTypes.STRING)
                .addScalar("SF_DIS", StandardBasicTypes.STRING)
                .addScalar("TOTAL", StandardBasicTypes.INTEGER)
                .addScalar("LNG", StandardBasicTypes.DOUBLE)
                .addScalar("LAT", StandardBasicTypes.DOUBLE);
        for (Object o : colObj) {
            sq.addScalar((String) o, StandardBasicTypes.STRING);
        }
        sq.addScalar("ROWXH", StandardBasicTypes.INTEGER);
        int total = 0;
        for (Object o : sq.list()) {
            JSONObject jo = new JSONObject();
            Object[] objects = (Object[]) o;
            jo.put("ROWVAL", (String) objects[0]);
            jo.put("SF_DIS", (String) objects[1]);
            Double lng = (Double) objects[3];
            Double lat = (Double) objects[4];
            jo.put("LNG", lng);
            jo.put("LAT", lat);
            if (total == 0) {
                total = (int) objects[2];
            }
            for (int i = 0; i < colObj.size(); i++) {
                jo.put((String) colObj.get(i), (String) objects[i + 5]);
            }
            json.add(jo);
        }
        session.close();
        return "{\"code\":0,\"msg\":\"\",\"count\":" + total + ",\"data\":" + json.toJSONString() + "}";
    }

    @Override
    public void saveInvit(String tab, String rowv, String invRes, String faultRes, String userid) throws SQLException {
        String[] rw = rowv.split(",");
        String sql = "INSERT INTO T_SURVEY_INVITE(TAB, ROWVAL, USERID, IN_FLAG, FAULT_CODE) VALUES(?1,?2,?3,?4,?5)";
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        NativeQuery query = session.createSQLQuery(sql);
        for (int i = 0; i < rw.length; i++) {
            if ("".equals(rw[i])) {
                break;
            }
            query.setParameter("1", tab);
            query.setParameter("2", rw[i]);
            query.setParameter("3", userid);
            query.setParameter("4", invRes);
            query.setParameter("5", faultRes);
            query.executeUpdate();
        }
        transaction.commit();
        session.close();
    }

    @Override
    public void saveDist(String tab, String rowv, String distRes, String diaocy) throws SQLException {
        String[] rw = rowv.split(",");
        String sql = "UPDATE T_SURVEY_INVITE SET DISTRI=?1,DIS_RM=?2 WHERE TAB=?3 AND ROWVAL=?4";
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        NativeQuery query = session.createSQLQuery(sql);
        for (int i = 0; i < rw.length; i++) {
            if ("".equals(rw[i])) {
                break;
            }
            query.setParameter("1", distRes);
            query.setParameter("2", diaocy);
            query.setParameter("3", tab);
            query.setParameter("4", rw[i]);
            query.executeUpdate();
        }
        transaction.commit();
        session.close();
    }

    @Override
    public String getSurveyUser(String userid) throws SQLException {
        Session session = sessionFactory.openSession();
        String sql = "SELECT T.S_USER_ID,T.S_USER_NAME FROM T_SURVEY_USER T WHERE T.USERID=?1";
        Query query = session.createSQLQuery(sql)
                .setParameter("1", userid)
                .addScalar("S_USER_ID", StandardBasicTypes.STRING)
                .addScalar("S_USER_NAME", StandardBasicTypes.STRING);
        JSONArray json = new JSONArray();
        for (Object o : query.list()) {
            Object[] objects = (Object[]) o;
            JSONObject jo = new JSONObject();
            jo.put("S_USER_ID", (String) objects[0]);
            jo.put("S_USER_NAME", (String) objects[1]);
            json.add(jo);
        }
        session.close();
        return "{\"dataList\":" + json.toJSONString() + "}";
    }

    @Override
    public String getOrgname(String orgid) {
        String orgname = "";
        String sql = "SELECT ORGNAME FROM T_ORG  O WHERE O.ORGID = ?1";
        Session session = sessionFactory.openSession();
        Query query = session.createSQLQuery(sql)
                .setParameter("1", orgid)
                .addScalar("ORGNAME", StandardBasicTypes.STRING);
        JSONArray json = new JSONArray();
        for (Object o : query.list()) {
            orgname = o.toString();
        }
        session.close();
        return orgname;
    }
}
