package cn.softlq.ss2h.common;

import cn.softlq.ss2h.dao.IUserDao;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

public class LoginInterceptor extends AbstractInterceptor {
    @Autowired
    private HttpSession session;
    @Autowired
    private IUserDao userDao;
    private static final String[] ignores = {"/user/user_login"};
//    private static final String logonUri = "/user/user_logon";

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
//        if (uri.equals(logonUri)) {
//            //先获取userId，然后修改时间。
//            String userId = request.getParameter("userEntity.userid");
//            //记录用户登陆信息
//            TUserLoginEntity userLoginEntity = new TUserLoginEntity();
//            userLoginEntity.setUserid(userId);
//            userLoginEntity.setSessionId(session.getId());
//            userDao.userLoginInfoExec(userLoginEntity, true);
//        }
        if (isIgnore) {
            return actionInvocation.invoke();
        }
        Map session = actionInvocation.getInvocationContext().getSession();
        String login = (String) session.get("userId");
        if (login != null && login.length() > 0) {
            return actionInvocation.invoke();
        } else {
            return "isLogon";
        }
    }
}