package com.example.springbootecommerceapi.model;

import com.example.springbootecommerceapi.entity.UserEntity;

public class UserBuilder {

    private Long userNumber;
    private String firstName;
    private String lastName;
    private Role role;
    private Gender gender;
    private String phone;
    private String email;
    private String password;
    private String street;
    private String city;
    private String state;
    private String zipCode;

    public UserBuilder userNumber(Long userNumber) {
        this.userNumber = userNumber;
        return this;
    }

    public UserBuilder firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserBuilder lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserBuilder role(Role role) {
        this.role = role;
        return this;
    }

    public UserBuilder gender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public UserBuilder phone(String phone) {
        this.phone = phone;
        return this;
    }

    public UserBuilder email(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder password(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder street(String street) {
        this.street = street;
        return this;
    }

    public UserBuilder city(String city) {
        this.city = city;
        return this;
    }

    public UserBuilder state(String state) {
        this.state = state;
        return this;
    }

    public UserBuilder zipCode(String zipCode) {
        this.zipCode = zipCode;
        return this;
    }

    public UserEntity build() {
        return new UserEntity(userNumber, firstName, lastName, role, gender, phone, email, password, street, city, state, zipCode, false);
    }
}
