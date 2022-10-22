package com.example.springbootecommerceapi.controller;

import com.example.springbootecommerceapi.model.EmailDTO;
import com.example.springbootecommerceapi.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("api/ecommerce/v1/admins")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/employee-token")
    public ResponseEntity<Void> generateEmployeeToken(
            @Valid @RequestBody EmailDTO employeeEmail,
            @AuthenticationPrincipal(expression = "username") String adminEmail
    ) {
        adminService.createEmployeeRegistrationToken(adminEmail, employeeEmail.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
