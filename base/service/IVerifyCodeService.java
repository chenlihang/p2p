package cn.wolfcode.p2p.base.service;

/**
 * Created by wolfcode on 0012.
 */
public interface IVerifyCodeService {
    /**
     * 发送手机认证码
     * @param phoneNumber
     */
    void sendVerifyCode(String phoneNumber);

    /**
     * 验证验证码是否有效
     * @param phoneNumber
     * @param verifyCode
     * @return
     */
    boolean validate(String phoneNumber, String verifyCode);
}
