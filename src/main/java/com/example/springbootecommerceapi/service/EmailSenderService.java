package com.example.springbootecommerceapi.service;

import com.example.springbootecommerceapi.model.Email;
import org.springframework.stereotype.Service;

import java.util.Map;


public interface EmailSenderService {

    void sendEmail(Email email);
    String getContentFromTemplate(Map<String, Object> model);
}
