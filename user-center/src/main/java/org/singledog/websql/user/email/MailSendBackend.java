package org.singledog.websql.user.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-14
 * @modify
 * @copyright Navi Tsp
 */
@Component
public class MailSendBackend {
    private static final Logger logger = LoggerFactory.getLogger(MailSendBackend.class);

    @Value("${email.from:}")
    private String fromEmail;
    @Autowired
    private MailSender mailSender;

    public void sendEmail(String email, String subject, String content){
        this.sendEmail(new String[]{email}, subject, content);
    }

    public void sendEmail(String[] email, String subject, String content){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(email);
        simpleMailMessage.setFrom(fromEmail);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(content);
        mailSender.send(simpleMailMessage);
        logger.info("send simple email success !");
    }

    public void sendHtmlEmail(String email, String subject, String content){
        this.sendHtmlEmail(new String[]{email}, subject, content);
    }

    public void sendHtmlEmail(String[] email, String subject, String content){
        if(!(mailSender instanceof JavaMailSenderImpl)) {
            throw new RuntimeException("can not send html email !");
        }

        JavaMailSenderImpl javaMailSender = (JavaMailSenderImpl) mailSender;

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true,"utf-8");
            messageHelper.setTo(email);
            messageHelper.setFrom(fromEmail);
            messageHelper.setSubject(subject);
            messageHelper.setText(content, true);
            ((JavaMailSenderImpl) mailSender).send(mimeMessage);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }

        logger.info("send html email success !");
    }

}
