package com.hill.gwyb.controller;

import com.hill.gwyb.dao.ISurveyDao;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class SurveyController extends ActionSupport {

    public SurveyController() {
        response.setCharacterEncoding("utf-8");
    }

    @Autowired
    private HttpSession session;
    private HttpServletRequest request = ServletActionContext.getRequest();
    private HttpServletResponse response = ServletActionContext.getResponse();
    @Autowired
    private ISurveyDao surveyDao;

    private String tab;
    private String rowv;
    private String distRes;
    private String diaocy;
    private String invRes;
    private String faultRes;

    private String page;
    private String limit;
    private String city;
    private String custType;

    private String address;
    private String dist;

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    public String getRowv() {
        return rowv;
    }

    public void setRowv(String rowv) {
        this.rowv = rowv;
    }

    public String getDistRes() {
        return distRes;
    }

    public void setDistRes(String distRes) {
        this.distRes = distRes;
    }

    public String getDiaocy() {
        return diaocy;
    }

    public void setDiaocy(String diaocy) {
        this.diaocy = diaocy;
    }

    public String getInvRes() {
        return invRes;
    }

    public void setInvRes(String invRes) {
        this.invRes = invRes;
    }

    public String getFaultRes() {
        return faultRes;
    }

    public void setFaultRes(String faultRes) {
        this.faultRes = faultRes;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCustType(String custType) {
        this.custType = custType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }

    public String getCity() throws IOException, SQLException {
        PrintWriter pw = response.getWriter();
        String orgid = (String) request.getSession().getAttribute("uorg");
        pw.write(surveyDao.getCityList(orgid));
        pw.close();
        return null;
    }

    public String subDist() throws IOException, SQLException {
        PrintWriter pw = response.getWriter();
        surveyDao.saveDist(tab, rowv, distRes, diaocy);
        pw.write("ok");
        pw.close();
        return null;
    }

    public String subInvit() throws IOException, SQLException {
        PrintWriter pw = response.getWriter();
        Object userId = request.getSession().getAttribute("userId");
        surveyDao.saveInvit(tab, rowv, invRes, faultRes, userId.toString());
        pw.write("ok");
        pw.close();
        return null;
    }

    public String getInvitData() throws IOException, SQLException {
        PrintWriter pw = response.getWriter();
        Object userId = request.getSession().getAttribute("userId");
        int start = Integer.parseInt(page);
        int end = Integer.parseInt(limit);
        StringBuilder sql = new StringBuilder();
        if (!"".equals(city) && null != city) {
            sql.append(" AND EXISTS (SELECT 1 FROM T_ORG O WHERE O.ORGID='" + city + "' AND A.CITY LIKE '%'||O.ORGNAME||'%') ");
        }
        if (!"".equals(custType) && null != custType) {
            sql.append(" AND EXISTS (SELECT 1 FROM T_P_CODE P WHERE P.PID='" + custType + "' AND A.YDLB LIKE '%'||P.PNAME||'%') ");
        }
        pw.write(surveyDao.getSurveyInvData(tab, sql.toString(), userId.toString(), end * (start - 1), end * start));
        pw.close();
        return null;
    }

    public String getData() throws IOException, SQLException {
        PrintWriter pw = response.getWriter();
        String orgid = (String) request.getSession().getAttribute("uorg");
        StringBuilder sql = new StringBuilder();
        if (!"".equals(city) && null != city) {
            sql.append(" AND EXISTS (SELECT 1 FROM T_ORG O WHERE O.ORGID='" + city + "' AND A.CITY LIKE '%'||O.ORGNAME||'%') ");
        }
        if (!"".equals(custType) && null != custType) {
            sql.append(" AND EXISTS (SELECT 1 FROM T_P_CODE P WHERE P.PID='" + custType + "' AND A.YDLB LIKE '%'||P.PNAME||'%') ");
        }
        pw.write(surveyDao.getSurveyData(tab, orgid, sql.toString()));
        pw.close();
        return null;
    }

    public String getCol() throws IOException, SQLException {
        PrintWriter pw = response.getWriter();
        pw.write(surveyDao.getSurveyCol(tab));
        pw.close();
        return null;
    }

    public String getStype() throws IOException, SQLException {
        PrintWriter pw = response.getWriter();
        pw.write(surveyDao.getSurveyType());
        pw.close();
        return null;
    }

    public String getSurveyUser() throws IOException, SQLException {
        PrintWriter pw = response.getWriter();
        Object userId = request.getSession().getAttribute("userId");
        pw.write(surveyDao.getSurveyUser(userId.toString()));
        pw.close();
        return null;
    }

    public String getCustType() throws IOException, SQLException {
        PrintWriter pw = response.getWriter();
        pw.write(surveyDao.getCustType("客户类型"));
        pw.close();
        return null;
    }

    public String getFault() throws IOException, SQLException {
        PrintWriter pw = response.getWriter();
        pw.write(surveyDao.getCustType("预约失败原因"));
        pw.close();
        return null;
    }
}
