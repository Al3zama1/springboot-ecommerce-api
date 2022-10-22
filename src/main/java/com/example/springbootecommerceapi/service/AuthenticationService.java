package com.example.springbootecommerceapi.service;


import com.example.springbootecommerceapi.entity.ActivationTokenEntity;
import com.example.springbootecommerceapi.entity.EmployeeRegistrationToken;
import com.example.springbootecommerceapi.entity.PasswordTokenEntity;
import com.example.springbootecommerceapi.entity.UserEntity;
import com.example.springbootecommerceapi.event.ChangePasswordEvent;
import com.example.springbootecommerceapi.event.UserRegistrationEvent;
import com.example.springbootecommerceapi.exception.*;
import com.example.springbootecommerceapi.model.ForgottenPassword;
import com.example.springbootecommerceapi.model.KnownPassword;
import com.example.springbootecommerceapi.model.EmailDTO;
import com.example.springbootecommerceapi.model.Role;
import com.example.springbootecommerceapi.repository.ActivationTokenRepository;
import com.example.springbootecommerceapi.repository.EmployeeRegistrationRepository;
import com.example.springbootecommerceapi.repository.PasswordTokenRepository;
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
    private final PasswordTokenRepository passwordTokenRepository;
    private final EmployeeRegistrationRepository employeeRegistrationRepository;

    @Autowired
    public AuthenticationService(
            UserRepository userRepository,
            ActivationTokenRepository activationTokenRepository,
            ApplicationEventPublisher publisher,
            HttpServletRequest request,
            PasswordEncoder passwordEncoder,
            PasswordTokenRepository passwordTokenRepository,
            EmployeeRegistrationRepository employeeRegistrationRepository

    ) {
        this.userRepository = userRepository;
        this.activationTokenRepository = activationTokenRepository;
        this.publisher = publisher;
        this.request = request;
        this.passwordEncoder = passwordEncoder;
        this.passwordTokenRepository = passwordTokenRepository;
        this.employeeRegistrationRepository = employeeRegistrationRepository;
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

        String url = applicationUrl(token, "activate-account");
        publisher.publishEvent(new UserRegistrationEvent(customer, url));

    }

    public void registerEmployee(UserEntity user, String token) {
        Optional<EmployeeRegistrationToken> registrationToken =
                employeeRegistrationRepository.findByToken(token);

        if (registrationToken.isEmpty()) {
            throw new EmployeeRegistrationTokenException("Invalid employee registration token.");
        }

        // check the email of the employee matches to the registration token employee email
        if (!user.getEmail().equals(registrationToken.get().getEmployeeEmail())) {
            throw new EmployeeRegistrationTokenException("Invalid employee registration token.");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserException("A user with given email already exists. " +
                    "Contact admin to get a token for a different email.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.EMPLOYEE);

        // save new employee
        UserEntity savedUser = userRepository.save(user);
        employeeRegistrationRepository.delete(registrationToken.get());

        // generate account activation token
        String accountActivationToken = UUID.randomUUID().toString();
        ActivationTokenEntity activationToken = new ActivationTokenEntity(savedUser, accountActivationToken);
        activationTokenRepository.save(activationToken);

        // send account activation link
        String url = applicationUrl(accountActivationToken, "activate-account");
        publisher.publishEvent(new UserRegistrationEvent(user, url));
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

    public void updatePassword(KnownPassword knownPassword) {
        Optional<UserEntity> user = userRepository.findByEmail(knownPassword.getEmail());

        if (user.isEmpty()) {
            throw new UserException("User not found");
        }

        boolean passwordsMatch = passwordEncoder
                .matches(knownPassword.getOldPassword(), user.get().getPassword());

        if (!passwordsMatch) {
            throw new PasswordException("Old Password is incorrect");
        }

        // update password
        user.get().setPassword(passwordEncoder.encode(knownPassword.getNewPassword()));

        // save changes
        userRepository.save(user.get());
    }

    public void generatePasswordToken(EmailDTO emailDTO) {
        // make sure email is associated with a user
        Optional<UserEntity> user = userRepository.findByEmail(emailDTO.getEmail());

        if (user.isEmpty()) {
            throw new UserException("User not found");
        }

        String token = UUID.randomUUID().toString();
        PasswordTokenEntity passwordToken = new PasswordTokenEntity(user.get(), token);

        passwordTokenRepository.save(passwordToken);
        publisher.publishEvent(new ChangePasswordEvent(user.get(), applicationUrl(token, "change-password")));
    }

    public void changePassword(ForgottenPassword forgottenPassword, String token) {
        // verify token
        Optional<PasswordTokenEntity> passwordToken = passwordTokenRepository.findByToken(token);

        if (passwordToken.isEmpty()) {
            throw  new PasswordTokenException("Invalid token");
        }

        if (!forgottenPassword.getPassword().equals(forgottenPassword.getVerifyPassword())) {
            throw new PasswordException("Passwords must match");
        }

        // change password
        UserEntity user = passwordToken.get().getUserEntity();
        user.setPassword(passwordEncoder.encode(forgottenPassword.getVerifyPassword()));

        // save changes
        userRepository.save(user);
        passwordTokenRepository.delete(passwordToken.get());
    }

    private String applicationUrl(String token, String path) {
        return "http://" + request.getServerName() + ":" +
                request.getServerPort() +
                "/api/ecommerce/v1/authentication" +
                "/" + path + "?token=" + token;
    }

}
