package com.hill.gwyb.dao;

import com.hill.gwyb.po.TPCodeEntity;

import java.util.List;

public interface ISConfigDao {
    List<TPCodeEntity> getVisibleConfig() throws Exception;

    //带条件分页查询
    List<TPCodeEntity> configFindAll(int currentTotal, int current, String typeId) throws Exception;

    Integer configFindAllTotal(String typeId) throws Exception;

    TPCodeEntity configFindOne(TPCodeEntity tpCodeEntity) throws Exception;

    void configAdd(TPCodeEntity tpCodeEntity) throws Exception;

    void configDelete(TPCodeEntity tpCodeEntity) throws Exception;
}
