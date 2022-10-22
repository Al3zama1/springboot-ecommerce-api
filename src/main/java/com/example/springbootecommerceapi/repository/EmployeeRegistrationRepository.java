package com.example.springbootecommerceapi.repository;

import com.example.springbootecommerceapi.entity.EmployeeRegistrationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRegistrationRepository extends JpaRepository<EmployeeRegistrationToken, Long> {

    Optional<EmployeeRegistrationToken> findByToken(String token);
}
