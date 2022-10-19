package com.example.springbootecommerceapi.controller;


import com.example.springbootecommerceapi.entity.UserEntity;
import com.example.springbootecommerceapi.model.ChangeKnownPasswordDTO;
import com.example.springbootecommerceapi.model.EmailDTO;
import com.example.springbootecommerceapi.model.SecurityUser;
import com.example.springbootecommerceapi.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;


@RestController
@RequestMapping("api/ecommerce/v1/authentication")
@Validated
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register-customer")
    public ResponseEntity<Void> registerCustomer(@Valid @RequestBody UserEntity user) {
        authenticationService.registerCustomer(user);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/activate-account")
    public ResponseEntity<Void> activateAccount(@RequestParam String token) {
        authenticationService.activateAccount(token);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/update-password")
    public ResponseEntity<Void> updatePassword(@Valid @RequestBody ChangeKnownPasswordDTO knownPasswordDTO) {

        authenticationService.updatePassword(knownPasswordDTO);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/password-token")
    public ResponseEntity<Void> generatePasswordToken(@Valid @RequestBody EmailDTO emailDTO) {

        authenticationService.generatePasswordToken(emailDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }




}
