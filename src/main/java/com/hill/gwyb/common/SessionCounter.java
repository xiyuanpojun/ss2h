package com.hill.gwyb.common;

import com.hill.gwyb.dao.IUserDao;
import com.hill.gwyb.dao.impl.UserDaoImpl;
import com.hill.gwyb.po.TUserLoginEntity;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionCounter implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        SessionContext.AddSession(httpSessionEvent.getSession());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        HttpSession session = httpSessionEvent.getSession();
        SessionContext.DelSession(session);
        TUserLoginEntity userLoginEntity = new TUserLoginEntity();
        userLoginEntity.setSessionId(session.getId());
        IUserDao userDao = (UserDaoImpl) WebApplicationContextUtils.getWebApplicationContext(session.getServletContext()).getBean("userDaoImpl");
        try {
            userDao.userLoginInfoExec(userLoginEntity, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
