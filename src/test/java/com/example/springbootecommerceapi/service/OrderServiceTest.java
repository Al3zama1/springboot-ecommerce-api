package com.example.springbootecommerceapi.service;

import com.example.springbootecommerceapi.entity.*;
import com.example.springbootecommerceapi.exception.ProductOutOfStockException;
import com.example.springbootecommerceapi.exception.UserException;
import com.example.springbootecommerceapi.model.Gender;
import com.example.springbootecommerceapi.model.OrderDTO;
import com.example.springbootecommerceapi.model.Role;
import com.example.springbootecommerceapi.model.UserBuilder;
import com.example.springbootecommerceapi.repository.OrderItemRepository;
import com.example.springbootecommerceapi.repository.OrderRepository;
import com.example.springbootecommerceapi.repository.ProductRepository;
import com.example.springbootecommerceapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private OrderItemService orderItemService;
    @InjectMocks
    OrderService orderService;


    @Test
    void createOrder_whenUserExists() {
        // GIVEN
        String email = "john.last@gmail.com";

        UserEntity user = new UserBuilder()
                .firstName("John")
                .lastName("Last")
                .gender(Gender.MALE)
                .phone("(323) 456-1234")
                .email(email)
                .password("12345678")
                .street("5678 S 88Th St")
                .city("Los Angeles")
                .state("California")
                .zipCode("90002")
                .build();
        user.setRole(Role.CUSTOMER);
        user.setActive(true);
        user.setUserNumber(1L);

        ProductEntity product = new ProductEntity(
                1L, "Soccer Ball", 10,
                "The official World Cup 2022 soccer ball", 40
        );

        OrderDTO orderDTO1 = new OrderDTO(1L, 2);

        // list of order items
        List<OrderDTO> orderDTOs = List.of(orderDTO1);


        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));
        given(productRepository.findAllById(Set.of(1L))).willReturn(List.of(product));


        // WHEN
        orderService.createOrder(orderDTOs, email);

        // THEN
        ArgumentCaptor<OrderEntity> orderCaptor = ArgumentCaptor.forClass(OrderEntity.class);
        then(orderRepository).should().save(orderCaptor.capture());
        assertThat(orderCaptor.getValue().getCustomer()).isEqualTo(user);
    }

    @Test
    void createOrder_whenUserDoesNotExist_throwUserException() {
        // GIVEN
        String email = "john.last@gmail.com";


        OrderDTO orderDTO1 = new OrderDTO(1L, 2);
        OrderDTO orderDTO2 = new OrderDTO(2L, 1);

        // list of order items
        List<OrderDTO> orderDTOs = List.of(orderDTO1, orderDTO2);


        given(userRepository.findByEmail(email)).willReturn(Optional.empty());

        // WHEN
        assertThatThrownBy(() -> orderService.createOrder(orderDTOs, email))
                .isInstanceOf(UserException.class)
                .hasMessage("User does not exist");

        // THEN
        then(orderRepository).should(never()).save(any());
    }

    @Test
    void createOrder_whenWantedQuantityIsMoreThanStockQuantity_throwOutOfStockException() {
        // GIVEN
        String email = "john.last@gmail.com";

        UserEntity user = new UserBuilder()
                .firstName("John")
                .lastName("Last")
                .gender(Gender.MALE)
                .phone("(323) 456-1234")
                .email(email)
                .password("12345678")
                .street("5678 S 88Th St")
                .city("Los Angeles")
                .state("California")
                .zipCode("90002")
                .build();
        user.setRole(Role.CUSTOMER);
        user.setActive(true);
        user.setUserNumber(1L);

        ProductEntity product = new ProductEntity(
                1L, "Soccer Ball", 10,
                "The official World Cup 2022 soccer ball", 40
        );

        OrderDTO orderDTO1 = new OrderDTO(1L, 20);

        // list of order items
        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));
        given(productRepository.findAllById(Set.of(1L))).willReturn(List.of(product));

        // WHEN
        assertThatThrownBy(() -> orderService.createOrder(List.of(orderDTO1), email))
                .isInstanceOf(ProductOutOfStockException.class);

        // THEN
        then(orderRepository).should(never()).save(any());


    }

}