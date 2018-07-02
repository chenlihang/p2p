package cn.wolfcode.p2p.base.service;

import cn.wolfcode.p2p.base.domain.MailVerify;

/**
 * Created by wolfcode on 0012.
 */
public interface IMailVerifyService {
    int save(MailVerify mailVerify);
    MailVerify selectByUUID(String uuid);
}
