package com.example.springbootecommerceapi.controller;

import com.example.springbootecommerceapi.config.SecurityConfiguration;
import com.example.springbootecommerceapi.entity.OrderItemEntity;
import com.example.springbootecommerceapi.model.OrderDTO;
import com.example.springbootecommerceapi.repository.UserRepository;
import com.example.springbootecommerceapi.service.JpaUserDetailsService;
import com.example.springbootecommerceapi.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = {OrderController.class})
@Import(value = {SecurityConfiguration.class, JpaUserDetailsService.class})
class OrderControllerTest {

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private OrderService orderService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MockMvc mockMvc;



    @Test
    void createOrder_whenValidValues_return201() throws Exception {
        // GIVEN
        OrderDTO item =  new OrderDTO(1L , 2);
        String userEmail = "john.last@gmail.com";
        List<OrderDTO> orderItems = new ArrayList<>();
        orderItems.add(item);

        // WHEN
        mockMvc.perform(post("/api/ecommerce/v1/orders")
                .with(csrf())
                .with(user(userEmail).password("12345678").roles("CUSTOMER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderItems)))
                .andExpect(status().isCreated());


        // THEN
        then(orderService).should().createOrder(orderItems, userEmail);
    }

    @Test
    void createOrder_whenInvalidValues_return422() throws Exception {
        // GIVEN
        OrderDTO item =  new OrderDTO(1L , -2);
        String userEmail = "john.last@gmail.com";
        List<OrderDTO> orderItems = new ArrayList<>();
        orderItems.add(item);

        // WHEN
        mockMvc.perform(post("/api/ecommerce/v1/orders")
                        .with(csrf())
                        .with(user(userEmail).password("12345678").roles("CUSTOMER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderItems)))
                .andExpect(status().isUnprocessableEntity());


        // THEN
        then(orderService).should(never()).createOrder(anyList(), anyString());
    }

}