package com.hill.gwyb.dao;

import java.util.List;

import com.hill.gwyb.po.TFuncEntity;
import com.hill.gwyb.po.TOrgEntity;

public interface IOrgDao {
//分页查找功能列表
List<TOrgEntity> findAll(int currentTotal, int current, String porgid);
//查找所有的功能记录总数
Integer findTotal(String porgid);
//根据id查找单个功能
TOrgEntity findOneById(String oid);
//根据机构查找 是否存在该机构 
boolean findOne(TOrgEntity orgentity);
//删除单个功能
void delete(TOrgEntity orgEntity) throws Exception;
//添加功能列表
void add(TOrgEntity orgEntity) throws Exception;
List<TOrgEntity> showporglist() throws Exception;
//根据上级查询下级机构
List<TOrgEntity> showcitylist(String porgid);
}
