package cn.wolfcode.p2p.bussiness.service;

import cn.wolfcode.p2p.base.query.QueryObject;
import cn.wolfcode.p2p.bussiness.domain.PlatformBankinfo;
import cn.wolfcode.p2p.bussiness.domain.RechargeOffline;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface IRechargeOfflineService {
    int save(RechargeOffline rechargeOffline);
    int update(RechargeOffline rechargeOffline);
    RechargeOffline get(Long id);
    PageInfo queryPage(QueryObject qo);
    List<PlatformBankinfo> selectAll();

    /**
     * 充值成功
     * @param rechargeOffline
     */
    void rechargeSave(RechargeOffline rechargeOffline);

    /**
     * 充值审核
     * @param id
     * @param state
     * @param remark
     */
    void audit(Long id, int state, String remark);
}
