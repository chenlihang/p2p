package cn.wolfcode.p2p.bussiness.service;

import cn.wolfcode.p2p.base.query.QueryObject;
import cn.wolfcode.p2p.bussiness.domain.PlatformBankinfo;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface IPlatformBankinfoService {
    int save(PlatformBankinfo platformBankinfo);
    int update(PlatformBankinfo platformBankinfo);
    PlatformBankinfo get(Long id);
    PageInfo queryPage(QueryObject qo);

    void saveOrupdate(PlatformBankinfo platformBankinfo);

    List<PlatformBankinfo> selectAll();
}
