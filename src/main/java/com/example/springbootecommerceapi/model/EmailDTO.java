package com.example.springbootecommerceapi.model;

import javax.validation.constraints.Email;
import java.util.Objects;

public class EmailDTO {
    @Email
    private String email;

    public EmailDTO() {
    }

    public EmailDTO(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmailDTO emailDTO = (EmailDTO) o;
        return Objects.equals(email, emailDTO.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }


}
