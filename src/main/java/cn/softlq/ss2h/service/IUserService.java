package cn.softlq.ss2h.service;

import cn.softlq.ss2h.po.TUserEntity;

import java.util.Map;

public interface IUserService {
    //用户登陆
    Map<String, Object> userLogin(TUserEntity userEntity) throws Exception;

    //用户退出
    Map<String, Object> userLogon(TUserEntity userEntity) throws Exception;

    //检测用户登陆
    boolean checkLogin();

    //添加用户
    Map<String, Object> userAdd(TUserEntity userEntity) throws Exception;

    //删除用户
    Map<String, Object> userDelete(TUserEntity userEntity) throws Exception;

    //更新用户
    Map<String, Object> userUpdate(TUserEntity userEntity) throws Exception;

    //带条见分页查询用户
    Map<String, Object> userFindAll(int currentTotal, int current, String province) throws Exception;

    //查找一个用户的消息
    Map<String, Object> userFindOne(TUserEntity userEntity) throws Exception;

    //得到用户功能列表
    void userFunction() throws Exception;

    //得到用户的可见的省份信息
    void getVisibleProvince() throws Exception;
}
