package com.example.springbootecommerceapi.controller;


import com.example.springbootecommerceapi.entity.UserEntity;
import com.example.springbootecommerceapi.model.ForgottenPassword;
import com.example.springbootecommerceapi.model.KnownPassword;
import com.example.springbootecommerceapi.model.EmailDTO;
import com.example.springbootecommerceapi.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


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
    public ResponseEntity<Void> updatePassword(@Valid @RequestBody KnownPassword knownPasswordDTO) {

        authenticationService.updatePassword(knownPasswordDTO);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/password-token")
    public ResponseEntity<Void> generatePasswordToken(@Valid @RequestBody EmailDTO emailDTO) {

        authenticationService.generatePasswordToken(emailDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @PatchMapping("/change-password")
    public ResponseEntity<Void> changePassword(
            @Valid @RequestBody ForgottenPassword forgottenPassword,
            @RequestParam String token
    ) {

        authenticationService.changePassword(forgottenPassword, token);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }




}
