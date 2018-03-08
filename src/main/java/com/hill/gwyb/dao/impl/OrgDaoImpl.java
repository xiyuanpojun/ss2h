package com.hill.gwyb.dao.impl;

import com.hill.gwyb.dao.IOrgDao;
import com.hill.gwyb.po.TOrgEntity;
import com.hill.gwyb.po.TUserEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class OrgDaoImpl implements IOrgDao {
    @Autowired
    private HibernateTemplate hibernateTemplate;
    @Autowired
    private SessionFactory sessionFactory;

    //查找机构列表
    @Override
    public List<TOrgEntity> findAll(int currentTotal, int current, String porgid) {
        Session session = sessionFactory.openSession();
        String hql;
        if ("all".equals(porgid)) {
            hql = "FROM TOrgEntity o  order by TO_NUMBER(o.orgid) asc";
        } else {
            hql = "FROM TOrgEntity o where o.pOrgid = ? order by TO_NUMBER(o.orgid) asc";
        }
        Query query = session.createQuery(hql);
        if (!"all".equals(porgid)) {
            query.setParameter(0, porgid);
        }
        query.setFirstResult(currentTotal * (current - 1));
        query.setMaxResults(currentTotal);
        List<TOrgEntity> list = query.list();
        session.close();
        return list;
    }

    //查找机构列表总记录数
    @Override
    public Integer findTotal(String porgid) {
        Session session = sessionFactory.openSession();
        String hql;
        if ("all".equals(porgid)) {
            hql = "From TOrgEntity";

        } else {
            hql = "From TOrgEntity o where  o.pOrgid = ?";
        }
        Query query = session.createQuery(hql);
        if (porgid != "all" && !"all".equals(porgid)) {
            query.setParameter(0, porgid);
        }
        List<TUserEntity> list = query.list();
        session.close();
        return list.size();
    }

    //获取单个机构信息
    @Override
    public TOrgEntity findOneById(String oid) {

        return hibernateTemplate.get(TOrgEntity.class, oid);
    }

    //删除单个机构信息
    @Override
    public void delete(TOrgEntity orgEntity) throws Exception {
        hibernateTemplate.delete(orgEntity);
    }

    //添加机构
    @Override
    public void add(TOrgEntity orgEntity) throws Exception {
        hibernateTemplate.save(orgEntity);
    }

    //查找上级机构列表
    @Override
    public List<TOrgEntity> showporglist() throws Exception {
        Session session = sessionFactory.openSession();
        String hql;
        hql = "FROM TOrgEntity o  where o.pOrgid = null order by TO_NUMBER(o.orgid) asc";
        Query query = session.createQuery(hql);
        List<TOrgEntity> list = query.list();
        session.close();
        return list;
    }

    @Override
    public List<TOrgEntity> showcitylist(String porgid) {
        Session session = sessionFactory.openSession();
        String hql;
        hql = "FROM TOrgEntity o  where o.pOrgid = ? order by TO_NUMBER(o.orgid) asc";
        Query query = session.createQuery(hql);
        query.setParameter(0,porgid);
        List<TOrgEntity> list = query.list();
        session.close();
        return list;
    }

    @Override
    public boolean findOne(TOrgEntity orgentity) {
        Session session = sessionFactory.openSession();
        String hql;
        if (!"".equals(orgentity.getpOrgid())) {
            hql = "FROM TOrgEntity o  where  o.orgname = ? and pOrgid = ?";
        } else {
            hql = "FROM  TOrgEntity o where o.orgname = ?";
        }
        Query query = session.createQuery(hql);
        query.setParameter(0, orgentity.getOrgname());
        if (!"".equals(orgentity.getpOrgid())) {
            query.setParameter(1, orgentity.getpOrgid());
        }
        List<TOrgEntity> list = query.list();
        session.close();
        return list.size() > 0;
    }

}
