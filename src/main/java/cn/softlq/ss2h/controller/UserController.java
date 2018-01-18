package cn.softlq.ss2h.controller;

import cn.softlq.ss2h.po.TUserEntity;
import cn.softlq.ss2h.service.IUserService;
import com.opensymphony.xwork2.ActionSupport;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class UserController extends ActionSupport {
    //json数据map
    private Map<String, Object> dataMap;
    //封装的用户信息
    private TUserEntity userEntity;
    //服务层接口对象
    @Autowired
    private IUserService userService;
    //分页
    private int currentTotal, current;
    //省份id
    private String province;

    public Map<String, Object> getDataMap() {
        return dataMap;
    }

    public TUserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(TUserEntity userEntity) {
        this.userEntity = userEntity;
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

    //登陆
    public String login() throws Exception {
        dataMap = userService.userLogin(userEntity);
        return ActionSupport.SUCCESS;
    }

    //退出
    public String logon() throws Exception {
        dataMap = userService.userLogon(userEntity);
        return ActionSupport.SUCCESS;
    }

    //显示用户界面，并把用户功能列表获取。
    public String showUserPage() throws Exception {
        Thread.sleep(1000);
        if (userService.checkLogin()) {
            //获取用户功能列表,存放到request中。
            userService.userFunction();
            return ActionSupport.SUCCESS;
        } else {
            return ActionSupport.ERROR;
        }
    }

    // 显示用户列表界面，并获取用户可见省份。
    public String showUserList() throws Exception {
        //获取省份，存放到request中。
        userService.getVisibleProvince();
        return ActionSupport.SUCCESS;
    }

    //添加用户
    public String add() throws Exception {
        dataMap = userService.userAdd(userEntity);
        return ActionSupport.SUCCESS;
    }

    //修改用户
    public String update() throws Exception {
        dataMap = userService.userUpdate(userEntity);
        return ActionSupport.SUCCESS;
    }

    //带条见分页查询用户
    public String findAll() throws Exception {
        dataMap = userService.userFindAll(currentTotal, current, province);
        return ActionSupport.SUCCESS;
    }

    //删除用户
    public String delete() throws Exception {
        dataMap = userService.userDelete(userEntity);
        return ActionSupport.SUCCESS;
    }

    //查找一个用户
    public String findOne() throws Exception {
        dataMap = userService.userFindOne(userEntity);
        return ActionSupport.SUCCESS;
    }

    //得到用户的登陆状态
    public String checkLogin() throws Exception {
        dataMap = new HashMap<>();
        dataMap.put("isLogin", userService.checkLogin());
        return ActionSupport.SUCCESS;
    }
}
