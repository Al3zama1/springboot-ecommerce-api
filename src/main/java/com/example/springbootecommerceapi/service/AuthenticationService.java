package com.example.springbootecommerceapi.service;


import com.example.springbootecommerceapi.entity.ActivationTokenEntity;
import com.example.springbootecommerceapi.entity.UserEntity;
import com.example.springbootecommerceapi.exception.AccountActivationException;
import com.example.springbootecommerceapi.exception.UserException;
import com.example.springbootecommerceapi.model.Role;
import com.example.springbootecommerceapi.repository.ActivationTokenRepository;
import com.example.springbootecommerceapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final ActivationTokenRepository activationTokenRepository;

    @Autowired
    public AuthenticationService(
            UserRepository userRepository,
            ActivationTokenRepository activationTokenRepository
    ) {
        this.userRepository = userRepository;
        this.activationTokenRepository = activationTokenRepository;
    }


    public void registerCustomer(UserEntity customer) {
        // set role to customer
        customer.setRole(Role.CUSTOMER);

        // check user email is not taken
        boolean exists = userRepository.existsByEmail(customer.getEmail());

        if (exists) {
            throw new UserException("User with given email exists");
        }

        userRepository.save(customer);
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
