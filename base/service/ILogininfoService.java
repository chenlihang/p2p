package cn.wolfcode.p2p.base.service;

import cn.wolfcode.p2p.base.domain.Logininfo;

/**
 * Created by wolfcode on 0008.
 */
public interface ILogininfoService {
    /**
     * 注册功能
     * @param username
     * @param password
     * @return
     */
    Logininfo register(String username,String password);

    /**
     * 检查用户名是否存在
     * @param username
     * @return
     */
    boolean checkUsername(String username);

    /**
     * 登录方法
     * @param username
     * @param password
     * @return
     */
    Logininfo login(String username, String password,int userType);

    /**
     * 初始化第一个管理员
     */
    void initAdmin();
}
