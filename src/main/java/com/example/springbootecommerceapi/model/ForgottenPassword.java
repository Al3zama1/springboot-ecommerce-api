package com.example.springbootecommerceapi.model;

import java.util.Objects;

public class ForgottenPassword {
    private String password;
    private String verifyPassword;

    public ForgottenPassword(String password, String verifyPassword) {
        this.password = password;
        this.verifyPassword = verifyPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVerifyPassword() {
        return verifyPassword;
    }

    public void setVerifyPassword(String verifyPassword) {
        this.verifyPassword = verifyPassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ForgottenPassword that = (ForgottenPassword) o;
        return Objects.equals(password, that.password) && Objects.equals(verifyPassword, that.verifyPassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password, verifyPassword);
    }

    @Override
    public String toString() {
        return "ForgottenPassword{" +
                "password='" + password + '\'' +
                ", verifyPassword='" + verifyPassword + '\'' +
                '}';
    }
}
