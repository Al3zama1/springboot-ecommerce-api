package com.example.springbootecommerceapi.event.listener;

import com.example.springbootecommerceapi.entity.UserEntity;
import com.example.springbootecommerceapi.event.EmployeeRegistrationTokenEvent;
import com.example.springbootecommerceapi.model.Email;
import com.example.springbootecommerceapi.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class EmployeeRegistrationTokenEventListener implements ApplicationListener<EmployeeRegistrationTokenEvent> {
    private final EmailSenderService emailSenderService;

    @Autowired
    public EmployeeRegistrationTokenEventListener(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }


    @Async
    @Override
    public void onApplicationEvent(EmployeeRegistrationTokenEvent event) {


        Email email = new Email(event.getEmployeeEmail(), "testing@gmail.com", "New Employee Registration");
        email.getModel().put("token", event.getToken());
        email.getModel().put("adminFirstName",event.getAdmin().getFirstName());
        email.getModel().put("adminLastName", event.getAdmin().getLastName());
        emailSenderService.sendEmail(email, "employee-registration-template");
    }
}
