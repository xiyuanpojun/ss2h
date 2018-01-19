package cn.softlq.ss2h.service;

import cn.softlq.ss2h.po.TSurveyUserEntity;

import java.util.Map;

public interface ISurveyorService {
    //获取可见城市
    void getVisibleProvince() throws Exception;

    //分页查询
    Map<String, Object> userFindAll(int currentTotal, int current, String province) throws Exception;

    Map<String, Object> userAdd(TSurveyUserEntity surveyUserEntity) throws Exception;

    Map<String, Object> userDelete(TSurveyUserEntity surveyUserEntity) throws Exception;

    Map<String, Object> userFindOne(TSurveyUserEntity surveyUserEntity) throws Exception;

    Map<String, Object> userUpdate(TSurveyUserEntity surveyUserEntity) throws Exception;

}
