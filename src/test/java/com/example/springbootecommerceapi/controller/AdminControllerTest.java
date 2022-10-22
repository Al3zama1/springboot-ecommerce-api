package com.example.springbootecommerceapi.controller;

import com.example.springbootecommerceapi.config.SecurityConfiguration;
import com.example.springbootecommerceapi.model.EmailDTO;
import com.example.springbootecommerceapi.repository.ProductRepository;
import com.example.springbootecommerceapi.repository.UserRepository;
import com.example.springbootecommerceapi.service.AdminService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {AdminController.class})
@Import(value = {SecurityConfiguration.class, JpaUserDetailsService.class})
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private AdminService adminService;
    @MockBean
    ProductRepository productRepository;

    @Test
    void generateEmployeeToken_whenValidEmployeeEmailFormat_return201() throws Exception {
        // GIVEN
        String adminEmail = "admin@gmail.com";
        EmailDTO emailDTO = new EmailDTO("employee@gmail.com");

        // WHEN
        mockMvc.perform(post("/api/ecommerce/v1/admins/employee-token")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emailDTO))
                .with(user("admin@gmail.com").password("12345678").roles("ADMIN")))
                .andExpect(status().isCreated());

        // THEN
        then(adminService).should().createEmployeeRegistrationToken(adminEmail, emailDTO.getEmail());

    }

    @Test
    void generateEmployeeToken_whenInvalidEmployeeEmailFormat_return422() throws Exception {
        // GIVEN
        String adminEmail = "admin@gmail.com";
        EmailDTO emailDTO = new EmailDTO("employeegmail.com");

        // WHEN
        mockMvc.perform(post("/api/ecommerce/v1/admins/employee-token")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailDTO))
                        .with(user("admin@gmail.com").password("12345678").roles("ADMIN")))
                .andExpect(status().isUnprocessableEntity());

        // THEN
        then(adminService).should(never())
                .createEmployeeRegistrationToken(anyString(), anyString());

    }


}