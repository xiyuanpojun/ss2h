package com.hill.gwyb.common;

import com.hill.gwyb.dao.IUserDao;
import com.hill.gwyb.dao.impl.UserDaoImpl;
import com.hill.gwyb.po.TUserLoginEntity;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
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
        //通过抽象的私有方法得到Spring容器中Bean的实例。
        IUserDao userDao = (UserDaoImpl) this.getObjectFromApplication(session.getServletContext(), "userDaoImpl");
        try {
            userDao.userLoginInfoExec(userLoginEntity, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过WebApplicationContextUtils 得到Spring容器的实例。根据bean的名称返回bean的实例。
     *
     * @param servletContext ：ServletContext上下文。
     * @param beanName       :要取得的Spring容器中Bean的名称。
     * @return 返回Bean的实例。
     */
    private Object getObjectFromApplication(ServletContext servletContext, String beanName) {
        //通过WebApplicationContextUtils 得到Spring容器的实例。
        ApplicationContext application = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        //返回Bean的实例。
        return application.getBean(beanName);
    }
}
