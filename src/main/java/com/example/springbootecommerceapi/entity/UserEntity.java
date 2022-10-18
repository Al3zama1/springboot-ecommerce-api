package com.example.springbootecommerceapi.entity;

import com.example.springbootecommerceapi.model.Gender;

import com.example.springbootecommerceapi.model.Role;
import com.example.springbootecommerceapi.validation.Phone;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@Table(
        name = "userEntity"
)
public class UserEntity {

    @Id
    @Column(
            name = "userNumber"
    )
    @SequenceGenerator(
            name = "userSequence",
            sequenceName = "userSequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "userSequence"
    )
    private Long userNumber;
    @NotBlank
    @Column(
            name = "firstName",
            nullable = false
    )
    private String firstName;
    @NotBlank
    @Column(
            name = "lastName",
            nullable = false
    )
    private String lastName;
    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(
            nullable = false
    )

    private Role role;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false
    )
    private Gender gender;
    @Phone
    @Column(
            nullable = false
    )
    private String phone;
    @NotBlank
    @Email
    @Column(
            nullable = false,
            unique = true
    )
    private String email;
    @Size(min = 8)
    @Column(
            nullable = false
    )
    private String password;
    @NotBlank
    @Column(
            nullable = false
    )
    private String street;
    @NotBlank
    @Column(
            nullable = false
    )
    private String city;
    @NotBlank
    @Column(
            nullable = false
    )
    private String state;
    @NotBlank
    @Column(
            name = "zipCode",
            nullable = false
    )
    private String zipCode;
    private boolean active;


    public UserEntity() {
    }

    public UserEntity(Long userNumber, String firstName, String lastName, Role role, Gender gender, String phone, String email, String password, String street, String city, String state, String zipCode, boolean active) {
        this.userNumber = userNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.active = active;
    }


    public Long getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(Long userNumber) {
        this.userNumber = userNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(String roles) {
        this.role = role;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return active == that.active && Objects.equals(userNumber, that.userNumber) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(role, that.role) && gender == that.gender && Objects.equals(phone, that.phone) && Objects.equals(email, that.email) && Objects.equals(password, that.password) && Objects.equals(street, that.street) && Objects.equals(city, that.city) && Objects.equals(state, that.state) && Objects.equals(zipCode, that.zipCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userNumber, firstName, lastName, role, gender, phone, email, password, street, city, state, zipCode, active);
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "userNumber=" + userNumber +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", roles='" + role + '\'' +
                ", gender=" + gender +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", active=" + active +
                '}';
    }
}
