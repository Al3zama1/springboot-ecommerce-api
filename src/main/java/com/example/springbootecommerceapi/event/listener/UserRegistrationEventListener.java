package com.example.springbootecommerceapi.event.listener;

import com.example.springbootecommerceapi.entity.UserEntity;
import com.example.springbootecommerceapi.event.UserRegistrationEvent;
import com.example.springbootecommerceapi.model.Email;
import com.example.springbootecommerceapi.service.AccountActivationEmailService;
import com.example.springbootecommerceapi.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrationEventListener implements ApplicationListener<UserRegistrationEvent> {

    private final EmailSenderService emailSenderService;

    @Autowired
    public UserRegistrationEventListener(AccountActivationEmailService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    @Async
    @Override
    public void onApplicationEvent(UserRegistrationEvent event) {
        UserEntity user = event.getUser();

        String url = event.getUrl();
        Email email = new Email(user.getEmail(), "testing@gmail.com", "Activate Your Account");
        email.getModel().put("activationUrl", url);
        email.getModel().put("firstName",user.getFirstName());
        email.getModel().put("lastName", user.getLastName());
        emailSenderService.sendEmail(email);
    }
}
