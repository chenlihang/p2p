package cn.wolfcode.p2p.base.service.impl;

import cn.wolfcode.p2p.base.domain.MailVerify;
import cn.wolfcode.p2p.base.service.IEmailService;
import cn.wolfcode.p2p.base.service.IMailVerifyService;
import cn.wolfcode.p2p.base.util.BidConst;
import cn.wolfcode.p2p.base.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.UUID;

/**
 * Created by wolfcode on 0012.
 */
@Service@Transactional
public class EmailServiceImpl implements IEmailService {
    @Autowired
    private IMailVerifyService mailVerifyService;
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${email.applicationUrl}")
    private String applicationUrl;
    @Value("${spring.mail.username}")
    private String form;
    @Override
    public void sendEmail(String email) {
        //1.创建UUID.
        String uuid = UUID.randomUUID().toString();
        //2.把邮件相关的信息封装到MailVerify实体中.
        MailVerify mailVerify = new MailVerify();
        mailVerify.setEmail(email);
        mailVerify.setSendTime(new Date());
        mailVerify.setUserId(UserContext.getCurrent().getId());
        mailVerify.setUuid(uuid);
        mailVerifyService.save(mailVerify);
        //执行真正邮件发送
        //System.out.println(content);
        try {
            //3.发送邮件
            StringBuilder content = new StringBuilder(100);
            content.append("感谢注册P2P,这是您的认证邮件,点击<a href='").append(applicationUrl)
                    .append("/bindEmail?key=")
                    .append(uuid)
                    .append("'>这里</a>完成认证,有效期为").append(BidConst.EMAIL_VAILD_DAY)
                    .append("天,请尽快认证");
            sendEmail(email,content.toString());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    private void sendEmail(String email,String content) throws Exception {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        //设置收件人
        helper.setTo(email);
        //设置邮件标题
        helper.setSubject("这是一份认证的邮件!");
        //设置发件人
        helper.setFrom(form);
        //设置内容
        helper.setText(content,true);
        //执行发送
        javaMailSender.send(mimeMessage);
    }
}
