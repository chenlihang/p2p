package cn.wolfcode.p2p.base.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by wolfcode on 0008.
 */
@Setter@Getter
public class Logininfo extends BaseDomain {
    public static final int STATE_NORMAL = 0;//正常状态
    public static final int STATE_LOCK = 1;//锁定状态
    public static final int USERTYPE_USER = 0;//普通用户
    public static final int USERTYPE_MANAGER = 1;//管理员用户
    private String username;//用户名
    private String password;//密码
    private int state;//状态
    private int userType;//用户的类型
}
