package cn.wolfcode.p2p.base.service.impl;

import cn.wolfcode.p2p.base.domain.Logininfo;
import cn.wolfcode.p2p.base.domain.UserFile;
import cn.wolfcode.p2p.base.domain.Userinfo;
import cn.wolfcode.p2p.base.domain.VedioAuth;
import cn.wolfcode.p2p.base.mapper.VedioAuthMapper;
import cn.wolfcode.p2p.base.query.QueryObject;
import cn.wolfcode.p2p.base.service.IUserFileService;
import cn.wolfcode.p2p.base.service.IUserinfoService;
import cn.wolfcode.p2p.base.service.IVedioAuthService;
import cn.wolfcode.p2p.base.util.BitStatesUtils;
import cn.wolfcode.p2p.base.util.UserContext;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by wolfcode on 2018/03/16 0016.
 */
@Service@Transactional
public class VedioAuthServiceImpl implements IVedioAuthService {
    @Autowired
    private VedioAuthMapper vedioAuthMapper;
    @Autowired
    private IUserinfoService userinfoService;
    @Override
    public void save(VedioAuth vedioAuth) {
        vedioAuthMapper.insert(vedioAuth);
    }

    @Override
    public PageInfo queryPage(QueryObject qo) {
        PageHelper.startPage(qo.getCurrentPage(),qo.getPageSize());
        List<VedioAuth> list = vedioAuthMapper.selectList(qo);
        return new PageInfo(list);
    }

    @Override
    public void aduit(Long loginInfoValue, int state, String remark) {
        Userinfo userinfo = userinfoService.get(loginInfoValue);
        if(userinfo!=null && !userinfo.getIsVedioAuth()){
            VedioAuth vedioAuth = new VedioAuth();
            Logininfo applier = new Logininfo();
            applier.setId(loginInfoValue);
            vedioAuth.setApplier(applier);
            vedioAuth.setApplyTime(new Date());
            vedioAuth.setAuditor(UserContext.getCurrent());
            vedioAuth.setAuditTime(new Date());
            vedioAuth.setRemark(remark);
            vedioAuth.setState(state);
            if(state==VedioAuth.STATE_PASS){
                userinfo.addState(BitStatesUtils.OP_VEDIO_AUTH);
                userinfoService.update(userinfo);
            }
            this.save(vedioAuth);
        }
    }
}
