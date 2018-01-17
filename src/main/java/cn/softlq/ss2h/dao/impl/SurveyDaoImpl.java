package cn.softlq.ss2h.dao.impl;

import cn.softlq.ss2h.dao.ISurveyDao;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class SurveyDaoImpl implements ISurveyDao {
    @Autowired
    private SessionFactory sessionFactory;

    public String getCityList(String orgid) throws SQLException {
        Session session = sessionFactory.openSession();
        String sql="select ORGID,ORGNAME from T_ORG t WHERE P_ORGID =?";
        Query query =session.createSQLQuery(sql)
                .setParameter(0,orgid)
                .addScalar("ORGID", StandardBasicTypes.STRING)
                .addScalar("ORGNAME",StandardBasicTypes.STRING);
        JSONArray json = new JSONArray();
        for (Object o : query.list()) {
            Object[] objects = (Object[]) o;
            String ORGID = (String) objects[0];
            String ORGNAME = (String) objects[1];
            JSONObject jo = new JSONObject();
            jo.put("ORGID",  ORGID);
            jo.put("ORGNAME",  ORGNAME);
            json.add(jo);
        }
        return "{\"dataList\":"+json.toJSONString()+"}";
    }

    public String getSurveyType() throws SQLException{
        Session session = sessionFactory.openSession();
        String sql="select t.survey_type,t.tab from T_SURVEY_TYPE t";
        Query query =session.createSQLQuery(sql)
                .addScalar("survey_type",StandardBasicTypes.STRING)
                .addScalar("tab",StandardBasicTypes.STRING);
        JSONArray json = new JSONArray();
        for (Object o : query.list()) {
            Object[] objects = (Object[]) o;
            JSONObject jo = new JSONObject();
            jo.put("survey_type",  (String) objects[0]);
            jo.put("tab",  (String) objects[1]);
            json.add(jo);
        }
        return "{\"dataList\":"+json.toJSONString()+"}";
    }

    public String getCustType(String type) throws SQLException{
        Session session = sessionFactory.openSession();
        String sql="select PID,PNAME from T_P_CODE t WHERE T.PTYPE=?";
        Query query =session.createSQLQuery(sql)
                .setParameter(0,type)
                .addScalar("PID",StandardBasicTypes.STRING)
                .addScalar("PNAME",StandardBasicTypes.STRING);
        JSONArray json = new JSONArray();
        for (Object o : query.list()) {
            Object[] objects = (Object[]) o;
            JSONObject jo = new JSONObject();
            jo.put("PID",  (String) objects[0]);
            jo.put("PNAME",  (String) objects[1]);
            json.add(jo);
        }
        return "{\"dataList\":"+json.toJSONString()+"}";
    }

    public String getSurveyCol(String tab) throws SQLException{
        String sql="select COL,COL_NAME from T_SURVEY_COL t,T_SURVEY_TYPE S WHERE T.TYPE_ID=S.TYPE_ID AND S.TAB=?";
        Session session = sessionFactory.openSession();
        Query query =session.createSQLQuery(sql)
                .setParameter(0,tab)
                .addScalar("COL",StandardBasicTypes.STRING)
                .addScalar("COL_NAME",StandardBasicTypes.STRING);
        JSONArray json = new JSONArray();
        for (Object o : query.list()) {
            Object[] objects = (Object[]) o;
            JSONObject jo = new JSONObject();
            jo.put("COL",  (String) objects[0]);
            jo.put("COL_NAME",  (String) objects[1]);
            json.add(jo);
        }
        return "{\"dataList\":"+json.toJSONString()+"}";
    }
    public String getSurveyData(String tab,String orgid,String tick) throws SQLException{
        JSONArray json = new JSONArray();
        Session session = sessionFactory.openSession();
        String sql="select T.COL from T_SURVEY_TYPE S,T_SURVEY_COL t WHERE S.TYPE_ID=T.TYPE_ID AND S.TAB=?";
        Query query =session.createSQLQuery(sql)
                .setParameter(0,tab)
                .addScalar("COL",StandardBasicTypes.STRING);
        List colObj=query.list();
        StringBuilder col=new StringBuilder();
        for (Object o : colObj) {
            Object[] objects = (Object[]) o;
            col.append((String) objects[0]);
            col.append(",");
        }
        sql= "SELECT T.* FROM ("
                +"SELECT A.ROWID ROWVAL,"+col.toString()+"ROW_NUMBER() OVER(ORDER BY DBMS_RANDOM.random) RANDOM_VAL FROM "+tab+" A,T_ORG B "
                +"WHERE PROV LIKE '%'||B.ORGNAME||'%'"+tick+" AND B.ORGID=?"
                +"AND NOT EXISTS(SELECT 1 FROM T_SURVEY_INVITE F WHERE F.TAB=? AND F.ROWVAL=A.ROWID AND F.IN_FLAG=1)"
                +") T,T_SURVEY_TYPE S WHERE T.RANDOM_VAL<=S.SHOW_NUM AND S.TAB=?";
        query =session.createSQLQuery(sql)
                .setParameter(0,orgid)
                .setParameter(1,tab)
                .setParameter(2,tab);
        int total=100;
        for (Object o : query.list()) {
            JSONObject jo = new JSONObject();
            Object[] objects = (Object[]) o;
            jo.put("ROWVAL", (String) objects[0]);
            for(int i=0;i<colObj.size();i++){
                jo.put((String)colObj.get(i), (String) objects[i+1]);
            }
            json.add(jo);
        }
        return "{\"code\":0,\"msg\":\"\",\"count\":"+total+",\"data\":"+json.toJSONString()+"}";
    }

    public String getSurveyInvData(String tab,String tick,String userid,Integer start,Integer end) throws SQLException{
        JSONArray json = new JSONArray();
        Session session = sessionFactory.openSession();
        String sql="select T.COL from T_SURVEY_TYPE S,T_SURVEY_COL t WHERE S.TYPE_ID=T.TYPE_ID AND S.TAB=?";
        Query query =session.createSQLQuery(sql)
                .setParameter(0,tab)
                .addScalar("COL",StandardBasicTypes.STRING);
        List colObj=query.list();
        StringBuilder col=new StringBuilder();
        for (Object o : colObj) {
            Object[] objects = (Object[]) o;
            col.append((String) objects[0]);
            col.append(",");
        }
        sql= "SELECT * FROM(SELECT A.ROWID ROWVAL,CASE WHEN F.DISTRI IS NULL OR F.DISTRI='2' THEN '未分配' ELSE F.DIS_RM END SF_DIS,count(1) over() total,"+col+"ROWNUM rowxh FROM "+tab+" A,T_SURVEY_INVITE F "
                +"WHERE F.TAB=? AND F.USERID=? AND F.ROWVAL=A.ROWID AND F.IN_FLAG=1 "+tick
                +") T WHERE T.ROWXH>"+start+" AND T.ROWXH <="+end;
        query =session.createSQLQuery(sql)
                .setParameter(0,tab)
                .setParameter(1,userid);
        int total=0;
        for (Object o : query.list()) {
            JSONObject jo = new JSONObject();
            Object[] objects = (Object[]) o;
            jo.put("ROWVAL", (String) objects[0]);
            jo.put("SF_DIS", (String) objects[1]);
            if(total==0){
                total=(int)objects[2];
            }
            for(int i=0;i<colObj.size();i++){
                jo.put((String)colObj.get(i), (String) objects[i+3]);
            }
            json.add(jo);
        }
        return "{\"code\":0,\"msg\":\"\",\"count\":"+total+",\"data\":"+json.toJSONString()+"}";
    }

    public void saveInvit(String tab,String rowv,String invRes,String faultRes,String userid) throws SQLException{
        String[] rw=rowv.split(",");
        String sql="INSERT INTO T_SURVEY_INVITE(TAB, ROWVAL, USERID, IN_FLAG, FAULT_CODE) VALUES(?,?,?,?,?)";
        Session session = sessionFactory.openSession();
        Query query =session.createSQLQuery(sql);
        for(int i=0;i<rw.length;i++){
            if("".equals(rw[i])){
                break;
            }
            query.setParameter(0, tab);
            query.setParameter(1, rw[i]);
            query.setParameter(2, userid);
            query.setParameter(3, invRes);
            query.setParameter(4, faultRes);
            query.executeUpdate();
        }
    }

    public void saveDist(String tab,String rowv,String distRes,String diaocy) throws SQLException {
        String[] rw=rowv.split(",");
        String sql="UPDATE T_SURVEY_INVITE SET DISTRI=?,DIS_RM=? WHERE TAB=? AND ROWVAL=?";
        Session session = sessionFactory.openSession();
        Query query =session.createSQLQuery(sql);
        for(int i=0;i<rw.length;i++){
            if("".equals(rw[i])){
                break;
            }
            query.setParameter(0, distRes);
            query.setParameter(1, diaocy);
            query.setParameter(2, tab);
            query.setParameter(3, rw[i]);
            query.executeUpdate();
        }
    }
}
