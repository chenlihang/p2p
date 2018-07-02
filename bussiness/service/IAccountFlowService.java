package cn.wolfcode.p2p.bussiness.service;

import cn.wolfcode.p2p.base.domain.Account;
import cn.wolfcode.p2p.bussiness.domain.AccountFlow;

import java.math.BigDecimal;

public interface IAccountFlowService {
    int save(AccountFlow accountFlow);

    /**
     * 充值流水
     */
    void createRechargeOffLineFlow(Account account, BigDecimal amount);

    /**
     * 投标流水
     * @param account
     * @param amount
     */
    void createBidFlow(Account account, BigDecimal amount);

    /**
     * 投标失败的流水
     */
    /**
     * 投标失败的流水
     * @param account
     * @param amount
     */
    void createBidFaileFlow(Account account, BigDecimal amount);

}
