package cn.softlq.ss2h.controller;

import cn.softlq.ss2h.service.IConfigService;
import cn.softlq.ss2h.vo.ConfigItemView;
import com.opensymphony.xwork2.ActionSupport;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class ConfigController extends ActionSupport {
    //json数据map
    private Map<String, Object> dataMap;
    @Autowired
    private IConfigService configService;
    private ConfigItemView configItemView;
    private String fruits;
    //分页
    private int currentTotal, current;
    //专项配置id
    private String typeId;

    public Map<String, Object> getDataMap() {
        return dataMap;
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

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public ConfigItemView getConfigItemView() {
        return configItemView;
    }

    public void setConfigItemView(ConfigItemView configItemView) {
        this.configItemView = configItemView;
    }

    public String getFruits() {
        return fruits;
    }

    public void setFruits(String fruits) {
        this.fruits = fruits;
    }

    //显示专项配置列表页面，并获取用户可见专项。
    public String showConfigList() throws Exception {
        //获取专项
        configService.getVisibleConfig();
        return ActionSupport.SUCCESS;
    }

    //带条件分页查询
    public String findAll() throws Exception {
        dataMap = configService.configFindAll(currentTotal, current, typeId);
        return ActionSupport.SUCCESS;
    }

    //删除配置
    public String delete() throws Exception {
        dataMap = configService.configDelete(configItemView);
        return ActionSupport.SUCCESS;
    }

    //修改配置
    public String update() throws Exception {
        dataMap = configService.configUpdate(configItemView);
        return ActionSupport.SUCCESS;
    }

    //查找表字段
    public String findTabCol() throws Exception {
        dataMap = configService.configFindTabCol(typeId);
        return ActionSupport.SUCCESS;
    }

    //增加配置
    public String add() throws Exception {
        dataMap = configService.configAdd(configItemView, fruits);
        return ActionSupport.SUCCESS;
    }
}
