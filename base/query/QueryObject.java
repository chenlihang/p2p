package cn.wolfcode.p2p.base.query;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by wolfcode on 0011.
 */
@Setter@Getter
public class QueryObject {
    private Integer currentPage = 1;
    private Integer pageSize = 10;
}
