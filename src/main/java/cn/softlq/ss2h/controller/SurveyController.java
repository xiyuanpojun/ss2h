package cn.softlq.ss2h.controller;

import cn.softlq.ss2h.dao.ISurveyDao;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class SurveyController {
    @Autowired
    private HttpSession session;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;
    @Autowired
    private ISurveyDao surveyDao;
    public String getCity() throws IOException, SQLException {
        String arg = request.getParameter("arg");
        PrintWriter pw = response.getWriter();
        Object userid=request.getSession().getAttribute("userid");
        if (null != userid) {
            String orgid = (String) request.getSession().getAttribute("uorg");
            pw.write(surveyDao.getCityList(orgid));
        }else{
            pw.write("sessionOut");
        }
        pw.close();
        return "none";
    }

    public String subDist() throws IOException, SQLException{
        String arg=request.getParameter("arg");
        PrintWriter pw=response.getWriter();
        Object userid=request.getSession().getAttribute("userid");
        if(null!=userid){
            String tab=request.getParameter("tab");
            String rowv=request.getParameter("rowv");
            String distRes=request.getParameter("distRes");
            String diaocy=request.getParameter("diaocy");
            surveyDao.saveDist(tab,rowv,distRes,diaocy);
            pw.write("ok");
        }else{
            pw.write("sessionOut");
        }
        pw.close();
        return"none";
    }

    public String subInvit() throws IOException, SQLException{
        String arg=request.getParameter("arg");
        PrintWriter pw=response.getWriter();
        Object userid=request.getSession().getAttribute("userid");
        if(null!=userid){
            String tab=request.getParameter("tab");
            String rowv=request.getParameter("rowv");
            String invRes=request.getParameter("invRes");
            String faultRes=request.getParameter("faultRes");
            surveyDao.saveInvit(tab,rowv,invRes,faultRes,userid.toString());
            pw.write("ok");
        }else{
            pw.write("sessionOut");
        }
        pw.close();
        return"none";
    }

    public String getInvitData() throws IOException, SQLException{
        String arg=request.getParameter("arg");
        PrintWriter pw=response.getWriter();
        Object userid=request.getSession().getAttribute("userid");
        if(null!=userid){
            String page=request.getParameter("page");
            String limit=request.getParameter("limit");
            int start=Integer.parseInt(page);
            int end=Integer.parseInt(limit);
            String city=request.getParameter("city");
            String custType=request.getParameter("custType");
            StringBuilder sql=new StringBuilder();
            if(!"".equals(city)&&null!=city){
                sql.append(" AND EXISTS (SELECT 1 FROM T_ORG O WHERE O.ORGID='"+city+"' AND A.CITY LIKE '%'||O.ORGNAME||'%') ");
            }
            if(!"".equals(custType)&&null!=custType){
                sql.append(" AND EXISTS (SELECT 1 FROM T_P_CODE P WHERE P.PID='"+custType+"' AND A.YDLB LIKE '%'||P.PNAME||'%') ");
            }
            pw.write(surveyDao.getSurveyInvData(request.getParameter("tab"),sql.toString(), userid.toString(),end*(start-1),end*start));
        }else{
            pw.write("sessionOut");
        }
        pw.close();
        return"none";
    }

    public String getData() throws IOException, SQLException{
        String arg=request.getParameter("arg");
        PrintWriter pw=response.getWriter();
        Object userid=request.getSession().getAttribute("userid");
        if(null!=userid){
            String orgid = (String) request.getSession().getAttribute("uorg");
            String city=request.getParameter("city");
            String custType=request.getParameter("custType");
            StringBuilder sql=new StringBuilder();
            if(!"".equals(city)&&null!=city){
                sql.append(" AND EXISTS (SELECT 1 FROM T_ORG O WHERE O.ORGID='"+city+"' AND A.CITY LIKE '%'||O.ORGNAME||'%') ");
            }
            if(!"".equals(custType)&&null!=custType){
                sql.append(" AND EXISTS (SELECT 1 FROM T_P_CODE P WHERE P.PID='"+custType+"' AND A.YDLB LIKE '%'||P.PNAME||'%') ");
            }
            pw.write(surveyDao.getSurveyData(request.getParameter("tab"),orgid,sql.toString()));
        }else{
            pw.write("sessionOut");
        }
        pw.close();
        return"none";
    }

    public String getCol() throws IOException, SQLException{
        String arg=request.getParameter("arg");
        PrintWriter pw=response.getWriter();
        Object userid=request.getSession().getAttribute("userid");
        if(null!=userid){
            pw.write(surveyDao.getSurveyCol(request.getParameter("tab")));
        }else{
            pw.write("sessionOut");
        }
        pw.close();
        return"none";
    }

    public String getStype() throws IOException, SQLException{
        String arg=request.getParameter("arg");
        PrintWriter pw=response.getWriter();
        Object userid=request.getSession().getAttribute("userid");
        if(null!=userid){
            pw.write(surveyDao.getSurveyType());
        }else{
            pw.write("sessionOut");
        }
        pw.close();
        return"none";
    }

    public String getCustType() throws IOException, SQLException{
        String arg=request.getParameter("arg");
        PrintWriter pw=response.getWriter();
        Object userid=request.getSession().getAttribute("userid");
        if(null!=userid){
            pw.write(surveyDao.getCustType("客户类型"));
        }else{
            pw.write("sessionOut");
        }
        pw.close();
        return"none";
    }

    public String getFault() throws IOException, SQLException{
        String arg=request.getParameter("arg");
        PrintWriter pw=response.getWriter();
        Object userid=request.getSession().getAttribute("userid");
        if(null!=userid){
            pw.write(surveyDao.getCustType("预约失败原因"));
        }else{
            pw.write("sessionOut");
        }
        pw.close();
        return"none";
    }
}
