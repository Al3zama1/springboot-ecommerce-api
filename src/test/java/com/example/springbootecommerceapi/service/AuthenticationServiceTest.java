package com.example.springbootecommerceapi.service;

import com.example.springbootecommerceapi.entity.ActivationTokenEntity;
import com.example.springbootecommerceapi.entity.UserEntity;
import com.example.springbootecommerceapi.exception.AccountActivationException;
import com.example.springbootecommerceapi.exception.UserException;
import com.example.springbootecommerceapi.model.Gender;
import com.example.springbootecommerceapi.model.Role;
import com.example.springbootecommerceapi.model.UserBuilder;
import com.example.springbootecommerceapi.repository.ActivationTokenRepository;
import com.example.springbootecommerceapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;

import javax.servlet.http.HttpServletRequest;
import java.util.Currency;
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
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ActivationTokenRepository activationTokenRepository;
    @Mock
    HttpServletRequest request;
    @Mock
    ApplicationEventPublisher publisher;
    @InjectMocks
    private AuthenticationService authenticationService;


    @Test
    void registerCustomer_whenEmailDoesNotExist() {
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


        // assume token with given email does not exist
        given(userRepository.existsByEmail(customer.getEmail())).willReturn(false);


        // WHEN
        authenticationService.registerCustomer(customer);

        // THEN
        then(userRepository).should().save(customer);
        then(activationTokenRepository).should().save(any(ActivationTokenEntity.class));

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
        customer.setRole(Role.CUSTOMER);

        given(userRepository.existsByEmail(customer.getEmail())).willReturn(true);

        //  WHEN
        assertThatThrownBy(() -> authenticationService.registerCustomer(customer))
                .isInstanceOf(UserException.class)
                .hasMessage("User with given email exists");

        // THEN
        then(userRepository).should(never()).save(any());
    }

    @Test
    void activateAccount_whenTokenIsValid(){
        // GIVEN
        String token = UUID.randomUUID().toString();
        UserEntity user = new UserBuilder()
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
        user.setRole(Role.CUSTOMER);
        ActivationTokenEntity activationToken = new ActivationTokenEntity(user ,token);

        // assume token exists
        given(activationTokenRepository.findByToken(token)).willReturn(Optional.of(activationToken));

        // WHEN
        authenticationService.activateAccount(token);

        // THEN
        ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);
        then(activationTokenRepository).should().delete(activationToken);
        then(userRepository).should().save(userCaptor.capture());
        assertThat(userCaptor.getValue().isActive()).isEqualTo(true);

    }

    @Test
    void activateAccount_whenTokenIsNotValid_throwAccountActivationException() {
        // GIVEN
        String token = UUID.randomUUID().toString();

        given(activationTokenRepository.findByToken(token)).willReturn(Optional.empty());

        // WHEN
        assertThatThrownBy(() -> authenticationService.activateAccount(token))
                .isInstanceOf(AccountActivationException.class)
                .hasMessage("Invalid activation token");

        // THEN
        then(activationTokenRepository).should(never()).delete(any());
        then(userRepository).should(never()).save(any());

    }

}