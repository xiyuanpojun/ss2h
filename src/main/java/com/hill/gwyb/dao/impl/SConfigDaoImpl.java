package com.hill.gwyb.dao.impl;

import com.hill.gwyb.dao.ISConfigDao;
import com.hill.gwyb.po.TPCodeEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class SConfigDaoImpl implements ISConfigDao {
    @Autowired
    private HibernateTemplate hibernateTemplate;
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<TPCodeEntity> getVisibleConfig() throws Exception {
        Session session = sessionFactory.openSession();
        String sql = "SELECT DISTINCT PTYPE  FROM T_P_CODE";
        NativeQuery sq = session.createSQLQuery(sql);
        sq.addScalar("PTYPE", StandardBasicTypes.STRING);
        List<TPCodeEntity> list = new ArrayList<>();
        for (Object o : sq.list()) {
            list.add(new TPCodeEntity((String) o));
        }
        session.close();
        return list;
    }

    @Override
    public List<TPCodeEntity> configFindAll(int currentTotal, int current, String typeId) throws Exception {
        Session session = sessionFactory.openSession();
        String hql;
        if ("".equals(typeId)) {
            hql = "FROM TPCodeEntity";
        } else {
            hql = "FROM TPCodeEntity WHERE ptype = ?1";
        }
        Query query = session.createQuery(hql);
        if (!"".equals(typeId)) {
            query.setParameter("1", typeId);
        }
        query.setFirstResult(currentTotal * (current - 1));
        query.setMaxResults(currentTotal);
        List<TPCodeEntity> list = query.list();
        session.close();
        return list;
    }

    @Override
    public Integer configFindAllTotal(String typeId) throws Exception {
        Session session = sessionFactory.openSession();
        String hql;
        if ("".equals(typeId)) {
            hql = "FROM TPCodeEntity";
        } else {
            hql = "FROM TPCodeEntity WHERE ptype  = ?1";
        }
        Query query = session.createQuery(hql);
        if (!"".equals(typeId)) {
            query.setParameter("1", typeId);
        }
        List<TPCodeEntity> list = query.list();
        session.close();
        return list.size();
    }

    @Override
    public TPCodeEntity configFindOne(TPCodeEntity tpCodeEntity) throws Exception {
        String sql = "SELECT PID,PNAME,PTYPE FROM T_P_CODE WHERE PNAME = ?1 AND PTYPE = ?2";
        Session session = sessionFactory.openSession();
        NativeQuery sq = session.createSQLQuery(sql);
        sq.setParameter("1", tpCodeEntity.getPname());
        sq.setParameter("2", tpCodeEntity.getPtype());
        sq.addScalar("PID", StandardBasicTypes.STRING);
        sq.addScalar("PNAME", StandardBasicTypes.STRING);
        sq.addScalar("PTYPE", StandardBasicTypes.STRING);
        TPCodeEntity entity = null;
        for (Object o : sq.list()) {
            Object[] os = (Object[]) o;
            if (entity == null) entity = new TPCodeEntity();
            entity.setPid((String) os[0]);
            entity.setPname((String) os[1]);
            entity.setPtype((String) os[2]);
        }
        session.close();
        return entity;
    }

    @Override
    public void configAdd(TPCodeEntity tpCodeEntity) throws Exception {
        hibernateTemplate.save(tpCodeEntity);
    }

    @Override
    public void configDelete(TPCodeEntity tpCodeEntity) throws Exception {
        hibernateTemplate.delete(tpCodeEntity);
    }
}
