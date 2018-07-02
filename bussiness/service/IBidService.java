package cn.wolfcode.p2p.bussiness.service;

import cn.wolfcode.p2p.bussiness.domain.Bid;

/**
 * Created by wolfcode on 2018/03/16 0016.
 */
public interface IBidService {
    int save(Bid bid);
    int update(Bid bid);
    Bid get(Long id);

    /**
     * 批量修改投标的状态
     * @param bidRequestId
     * @param state
     */
    void updateState(Long bidRequestId, int state);
}
