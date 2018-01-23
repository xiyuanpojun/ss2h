package com.hill.gwyb.controller;

import java.util.Map;

import com.hill.gwyb.po.TFuncEntity;
import com.hill.gwyb.po.TOrgEntity;
import com.hill.gwyb.service.IFuncService;
import com.hill.gwyb.service.IOrgService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;

@Controller
public class OrgController extends ActionSupport{ 
//自动装配
@Autowired
private IOrgService orgService;
//json数据
private Map<String,Object> dataMap;
//获取请求中传过来的orgentity对象
private TOrgEntity orgentity;
public TOrgEntity getOrgentity() {
	return orgentity;
}

public void setOrgentity(TOrgEntity orgentity) {
	this.orgentity = orgentity;
}
private String porgid;
public String getPorgid() {
	return porgid;
}

public void setPorgid(String porgid) {
	this.porgid = porgid;
}
//用于检查新增机构是否的存在的id
private String oid;
//分页
private int currentTotal, current;

public String getOid() {
	return oid;
}

public void setOid(String oid) {
	this.oid = oid;
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


public String findAll() throws Exception {
	dataMap=orgService.findAll(currentTotal,current,porgid);
	return ActionSupport.SUCCESS;
}
//显示 机构列表页面
public String showorgpage() throws Exception {
	orgService.showporglist();
	return ActionSupport.SUCCESS;
}
//查询单个机构的信息
public String findOne() throws Exception {
	dataMap= orgService.findOne(orgentity);
	return ActionSupport.SUCCESS;
}
//删除单个机构
public String delete() throws Exception{
	dataMap=orgService.delete(orgentity);
	return ActionSupport.SUCCESS;
}
//添加机构
public String add() throws Exception {
	dataMap=orgService.add(orgentity);
	return ActionSupport.SUCCESS;
}

//根据id异步校验
public String checkId() {
	 dataMap=orgService.checkId(oid);
	return ActionSupport.SUCCESS;
}



}
