package cn.softlq.ss2h.dao.impl;

import cn.softlq.ss2h.dao.IUserDao;
import cn.softlq.ss2h.po.TFuncEntity;
import cn.softlq.ss2h.po.TOrgEntity;
import cn.softlq.ss2h.po.TUserEntity;
import cn.softlq.ss2h.po.TUserLoginEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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
        System.out.println(userLoginEntity.toString());
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        if (isend) {
            //退出
            String sql = "UPDATE T_USER_LOGIN SET OUT_TIME = ? WHERE OUT_TIME IS NULL AND USERID = ?";
            NativeQuery sqlQuery = session.createSQLQuery(sql)
                    .setParameter(0, userLoginEntity.getOutTime())
                    .setParameter(1, userLoginEntity.getUserid());
            sqlQuery.executeUpdate();
        } else {
            //登陆
            Query query = session.createSQLQuery("INSERT INTO T_USER_LOGIN(USERID, LOG_TIME) VALUES (?,?)")
                    .setParameter(0, userLoginEntity.getUserid())
                    .setParameter(1, userLoginEntity.getLogTime());
            query.executeUpdate();
        }
        transaction.commit();
        session.close();
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
            hql = "FROM TUserEntity WHERE orgid = ?";
        }
        Query query = session.createQuery(hql);
        if (Integer.parseInt(province) >= 1) {
            query.setParameter(0, province);
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
            hql = "FROM TUserEntity WHERE orgid = ?";
        }
        Query query = session.createQuery(hql);
        if (Integer.parseInt(province) >= 1) {
            query.setParameter(0, province);
        }
        List<TUserEntity> list = query.list();
        session.close();
        return list.size();
    }

    @Override
    public List<TFuncEntity> userFunction(String urole) throws Exception {
        Session session = sessionFactory.openSession();
        Query query1 = session.createSQLQuery("SELECT FUNC_ID FROM T_ROLE_FUNC WHERE ROLEID = ?")
                .setParameter(0, urole)
                .addScalar("FUNC_ID", StandardBasicTypes.STRING);

        List<TFuncEntity> list = new ArrayList<>();
        for (Object fId : query1.list()) {
            //把FUNC_ID找到以后，取出相关url。
            Query query2 = session.createSQLQuery("SELECT F_NAME,F_URL FROM T_FUNC WHERE F_ID = ?")
                    .setParameter(0, fId)
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
