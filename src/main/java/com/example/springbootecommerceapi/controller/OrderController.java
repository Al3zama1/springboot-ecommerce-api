package com.example.springbootecommerceapi.controller;

import com.example.springbootecommerceapi.model.OrderDTO;
import com.example.springbootecommerceapi.model.SecurityUser;
import com.example.springbootecommerceapi.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;
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
}
