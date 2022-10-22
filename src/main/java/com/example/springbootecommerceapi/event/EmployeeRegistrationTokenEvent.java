package com.example.springbootecommerceapi.event;

import com.example.springbootecommerceapi.entity.UserEntity;
import org.springframework.context.ApplicationEvent;

public class EmployeeRegistrationTokenEvent extends ApplicationEvent {

    private final UserEntity admin;
    private final String employeeEmail;
    private final String token;

    public EmployeeRegistrationTokenEvent(UserEntity source, String employeeEmail, String token) {
        super(source);
        this.admin = source;
        this.employeeEmail = employeeEmail;
        this.token = token;
    }

    public UserEntity getAdmin() {
        return admin;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public String getToken() {
        return token;
    }
}
