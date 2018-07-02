package cn.wolfcode.p2p.bussiness.query;

import cn.wolfcode.p2p.base.query.QueryObject;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by wolfcode on 2018/03/16 0016.
 */
@Setter@Getter
public class BidRequestQueryObject extends QueryObject {
    private int bidRequestState = -1;//标的状态
    private int[] states;//多个状态
    private String orderByCondition;//命令通过条件

}
