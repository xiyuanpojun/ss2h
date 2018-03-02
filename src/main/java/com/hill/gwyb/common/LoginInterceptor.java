package com.hill.gwyb.common;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class LoginInterceptor extends AbstractInterceptor {
    private static final String[] ignores = {"/gwyb/user/user_login"};

    @Override
    public String intercept(ActionInvocation actionInvocation) throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        String uri = request.getRequestURI();
        boolean isIgnore = false;
        for (String ignore : ignores) {
            if (ignore.equals(uri)) {
                isIgnore = true;
                break;
            }
        }
        if (isIgnore) {
            return actionInvocation.invoke();
        }
        Map session = actionInvocation.getInvocationContext().getSession();
        String login = (String) session.get("userId");
        if (login != null && login.length() > 0) {
            return actionInvocation.invoke();
        } else {
            String header = request.getHeader("X-Requested-With");
            if (header == null) {
                HttpServletResponse response = ServletActionContext.getResponse();
                response.sendRedirect("/gwyb/index.jsp");
                return ActionSupport.NONE;
            }
            return "isLogon";
        }
    }
}