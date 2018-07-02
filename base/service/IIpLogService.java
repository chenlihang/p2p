package cn.wolfcode.p2p.base.service;

import cn.wolfcode.p2p.base.domain.IpLog;
import cn.wolfcode.p2p.base.query.IpLogQueryObject;
import com.github.pagehelper.PageInfo;

/**
 * Created by wolfcode on 0011.
 */
public interface IIpLogService {
    int save(IpLog ipLog);

    /**
     * 分页方法
     * @param qo
     * @return
     */
    PageInfo queryPage(IpLogQueryObject qo);
}
