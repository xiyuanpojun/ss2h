package com.hill.gwyb.dao.impl;

import com.hill.gwyb.common.SessionContext;
import com.hill.gwyb.dao.IUserDao;
import com.hill.gwyb.po.TFuncEntity;
import com.hill.gwyb.po.TOrgEntity;
import com.hill.gwyb.po.TUserEntity;
import com.hill.gwyb.po.TUserLoginEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class UserDaoImpl implements IUserDao {
    @Autowired
    private HibernateTemplate hibernateTemplate;
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public TUserEntity userFindOne(String userid) throws Exception {
        return hibernateTemplate.get(TUserEntity.class, userid);
    }

    @Override
    public void userLoginInfoExec(TUserLoginEntity userLoginEntity, boolean isend) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        if (isend) {
            //退出
            String sql = "UPDATE T_USER_LOGIN SET OUT_TIME = SYSDATE WHERE OUT_TIME IS NULL AND SESSION_ID = ?1";
            NativeQuery sqlQuery = session.createSQLQuery(sql)
                    .setParameter("1", userLoginEntity.getSessionId());
            sqlQuery.executeUpdate();
        } else {
            //登陆
            Query query = session.createSQLQuery("INSERT INTO T_USER_LOGIN(USERID, LOG_TIME,SESSION_ID) VALUES (?1,SYSDATE,?2)")
                    .setParameter("1", userLoginEntity.getUserid())
                    .setParameter("2", userLoginEntity.getSessionId());
            query.executeUpdate();
        }
        transaction.commit();
        session.close();
    }

    @Override
    public Integer userLoginInfoCheck(TUserLoginEntity userLoginEntity) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        String sql = "SELECT LOG_TIME,SESSION_ID FROM (SELECT LOG_TIME,SESSION_ID FROM T_USER_LOGIN WHERE USERID = ?1 AND OUT_TIME IS NULL ORDER BY LOG_TIME DESC) WHERE ROWNUM = 1";
        NativeQuery sqlQuery = session.createSQLQuery(sql)
                .setParameter("1", userLoginEntity.getUserid())
                .addScalar("LOG_TIME", StandardBasicTypes.TIMESTAMP)
                .addScalar("SESSION_ID", StandardBasicTypes.STRING);
        //没有查到数据说明是未登录状态，反之就已经登陆。
        //true代表正常。
        List list = sqlQuery.list();
        String date = null;
        String sessionID = null;
        Boolean flag = true;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Object o : list) {
            flag = false;
            Object[] objects = (Object[]) o;
            date = df.format((Timestamp) objects[0]);
            sessionID = (String) objects[1];
        }
        //正常
        if (flag) {
            transaction.commit();
            session.close();
            return 0;
        } else {
            Date dt1 = df.parse(date);
            Date dt2 = df.parse(new Timestamp(System.currentTimeMillis()).toString());
            //登陆超过三十分钟还没有退出。
            if (dt2.getTime() - dt1.getTime() > 1000 * 60 * 30) {
                //修改时间
                //记录用户登陆信息
                HttpSession httpSession = SessionContext.getSession(sessionID);

                if (null != httpSession) {
                    httpSession.invalidate();
                }
                transaction.commit();
                session.close();
                return 1;
            } else {
                transaction.commit();
                session.close();
                return 2;
            }
        }
    }

    @Override
    public void userAdd(TUserEntity userEntity) throws Exception {
        hibernateTemplate.save(userEntity);
    }

    @Override
    public void userDelete(TUserEntity userEntity) throws Exception {
        hibernateTemplate.delete(userEntity);
    }

    @Override
    public void userUpdate(TUserEntity userEntity) throws Exception {
        hibernateTemplate.update(userEntity);
    }

    @Override
    public List<TUserEntity> userFindAll(int currentotal, int current, String province) throws Exception {
        Session session = sessionFactory.openSession();
        String hql;
        if (Integer.parseInt(province) < 1) {
            hql = "FROM TUserEntity";
        } else {
            hql = "FROM TUserEntity WHERE orgid = ?1";
        }
        Query query = session.createQuery(hql);
        if (Integer.parseInt(province) >= 1) {
            query.setParameter("1", province);
        }
        query.setFirstResult(currentotal * (current - 1));
        query.setMaxResults(currentotal);
        List<TUserEntity> list = query.list();
        session.close();
        return list;
    }

    @Override
    public Integer userFindAllTotal(String province) throws Exception {
        Session session = sessionFactory.openSession();
        String hql;
        if (Integer.parseInt(province) < 1) {
            hql = "FROM TUserEntity";
        } else {
            hql = "FROM TUserEntity WHERE orgid = ?1";
        }
        Query query = session.createQuery(hql);
        if (Integer.parseInt(province) >= 1) {
            query.setParameter("1", province);
        }
        List<TUserEntity> list = query.list();
        session.close();
        return list.size();
    }

    @Override
    public List<TFuncEntity> userFunction(String urole) throws Exception {
        Session session = sessionFactory.openSession();
        Query query1 = session.createSQLQuery("SELECT FUNC_ID FROM T_ROLE_FUNC WHERE ROLEID = ?1 ORDER BY ORDER_NUM ASC")
                .setParameter("1", urole)
                .addScalar("FUNC_ID", StandardBasicTypes.STRING);

        List<TFuncEntity> list = new ArrayList<>();
        for (Object fId : query1.list()) {
            //把FUNC_ID找到以后，取出相关url。
            Query query2 = session.createSQLQuery("SELECT F_NAME,F_URL FROM T_FUNC WHERE F_ID = ?1")
                    .setParameter("1", fId)
                    .addScalar("F_NAME", StandardBasicTypes.STRING)
                    .addScalar("F_URL", StandardBasicTypes.STRING);
            for (Object o : query2.list()) {
                Object[] objects = (Object[]) o;
                TFuncEntity funcEntity = new TFuncEntity((String) fId, (String) objects[0], (String) objects[1]);
                list.add(funcEntity);
            }
        }
        session.close();
        return list;
    }

    @Override
    public List<TOrgEntity> getVisibleProvince(String urole, String orgid) throws Exception {
        Session session = sessionFactory.openSession();
        //1 代表管理员 2 代表供应商
        String hql = "FROM TOrgEntity WHERE pOrgid is null ";
        if ("2".equals(urole)) {
            hql = "FROM TOrgEntity WHERE orgid = " + orgid;
        }
        Query query = session.createQuery(hql);
        List<TOrgEntity> list = query.list();
        session.close();
        return list;
    }

    @Override
    public String userProvinceName(String orgid) throws Exception {
        if (orgid != null) {
            TOrgEntity orgEntity = hibernateTemplate.get(TOrgEntity.class, orgid);
            if (orgEntity.getpOrgid() != null) {
                return hibernateTemplate.get(TOrgEntity.class, orgEntity.getpOrgid()).getOrgname();
            } else {
                return orgEntity.getOrgname();
            }
        } else {
            return "";
        }

    }
}
