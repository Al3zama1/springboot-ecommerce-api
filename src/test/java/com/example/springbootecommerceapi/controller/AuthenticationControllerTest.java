package com.example.springbootecommerceapi.controller;

import com.example.springbootecommerceapi.config.SecurityConfiguration;
import com.example.springbootecommerceapi.entity.UserEntity;
import com.example.springbootecommerceapi.model.Gender;
import com.example.springbootecommerceapi.model.Role;
import com.example.springbootecommerceapi.model.UserBuilder;
import com.example.springbootecommerceapi.repository.UserRepository;
import com.example.springbootecommerceapi.service.AuthenticationService;
import com.example.springbootecommerceapi.service.JpaUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        // given
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

        // when
        mockMvc.perform(post("/api/ecommerce/v1/register/customer")
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
        // given
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

        // when
        mockMvc.perform(post("/api/ecommerce/v1/register/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer))
                .accept(MediaType.APPLICATION_JSON)
                .with(csrf())
        ).andExpect(status().isUnprocessableEntity());

        // then
        then(authenticationService).should(never()).registerCustomer(any());

    }

}