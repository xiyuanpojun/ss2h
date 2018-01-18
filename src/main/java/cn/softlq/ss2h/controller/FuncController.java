package cn.softlq.ss2h.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;

import cn.softlq.ss2h.po.TFuncEntity;
import cn.softlq.ss2h.service.IFuncService;
@Controller
public class FuncController extends ActionSupport{ 
//自动装配
@Autowired
private IFuncService funcService; 
//json数据
private Map<String,Object> dataMap;
//获取请求中传过来的funcentity对象
private TFuncEntity funcentity;
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
	System.out.println("请求的 功能名称为------------"+funcentity.toString());
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
	 System.out.println("哩哩啦啦哩哩啦啦哩哩啦啦哩哩啦啦哩哩啦啦哩哩啦啦哩哩啦啦零零落落"+funcentity);
	dataMap=funcService.add(funcentity);
	return ActionSupport.SUCCESS;
}

}
