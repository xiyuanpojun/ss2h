package com.hill.gwyb.controller;

import com.hill.gwyb.po.TPCodeEntity;
import com.hill.gwyb.service.ISConfigService;
import com.opensymphony.xwork2.ActionSupport;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class SConfigController extends ActionSupport {
    //分页
    private int currentTotal, current;
    //专项配置id
    private String province;
    //json数据map
    private Map<String, Object> dataMap;
    @Autowired
    private ISConfigService isConfigService;
    private TPCodeEntity tpCodeEntity;

    public int getCurrentTotal() {
        return currentTotal;
    }

    public void setCurrentTotal(int currentTotal) {
        this.currentTotal = currentTotal;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Map<String, Object> getDataMap() {
        return dataMap;
    }

    public TPCodeEntity getTpCodeEntity() {
        return tpCodeEntity;
    }

    public void setTpCodeEntity(TPCodeEntity tpCodeEntity) {
        this.tpCodeEntity = tpCodeEntity;
    }

    public String showConfigList() throws Exception {
        isConfigService.getVisibleConfig();
        return ActionSupport.SUCCESS;
    }

    public String findAll() throws Exception {
        dataMap = isConfigService.configFindAll(currentTotal, current, province);
        return ActionSupport.SUCCESS;
    }

    public String add() throws Exception {
        dataMap = isConfigService.configAdd(tpCodeEntity);
        return ActionSupport.SUCCESS;
    }

    public String delete() throws Exception {
        dataMap = isConfigService.configDelete(tpCodeEntity);
        return ActionSupport.SUCCESS;
    }
}
