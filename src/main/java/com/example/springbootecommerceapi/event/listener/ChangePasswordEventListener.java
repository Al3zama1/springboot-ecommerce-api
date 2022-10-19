package com.example.springbootecommerceapi.event.listener;

import com.example.springbootecommerceapi.entity.UserEntity;
import com.example.springbootecommerceapi.event.ChangePasswordEvent;
import com.example.springbootecommerceapi.model.Email;
import com.example.springbootecommerceapi.service.EmailSenderService;
import com.example.springbootecommerceapi.service.PasswordResetEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ChangePasswordEventListener implements ApplicationListener<ChangePasswordEvent> {

    private final EmailSenderService emailSenderService;

    @Autowired
    public ChangePasswordEventListener(PasswordResetEmailService passwordResetEmailService) {
        this.emailSenderService = passwordResetEmailService;
    }

    @Async
    @Override
    public void onApplicationEvent(ChangePasswordEvent event) {
        UserEntity user = event.getUser();

        String url = event.getUrl();
        Email email = new Email(user.getEmail(), "testing@gmail.com", "Change Password Link");
        email.getModel().put("passwordResetUrl", url);
        email.getModel().put("firstName",user.getFirstName());
        email.getModel().put("lastName", user.getLastName());
        emailSenderService.sendEmail(email);

    }
}
