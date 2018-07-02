package cn.wolfcode.p2p.base.service;

import cn.wolfcode.p2p.base.domain.VedioAuth;
import cn.wolfcode.p2p.base.query.QueryObject;
import com.github.pagehelper.PageInfo;

/**
 * Created by wolfcode on 2018/03/16 0016.
 */
public interface IVedioAuthService {
    void save(VedioAuth vedioAuth);
    PageInfo queryPage(QueryObject qo);

    /**
     * 视频认证审核
     * @param loginInfoValue
     * @param state
     * @param remark
     */
    void aduit(Long loginInfoValue, int state, String remark);
}
