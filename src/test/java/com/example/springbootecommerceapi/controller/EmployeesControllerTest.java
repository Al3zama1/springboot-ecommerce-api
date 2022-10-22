package com.example.springbootecommerceapi.controller;

import com.example.springbootecommerceapi.config.SecurityConfiguration;
import com.example.springbootecommerceapi.repository.ProductRepository;
import com.example.springbootecommerceapi.repository.UserRepository;
import com.example.springbootecommerceapi.service.EmployeesService;
import com.example.springbootecommerceapi.service.JpaUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {EmployeesController.class})
@Import(value = {SecurityConfiguration.class, JpaUserDetailsService.class})
class EmployeesControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ProductRepository productRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private EmployeesService employeesService;

    @Test
    void shipOrder_whenOrderNumberIsValid_return204() throws Exception {
        // GIVE
        long orderNumber = 1L;


        // WHEN
        mockMvc.perform(patch("/api/ecommerce/v1/employees/ship-order/{orderNumber}", orderNumber)
                .with(csrf())
                .with(user("john.last@gmail.com").password("12345678").roles("EMPLOYEE")))
                .andExpect(status().is(HttpStatus.NO_CONTENT.value()));

        // THEN
        then(employeesService).should().shipOrder(orderNumber);
    }

    @Test
    void shipOrder_whenOrderNumberIsInvalid_return422() throws Exception {
        // GIVE
        long orderNumber = -1L;

        // WHEN
        mockMvc.perform(patch("/api/ecommerce/v1/employees/ship-order/{orderNumber}", orderNumber)
                        .with(csrf())
                        .with(user("john.last@gmail.com").password("12345678").roles("EMPLOYEE")))
                .andExpect(status().isUnprocessableEntity());

        // THEN
        then(employeesService).should(never()).shipOrder(anyLong());
    }

    @Test
    void deliverOrder_whenOrderNumberIsValid_return204() throws Exception {
        // GIVE
        long orderNumber = 1L;

        // WHEN
        mockMvc.perform(patch("/api/ecommerce/v1/employees/deliver-order/{orderNumber}", orderNumber)
                        .with(csrf())
                        .with(user("john.last@gmail.com").password("12345678").roles("EMPLOYEE")))
                .andExpect(status().is(HttpStatus.NO_CONTENT.value()));

        // THEN
        then(employeesService).should().deliverOrder(orderNumber);
    }

    @Test
    void deliverOrder_whenOrderNumberIsInvalid_return422() throws Exception {
        // GIVE
        long orderNumber = -1L;

        // WHEN
        mockMvc.perform(patch("/api/ecommerce/v1/employees/deliver-order/{orderNumber}", orderNumber)
                        .with(csrf())
                        .with(user("john.last@gmail.com").password("12345678").roles("EMPLOYEE")))
                .andExpect(status().isUnprocessableEntity());

        // THEN
        then(employeesService).should(never()).deliverOrder(anyLong());
    }


}