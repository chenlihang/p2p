package cn.wolfcode.p2p.base.service.impl;

import cn.wolfcode.p2p.base.domain.RealAuth;
import cn.wolfcode.p2p.base.domain.Userinfo;
import cn.wolfcode.p2p.base.mapper.RealAuthMapper;
import cn.wolfcode.p2p.base.query.RealAuthQueryObject;
import cn.wolfcode.p2p.base.service.IRealAuthService;
import cn.wolfcode.p2p.base.service.IUserinfoService;
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
 * Created by wolfcode on 2018/03/14 0014.
 */
@Service@Transactional
public class RealAuthServiceImpl implements IRealAuthService {
    @Autowired
    private RealAuthMapper realAuthMapper;
    @Autowired
    private IUserinfoService userinfoService;
    @Override
    public int save(RealAuth realAuth) {
        return realAuthMapper.insert(realAuth);
    }

    @Override
    public int update(RealAuth realAuth) {
        return realAuthMapper.updateByPrimaryKey(realAuth);
    }

    @Override
    public RealAuth get(Long id) {
        return realAuthMapper.selectByPrimaryKey(id);
    }

    @Override
    public void realAuthSave(RealAuth realAuth) {
        RealAuth ra = new RealAuth();
        ra.setAddress(realAuth.getAddress());
        ra.setApplier(UserContext.getCurrent());
        ra.setApplyTime(new Date());
        ra.setBornDate(realAuth.getBornDate());
        ra.setIdNumber(realAuth.getIdNumber());
        ra.setImage1(realAuth.getImage1());
        ra.setImage2(realAuth.getImage2());
        ra.setRealName(realAuth.getRealName());
        ra.setSex(realAuth.getSex());
        ra.setState(RealAuth.STATE_NORMAL);
        this.save(ra);
        Userinfo current = userinfoService.getCurrent();
        current.setRealAuthId(ra.getId());
        userinfoService.update(current);
    }

    @Override
    public PageInfo queryPage(RealAuthQueryObject qo) {
        PageHelper.startPage(qo.getCurrentPage(),qo.getPageSize());
        List list = realAuthMapper.selectList(qo);
        return new PageInfo(list);
    }

    @Override
    public void audit(Long id, int state, String remark) {
        //1.根据id获取到实名认证对象,判断是否处于待审核的状态
        RealAuth realAuth = this.get(id);
        if(realAuth!=null && realAuth.getState() == RealAuth.STATE_NORMAL){
            //2.设置相关的属性,审核人,审核时间,审核备注,状态
            realAuth.setAuditor(UserContext.getCurrent());
            realAuth.setAuditTime(new Date());
            realAuth.setRemark(remark);
            Userinfo applierUserinfo = userinfoService.get(realAuth.getApplier().getId());
            if(state == RealAuth.STATE_PASS){
                //3.审核通过:
                realAuth.setState(RealAuth.STATE_PASS);
                //     3.1 给userinfo添加实名认证状态. 给userinfo添加真实姓名和身份证的属性
                applierUserinfo.setRealName(realAuth.getRealName());
                applierUserinfo.setIdNumber(realAuth.getIdNumber());
                applierUserinfo.addState(BitStatesUtils.OP_REAL_AUTH);
            }else{
                // 4.审核失败:
                //      4.1 把userinfo中realAuthId设置为null
                realAuth.setState(RealAuth.STATE_REJECT);
                applierUserinfo.setRealAuthId(null);
            }
            userinfoService.update(applierUserinfo);
            this.update(realAuth);
        }
    }
}
