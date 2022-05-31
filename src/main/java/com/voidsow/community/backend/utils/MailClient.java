package com.voidsow.community.backend.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class MailClient {
    private final static Logger logger = LoggerFactory.getLogger(MailClient.class);

    private JavaMailSender mailSender;

    @Value("${community.brand}")
    String brand;

    @Value("${community.domain}")
    String domain;

    @Value("${server.servlet.context-path}")
    String contextPath;

    TemplateEngine templateEngine;

    @Autowired
    public MailClient(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Value("${spring.mail.username}")
    String from;

    public void sendMail(String to, String subject, String content) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom(from);
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        messageHelper.setText(content, true);
        mailSender.send(messageHelper.getMimeMessage());
    }

    public void sendActivationEmail(String to, String activationCode) throws MessagingException {
        Context context = new Context();
        context.setVariable("brand", brand);
        context.setVariable("activationLink", "http://" + domain + contextPath + "activate/" + activationCode);
        sendMail(to, "激活" + brand + "账号", templateEngine.process("activation", context));
    }
}
