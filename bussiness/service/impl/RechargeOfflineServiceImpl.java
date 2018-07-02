package cn.wolfcode.p2p.bussiness.service.impl;

import cn.wolfcode.p2p.base.domain.Account;
import cn.wolfcode.p2p.base.query.QueryObject;
import cn.wolfcode.p2p.base.service.IAccountService;
import cn.wolfcode.p2p.base.util.UserContext;
import cn.wolfcode.p2p.bussiness.domain.PlatformBankinfo;
import cn.wolfcode.p2p.bussiness.domain.RechargeOffline;
import cn.wolfcode.p2p.bussiness.mapper.RechargeOfflineMapper;
import cn.wolfcode.p2p.bussiness.service.IAccountFlowService;
import cn.wolfcode.p2p.bussiness.service.IRechargeOfflineService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
@Service
@Transactional
public class RechargeOfflineServiceImpl implements IRechargeOfflineService {
    @Autowired
    private RechargeOfflineMapper rechargeOfflineMapper;
    @Autowired
    private IAccountFlowService accountFlowService;
    @Autowired
    private IAccountService accountService;
    @Override
    public int save(RechargeOffline rechargeOffline) {
        return rechargeOfflineMapper.insert(rechargeOffline);
    }

    @Override
    public int update(RechargeOffline rechargeOffline) {
        return rechargeOfflineMapper.updateByPrimaryKey(rechargeOffline);
    }

    @Override
    public RechargeOffline get(Long id) {
        return rechargeOfflineMapper.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo queryPage(QueryObject qo) {
        PageHelper.startPage(qo.getCurrentPage(),qo.getPageSize());
        List<RechargeOffline> list = rechargeOfflineMapper.selectLiast(qo);
        return new PageInfo(list);
    }

    @Override
    public List<PlatformBankinfo> selectAll() {
        return rechargeOfflineMapper.selectAll();
    }

    @Override
    public void rechargeSave(RechargeOffline rechargeOffline) {
        RechargeOffline ro = new RechargeOffline();
        ro.setAmount(rechargeOffline.getAmount());
        ro.setBankInfo(rechargeOffline.getBankInfo());
        ro.setNote(rechargeOffline.getNote());
        ro.setTradeCode(rechargeOffline.getTradeCode());
        ro.setTradeTime(rechargeOffline.getTradeTime());
        ro.setApplier(UserContext.getCurrent());
        ro.setApplyTime(new Date());
        ro.setState(RechargeOffline.STATE_NORMAL);
        this.save(ro);//操作完就添加

    }

    @Override
    public void audit(Long id, int state, String remark) {
      //1根据id获取充值申请对象
        RechargeOffline rechargeOffline = this.get(id);
        //2判断是否处于待审核的状态
        if(rechargeOffline != null && rechargeOffline.getState()==RechargeOffline.STATE_NORMAL){
         //设置审核相关的属性

            rechargeOffline.setAuditor(UserContext.getCurrent());
            rechargeOffline.setApplyTime(new Date());
            rechargeOffline.setRemark(remark);
            if (state == RechargeOffline.STATE_PASS){
                rechargeOffline.setState(RechargeOffline.STATE_PASS);
                //找到对应申请人的账户
                Account applierAccount = accountService.get(rechargeOffline.getApplier().getId());
                //记录流水

                 applierAccount.setUsableAmount(applierAccount.getUnReturnAmount().add(rechargeOffline.getAmount()));
                 accountService.update(applierAccount);

                accountFlowService.createRechargeOffLineFlow(applierAccount,rechargeOffline.getAmount());
            }else {
                rechargeOffline.setState(RechargeOffline.STATE_REJECT);
            }
        }this.update(rechargeOffline);
    }
}
