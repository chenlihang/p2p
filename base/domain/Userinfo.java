package cn.wolfcode.p2p.base.domain;

import cn.wolfcode.p2p.base.util.BitStatesUtils;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by wolfcode on 0010.
 */
@Setter@Getter
public class Userinfo extends BaseDomain {
    private int version;//版本号，用作乐观锁
    private long bitState = 0;//用户状态值
    private String realName;//用户实名值（冗余数据）
    private String idNumber;//用户身份证号（冗余数据）
    private String phoneNumber;//用户电话
    private String email;//电子邮箱
    private int score;//风控材料分数
    private Long realAuthId;//实名认证ID
    private SystemDictionaryItem incomeGrade;//收入
    private SystemDictionaryItem marriage;//婚姻情况
    private SystemDictionaryItem kidCount;//子女情况
    private SystemDictionaryItem educationBackground;//学历
    private SystemDictionaryItem houseCondition;//住房条件
    public boolean getHasBindPhone(){
        return BitStatesUtils.hasState(this.bitState,BitStatesUtils.OP_BIND_PHONE);
    }
    public boolean getHasBindEmail(){
        return BitStatesUtils.hasState(this.bitState,BitStatesUtils.OP_BIND_EMAIL);
    }
    public void addState(long state){
        this.bitState = BitStatesUtils.addState(this.bitState,state);
    }
    public boolean getIsBasicInfo(){
        return BitStatesUtils.hasState(this.bitState,BitStatesUtils.OP_BASIC_INFO);
    }
    public boolean getIsRealAuth(){
        return BitStatesUtils.hasState(this.bitState,BitStatesUtils.OP_REAL_AUTH);
    }
    public boolean getIsVedioAuth(){
        return BitStatesUtils.hasState(this.bitState,BitStatesUtils.OP_VEDIO_AUTH);
    }
    public boolean getHasBidRequestProcess(){
        return BitStatesUtils.hasState(this.bitState,BitStatesUtils.HAS_BIDREQUEST_PROCESS);
    }
    public void removeState(long state){
        this.bitState = BitStatesUtils.removeState(this.bitState,state);
    }
}
