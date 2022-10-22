package com.example.springbootecommerceapi.controller;

import com.example.springbootecommerceapi.entity.OrderEntity;
import com.example.springbootecommerceapi.entity.OrderItemEntity;
import com.example.springbootecommerceapi.model.OrderDTO;
import com.example.springbootecommerceapi.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("api/ecommerce/v1/orders")
@Validated
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("")
    public ResponseEntity<Void> createOrder(
            @Valid @RequestBody List<OrderDTO> orderItems,
            @AuthenticationPrincipal(expression = "username") String userEmail
    ) {
        orderService.createOrder(orderItems, userEmail);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("")
    public ResponseEntity<List<OrderEntity>> getAllOrders(
            @AuthenticationPrincipal(expression = "username") String userEmail
    ) {
        List<OrderEntity> orders = orderService.getAllOrdersFromCustomer(userEmail);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderNumber}")
    public ResponseEntity<List<OrderItemEntity>> getOrderItems(
            @Positive @PathVariable long orderNumber,
            @AuthenticationPrincipal(expression = "username") String userEmail
    ) {
        List<OrderItemEntity> orderItemEntities = orderService.getOrderItems(orderNumber, userEmail);
        return ResponseEntity.ok(orderItemEntities);
    }

    @PatchMapping("/{orderNumber}/cancel")
    public ResponseEntity<Void> cancelOrder(
            @Positive @PathVariable long orderNumber,
            @AuthenticationPrincipal(expression = "username") String userEmail
    ) {
        orderService.cancelCustomerOrder(orderNumber, userEmail);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
