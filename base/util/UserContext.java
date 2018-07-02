package cn.wolfcode.p2p.base.util;

import cn.wolfcode.p2p.base.domain.Logininfo;
import cn.wolfcode.p2p.base.vo.VerifyCodeVo;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by wolfcode on 0010.
 */
public class UserContext {
    public static final String USER_IN_SESSION = "logininfo";
    public static final String VERIFYCODE_IN_SESSION = "verifyCode";
    public static HttpServletRequest getRequest(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //HttpServletRequest request = MyRequestContextHolder.getRequest();
        System.out.println("在UserContext中的REquest"+request);
        System.out.println(request);
        return request;
    }
    public static void setCurrent(Logininfo current) {
       getRequest().getSession().setAttribute(USER_IN_SESSION,current);
    }
    public static Logininfo getCurrent(){
        return (Logininfo) getRequest().getSession().getAttribute(USER_IN_SESSION);
    }
    public static String getIP(){
        return getRequest().getRemoteAddr();
    }

    /**
     * 把手机认证码存入到session中
     * @param verifyCodeVo
     */
    public static void setVerifyCodeVo(VerifyCodeVo verifyCodeVo) {
       getRequest().getSession().setAttribute(VERIFYCODE_IN_SESSION,verifyCodeVo);
    }
    //从session中获取手机认证码
    public static VerifyCodeVo getVerifyCodeVo(){
        return (VerifyCodeVo) getRequest().getSession().getAttribute(VERIFYCODE_IN_SESSION);
    }
}
