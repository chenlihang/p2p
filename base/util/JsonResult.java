package cn.wolfcode.p2p.base.util;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JsonResult {
    private boolean success=true;
    private String msg;
    public JsonResult(String msg){
        this.msg = msg;
    }
    public JsonResult(boolean success,String msg){
        this.success = success;
        this.msg = msg;
    }
}
