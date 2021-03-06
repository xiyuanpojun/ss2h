package com.hill.gwyb.service;

import com.hill.gwyb.po.TPCodeEntity;

import java.util.Map;

public interface ISConfigService {
    void getVisibleConfig() throws Exception;

    Map<String, Object> configFindAll(int currentTotal, int current, String typeId) throws Exception;

    Map<String, Object> configAdd(TPCodeEntity tpCodeEntity) throws Exception;

    Map<String, Object> configDelete(TPCodeEntity tpCodeEntity) throws Exception;
}
