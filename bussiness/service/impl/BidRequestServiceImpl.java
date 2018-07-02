package cn.wolfcode.p2p.bussiness.service.impl;

import cn.wolfcode.p2p.base.domain.Account;
import cn.wolfcode.p2p.base.domain.Userinfo;
import cn.wolfcode.p2p.base.query.QueryObject;
import cn.wolfcode.p2p.base.service.IAccountService;
import cn.wolfcode.p2p.base.service.IUserinfoService;
import cn.wolfcode.p2p.base.util.BidConst;
import cn.wolfcode.p2p.base.util.BitStatesUtils;
import cn.wolfcode.p2p.base.util.UserContext;
import cn.wolfcode.p2p.bussiness.domain.Bid;
import cn.wolfcode.p2p.bussiness.domain.BidRequest;
import cn.wolfcode.p2p.bussiness.domain.BidRequestAuditHistory;
import cn.wolfcode.p2p.bussiness.mapper.BidRequestMapper;
import cn.wolfcode.p2p.bussiness.query.BidRequestQueryObject;
import cn.wolfcode.p2p.bussiness.service.IAccountFlowService;
import cn.wolfcode.p2p.bussiness.service.IBidRequestAuditHistoryService;
import cn.wolfcode.p2p.bussiness.service.IBidRequestService;
import cn.wolfcode.p2p.bussiness.service.IBidService;
import cn.wolfcode.p2p.bussiness.util.CalculatetUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wolfcode on 2018/03/16 0016.
 */
@Service@Transactional
public class BidRequestServiceImpl implements IBidRequestService {
    @Autowired
    private BidRequestMapper bidRequestMapper;
    @Autowired
    private IUserinfoService userinfoService;
    @Autowired
    private IBidService bidService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IAccountFlowService accountFlowService;
    @Autowired
    private IBidRequestAuditHistoryService bidRequestAuditHistoryService;
    @Override
    public int save(BidRequest bidRequest) {
        return bidRequestMapper.insert(bidRequest);
    }

    @Override
    public int update(BidRequest bidRequest) {
        int count = bidRequestMapper.updateByPrimaryKey(bidRequest);
        if(count==0){
            throw new RuntimeException("乐观锁异常:bidRequestId="+bidRequest.getId());
        }
        return count;
    }

