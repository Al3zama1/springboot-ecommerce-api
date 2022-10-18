package com.example.springbootecommerceapi.service;


import com.example.springbootecommerceapi.entity.ActivationTokenEntity;
import com.example.springbootecommerceapi.entity.UserEntity;
import com.example.springbootecommerceapi.event.UserRegistrationEvent;
import com.example.springbootecommerceapi.exception.AccountActivationException;
import com.example.springbootecommerceapi.exception.UserException;
import com.example.springbootecommerceapi.model.Role;
import com.example.springbootecommerceapi.repository.ActivationTokenRepository;
import com.example.springbootecommerceapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher publisher;
    private final ActivationTokenRepository activationTokenRepository;
    private final HttpServletRequest request;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationService(
            UserRepository userRepository,
            ActivationTokenRepository activationTokenRepository,
            ApplicationEventPublisher publisher,
            HttpServletRequest request,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.activationTokenRepository = activationTokenRepository;
        this.publisher = publisher;
        this.request = request;
        this.passwordEncoder = passwordEncoder;
    }


    public void registerCustomer(UserEntity customer) {
        // set role to customer
        customer.setRole(Role.CUSTOMER);

        // check user email is not taken
        boolean exists = userRepository.existsByEmail(customer.getEmail());

        if (exists) {
            throw new UserException("User with given email exists");
        }

        // encrypt password
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));

        UserEntity savedUser = userRepository.save(customer);

        String token = UUID.randomUUID().toString();
        ActivationTokenEntity activationToken = new ActivationTokenEntity(savedUser, token);
        activationTokenRepository.save(activationToken);

        String url = authenticationUrl(token);
        publisher.publishEvent(new UserRegistrationEvent(customer, url));

    }

    private String authenticationUrl(String token) {
        return "http://" + request.getServerName() + ":" +
                request.getServerPort() +
                "/api/ecommerce/v1/authentication" +
                "/activate-account?token=" + token;
    }

    public void RegisterEmployee(UserEntity employee) {
        employee.setRole(Role.EMPLOYEE);
    }

    public void activateAccount(String token) {
        // verify token existence
        Optional<ActivationTokenEntity> activationToken =
                activationTokenRepository.findByToken(token);

        if (activationToken.isEmpty()) {
            throw new AccountActivationException("Invalid activation token");
        }

        UserEntity user = activationToken.get().getUserEntity();
        user.setActive(true);

        userRepository.save(user);
        activationTokenRepository.delete(activationToken.get());
    }
}
