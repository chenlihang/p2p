package cn.wolfcode.p2p.bussiness.service.impl;

import cn.wolfcode.p2p.base.domain.Account;
import cn.wolfcode.p2p.base.util.BidConst;
import cn.wolfcode.p2p.bussiness.domain.AccountFlow;
import cn.wolfcode.p2p.bussiness.mapper.AccountFlowMapper;
import cn.wolfcode.p2p.bussiness.service.IAccountFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

@Service@Transactional
public class AccountFlowServiceImpl implements IAccountFlowService {
    @Autowired
    private AccountFlowMapper accountFlowMapper;
    @Override
    public int save(AccountFlow accountFlow) {
        return accountFlowMapper.insert(accountFlow);
    }

    @Override
    public void createRechargeOffLineFlow(Account account, BigDecimal amount) {
        createFlow(account,amount,BidConst.ACCOUNT_ACTIONTYPE_RECHARGE_OFFLINE,"线下充值:"+amount);
    }

    @Override
    public void createBidFlow(Account account, BigDecimal amount) {
        createFlow(account,amount,BidConst.ACCOUNT_ACTIONTYPE_BID_FREEZED,"投标:"+amount);
    }

    @Override
    public void createBidFaileFlow(Account account, BigDecimal amount) {
        createFlow(account,amount,BidConst.ACCOUNT_ACTIONTYPE_BID_UNFREEZED,"投标失败:"+amount);
    }

    private void createFlow(Account account, BigDecimal amount,int actionType,String remark){
        AccountFlow flow =new AccountFlow();
        flow.setAccountId(account.getId());//账户id
        flow.setAmount(amount);//这次交易的金额
        flow.setTradeTime(new Date());//交易的日期
        flow.setUsableAmount(account.getUsableAmount());//账户交易后的可用金额
        flow.setFreezedAmount(account.getFreezedAmount());//账户交易后的冻结金额
        flow.setActionType(actionType);//交易类型
        flow.setRemark(remark);
        this.save(flow);
    }
}
