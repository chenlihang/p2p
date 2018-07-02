package cn.wolfcode.p2p.base.service.impl;

import cn.wolfcode.p2p.base.domain.MailVerify;
import cn.wolfcode.p2p.base.domain.Userinfo;
import cn.wolfcode.p2p.base.mapper.UserinfoMapper;
import cn.wolfcode.p2p.base.service.IMailVerifyService;
import cn.wolfcode.p2p.base.service.IUserinfoService;
import cn.wolfcode.p2p.base.service.IVerifyCodeService;
import cn.wolfcode.p2p.base.util.BidConst;
import cn.wolfcode.p2p.base.util.BitStatesUtils;
import cn.wolfcode.p2p.base.util.DateUtil;
import cn.wolfcode.p2p.base.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by wolfcode on 0010.
 */
@Service
@Transactional
public class UserinfoServiceImpl implements IUserinfoService {
    @Autowired
    private UserinfoMapper userinfoMapper;
    @Autowired
    private IVerifyCodeService verifyCodeService;
    @Autowired
    private IMailVerifyService mailVerifyService;
    @Override
    public int save(Userinfo userinfo) {
        return userinfoMapper.insert(userinfo);
    }

    @Override
    public int update(Userinfo userinfo) {
        int count = userinfoMapper.updateByPrimaryKey(userinfo);
        if(count==0){
            throw new RuntimeException("乐观锁异常:"+userinfo.getId());
        }
        return count;
    }

    @Override
    public Userinfo get(Long id) {
        return userinfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public Userinfo getCurrent() {
        return this.get(UserContext.getCurrent().getId());
    }

    @Override
    public void bindPhone(String phoneNumber, String verifyCode) {
        //1,判断手机号码和验证码是否合法的.
        boolean isVaild = verifyCodeService.validate(phoneNumber,verifyCode);
        if(isVaild){
            //2.获取到当前登录用户的userinfo信息,判断是否绑定过手机.
            Userinfo userinfo = this.getCurrent();
            if(userinfo.getHasBindPhone()){
                throw new RuntimeException("您已经绑定了手机号码,请不要重复绑定");
            }
            //3.给当前用户的userinfo添加手机认证的状态码,设置到userinfo中phoneNumber
            userinfo.setPhoneNumber(phoneNumber);
            userinfo.addState(BitStatesUtils.OP_BIND_PHONE);
            //4.更新userinfo对象
            this.update(userinfo);
        }else{
            throw new RuntimeException("验证码验证失败,请重新发送.");
        }

    }

    @Override
    public void bindEmail(String key) {
        //1.根据key查询是否有这个记录
        MailVerify mailVerify = mailVerifyService.selectByUUID(key);
        if(mailVerify==null){
            throw new RuntimeException("邮箱认证地址有误,请重新发送!");
        }
        //2.校验时间是否有效的
        if(DateUtil.getDateBetweenTime(mailVerify.getSendTime(),new Date())> BidConst.EMAIL_VAILD_DAY*24*60*60){
            throw new RuntimeException("邮件已经过了有效期,请重新发送!");
        }
        //3.判断用户是否已经绑定邮件了,
        Userinfo userinfo = this.get(mailVerify.getUserId());
        if(userinfo.getHasBindEmail()){
            throw new RuntimeException("您已经绑定邮箱了,请不要重复绑定!");
        }
        userinfo.setEmail(mailVerify.getEmail());
        userinfo.addState(BitStatesUtils.OP_BIND_EMAIL);
        //4.给userinfo设置email属性<给userinfo添加邮箱认证的状态码.
        this.update(userinfo);
    }

    @Override
    public void basicInfoSave(Userinfo userinfo) {
        Userinfo current = this.getCurrent();
        current.setEducationBackground(userinfo.getEducationBackground());
        current.setHouseCondition(userinfo.getHouseCondition());
        current.setIncomeGrade(userinfo.getIncomeGrade());
        current.setKidCount(userinfo.getKidCount());
        current.setMarriage(userinfo.getMarriage());
        if(!current.getIsBasicInfo()){
            current.addState(BitStatesUtils.OP_BASIC_INFO);
        }
        this.update(current);
    }
}
