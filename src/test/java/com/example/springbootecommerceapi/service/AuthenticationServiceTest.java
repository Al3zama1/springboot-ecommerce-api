package com.example.springbootecommerceapi.service;

import com.example.springbootecommerceapi.entity.UserEntity;
import com.example.springbootecommerceapi.exception.UserException;
import com.example.springbootecommerceapi.model.Gender;
import com.example.springbootecommerceapi.model.Role;
import com.example.springbootecommerceapi.model.UserBuilder;
import com.example.springbootecommerceapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void registerCustomer() {
        // GIVEN
        UserEntity customer = new UserBuilder()
                .firstName("John")
                .lastName("Last")
                .gender(Gender.MALE)
                .phone("(323) 456-1234")
                .email("john.last@gmail.com")
                .password("12345678")
                .street("5678 S 88Th St")
                .city("Los Angeles")
                .state("California")
                .zipCode("90002")
                .build();
        customer.setRole(Role.CUSTOMER);

        // user with given email does not exist already
        given(userRepository.existsByEmail(customer.getEmail())).willReturn(false);

        // WHEN
        authenticationService.registerCustomer(customer);

        // THEN
        then(userRepository).should().save(customer);

    }

    @Test
    void registerCustomer_whenEmailExists_throwUserException() {
        // GIVEN
        UserEntity customer = new UserBuilder()
                .firstName("John")
                .lastName("Last")
                .gender(Gender.MALE)
                .phone("(323) 456-1234")
                .email("john.last@gmail.com")
                .password("12345678")
                .street("5678 S 88Th St")
                .city("Los Angeles")
                .state("California")
                .zipCode("90002")
                .build();

        given(userRepository.existsByEmail(customer.getEmail())).willReturn(true);

        //  WHEN
        assertThatThrownBy(() -> authenticationService.registerCustomer(customer))
                .isInstanceOf(UserException.class)
                .hasMessage("User with given email exists");

        // THEN
        then(userRepository).should(never()).save(any());


    }

}