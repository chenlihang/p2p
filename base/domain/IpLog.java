package cn.wolfcode.p2p.base.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by wolfcode on 0011.
 */
@Setter@Getter
public class IpLog extends BaseDomain {
    public static final int LOGIN_FAILED = 0;
    public static final int LOGIN_SUCCESS=1;
    private String ip;
    private String username;
    private Date loginTime;
    private int state;
    private int userType;
    public String getStateDisplay(){
        return state==LOGIN_FAILED?"登录失败":"登录成功";
    }
    public String getUserTypeDisplay(){
        return userType==Logininfo.USERTYPE_USER?"普通用户":"管理员";
    }
}
