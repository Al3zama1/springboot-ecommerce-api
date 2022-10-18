package com.example.springbootecommerceapi.controller;


import com.example.springbootecommerceapi.model.SecurityUser;
import com.example.springbootecommerceapi.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/ecommerce/v1/authentication")
@Validated
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser() {


        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


}
