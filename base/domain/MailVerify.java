package cn.wolfcode.p2p.base.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by wolfcode on 0012.
 */
@Setter@Getter
public class MailVerify extends BaseDomain {
    private Long userId;
    private String email;
    private Date sendTime;
    private String uuid;
}
