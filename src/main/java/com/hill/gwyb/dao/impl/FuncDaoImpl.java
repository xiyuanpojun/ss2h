package com.hill.gwyb.dao.impl;

import java.util.List;

import com.hill.gwyb.dao.IFuncDao;
import com.hill.gwyb.po.TFuncEntity;
import com.hill.gwyb.po.TUserEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW )
public class FuncDaoImpl implements IFuncDao {
@Autowired
 private HibernateTemplate hibernateTemplate;
@Autowired
 private SessionFactory sessionFactory;
   @Override
	public List<TFuncEntity> findFuncAll(int currentTotal, int current) {
	   Session session = sessionFactory.openSession();
       String hql;
       hql="FROM TFuncEntity f order by TO_NUMBER(f.fId) asc";
       System.out.println("1%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
       Query query = session.createQuery(hql);
       System.out.println("2%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
       query.setFirstResult(currentTotal * (current - 1));
       query.setMaxResults(currentTotal);
       System.out.println("4%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
       List<TFuncEntity> list = query.list();
       System.out.println("1%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
       session.close();
       return list;
	}

	@Override
	public Integer findFuncTotal() {
		Session session = sessionFactory.openSession();
        String hql;
        hql="From TFuncEntity";
        System.out.println("a1%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        Query query = session.createQuery(hql);
        System.out.println("a2%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        List<TUserEntity> list = query.list();
        System.out.println("a3%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        session.close();
        return list.size();
	}

	@Override
	public TFuncEntity findOne(String fId) {
	
		 return hibernateTemplate.get(TFuncEntity.class,fId);
	}

	@Override
	public void funcUpdate(TFuncEntity funcentity) throws Exception {
		 hibernateTemplate.update(funcentity);
	}

	@Override
	public void delete(TFuncEntity funcentity) throws Exception {
		 hibernateTemplate.delete(funcentity);
	}

	@Override
	public void add(TFuncEntity funcentity) {
		 hibernateTemplate.save(funcentity);
	}

}
