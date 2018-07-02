package cn.wolfcode.p2p.base.util;

import com.sun.xml.internal.ws.resources.HttpserverMessages;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by wolfcode on 0010.
 */
public class MyRequestContextHolder {
    public static ThreadLocal<HttpServletRequest> threadLocal = new ThreadLocal<HttpServletRequest>();
    public static void setRequest(HttpServletRequest request){
        threadLocal.set(request);
    }
    public static HttpServletRequest getRequest(){
        return threadLocal.get();
    }
}
