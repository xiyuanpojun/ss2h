package com.hill.gwyb.dao.impl;

import com.hill.gwyb.dao.IConfigDao;
import com.hill.gwyb.po.TSimpleColEntity;
import com.hill.gwyb.po.TSurveyColEntity;
import com.hill.gwyb.po.TSurveyTypeEntity;
import com.hill.gwyb.vo.ConfigItemView;
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
public class ConfigDaoImpl implements IConfigDao {
    @Autowired
    private HibernateTemplate hibernateTemplate;
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<TSurveyTypeEntity> getVisibleConfig() throws Exception {
        Session session = sessionFactory.openSession();
        String hql = "FROM TSurveyTypeEntity";
        Query query = session.createQuery(hql);
        List<TSurveyTypeEntity> list = query.list();
        session.close();
        return list;
    }

    @Override
    public List<TSurveyColEntity> configFindAll(int currentTotal, int current, String typeId) throws Exception {
        Session session = sessionFactory.openSession();
        String hql;
        if (Integer.parseInt(typeId) < 1) {
            hql = "FROM TSurveyTypeEntity";
        } else {
            hql = "FROM TSurveyTypeEntity WHERE typeId = ?1";
        }
        Query query = session.createQuery(hql);
        List<TSurveyColEntity> items = new ArrayList<>();
        if (Integer.parseInt(typeId) >= 1) {
            query.setParameter("1", typeId);
            //获取唯一的专项
            TSurveyTypeEntity result = (TSurveyTypeEntity) query.uniqueResult();
            //根据typeId查找 currentTotal * (current - 1) 到 currentTotal * (current - 1)+currentTotal数据

            //找到所有相关的数据
            Query query2 = session.createSQLQuery("SELECT COL,COL_NAME FROM T_SURVEY_COL WHERE TYPE_ID = ?1")
                    .setParameter("1", result.getTypeId())
                    .addScalar("COL", StandardBasicTypes.STRING)
                    .addScalar("COL_NAME", StandardBasicTypes.STRING);

            //取出一条数据
            for (Object o : query2.list()) {
                Object[] objects = (Object[]) o;
                String col = (String) objects[0];
                String colName = (String) objects[1];
                TSurveyColEntity surveyColEntity = new TSurveyColEntity(typeId, col, colName, result.getSurveyType(), result.getShowNum());
                items.add(surveyColEntity);
            }
        } else {
            //获取所有的专项
            List<TSurveyTypeEntity> list = query.list();
            //获取到多少条就退出。
            for (int i = 0; i < list.size(); i++) {
                TSurveyTypeEntity result = list.get(i);
                //找到所有相关的数据
                Query query2 = session.createSQLQuery("SELECT COL,COL_NAME FROM T_SURVEY_COL WHERE TYPE_ID = ?1")
                        .setParameter("1", result.getTypeId())
                        .addScalar("COL", StandardBasicTypes.STRING)
                        .addScalar("COL_NAME", StandardBasicTypes.STRING);

                //取出一条数据
                for (Object o : query2.list()) {
                    Object[] objects = (Object[]) o;
                    String col = (String) objects[0];
                    String colName = (String) objects[1];
                    TSurveyColEntity surveyColEntity = new TSurveyColEntity(typeId, col, colName, result.getSurveyType(), result.getShowNum());
                    items.add(surveyColEntity);
                }
            }
        }
        session.close();
        return items;
    }

    @Override
    public Integer configDelete(ConfigItemView configItemView) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        String sql = "DELETE FROM T_SURVEY_COL WHERE TYPE_ID = ?1 AND COL = ?2 AND  COL_NAME = ?3";
        NativeQuery query = session.createSQLQuery(sql);

        //根据调查类型名称值找到id
        TSurveyTypeEntity surveyTypeEntity = configFindOne(configItemView.getSurveyType());

        //删除
        query.setParameter("1", surveyTypeEntity.getTypeId());
        query.setParameter("2", configItemView.getCol());
        query.setParameter("3", configItemView.getColName());
        Integer result = query.executeUpdate();
        transaction.commit();
        session.close();
        return result;
    }

    @Override
    public TSurveyTypeEntity configFindOne(String surveyType) throws Exception {
        Session session = sessionFactory.openSession();
        //根据调查类型名称值找到id
        Query query = session.createQuery("FROM TSurveyTypeEntity WHERE surveyType = ?1");
        query.setParameter("1", surveyType);
        TSurveyTypeEntity surveyTypeEntity = (TSurveyTypeEntity) query.uniqueResult();
        session.close();
        return surveyTypeEntity;
    }

    @Override
    public void configUpdate(TSurveyTypeEntity surveyTypeEntity) throws Exception {
        hibernateTemplate.update(surveyTypeEntity);
    }

    @Override
    public List<TSimpleColEntity> configFindTabCol(String typeId) throws Exception {
        Session session = sessionFactory.openSession();
        Query query = session.createQuery("FROM TSurveyTypeEntity WHERE typeId = ?1");
        query.setParameter("1", typeId);
        TSurveyTypeEntity surveyTypeEntity = (TSurveyTypeEntity) query.uniqueResult();
        String sql = "SELECT COL,COL_NAME FROM T_SIMPLE_COL WHERE TAB = ?1";
        NativeQuery query2 = session.createSQLQuery(sql)
                .setParameter("1", surveyTypeEntity.getTab())
                .addScalar("COL", StandardBasicTypes.STRING)
                .addScalar("COL_NAME", StandardBasicTypes.STRING);
        List<TSimpleColEntity> list = new ArrayList<>();
        for (Object o : query2.list()) {
            Object[] objects = (Object[]) o;
            String col = (String) objects[0];
            String colName = (String) objects[1];
            TSimpleColEntity simpleColEntity = new TSimpleColEntity(col, colName);
            list.add(simpleColEntity);
        }
        session.close();
        return list;
    }

    @Override
    public void configAdd(TSurveyTypeEntity surveyTypeEntity, String[] fruit) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        String sql = "INSERT INTO T_SURVEY_COL(TYPE_ID, COL, COL_NAME) VALUES (?1,?2,?3)";
        for (String str : fruit) {
            NativeQuery sqlQuery = session.createSQLQuery(sql);

            sqlQuery.setParameter("1", surveyTypeEntity.getTypeId());
            sqlQuery.setParameter("2", str);

            String sql2 = "SELECT COL_NAME FROM T_SIMPLE_COL WHERE TAB =?1 AND COL = ?2";
            NativeQuery query2 = session.createSQLQuery(sql2)
                    .setParameter("1", surveyTypeEntity.getTab())
                    .setParameter("2", str)
                    .addScalar("COL_NAME", StandardBasicTypes.STRING);

            String cloName = (String) query2.list().get(0);
            sqlQuery.setParameter("3", cloName);
            sqlQuery.executeUpdate();
        }
        transaction.commit();
        session.close();
    }

    @Override
    public void configDeleteAll(TSurveyTypeEntity surveyTypeEntity) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        String sql = "DELETE FROM T_SURVEY_COL WHERE TYPE_ID = ?1";
        NativeQuery sqlQuery = session.createSQLQuery(sql).setParameter("1", surveyTypeEntity.getTypeId());
        sqlQuery.executeUpdate();
        transaction.commit();
        session.close();
    }
}
