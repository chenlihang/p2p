package cn.wolfcode.p2p.base.util;

import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by wolfcode on 0010.
 */
@WebListener
public class MyRequestContextListener implements ServletRequestListener {
    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        //把创建出来的Request绑定到线程中.
        HttpServletRequest servletRequest = (HttpServletRequest) sre.getServletRequest();
        System.out.println("监听Request创建"+servletRequest);
        MyRequestContextHolder.setRequest(servletRequest);
    }
    @Override
    public void requestDestroyed(ServletRequestEvent sre) {

    }
}
