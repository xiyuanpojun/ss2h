package com.hill.gwyb.controller;

import java.util.Map;

import com.hill.gwyb.po.TFuncEntity;
import com.hill.gwyb.service.IFuncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;

@Controller
public class FuncController extends ActionSupport{ 
//自动装配
@Autowired
private IFuncService funcService;
//json数据
private Map<String,Object> dataMap;
//获取请求中传过来的funcentity对象
private TFuncEntity funcentity;
//用于检查新增功能是否的存在的id
private String fId;
//分页
private int currentTotal, current;
public TFuncEntity getFuncentity() {
	return funcentity;
}
public void setFuncentity(TFuncEntity funcentity) {
	this.funcentity = funcentity;
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

public Map<String, Object> getDataMap() {
    return dataMap;
}
public String getfId() {
	return fId;
}
public void setfId(String fId) {
	this.fId = fId;
}
//查询所有功能数据 
public String findFuncAll() throws Exception {
	dataMap=funcService.findFuncAll(currentTotal,current);
	return ActionSupport.SUCCESS;
}
//显示 功能页面
public String showfuncpage() {
	return ActionSupport.SUCCESS;
}
//查询单个功能的信息
public String findOne() throws Exception {
	dataMap=funcService.findOne(funcentity);
	return ActionSupport.SUCCESS;
}
//修改功能
public String update() throws Exception {
	dataMap=funcService.update(funcentity);
	return ActionSupport.SUCCESS;
}
//删除功能
public String delete() throws Exception{
	dataMap=funcService.delete(funcentity);
	return ActionSupport.SUCCESS;
}
//添加功能
public String add() throws Exception {
	dataMap=funcService.add(funcentity);
	return ActionSupport.SUCCESS;
}
//根据id异步校验
public String checkfId() {
	 dataMap=funcService.checkfId(fId);
	return ActionSupport.SUCCESS;
}


}
