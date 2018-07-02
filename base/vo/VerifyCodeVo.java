package cn.wolfcode.p2p.base.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by wolfcode on 0012.
 */
@Setter@Getter
public class VerifyCodeVo implements Serializable {
    private String randomCode;
    private String phoneNumber;
    private Date sendTime;
}
