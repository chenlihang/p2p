package cn.wolfcode.p2p.bussiness.service.impl;

import cn.wolfcode.p2p.bussiness.domain.BidRequestAuditHistory;
import cn.wolfcode.p2p.bussiness.mapper.BidRequestAuditHistoryMapper;
import cn.wolfcode.p2p.bussiness.service.IBidRequestAuditHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by wolfcode on 2018/03/16 0016.
 */
@Service@Transactional
public class BidRequestAuditHistoryServiceImpl implements IBidRequestAuditHistoryService {
    @Autowired
    private BidRequestAuditHistoryMapper bidRequestAuditHistoryMapper;
    @Override
    public int save(BidRequestAuditHistory bidRequestAuditHistory) {
        return bidRequestAuditHistoryMapper.insert(bidRequestAuditHistory);
    }

    @Override
    public List<BidRequestAuditHistory> queryListByBidRequestId(Long bidRequestId) {
        return bidRequestAuditHistoryMapper.queryListByBidRequestId(bidRequestId);
    }
}
