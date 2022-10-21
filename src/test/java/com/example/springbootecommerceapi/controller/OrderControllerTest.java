package com.example.springbootecommerceapi.controller;

import com.example.springbootecommerceapi.config.SecurityConfiguration;
import com.example.springbootecommerceapi.entity.*;
import com.example.springbootecommerceapi.model.Gender;
import com.example.springbootecommerceapi.model.OrderDTO;
import com.example.springbootecommerceapi.model.Role;
import com.example.springbootecommerceapi.model.UserBuilder;
import com.example.springbootecommerceapi.repository.ProductRepository;
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
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = {OrderController.class})
@Import(value = {SecurityConfiguration.class, JpaUserDetailsService.class})
class OrderControllerTest {

    @MockBean
    private UserRepository userRepository;
    @MockBean
    ProductRepository productRepository;
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

    @Test
    void getAllOrders_return200AndOrders() throws Exception {
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
        customer.setUserNumber(1L);
        customer.setRole(Role.CUSTOMER);
        customer.setActive(true);

        OrderEntity order = new OrderEntity(customer);

        given(orderService.getAllOrdersFromCustomer(customer.getEmail())).willReturn(List.of(order));

        // WHEN
        MvcResult result = mockMvc.perform(get("/api/ecommerce/v1/orders")
                .with(user(customer.getEmail()).password("12345678").roles("CUSTOMER")))
                .andExpect(status().isOk()).andReturn();

        OrderEntity[] returnedOrder = objectMapper.readValue(result.getResponse().getContentAsString(), OrderEntity[].class);

        // THEN
        then(orderService).should().getAllOrdersFromCustomer(customer.getEmail());
        assertThat(order.toString()).isEqualTo(returnedOrder[0].toString());
    }

    @Test
    void getOrderItems_whenValidOrderNumber_return200AndOrderItems() throws Exception {
        // GIVEN
        long orderNumber = 1L;
        String username = "john.last@gmail.com";
        ProductEntity product = new ProductEntity(
                1L, "Soccer Ball", 10,
                "The official World Cup 2022 soccer ball", 40
        );
        UserEntity customer = new UserBuilder()
                .firstName("John")
                .lastName("Last")
                .gender(Gender.MALE)
                .phone("(323) 456-1234")
                .email(username)
                .password("12345678")
                .street("5678 S 88Th St")
                .city("Los Angeles")
                .state("California")
                .zipCode("90002")
                .build();
        customer.setActive(true);
        customer.setRole(Role.CUSTOMER);
        customer.setUserNumber(1L);

        // create order
        OrderEntity orderEntity = new OrderEntity(customer);

        // generate order item associated with above order
        OrderItemPK orderItemPK = new OrderItemPK(product, orderEntity);
        OrderItemEntity orderItemEntity = new OrderItemEntity(orderItemPK, 30, 3);


        given(orderService.getOrderItems(orderNumber, username)).willReturn(List.of(orderItemEntity));

        // WHEN
        MvcResult result =  mockMvc.perform(get("/api/ecommerce/v1/orders/{orderNumber}", orderNumber)
                .accept(MediaType.APPLICATION_JSON)
                        .with(user(username).password("12345678").roles("CUSTOMER")))
                .andExpect(status().isOk()).andReturn();

        OrderItemEntity[] orderItemEntities = objectMapper.readValue(
                result.getResponse().getContentAsString(), OrderItemEntity[].class
        );

        // THEN
        then(orderService).should().getOrderItems(orderNumber, username);
        assertThat(List.of(orderItemEntity).get(0).toString()).isEqualTo(orderItemEntities[0].toString());
    }

    @Test
    void getOrderItems_whenInvalidOrderNumber_return422() throws Exception {
        // GIVEN
        long orderNumber = -1L;
        String username = "john.last@gmail.com";

        // WHEN
        mockMvc.perform(get("/api/ecommerce/v1/orders/{orderNumber}", orderNumber)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(user(username).password("12345678").roles("CUSTOMER")))
                .andExpect(status().isUnprocessableEntity());


        // THEN
        then(orderService).should(never()).getOrderItems(anyLong(), anyString());
    }

}