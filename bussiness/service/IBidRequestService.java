package cn.wolfcode.p2p.bussiness.service;

import cn.wolfcode.p2p.base.query.QueryObject;
import cn.wolfcode.p2p.bussiness.domain.BidRequest;
import cn.wolfcode.p2p.bussiness.query.BidRequestQueryObject;
import com.github.pagehelper.PageInfo;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by wolfcode on 2018/03/16 0016.
 */
public interface IBidRequestService {
    int save(BidRequest bidRequest);
    int update(BidRequest bidRequest);
    BidRequest get(Long id);
    PageInfo queryPage(QueryObject qo);

    /**
     * 借款申请
     * @param bidRequest
     */
    void apply(BidRequest bidRequest);

    /**
     * 发标前审核
     * @param id
     * @param state
     * @param remark
     */
    void publishAudit(Long id, int state, String remark);

    /**
     * 查询投标的数据
     * @param qo
     * @return
     */
    List<BidRequest> queryIndexData(BidRequestQueryObject qo);

    /**
     * 投标操作
     */
    void bid(Long bidRequestId, BigDecimal amount);

    /**
     * 满标一审
     * @param id
     * @param state
     * @param remark
     */
    void audit1(Long id, int state, String remark);

    /**
     * 满标二审
     * @param id
     * @param state
     * @param remark
     */
    void audit2(Long id, int state, String remark);
}
