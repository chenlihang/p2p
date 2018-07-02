package cn.wolfcode.p2p.base.query;

import cn.wolfcode.p2p.base.util.DateUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * Created by wolfcode on 0011.
 */
@Setter@Getter
public class IpLogQueryObject extends QueryObject {
    private Date beginDate;
    private Date endDate;
    private int state = -1;
    private String username;
    public String getUsername(){
        return StringUtils.isEmpty(username)?null:username;
    }
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public void setBeginDate(Date beginDate){
        this.beginDate = beginDate;
    }
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public void setEndDate(Date endDate){
        this.endDate = endDate;
    }
    public Date getEndDate(){
        return DateUtil.getEndDate(this.endDate);
    }
}
