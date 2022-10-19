package com.example.springbootecommerceapi.entity;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(
        name = "activationToken"
)
public class ActivationTokenEntity {


    @Id
    @Column(
            name = "tokenNumber"
    )
    @SequenceGenerator(
            name = "activationTokenSequence",
            sequenceName = "activationTokenSequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "activationTokenSequence"
    )
    private Long tokenNumber;
    @OneToOne
    @JoinColumn(
            name = "userNumber",
            referencedColumnName = "userNumber"
    )
    private UserEntity userEntity;

    @Column(
            unique = true
    )
    private String token;


    public ActivationTokenEntity() {
    }

    public ActivationTokenEntity(UserEntity userEntity, String token) {
        this.userEntity = userEntity;
        this.token = token;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActivationTokenEntity that = (ActivationTokenEntity) o;
        return Objects.equals(tokenNumber, that.tokenNumber) && Objects.equals(userEntity, that.userEntity) && Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tokenNumber, userEntity, token);
    }

    @Override
    public String toString() {
        return "ActivationTokenEntity{" +
                "tokenNumber=" + tokenNumber +
                ", userEntity=" + userEntity +
                ", token='" + token + '\'' +
                '}';
    }
}
