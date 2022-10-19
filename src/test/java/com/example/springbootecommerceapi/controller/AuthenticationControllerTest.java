package com.example.springbootecommerceapi.controller;

import com.example.springbootecommerceapi.config.SecurityConfiguration;
import com.example.springbootecommerceapi.entity.UserEntity;
import com.example.springbootecommerceapi.model.*;
import com.example.springbootecommerceapi.repository.UserRepository;
import com.example.springbootecommerceapi.service.AuthenticationService;
import com.example.springbootecommerceapi.service.JpaUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {AuthenticationController.class})
@Import(value = {SecurityConfiguration.class, JpaUserDetailsService.class})
class AuthenticationControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    AuthenticationService authenticationService;
    @MockBean
    UserRepository userRepository;

    @Test
    void registerCustomer_whenValidData_return201() throws Exception {
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

        // WHEN
        mockMvc.perform(post("/api/ecommerce/v1/authentication/register-customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer))
                .accept(MediaType.APPLICATION_JSON)
                .with(csrf())
                ).andExpect(status().isCreated());


        // then
        then(authenticationService).should().registerCustomer(customer);

    }

    @Test
    void registerCustomer_whenInvalidData_return422() throws Exception {
        // GIVEN
        UserEntity customer = new UserBuilder()
                .firstName("John")
                .lastName("Last")
                .gender(Gender.MALE)
                .phone("(323) 456-1234")
                .email("john.lastgmail.com")
                .password("12345678")
                .street("5678 S 88Th St")
                .city("Los Angeles")
                .state("California")
                .zipCode("90002")
                .build();

        // WHEN
        mockMvc.perform(post("/api/ecommerce/v1/authentication/register-customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer))
                .accept(MediaType.APPLICATION_JSON)
                .with(csrf())
        ).andExpect(status().isUnprocessableEntity());

        // THEN
        then(authenticationService).should(never()).registerCustomer(any());

    }

    @Test
    void activateAccount_whenTokenProvided_return200() throws Exception {
        // GIVEN
        String token = UUID.randomUUID().toString();

        // WHEN
        mockMvc.perform(get("/api/ecommerce/v1/authentication/activate-account")
                .with(csrf())
                .param("token", token))
                .andExpect(status().isOk());

        // THEN
        then(authenticationService).should().activateAccount(token);
    }

    @Test
    void activateAccount_whenTokenNotProvided_return400() throws Exception {
        // GIVEN

        // WHEN
        mockMvc.perform(get("/api/ecommerce/v1/authentication/activate-account")
                        .with(csrf()))
                .andExpect(status().isBadRequest());

        // THEN
        then(authenticationService).should(never()).activateAccount(anyString());
    }

    @Test
    void updatePassword_whenValidData_return204() throws Exception {
        // GIVEN
        KnownPassword knownPassword = new KnownPassword(
                "john.last@gmail.com", "12345678", "87654321");

        // WHEN
        mockMvc.perform(patch("/api/ecommerce/v1/authentication/update-password")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(knownPassword)))
                .andExpect(status().is(HttpStatus.NO_CONTENT.value()));

        // THEN
        then(authenticationService).should().updatePassword(knownPassword);
    }

    @Test
    void updatePassword_whenInvalidData_return422() throws Exception {
        // GIVEN
        KnownPassword knownPassword = new KnownPassword(
                "john.last@gmail.com", "1234567", "87654321");

        // WHEN
        mockMvc.perform(patch("/api/ecommerce/v1/authentication/update-password")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(knownPassword)))
                .andExpect(status().isUnprocessableEntity());

        // THEN
        then(authenticationService).should(never()).updatePassword(any());
    }

    @Test
    void generatePasswordToken_whenValidEmailFormat_return201() throws Exception {
        // GIVEN
        EmailDTO email = new EmailDTO("john.last@gmail.com");

        mockMvc.perform(post("/api/ecommerce/v1/authentication/password-token")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(email)))
                .andExpect(status().isCreated());

        // THEN
        then(authenticationService).should().generatePasswordToken(email);
    }

    @Test
    void generatePasswordToken_whenInvalidEmailFormat_return422() throws Exception {
        // GIVEN
        EmailDTO email = new EmailDTO("john.lastgmail.com");

        mockMvc.perform(post("/api/ecommerce/v1/authentication/password-token")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(email)))
                .andExpect(status().isUnprocessableEntity());

        // THEN
        then(authenticationService).should(never()).generatePasswordToken(any());
    }

    @Test
    void changePassword_whenTokenIsGiven_return204() throws Exception {
        // GIVEN
        String token = UUID.randomUUID().toString();
        ForgottenPassword forgottenPassword = new ForgottenPassword("87654321", "87654321");

        // WHEN
        mockMvc.perform(patch("/api/ecommerce/v1/authentication/change-password")
                .with(csrf())
                .param("token", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(forgottenPassword)))
                .andExpect(status().is(HttpStatus.NO_CONTENT.value()));

        // THEN
        then(authenticationService).should().changePassword(forgottenPassword, token);
    }

    @Test
    void changePassword_whenTokenIsNotGiven_return400() throws Exception {
        // GIVEN
        ForgottenPassword forgottenPassword = new ForgottenPassword("87654321", "87654321");

        // WHEN
        mockMvc.perform(patch("/api/ecommerce/v1/authentication/change-password")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(forgottenPassword)))
                .andExpect(status().isBadRequest());

        // THEN
        then(authenticationService).should(never()).changePassword(any(), anyString());
    }



}