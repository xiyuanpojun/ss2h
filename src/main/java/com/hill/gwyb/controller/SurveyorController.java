package com.hill.gwyb.controller;

import com.hill.gwyb.po.TSurveyUserEntity;
import com.hill.gwyb.service.ISurveyorService;
import com.opensymphony.xwork2.ActionSupport;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class SurveyorController extends ActionSupport {
    //json数据map
    private Map<String, Object> dataMap;
    @Autowired
    private ISurveyorService surveyorService;
    private TSurveyUserEntity surveyUserEntity;
    //分页
    private int currentTotal, current;
    //省份id
    private String province;

    public Map<String, Object> getDataMap() {
        return dataMap;
    }

    public ISurveyorService getSurveyorService() {
        return surveyorService;
    }

    public void setSurveyorService(ISurveyorService surveyorService) {
        this.surveyorService = surveyorService;
    }

    public TSurveyUserEntity getSurveyUserEntity() {
        return surveyUserEntity;
    }

    public void setSurveyUserEntity(TSurveyUserEntity surveyUserEntity) {
        this.surveyUserEntity = surveyUserEntity;
    }

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

    public String showSurveyorList() throws Exception {
        //获取城市，存放到request中。
        surveyorService.getVisibleProvince();
        return ActionSupport.SUCCESS;
    }

    //带条见分页查询用户
    public String findAll() throws Exception {
        dataMap = surveyorService.userFindAll(currentTotal, current, province);
        return ActionSupport.SUCCESS;
    }

    public String add() throws Exception {
        dataMap = surveyorService.userAdd(surveyUserEntity);
        return ActionSupport.SUCCESS;
    }

    public String delete() throws Exception {
        dataMap = surveyorService.userDelete(surveyUserEntity);
        return ActionSupport.SUCCESS;
    }

    //查找一个用户
    public String findOne() throws Exception {
        dataMap = surveyorService.userFindOne(surveyUserEntity);
        return ActionSupport.SUCCESS;
    }

    public String update() throws Exception {
        dataMap = surveyorService.userUpdate(surveyUserEntity);
        return ActionSupport.SUCCESS;
    }
}
