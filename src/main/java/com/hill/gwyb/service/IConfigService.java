package com.hill.gwyb.service;

import com.hill.gwyb.vo.ConfigItemView;

import java.util.Map;

public interface IConfigService {
    //得到用户可见专项信息
    void getVisibleConfig() throws Exception;

    //带条件分页查询专项
    Map<String, Object> configFindAll(int currentTotal, int current, String typeId) throws Exception;

    //删除配置
    Map<String, Object> configDelete(ConfigItemView configItemView) throws Exception;

    //更新配置
    Map<String, Object> configUpdate(ConfigItemView configItemView) throws Exception;

    //获取表字段名称
    Map<String, Object> configFindTabCol(String typeId) throws Exception;

    //添加配置
    Map<String, Object> configAdd(ConfigItemView configItemView, String fruits) throws Exception;

}
