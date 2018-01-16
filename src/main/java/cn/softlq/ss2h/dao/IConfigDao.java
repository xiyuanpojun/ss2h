package cn.softlq.ss2h.dao;

import cn.softlq.ss2h.po.TSimpleColEntity;
import cn.softlq.ss2h.po.TSurveyColEntity;
import cn.softlq.ss2h.po.TSurveyTypeEntity;
import cn.softlq.ss2h.vo.ConfigItemView;

import java.util.List;

public interface IConfigDao {
    //得到用户的可见的省份信息
    List<TSurveyTypeEntity> getVisibleConfig() throws Exception;

    //带条件分页查询
    List<TSurveyColEntity> configFindAll(int currentTotal, int current, String typeId) throws Exception;

    //删除用户
    Integer configDelete(ConfigItemView configItemView) throws Exception;

    //查找一个配置
    TSurveyTypeEntity configFindOne(String surveyType) throws Exception;

    //更新一个
    void configUpdate(TSurveyTypeEntity surveyTypeEntity) throws Exception;

    //通过typeId查找表字段
    List<TSimpleColEntity> configFindTabCol(String typeId) throws Exception;

    //添加配置
    void configAdd(TSurveyTypeEntity surveyTypeEntity, String[] fruit) throws Exception;

    //删除所有配置
    void configDeleteAll(TSurveyTypeEntity surveyTypeEntity) throws Exception;

}