    @Override
    public BidRequest get(Long id) {
        return bidRequestMapper.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo queryPage(QueryObject qo) {
        PageHelper.startPage(qo.getCurrentPage(),qo.getPageSize());
        List<BidRequest> list = bidRequestMapper.selectList(qo);
        return new PageInfo(list);
    }

    @Override
    public void apply(BidRequest bidRequest) {
        //1.判断是否符合借款的条件,是否有一个借款的申请在审核
        Userinfo userinfo = userinfoService.getCurrent();
        Account account = accountService.getCurrent();
        if(!userinfo.getHasBidRequestProcess()&&
                userinfo.getIsRealAuth()&&
                userinfo.getIsBasicInfo()&&
                userinfo.getIsVedioAuth()){
            //系统最小借款金额<=该次借款金额<=用户剩余授信额度
            //系统最小投标<=该次的最小投标
            //系统最小利息<=该次借款的利息<=系统中的最大利息
            if(bidRequest.getBidRequestAmount().compareTo(BidConst.SMALLEST_BIDREQUEST_AMOUNT)>=0&&//该次借款金额>=系统最小借款金额
                    bidRequest.getBidRequestAmount().compareTo(account.getRemainBorrowLimit())<=0&&//该次借款金额<=用户剩余授信额度
                    bidRequest.getMinBidAmount().compareTo(BidConst.SMALLEST_BID_AMOUNT)>=0&&//该次的最小投标>=0系统最小投标
                    bidRequest.getCurrentRate().compareTo(BidConst.SMALLEST_CURRENT_RATE)>=0&&//该次借款的利息>=系统最小利息
                    bidRequest.getCurrentRate().compareTo(BidConst.MAX_CURRENT_RATE)<=0//该次借款的利息<=系统中的最大利息
                    ){
                //2.创建BidRequest对象,设置相关的参数
                BidRequest br = new BidRequest();
                br.setApplyTime(new Date());//借款申请时间
                br.setBidRequestAmount(bidRequest.getBidRequestAmount());//该次的借款金额
                br.setBidRequestState(BidConst.BIDREQUEST_STATE_PUBLISH_PENDING);//借款的状态--->待发布
                br.setBidRequestType(BidConst.BIDREQUEST_TYPE_NORMAL);//标的类型---->信用标
                br.setCreateUser(UserContext.getCurrent());//借款人
                br.setCurrentRate(bidRequest.getCurrentRate());//年化利率
                br.setDescription(bidRequest.getDescription());//借款描述
                br.setDisableDays(bidRequest.getDisableDays());//招标天数
                br.setMinBidAmount(bidRequest.getMinBidAmount());//最小投标金额
                br.setMonthes2Return(bidRequest.getMonthes2Return());//借款期数
                br.setReturnType(BidConst.RETURN_TYPE_MONTH_INTEREST_PRINCIPAL);//还款方式(按月分期)
                br.setTitle(bidRequest.getTitle());//借款标题
                br.setTotalRewardAmount(CalculatetUtil.calTotalInterest(bidRequest.getReturnType(),bidRequest.getBidRequestAmount(),bidRequest.getCurrentRate(),bidRequest.getMonthes2Return()));//这次借款需要的利息
                this.save(br);
                //3.给当前申请的用户添加真正申请将借款的状态码
                userinfo.addState(BitStatesUtils.HAS_BIDREQUEST_PROCESS);
                userinfoService.update(userinfo);
            }
        }
    }

    @Override
    public void publishAudit(Long id, int state, String remark) {
        //1.做哪些判断?
        //  根据id获取bidREquest,不为Null,处于待发布的状态
        BidRequest bidRequest = this.get(id);
        if(bidRequest!=null && bidRequest.getBidRequestState() == BidConst.BIDREQUEST_STATE_PUBLISH_PENDING){
            //2.需要创建哪些对象
            BidRequestAuditHistory brah = new BidRequestAuditHistory();
            brah.setApplier(bidRequest.getCreateUser());//申请人==借款人
            brah.setApplyTime(bidRequest.getApplyTime());//申请时间
            brah.setAuditType(BidRequestAuditHistory.PUBLISH_AUDIT);//审核类型为:发标前审核
            brah.setBidRequestId(bidRequest.getId());//关联借款对象
            brah.setAuditor(UserContext.getCurrent());//审核人
            brah.setAuditTime(new Date());
            brah.setRemark(remark);
            //  创建审核历史对象,并设置相关的属性
            if(state==BidRequestAuditHistory.STATE_PASS){
                //3.审核通过,做什么事情?
                brah.setState(BidRequestAuditHistory.STATE_PASS);
                //  借款对象----->招标中
                bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_BIDDING);
                //  发布时间---->当前时间
                bidRequest.setPublishTime(new Date());
                //  截止时间-----发布时间+招标天数
                bidRequest.setDisableDate(DateUtils.addDays(bidRequest.getPublishTime(),bidRequest.getDisableDays()));
                //bidRequest
            }else{
                brah.setState(BidRequestAuditHistory.STATE_REJECT);
                //4.审核是失败,做什么事情,什么对象的属性需要变化
                //  借款对象----->发标前审核拒绝
                bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_PUBLISH_REFUSE);
                //  找到对应申请人,移除正在借款的状态码
                Userinfo createUserUserinfo = userinfoService.get(bidRequest.getCreateUser().getId());
                createUserUserinfo.removeState(BitStatesUtils.HAS_BIDREQUEST_PROCESS);
                userinfoService.update(createUserUserinfo);
            }
            this.update(bidRequest);
            bidRequestAuditHistoryService.save(brah);

        }

    }
    //查询投标的状态操作
    @Override
    public List<BidRequest> queryIndexData(BidRequestQueryObject qo) {
        PageHelper.offsetPage(0,5);
        List<BidRequest> bidRequests = bidRequestMapper.selectList(qo);
        return bidRequests;
    }

    @Override
    public void bid(Long bidRequestId, BigDecimal amount) {
        //1.需要由哪些判断条件?
        BidRequest bidRequest = this.get(bidRequestId);
        if(bidRequest!=null && bidRequest.getBidRequestState()==BidConst.BIDREQUEST_STATE_BIDDING){
            //      借款对象处于招标中.
            //      系统最小投标<=投标金额<=MIN(该标还需要的金额,账户可用余额)
            //      借款人!=投标人
            Account account = accountService.getCurrent();
            if(amount.compareTo(BidConst.SMALLEST_BID_AMOUNT)>=0&&//投标金额>=系统最小投标
                    amount.compareTo(bidRequest.getRemainAmount().min(account.getUsableAmount()))<=0&&//投标金额<=MIN(该标还需要的金额,账户可用余额)
                    !bidRequest.getCreateUser().getId().equals(UserContext.getCurrent().getId())
                    ){
                //2.需要创建什么对象
                //  创建投标Bid对象
                Bid bid = new Bid();
                bid.setActualRate(bidRequest.getCurrentRate());//标的年化利率
                bid.setAvailableAmount(amount);//投标金额
                bid.setBidRequestId(bidRequestId);//关联借款对象
                bid.setBidRequestState(bidRequest.getBidRequestState());//标的状态--->招标中
                bid.setBidRequestTitle(bidRequest.getTitle());//借款的标题
                bid.setBidTime(new Date());//投标时间
                bid.setBidUser(UserContext.getCurrent());//投标人账户
                bidService.save(bid);
                //  投资人的账户变化
                //  可用金额减少,冻结金额增加
                account.setUsableAmount(account.getUsableAmount().subtract(amount));
                account.setFreezedAmount(account.getFreezedAmount().add(amount));
                accountService.update(account);
                //  生成投标的流水
                accountFlowService.createBidFlow(account,amount);
                //3.借款对象中的哪些属性需要设置?
                //      bidCount,currentSum需要变化
                bidRequest.setBidCount(bidRequest.getBidCount()+1);
                bidRequest.setCurrentSum(bidRequest.getCurrentSum().add(amount));
                //4.怎么判断借款已经投满了?
                //      currentSum==bidRequestAmount
                if(bidRequest.getCurrentSum().compareTo(bidRequest.getBidRequestAmount())==0){
                    //5.如果投满了借款对象需要由哪些变化?
                    //      借款对象状态----->满标一审
                    bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_APPROVE_PENDING_1);
                    //      投标对象的状态---->满标一审
                    bidService.updateState(bidRequest.getId(),BidConst.BIDREQUEST_STATE_APPROVE_PENDING_1);
                }
                this.update(bidRequest);
            }
        }

    }

    @Override
    public void audit1(Long id, int state, String remark) {
        //1需要做什么判断
        //根据id查询借款对象,处于满标一审状态
        BidRequest bidRequest = this.get(id);
        if (bidRequest != null && bidRequest.getBidRequestState() == BidConst.BIDREQUEST_STATE_APPROVE_PENDING_1){
            //2需要参加什么对象
            //创建审核历史对象
            createBidRequestAuditHistory(bidRequest,BidRequestAuditHistory.FULL_AUDIT_1,state,remark);
          //3如果审核历史对象
            if(state == BidRequestAuditHistory.STATE_PASS){
                //等于标和借款对象有什么变化
                bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_APPROVE_PENDING_2);
                //  借款对象投标对象状态---->满标二审
                bidService.updateState(bidRequest.getId(),BidConst.BIDREQUEST_STATE_APPROVE_PENDING_2);
            }else {
                auditReject(bidRequest);
            }
            this.update(bidRequest);

        }
    }

    @Override
    public void audit2(Long id, int state, String remark) {

    }
    /**
     * 创建审核历史对象
     * @param bidRequest
     * @param auditType
     * @param state
     * @param remark
     */
    private void createBidRequestAuditHistory(BidRequest bidRequest,int auditType,int state,String remark){
        BidRequestAuditHistory brah = new BidRequestAuditHistory();
        brah.setBidRequestId(bidRequest.getId());
        brah.setAuditType(auditType);
        brah.setApplier(bidRequest.getCreateUser());
        brah.setApplyTime(new Date());
        brah.setAuditor(UserContext.getCurrent());
        brah.setAuditTime(new Date());
        brah.setRemark(remark);
        if(state==BidRequestAuditHistory.STATE_PASS){
            brah.setState(BidRequestAuditHistory.STATE_PASS);
        }else{
            brah.setState(BidRequestAuditHistory.STATE_REJECT);
        }
        bidRequestAuditHistoryService.save(brah);
    }
    private void auditReject(BidRequest bidRequest){
        //4.如果审核失败
        //  对于标和借款对象有什么变化
        bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_REJECTED);
        //  借款对象投标对象状态---->满审拒绝
        bidService.updateState(bidRequest.getId(),BidConst.BIDREQUEST_STATE_REJECTED);
        //  投资人账户有哪些变化?
        //     冻结减少,可用金额增加
        Map<Long,Account> accountMap = new HashMap<Long,Account>();
        Account account;
        Long accountId;
        for(Bid bid:bidRequest.getBids()){
            accountId = bid.getBidUser().getId();
            account = accountMap.get(accountId);
            if(account==null){
                account = accountService.get(accountId);
                accountMap.put(accountId,account);
            }
            account.setFreezedAmount(account.getFreezedAmount().subtract(bid.getAvailableAmount()));
            account.setUsableAmount(account.getUsableAmount().add(bid.getAvailableAmount()));
            //  需要记录哪些流水
            //      添加投标失败的流水
            accountFlowService.createBidFaileFlow(account,bid.getAvailableAmount());
        }
        for(Account bidUserAccount:accountMap.values()){
            accountService.update(bidUserAccount);
        }
        //  对于借款用户有哪些变化?
        //  用户移除正在借款的状态码
        Userinfo userinfo = userinfoService.get(bidRequest.getCreateUser().getId());
        userinfo.removeState(BitStatesUtils.HAS_BIDREQUEST_PROCESS);
        userinfoService.update(userinfo);
    }
}
