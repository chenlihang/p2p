package cn.wolfcode.p2p.bussiness.service;

import cn.wolfcode.p2p.bussiness.domain.BidRequestAuditHistory;

import java.util.List;

/**
 * Created by wolfcode on 2018/03/16 0016.
 */
public interface IBidRequestAuditHistoryService {
    int save(BidRequestAuditHistory bidRequestAuditHistory);

    List<BidRequestAuditHistory> queryListByBidRequestId(Long bidRequestId);
}
