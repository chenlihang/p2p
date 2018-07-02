package cn.wolfcode.p2p.base.domain;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wolfcode on 2018/03/15 0015.
 */
@Setter@Getter
public class UserFile extends BaseAuthDomain {
    private int score;//审核得分
    private String image;//图片
    private SystemDictionaryItem fileType;//风控资料类型
    public String getJsonString(){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("id",getId());
        map.put("applier",applier.getUsername());
        map.put("fileType",fileType.getTitle());
        map.put("image",image);
        return  JSON.toJSONString(map);
    }
}
