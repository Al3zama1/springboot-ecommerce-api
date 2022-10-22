package com.example.springbootecommerceapi.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "employeeRegistrationToken")
public class EmployeeRegistrationToken {

    @Id
    @Column(
            name = "tokenNumber"
    )
    @SequenceGenerator(
            name = "employeeRegistrationTokenSequence",
            sequenceName = "employeeRegistrationTokenSequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "employeeRegistrationTokenSequence"
    )
    private Long tokenNumber;
    @OneToOne
    @JoinColumn(
            name = "createdBy",
            referencedColumnName = "userNumber"
    )
    private UserEntity userEntity;

    @Column(
            unique = true,
            nullable = false
    )
    private String token;
    @Column(
            unique = true,
            name = "employeeEmail",
            nullable = false
    )
    private String employeeEmail;

    public EmployeeRegistrationToken() {
    }

    public EmployeeRegistrationToken(UserEntity userEntity, String token, String employeeEmail) {
        this.userEntity = userEntity;
        this.token = token;
        this.employeeEmail = employeeEmail;
    }

    public Long getTokenNumber() {
        return tokenNumber;
    }

    public void setTokenNumber(Long tokenNumber) {
        this.tokenNumber = tokenNumber;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeRegistrationToken that = (EmployeeRegistrationToken) o;
        return Objects.equals(tokenNumber, that.tokenNumber) && Objects.equals(userEntity, that.userEntity) && Objects.equals(token, that.token) && Objects.equals(employeeEmail, that.employeeEmail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tokenNumber, userEntity, token, employeeEmail);
    }

    @Override
    public String toString() {
        return "EmployeeRegistrationToken{" +
                "tokenNumber=" + tokenNumber +
                ", userEntity=" + userEntity +
                ", token='" + token + '\'' +
                ", employeeEmail='" + employeeEmail + '\'' +
                '}';
    }
}
