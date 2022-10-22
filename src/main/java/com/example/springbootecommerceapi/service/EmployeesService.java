package com.example.springbootecommerceapi.service;

import com.example.springbootecommerceapi.entity.OrderEntity;
import com.example.springbootecommerceapi.exception.OrderException;
import com.example.springbootecommerceapi.model.OrderStatus;
import com.example.springbootecommerceapi.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class EmployeesService {

    private OrderRepository orderRepository;

    @Autowired
    public EmployeesService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void shipOrder(long orderNumber) {
        Optional<OrderEntity> order = orderRepository.findById(orderNumber);

        if (order.isEmpty()) {
            throw new OrderException("Order does not exist");
        }

        order.get().setStatus(OrderStatus.SHIPPED);
        order.get().setDateShipped(LocalDateTime.now());

        orderRepository.save(order.get());
    }

    public void deliverOrder(long orderNumber) {
        Optional<OrderEntity> order = orderRepository.findById(orderNumber);

        if (order.isEmpty()) {
            throw new OrderException("Order does not exist");
        }

        order.get().setStatus(OrderStatus.DELIVERED);
        order.get().setDateDelivered(LocalDateTime.now());

        orderRepository.save(order.get());
    }
}
