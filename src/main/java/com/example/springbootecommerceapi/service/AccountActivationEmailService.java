package com.example.springbootecommerceapi.service;

import com.example.springbootecommerceapi.model.Email;
import freemarker.template.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

@Service
public class AccountActivationEmailService implements EmailSenderService{

//    private final JavaMailSender mailSender;
    private final JavaMailSender mailSender;
    private final Configuration fmConfiguration;

    @Autowired
    public AccountActivationEmailService(JavaMailSender mailSender, Configuration fmConfiguration) {
        this.mailSender = mailSender;
        this.fmConfiguration = fmConfiguration;
    }

    @Override
    public void sendEmail(Email email) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setSubject(email.getSubject());
            mimeMessageHelper.setFrom(email.getFrom());
            mimeMessageHelper.setTo(email.getTo());

            email.setContent(getContentFromTemplate(email.getModel()));
            mimeMessageHelper.setText(email.getContent(), true);

            mailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException e) {
            throw new RuntimeException("Something went Wrong");
        }

    }

    @Override
    public String getContentFromTemplate(Map<String, Object> model) {
        StringBuffer content = new StringBuffer();

        try {
            content.append(FreeMarkerTemplateUtils.processTemplateIntoString(
                    fmConfiguration.getTemplate("activate-account-template.flth"), model));
        } catch (Exception e) {
            throw new RuntimeException("Something went Wrong");
        }
        return  content.toString();
    }
}
