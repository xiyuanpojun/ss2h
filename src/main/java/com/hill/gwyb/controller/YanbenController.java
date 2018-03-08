package com.hill.gwyb.controller;

import com.hill.gwyb.dao.ISurveyDao;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class YanbenController extends ActionSupport {
    private HttpServletRequest request = ServletActionContext.getRequest();
    private HttpServletResponse response = ServletActionContext.getResponse();
    private String tab;
    private String city;
    private String custType;
    private String address;
    private String orgid;
    private int dist;
    @Autowired
    private ISurveyDao surveyDao;

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCustType() {
        return custType;
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

    public int getDist() {
        return dist;
    }

    public void setDist(int dist) {
        this.dist = dist;
    }

    public String getOrgid() {
        return orgid;
    }

    public void setOrgid(String orgid) {
        this.orgid = orgid;
    }

    public String getData() throws IOException, SQLException {
        response.setCharacterEncoding("utf-8");
        PrintWriter pw = response.getWriter();
        StringBuilder sql = new StringBuilder();
        if (!"".equals(city) && null != city) {
            sql.append(" AND EXISTS (SELECT 1 FROM T_ORG O WHERE O.ORGID='" + city + "' AND A.CITY LIKE '%'||O.ORGNAME||'%') ");
        }
        if (!"".equals(custType) && null != custType) {
            sql.append(" AND EXISTS (SELECT 1 FROM T_P_CODE P WHERE P.PID='" + custType + "' AND A.YDLB LIKE '%'||P.PNAME||'%') ");
        }
        pw.write(surveyDao.getSurveyData(tab, orgid, sql.toString(), city, address, dist));
        pw.close();
        return null;
    }
}
