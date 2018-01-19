package cn.softlq.ss2h.dao.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.softlq.ss2h.dao.IFuncDao;
import cn.softlq.ss2h.po.TFuncEntity;
import cn.softlq.ss2h.po.TUserEntity;
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
       Query query = session.createQuery(hql);
       query.setFirstResult(currentTotal * (current - 1));
       query.setMaxResults(currentTotal);
       List<TFuncEntity> list = query.list();
       session.close();
       return list;
	}

	@Override
	public Integer findFuncTotal() {
		Session session = sessionFactory.openSession();
        String hql;
        hql="From TFuncEntity";
        Query query = session.createQuery(hql);
        List<TUserEntity> list = query.list();
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
