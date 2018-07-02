package cn.wolfcode.p2p.bussiness.domain;

import cn.wolfcode.p2p.base.domain.BaseDomain;
import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter

public class PlatformBankinfo extends BaseDomain{
   private String bankName;//银行名称
   private String accountNumber;//银行账号
   private String bankForkName;//支行名称
   private String accountName;//开户人姓名
   public String getJsonString(){
       HashMap<String, Object> map = new HashMap<String, Object>();
       map.put("id",getId());
       map.put("bankName",bankName);
       map.put("bankForkName",bankForkName);
       map.put("acountName",accountName);
       return JSON.toJSONString(map);
   }

}
