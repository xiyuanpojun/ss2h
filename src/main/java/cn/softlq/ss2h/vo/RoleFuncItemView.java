package cn.softlq.ss2h.vo;

public class RoleFuncItemView {
 private String role_id;
 private String role_name;
 private int order;
 private String func_id;
 private String func_name;
 public RoleFuncItemView(String role_id,String role_name,int order,String func_id,String func_name) {
 this.role_id=role_id;
 this.role_name=role_name;
 this.order=order;
 this.func_id=func_id;
 this.func_name=func_name;
}
public String getFunc_id() {
	return func_id;
}
public void setFunc_id(String func_id) {
	this.func_id = func_id;
}
public String getFunc_name() {
	return func_name;
}
public void setFunc_name(String func_name) {
	this.func_name = func_name;
}

public String getRole_id() {
	return role_id;
}
public void setRole_id(String role_id) {
	this.role_id = role_id;
}
public String getRole_name() {
	return role_name;
}
public void setRole_name(String role_name) {
	this.role_name = role_name;
}
public int getOrder() {
	return order;
}
public void setOrder(int order) {
	this.order = order;
}
}
