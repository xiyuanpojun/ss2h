package cn.softlq.ss2h.dao;

import java.util.List;
import java.util.Map;

import cn.softlq.ss2h.po.TFuncEntity;
import cn.softlq.ss2h.po.TSurveyTypeEntity;

public interface IFuncDao {
//分页查找功能列表
List<TFuncEntity> findFuncAll(int currentTotal, int current);
//查找所有的功能记录总数
Integer findFuncTotal();
//查找单个功能
TFuncEntity findOne(String fId);
//修改单个功能
void funcUpdate(TFuncEntity funcentity) throws Exception;
//删除单个功能
void delete(TFuncEntity funcentity) throws Exception;
//添加功能列表
void add(TFuncEntity funcentity) throws Exception;
}
