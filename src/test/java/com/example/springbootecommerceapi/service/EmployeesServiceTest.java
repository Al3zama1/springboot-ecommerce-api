package com.example.springbootecommerceapi.service;

import com.example.springbootecommerceapi.entity.OrderEntity;
import com.example.springbootecommerceapi.entity.UserEntity;
import com.example.springbootecommerceapi.exception.OrderException;
import com.example.springbootecommerceapi.model.Gender;
import com.example.springbootecommerceapi.model.OrderStatus;
import com.example.springbootecommerceapi.model.Role;
import com.example.springbootecommerceapi.model.UserBuilder;
import com.example.springbootecommerceapi.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class EmployeesServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private EmployeesService employeesService;
    @Captor
    private ArgumentCaptor<OrderEntity> orderCaptor;

    @Test
    void shipOrder_whenOrderExists_setStatusToShipped() {
        // GIVEN
        long orderNumber = 1L;
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
        customer.setActive(true);
        customer.setUserNumber(1L);

        OrderEntity order = new OrderEntity(customer);

        given(orderRepository.findById(orderNumber)).willReturn(Optional.of(order));

        // WHEN
        employeesService.shipOrder(orderNumber);

        // THEN
        then(orderRepository).should().save(orderCaptor.capture());
        assertThat(orderCaptor.getValue().getStatus()).isEqualTo(OrderStatus.SHIPPED);
    }

    @Test
    void shipOrder_whenOrderDoesNotExists_throwOrderException() {
        // GIVEN
        long orderNumber = 1L;


        given(orderRepository.findById(orderNumber)).willReturn(Optional.empty());

        // WHEN
        assertThatThrownBy(() -> employeesService.shipOrder(orderNumber))
                .isInstanceOf(OrderException.class)
                        .hasMessage("Order does not exist");

        // THEN
        then(orderRepository).should(never()).save(any());
    }

    @Test
    void deliverOrder_whenOrderExists_setStatusToDelivered() {
        // GIVEN
        long orderNumber = 1L;
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
        customer.setActive(true);
        customer.setUserNumber(1L);

        OrderEntity order = new OrderEntity(customer);

        given(orderRepository.findById(orderNumber)).willReturn(Optional.of(order));

        // WHEN
        employeesService.deliverOrder(orderNumber);

        // THEN
        then(orderRepository).should().save(orderCaptor.capture());
        assertThat(orderCaptor.getValue().getStatus()).isEqualTo(OrderStatus.DELIVERED);
    }

    @Test
    void deliverOrder_whenOrderDoesNotExists_throwOrderException() {
        // GIVEN
        long orderNumber = 1L;


        given(orderRepository.findById(orderNumber)).willReturn(Optional.empty());

        // WHEN
        assertThatThrownBy(() -> employeesService.deliverOrder(orderNumber))
                .isInstanceOf(OrderException.class)
                .hasMessage("Order does not exist");

        // THEN
        then(orderRepository).should(never()).save(any());
    }

}