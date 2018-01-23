package com.hill.gwyb.dao;

import com.hill.gwyb.po.TOrgEntity;
import com.hill.gwyb.po.TSurveyUserEntity;

import java.util.List;

public interface ISurveyorDao {
    //查找用户
    TSurveyUserEntity userFindOne(String userid) throws Exception;

    //添加用户
    void userAdd(TSurveyUserEntity entity) throws Exception;

    List<TOrgEntity> getVisibleProvince(String orgid) throws Exception;

    List<TSurveyUserEntity> userFindAll(int currentotal, int current, String province, String userId) throws Exception;

    Integer userFindAllTotal(String province, String userId) throws Exception;

    String userProvinceName(String orgid) throws Exception;

    void userDelete(TSurveyUserEntity result) throws Exception;

    void userUpdate(TSurveyUserEntity entity) throws Exception;

    Integer getDisrmNumber(TSurveyUserEntity entity) throws Exception;
}
