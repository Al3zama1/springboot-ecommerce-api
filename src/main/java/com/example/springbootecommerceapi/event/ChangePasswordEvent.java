package com.example.springbootecommerceapi.event;

import com.example.springbootecommerceapi.entity.UserEntity;
import org.springframework.context.ApplicationEvent;

public class ChangePasswordEvent extends ApplicationEvent {

    private final UserEntity user;
    private final String url;

    public ChangePasswordEvent(UserEntity source, String url) {
        super(source);
        this.user = source;
        this.url = url;
    }

    public UserEntity getUser() {
        return user;
    }

    public String getUrl() {
        return url;
    }
}
