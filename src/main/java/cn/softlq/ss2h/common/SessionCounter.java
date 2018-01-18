package cn.softlq.ss2h.common;

import cn.softlq.ss2h.dao.IUserDao;
import cn.softlq.ss2h.dao.impl.UserDaoImpl;
import cn.softlq.ss2h.po.TUserLoginEntity;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionCounter implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        HttpSession session = httpSessionEvent.getSession();
        TUserLoginEntity userLoginEntity = new TUserLoginEntity();
        userLoginEntity.setSessionId(session.getId());
        System.out.println("session超时了");
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
