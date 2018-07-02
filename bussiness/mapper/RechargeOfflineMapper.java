package cn.wolfcode.p2p.bussiness.mapper;

import cn.wolfcode.p2p.base.query.QueryObject;
import cn.wolfcode.p2p.bussiness.domain.PlatformBankinfo;
import cn.wolfcode.p2p.bussiness.domain.RechargeOffline;
import java.util.List;

public interface RechargeOfflineMapper {

    int insert(RechargeOffline record);

    RechargeOffline selectByPrimaryKey(Long id);

    List<RechargeOffline> selectLiast(QueryObject qo);

    int updateByPrimaryKey(RechargeOffline record);

    List<PlatformBankinfo> selectAll();
}