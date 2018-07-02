package cn.wolfcode.p2p.base.service;

import cn.wolfcode.p2p.base.domain.Userinfo;

/**
 * Created by wolfcode on 0010.
 */
public interface IUserinfoService {
    int save(Userinfo userinfo);
    int update(Userinfo userinfo);
    Userinfo get(Long id);
    Userinfo getCurrent();

    /**
     * 手机绑定方法
     * @param phoneNumber
     * @param verifyCode
     */
    void bindPhone(String phoneNumber, String verifyCode);

    /**
     * 邮箱绑定方法
     * @param key
     */
    void bindEmail(String key);

    /**
     * 基本信息保存
     * @param userinfo
     */
    void basicInfoSave(Userinfo userinfo);
}
