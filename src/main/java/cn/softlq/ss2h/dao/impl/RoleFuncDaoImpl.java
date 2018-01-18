package cn.softlq.ss2h.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.softlq.ss2h.dao.IRoleFuncDao;
import cn.softlq.ss2h.po.TFuncEntity;
import cn.softlq.ss2h.po.TRoleFuncEntity;
import cn.softlq.ss2h.po.TUserEntity;
import cn.softlq.ss2h.vo.RoleFuncItemView;

@Repository
@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW )
public class RoleFuncDaoImpl implements IRoleFuncDao {
	@Autowired
	private HibernateTemplate hibernateTemplate;
    @Autowired
	private SessionFactory sessionFactory;
	@Override
	public List<RoleFuncItemView> findAll(int currentTotal, int current, String role) throws Exception {
		// TODO Auto-generated method stub
		Session session = sessionFactory.openSession();
		String hql="";
		if(role=="0") {	
		//String hql="select r.roleid,r.orderNum,f.fId,f.fName from TRoleFuncEntity inner left jion  TFuncEntity f on r.funcId= f.fId";
		 hql="select new cn.softlq.ss2h.vo.RoleFuncItemView(rf.roleid,r.rname,rf.orderNum,rf.funcId,f.fName) from TRoleFuncEntity rf ,TRoleEntity r ,TFuncEntity f  where rf.roleid = r.roleid and  rf.funcId = f.fId";
		
		}
		else {
		 hql="select new cn.softlq.ss2h.vo.RoleFuncItemView(rf.roleid,r.rname,rf.orderNum,rf.funcId,f.fName) from TRoleFuncEntity rf,TRoleEntity r,TFuncEntity f where rf.roleid = r.roleid and rf.funcId = f.fId and rf.roleid = ?";
		
		}
		   Query query = session.createQuery(hql);
		 if(role!="0") {
	       query.setParameter(0, role);
		 }
	       query.setFirstResult(currentTotal * (current - 1));
	       query.setMaxResults(currentTotal);
	       List<RoleFuncItemView> list = query.list();
	       session.close();
	       return list;
		
		
	}

	@Override
	public Integer findTotal(String role) throws Exception{
		// TODO Auto-generated method stub
		Session session = sessionFactory.openSession();
		String hql="";
		if(role=="0") {	
		//String hql="select r.roleid,r.orderNum,f.fId,f.fName from TRoleFuncEntity inner left jion  TFuncEntity f on r.funcId= f.fId";
		 hql="select new cn.softlq.ss2h.vo.RoleFuncItemView(rf.roleid,r.rname,rf.orderNum,rf.funcId,f.fName) from TRoleFuncEntity rf ,TRoleEntity r ,TFuncEntity f  where rf.roleid = r.roleid and  rf.funcId = f.fId";
		
		}
		else {
		 hql="select new cn.softlq.ss2h.vo.RoleFuncItemView(rf.roleid,r.rname,rf.orderNum,rf.funcId,f.fName) from TRoleFuncEntity rf,TRoleEntity r,TFuncEntity f where rf.roleid = r.roleid and rf.funcId = f.fId and rf.roleid = ?";
		
		}
		   Query query = session.createQuery(hql);
		 if(role!="0") {
	       query.setParameter(0, role);
		 }
	       List<RoleFuncItemView> list = query.list();
	       session.close();
	       return list.size();
	}

	@Override 
	public void delete (TRoleFuncEntity tRoleFuncEntity)  {
		// TODO Auto-generated method stub
		Session session = sessionFactory.openSession();
		Transaction tran = session.beginTransaction() ;
		
		String hql="delete from TRoleFuncEntity r where r.roleid = ? and r.orderNum = ? and r.funcId = ?";
		Query query = session.createQuery(hql);
	    query.setString(0, tRoleFuncEntity.getRoleid());
	    query.setLong(1,tRoleFuncEntity.getOrderNum());
	    query.setString(2,tRoleFuncEntity.getFuncId());
	    
	    query.executeUpdate() ;     
        tran.commit() ;    ;
	}
	@Override
	public void add(TRoleFuncEntity tRoleFuncEntity) throws Exception {
		// TODO Auto-generated method stub
		hibernateTemplate.save(tRoleFuncEntity);
	}

	@Override
	public List<TFuncEntity> findnofuncbyrole(String role) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.openSession();
		 String hql="from TFuncEntity fc where fc.fId not in (select rf.funcId from TRoleFuncEntity rf,TRoleEntity r,TFuncEntity f where rf.roleid = r.roleid and rf.funcId = f.fId and rf.roleid = ?)";
		   Query query = session.createQuery(hql);
	       query.setParameter(0, role);
	       List<TFuncEntity> list = query.list();
	       session.close();
	       return list;
	}

	@Override
	public Long findMaxOrderNUM(String role) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
				Session session = sessionFactory.openSession();
				String  hql="select new cn.softlq.ss2h.vo.RoleFuncItemView(rf.roleid,r.rname,rf.orderNum,rf.funcId,f.fName) from TRoleFuncEntity rf,TRoleEntity r,TFuncEntity f where rf.roleid = r.roleid and rf.funcId = f.fId and rf.roleid = ?";
				  Query query = session.createQuery(hql);
			     query.setParameter(0, role);
			    List<RoleFuncItemView> list = query.list();
			   session.close();
			 return (long) list.size();
	}




}
