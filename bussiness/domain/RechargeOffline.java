package cn.wolfcode.p2p.bussiness.domain;

import cn.wolfcode.p2p.base.domain.BaseAuthDomain;
import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

@Setter
@Getter
public class RechargeOffline extends BaseAuthDomain{
    private PlatformBankinfo bankInfo;//银行信息
    private String tradeCode;//交易号
    private BigDecimal amount;//充值金额
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date tradeTime;//充值时间
    private String note;//充值说明
    public String getJsonString(){
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("id",getId());
        map.put("username",applier.getUsername());
        map.put("tradeCode",tradeCode);
        map.put("amount",amount);
        map.put("tradeTime",tradeTime);
        return JSON.toJSONString(map);
    }

}
