package cn.wolfcode.p2p.base.service.impl;

import cn.wolfcode.p2p.base.domain.Account;
import cn.wolfcode.p2p.base.domain.IpLog;
import cn.wolfcode.p2p.base.domain.Logininfo;
import cn.wolfcode.p2p.base.domain.Userinfo;
import cn.wolfcode.p2p.base.mapper.LogininfoMapper;
import cn.wolfcode.p2p.base.service.IAccountService;
import cn.wolfcode.p2p.base.service.IIpLogService;
import cn.wolfcode.p2p.base.service.ILogininfoService;
import cn.wolfcode.p2p.base.service.IUserinfoService;
import cn.wolfcode.p2p.base.util.BidConst;
import cn.wolfcode.p2p.base.util.MD5;
import cn.wolfcode.p2p.base.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by wolfcode on 0008.
 */
@Service
@Transactional
public class LogininfoServiceImpl implements ILogininfoService {
    @Autowired
    private LogininfoMapper logininfoMapper;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IUserinfoService userinfoService;
    @Autowired
    private IIpLogService ipLogService;
    @Override
    public Logininfo register(String username, String password) {
        //1.查看该用户名在数据库中是否已经存在.
        int count = logininfoMapper.selectCountByUsername(username);
        //2.如果没有存在就创建一条记录.
        if(count>0){
            throw new RuntimeException("用户名已存在!");
        }
        Logininfo logininfo = new Logininfo();
        logininfo.setUsername(username);
        logininfo.setPassword(MD5.encoder(password));
        logininfo.setState(Logininfo.STATE_NORMAL);
        logininfo.setUserType(Logininfo.USERTYPE_USER);
        logininfoMapper.insert(logininfo);
        //创建用户的账户信息和基本信息
        Account account = new Account();
        account.setId(logininfo.getId());
        accountService.save(account);
        Userinfo userinfo = new Userinfo();
        userinfo.setId(logininfo.getId());
        userinfoService.save(userinfo);
        return logininfo;
    }

    @Override
    public boolean checkUsername(String username) {
        return logininfoMapper.selectCountByUsername(username)==0;
    }

    @Override
    public Logininfo login(String username, String password,int userType) {
        //1.把账户密码去数据库中查询,封装loginifo对象
        Logininfo logininfo = logininfoMapper.login(username,MD5.encoder(password),userType);
        IpLog ipLog = new IpLog();
        ipLog.setLoginTime(new Date());
        ipLog.setUsername(username);
        ipLog.setIp(UserContext.getIP());
        ipLog.setUserType(userType);
        //2.如果对象为null,抛出异常
        if(logininfo==null){
            ipLog.setState(IpLog.LOGIN_FAILED);
        }else{
            //3.如果对象不为null,把登录的对象存入到session中.
            ipLog.setState(IpLog.LOGIN_SUCCESS);
            UserContext.setCurrent(logininfo);
        }
        ipLogService.save(ipLog);
        return logininfo;
    }

    @Override
    public void initAdmin() {
        //1.判断数据库中是否有管理员
        int count = logininfoMapper.queryByUserType(Logininfo.USERTYPE_MANAGER);
        //2.如果没有创建一个
        if(count==0){
            Logininfo logininfo = new Logininfo();
            logininfo.setUserType(Logininfo.USERTYPE_MANAGER);
            logininfo.setState(Logininfo.STATE_NORMAL);
            logininfo.setUsername(BidConst.DEFAULT_ADMIN_ACCOUNT);
            logininfo.setPassword(MD5.encoder(BidConst.DEFAULT_ADMIN_PASSWORD));
            logininfoMapper.insert(logininfo);
        }
    }
}
