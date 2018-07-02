package cn.wolfcode.p2p.base.service.impl;

import cn.wolfcode.p2p.base.service.IVerifyCodeService;
import cn.wolfcode.p2p.base.util.BidConst;
import cn.wolfcode.p2p.base.util.DateUtil;
import cn.wolfcode.p2p.base.util.UserContext;
import cn.wolfcode.p2p.base.vo.VerifyCodeVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.UUID;

/**
 * Created by wolfcode on 0012.
 */
@Service
@Transactional
public class VerifyCodeServiceImpl implements IVerifyCodeService {
    @Value("${msg.url}")
    private String msgUrl;
    @Value("${msg.username}")
    private String username;
    @Value("${msg.password}")
    private String password;
    @Value("${msg.apiKey}")
    private String apiKey;
    @Override
    public void sendVerifyCode(String phoneNumber) {
        //0.判断用户是否有发送过认证码,当前是否在发送时间间隔之间
        VerifyCodeVo vo = UserContext.getVerifyCodeVo();
        //(当前时间-发送的时候)>90
        if(vo==null || DateUtil.getDateBetweenTime(new Date(),vo.getSendTime())>90){
            //1.生成随机验证码
            String randomCode = UUID.randomUUID().toString().substring(0, 4);
            StringBuilder msg = new StringBuilder(50);
            msg.append("您的手机认证码为:").append(randomCode).append(",有效期为").append(BidConst.MESSAGE_VAILD_TIME).append("分钟,请尽快使用");
            //2.执行短信的发送.
            //模拟短信的发送.
            //System.out.println(msg);
            try {
                sendMessage(phoneNumber,msg.toString());
                //sendRealMessage(phoneNumber,msg.toString());
                vo = new VerifyCodeVo();
                vo.setPhoneNumber(phoneNumber);
                vo.setRandomCode(randomCode);
                vo.setSendTime(new Date());
                UserContext.setVerifyCodeVo(vo);
            } catch (Exception e) {
               throw new RuntimeException(e.getMessage());
            }
        }else{
            throw new RuntimeException("发送短信太频繁,请稍后再试.");
        }
    }
    private void sendMessage(String phoneNumber,String msg) throws Exception {
        //模拟Http请求,访问模拟短信网关
        URL url = new URL(msgUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        StringBuilder param = new StringBuilder(100);
        param.append("username=").append(username)
                .append("&password=").append(password)
                .append("&apiKey=").append(apiKey)
                .append("&phoneNumber=").append(phoneNumber)
                .append("&content=").append(msg);
        conn.getOutputStream().write(param.toString().getBytes("UTF-8"));
        conn.connect();
        String repStr = StreamUtils.copyToString(conn.getInputStream(), Charset.forName("UTF-8"));
        if(!"success".equalsIgnoreCase(repStr)){
            throw new RuntimeException("短信调用失败!!!");
        }
    }
    private void sendRealMessage(String phoneNumber,String msg) throws Exception {
        //http://utf8.api.smschinese.cn/?Uid=本站用户名&Key=接口安全秘钥&smsMob=手机号码&smsText=验证码:8888
        //模拟Http请求,访问模拟短信网关
        URL url = new URL("http://utf8.api.smschinese.cn");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        StringBuilder param = new StringBuilder(100);
        param.append("Uid=").append("lanxw02")
                .append("&Key=").append("f674f701346030e3af68")
                .append("&smsMob=").append(phoneNumber)
                .append("&smsText=").append(msg);
        conn.getOutputStream().write(param.toString().getBytes("UTF-8"));
        conn.connect();
        String respCode = StreamUtils.copyToString(conn.getInputStream(), Charset.forName("UTF-8"));
        int code = Integer.parseInt(respCode);
        System.out.println(code);
        if(code<=0){
            throw new RuntimeException("短信调用失败,请联系管理员!");
        }
    }
    @Override
    public boolean validate(String phoneNumber, String verifyCode) {
        //1.判断手机号码是否和接收验证码的手机号码是一致.
        //2.验证码是正确的
        //3.验证码在有效时间之内
        VerifyCodeVo vo = UserContext.getVerifyCodeVo();
        if(vo!=null
                && vo.getPhoneNumber().equals(phoneNumber)
                && vo.getRandomCode().equalsIgnoreCase(verifyCode)
                && DateUtil.getDateBetweenTime(vo.getSendTime(),new Date())<BidConst.MESSAGE_VAILD_TIME*60
        ){
            return true;
        }
        return false;
    }
}
