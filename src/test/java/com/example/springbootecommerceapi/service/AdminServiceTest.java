package com.example.springbootecommerceapi.service;

import com.example.springbootecommerceapi.entity.EmployeeRegistrationToken;
import com.example.springbootecommerceapi.entity.UserEntity;
import com.example.springbootecommerceapi.exception.UserException;
import com.example.springbootecommerceapi.model.Gender;
import com.example.springbootecommerceapi.model.Role;
import com.example.springbootecommerceapi.model.UserBuilder;
import com.example.springbootecommerceapi.repository.EmployeeRegistrationRepository;
import com.example.springbootecommerceapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmployeeRegistrationRepository employeeRegistrationRepository;
    @InjectMocks
    private AdminService adminService;
    @Captor
    ArgumentCaptor<EmployeeRegistrationToken> employeeRegistrationTokenCaptor;

    @Test
    void createEmployeeRegistrationToken_whenIsAdmin_createToken() {
        // GIVEN
        String adminEmail = "admin@gmail.com";
        String employeeEmail = "employee@gmail.com";

        UserEntity admin = new UserBuilder()
                .firstName("John")
                .lastName("Last")
                .gender(Gender.MALE)
                .phone("(323) 456-1234")
                .email(adminEmail)
                .password("12345678")
                .street("5678 S 88Th St")
                .city("Los Angeles")
                .state("California")
                .zipCode("90002")
                .build();
        admin.setActive(true);
        admin.setUserNumber(1L);
        admin.setRole(Role.ADMIN);

        String token = UUID.randomUUID().toString();

        EmployeeRegistrationToken registrationToken =
                new EmployeeRegistrationToken(admin, token, employeeEmail);

        // assume admin email exists and is actually and admin
        given(userRepository.findByEmail(adminEmail)).willReturn(Optional.of(admin));

        // WHEN
        adminService.createEmployeeRegistrationToken(adminEmail, employeeEmail);

        // THEN
        then(employeeRegistrationRepository).should().save(employeeRegistrationTokenCaptor.capture());
        assertThat(employeeRegistrationTokenCaptor.getValue().getEmployeeEmail())
                .isEqualTo(employeeEmail);
        assertThat(employeeRegistrationTokenCaptor.getValue().getUserEntity())
                .isEqualTo(admin);
    }

    @Test
    void createEmployeeRegistrationToken_whenIsNotAdmin_throwUserException() {
        // GIVEN
        String adminEmail = "admin@gmail.com";
        String employeeEmail = "employee@gmail.com";

        UserEntity admin = new UserBuilder()
                .firstName("John")
                .lastName("Last")
                .gender(Gender.MALE)
                .phone("(323) 456-1234")
                .email(adminEmail)
                .password("12345678")
                .street("5678 S 88Th St")
                .city("Los Angeles")
                .state("California")
                .zipCode("90002")
                .build();
        admin.setActive(true);
        admin.setUserNumber(1L);
        admin.setRole(Role.EMPLOYEE);

        String token = UUID.randomUUID().toString();

        EmployeeRegistrationToken registrationToken =
                new EmployeeRegistrationToken(admin, token, employeeEmail);

        // assume admin email exists and is actually and admin
        given(userRepository.findByEmail(adminEmail)).willReturn(Optional.of(admin));

        // WHEN
        assertThatThrownBy(() -> adminService.createEmployeeRegistrationToken(adminEmail, employeeEmail))
                .isInstanceOf(UserException.class)
                        .hasMessage("You must be an admin to create employee registration token");

        // THEN
        then(employeeRegistrationRepository).should(never())
                .save(any());
    }

    @Test
    void createEmployeeRegistrationToken_whenAdminEmailDoesNotExist_throwUserException() {
        // GIVEN
        String adminEmail = "admin@gmail.com";
        String employeeEmail = "employee@gmail.com";

        String token = UUID.randomUUID().toString();
        

        // assume admin email exists and is actually and admin
        given(userRepository.findByEmail(adminEmail)).willReturn(Optional.empty());

        // WHEN
        assertThatThrownBy(() ->  adminService.createEmployeeRegistrationToken(adminEmail, employeeEmail))
                .isInstanceOf(UserException.class)
                .hasMessage("User does not exist");

        // THEN
        then(employeeRegistrationRepository)
                .should(never()).save(any());

    }



}