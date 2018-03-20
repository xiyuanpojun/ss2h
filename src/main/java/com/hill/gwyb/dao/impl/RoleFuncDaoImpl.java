package com.hill.gwyb.dao.impl;

import com.hill.gwyb.dao.IRoleFuncDao;
import com.hill.gwyb.po.TFuncEntity;
import com.hill.gwyb.po.TRoleFuncEntity;
import com.hill.gwyb.vo.RoleFuncItemView;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
public class RoleFuncDaoImpl implements IRoleFuncDao {
    @Autowired
    private HibernateTemplate hibernateTemplate;
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<RoleFuncItemView> findAll(int currentTotal, int current, String role) throws Exception {
        Session session = sessionFactory.openSession();
        String hql = "";
        if (role.equals("0")) {
            //String hql="select r.roleid,r.orderNum,f.fId,f.fName from TRoleFuncEntity inner left jion  TFuncEntity f on r.funcId= f.fId";
            hql = "select new com.hill.gwyb.vo.RoleFuncItemView(rf.roleid,r.rname,rf.orderNum,rf.funcId,f.fName) from TRoleFuncEntity rf ,TRoleEntity r ,TFuncEntity f  where rf.roleid = r.roleid and  rf.funcId = f.fId";

        } else {
            hql = "select new com.hill.gwyb.vo.RoleFuncItemView(rf.roleid,r.rname,rf.orderNum,rf.funcId,f.fName) from TRoleFuncEntity rf,TRoleEntity r,TFuncEntity f where rf.roleid = r.roleid and rf.funcId = f.fId and rf.roleid = ?1";

        }
        Query query = session.createQuery(hql);
        if (!role.equals("0")) {
            query.setParameter("1", role);
        }
        query.setFirstResult(currentTotal * (current - 1));
        query.setMaxResults(currentTotal);
        List<RoleFuncItemView> list = query.list();
        session.close();
        return list;


    }

    @Override
    public Integer findTotal(String role) throws Exception {
        Session session = sessionFactory.openSession();
        String hql = "";
        if (role.equals("0")) {
            //String hql="select r.roleid,r.orderNum,f.fId,f.fName from TRoleFuncEntity inner left jion  TFuncEntity f on r.funcId= f.fId";
            hql = "select new com.hill.gwyb.vo.RoleFuncItemView(rf.roleid,r.rname,rf.orderNum,rf.funcId,f.fName) from TRoleFuncEntity rf ,TRoleEntity r ,TFuncEntity f  where rf.roleid = r.roleid and  rf.funcId = f.fId order by TO_NUMBER(rf.roleid) asc";

        } else {
            hql = "select new com.hill.gwyb.vo.RoleFuncItemView(rf.roleid,r.rname,rf.orderNum,rf.funcId,f.fName) from TRoleFuncEntity rf,TRoleEntity r,TFuncEntity f where rf.roleid = r.roleid and rf.funcId = f.fId and rf.roleid = ?1  order by TO_NUMBER(rf.roleid) asc";
        }
        Query query = session.createQuery(hql);
        if (!role.equals("0")) {
            query.setParameter("1", role);
        }
        List<RoleFuncItemView> list = query.list();
        session.close();
        return list.size();
    }

    @Override
    public void delete(TRoleFuncEntity tRoleFuncEntity) {
        Session session = sessionFactory.openSession();
        Transaction tran = session.beginTransaction();

        String hql = "delete from TRoleFuncEntity r where r.roleid = ?1 and r.orderNum = ?2 and r.funcId = ?3";
        Query query = session.createQuery(hql);
        query.setString("1", tRoleFuncEntity.getRoleid());
        query.setLong("2", tRoleFuncEntity.getOrderNum());
        query.setString("3", tRoleFuncEntity.getFuncId());

        query.executeUpdate();
        tran.commit();
        ;
    }

    @Override
    public void add(TRoleFuncEntity tRoleFuncEntity) throws Exception {
        hibernateTemplate.save(tRoleFuncEntity);
    }

    @Override
    public List<TFuncEntity> findnofuncbyrole(String role) {
        Session session = sessionFactory.openSession();
        String hql = "from TFuncEntity fc where fc.fId not in (select rf.funcId from TRoleFuncEntity rf,TRoleEntity r,TFuncEntity f where rf.roleid = r.roleid and rf.funcId = f.fId and rf.roleid = ?1)";
        Query query = session.createQuery(hql);
        query.setParameter("1", role);
        List<TFuncEntity> list = query.list();
        session.close();
        return list;
    }

    @Override
    public int findMaxOrderNUM(String role) {
        Session session = sessionFactory.openSession();
        String hql = "select max(rf.orderNum) from TRoleFuncEntity rf,TRoleEntity r where rf.roleid = r.roleid and rf.roleid = ?1";
        Query query = session.createQuery(hql);
        query.setParameter("1", role);
        int maxnum = 0;
        if (query.list().get(0) != null) {
            maxnum = (int) query.list().get(0);
        }
        session.close();
        return maxnum;
    }


}
