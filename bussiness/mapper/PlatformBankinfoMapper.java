package cn.wolfcode.p2p.bussiness.mapper;

import cn.wolfcode.p2p.base.query.QueryObject;
import cn.wolfcode.p2p.bussiness.domain.PlatformBankinfo;
import java.util.List;

public interface PlatformBankinfoMapper {

    int insert(PlatformBankinfo record);

    PlatformBankinfo selectByPrimaryKey(Long id);

    List<PlatformBankinfo> selectList(QueryObject qo);

    int updateByPrimaryKey(PlatformBankinfo record);

    List<PlatformBankinfo> selectAll();
}