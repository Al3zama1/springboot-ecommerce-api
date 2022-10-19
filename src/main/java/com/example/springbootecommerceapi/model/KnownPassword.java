package com.example.springbootecommerceapi.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.Objects;

public class KnownPassword {
    @Email
    private String email;
    @Size(min = 8)
    private String oldPassword;
    @Size(min = 8)
    private String newPassword;

    public KnownPassword() {
    }

    public KnownPassword(String email, String oldPassword, String newPassword) {
        this.email = email;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KnownPassword that = (KnownPassword) o;
        return Objects.equals(email, that.email) && Objects.equals(oldPassword, that.oldPassword) && Objects.equals(newPassword, that.newPassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, oldPassword, newPassword);
    }

    @Override
    public String toString() {
        return "ChangeKnownPasswordDTO{" +
                "email='" + email + '\'' +
                ", oldPassword='" + oldPassword + '\'' +
                ", newPassword='" + newPassword + '\'' +
                '}';
    }
}
