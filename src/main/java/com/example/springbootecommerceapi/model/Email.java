package com.example.springbootecommerceapi.model;

import java.util.HashMap;
import java.util.Map;

public class Email {

    private String to;
    private String from;
    private String subject;
    private String content;
    private Map<String, Object> model;

    public Email(String to, String from, String subject) {
        this.to = to;
        this.from = from;
        this.subject = subject;
        this.model = new HashMap<>();
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Map<String, Object> getModel() {
        return model;
    }

    public void setModel(Map<String, Object> model) {
        this.model = model;
    }
}
