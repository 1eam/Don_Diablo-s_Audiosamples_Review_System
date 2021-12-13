package com.esther.dds.service;

import java.util.Locale;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.esther.dds.domain.Admin;
import com.esther.dds.domain.BoUser;
import com.esther.dds.domain.Demo;
import com.esther.dds.domain.User;

@EnableAsync
@Service
public class MailService {
    private final Logger log = LoggerFactory.getLogger(MailService.class);
    private final SpringTemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;
    // TODO  should be a configurable setting
    private final String BASE_URL = "http://localhost:8080";

    public MailService(JavaMailSender javaMailSender, SpringTemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultiPart, boolean isHtml) {
        log.debug("Sending Email");

        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
            message.setTo(to);
            message.setFrom("noreply@hexagon-backoffice-team.com");
            message.setSubject(subject);
            message.setText(content,isHtml);
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            log.warn("Email could not be sent to user '{}': {}", to, e.getMessage(), e);
        }
    }

    //user-side
    @Async
    public void sendEmailFromTemplate(User user, String templateName, String subject) {
        Locale locale = Locale.ENGLISH;
        Context context = new Context(locale);
        //sort of model
        context.setVariable("user",user);
        context.setVariable("baseURL",BASE_URL);
        String content = templateEngine.process(templateName,context);
        sendEmail(user.getEmail(),subject,content,false,true);
    }

    @Async
    public void sendEmailFromTemplate2(User user, Demo demo, String templateName, String subject) {
        Locale locale = Locale.ENGLISH;
        Context context = new Context(locale);
        //sort of model
        context.setVariable("user",user);
        context.setVariable("baseURL",BASE_URL);
        context.setVariable("demo",demo);
        String content = templateEngine.process(templateName,context);
        sendEmail(user.getEmail(),subject,content,false,true);
    }

    //bo-side
    @Async
    public void sendBoEmailFromTemplate(BoUser boUser, String templateName, String subject) {
        Locale locale = Locale.ENGLISH;
        Context context = new Context(locale);
        //sort of model
        context.setVariable("boUser",boUser);
        context.setVariable("baseURL",BASE_URL);
        String content = templateEngine.process(templateName,context);
        sendEmail(boUser.getEmail(),subject,content,false,true);
    }

    //admin-side
    @Async
    public void sendAdminEmailFromTemplate(Admin admin, String templateName, String subject) {
        Locale locale = Locale.ENGLISH;
        Context context = new Context(locale);
        //sort of model
        context.setVariable("admin",admin);
        context.setVariable("baseURL",BASE_URL);
        String content = templateEngine.process(templateName,context);
        sendEmail(admin.getEmail(),subject,content,false,true);
    }

    //user-side emails
    @Async
    public void sendActivationEmail(User user) {
        log.debug("Sending activation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "email/activation-link-email", "Hexagon Demo Drop, Activation-link");
    }

    @Async
    public void sendWelcomeEmail(User user) {
        log.debug("Sending welcome email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "email/welcome-email", "Demo drop system welcome");
    }

    @Async
    public void sendRejectionEmail(User user, Demo demo) {
        log.debug("Sending demo rejection email to '{}'", user.getEmail());
        sendEmailFromTemplate2(user, demo, "email/demo-rejection-email", "Updates regarding a sent demo");
    }

    @Async
    public void sendForwardedEmail(User user, Demo demo) {
        log.debug("Sending email regarding demo being forwarded to DD '{}'", user.getEmail());
        sendEmailFromTemplate2(user, demo,"email/demo-forwarded-email", "Updates regarding a sent demo");
    }

    //bo-side emails
    @Async
    public void sendBoActivationEmail(BoUser boUser) {
        log.debug("Sending activation email to '{}'", boUser.getEmail());
        sendBoEmailFromTemplate(boUser, "email/bo-activation-link-email", "Hexagon Demo Drop, Backoffice-worker Activation-link");
    }

    //admin-side emails
    @Async
    public void sendAdminActivationEmail(Admin admin) {
        log.debug("Sending admin activation email to '{}'", admin.getEmail());
        sendAdminEmailFromTemplate(admin, "email/admin-activation-link-email", "Hexagon Demo Drop, Admin Activation");
    }
}