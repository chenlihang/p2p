package cn.wolfcode.p2p.bussiness.service.impl;

import cn.wolfcode.p2p.bussiness.domain.Bid;
import cn.wolfcode.p2p.bussiness.mapper.BidMapper;
import cn.wolfcode.p2p.bussiness.service.IBidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by wolfcode on 2018/03/16 0016.
 */
@Service@Transactional
public class BidServiceImpl implements IBidService {
    @Autowired
    private BidMapper bidMapper;
    @Override
    public int save(Bid bid) {
        return bidMapper.insert(bid);
    }

    @Override
    public int update(Bid bid) {
        return bidMapper.updateByPrimaryKey(bid);
    }

    @Override
    public Bid get(Long id) {
        return bidMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateState(Long bidRequestId, int state) {
        bidMapper.updateState(bidRequestId,state);
    }
}
