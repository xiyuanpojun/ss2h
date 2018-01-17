package cn.softlq.ss2h.dao;

import cn.softlq.ss2h.po.TFuncEntity;
import cn.softlq.ss2h.po.TOrgEntity;
import cn.softlq.ss2h.po.TUserEntity;
import cn.softlq.ss2h.po.TUserLoginEntity;

import java.util.List;

public interface IUserDao {
    //查找用户
    TUserEntity userFindOne(String userid) throws Exception;

    //用户登陆信息记录
    void userLoginInfoExec(TUserLoginEntity userLoginEntity, boolean isend) throws Exception;

    //检测用户是否已经登陆
    Boolean userLoginInfoCheck(TUserLoginEntity userLoginEntity) throws Exception;

    //添加用户
    void userAdd(TUserEntity userEntity) throws Exception;

    //删除用户
    void userDelete(TUserEntity userEntity) throws Exception;

    //更新用户
    void userUpdate(TUserEntity userEntity) throws Exception;

    //带条见分页查询用户
    List<TUserEntity> userFindAll(int currentotal, int current, String province) throws Exception;

    //带条件分页查询时的总数量
    Integer userFindAllTotal(String province) throws Exception;

    //得到用户功能列表
    List<TFuncEntity> userFunction(String urole) throws Exception;

    //得到用户的可见的省份信息
    List<TOrgEntity> getVisibleProvince(String urole, String orgid) throws Exception;

    //得到用户的省份名称
    String userProvinceName(String orgid) throws Exception;
}
