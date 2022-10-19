package com.example.springbootecommerceapi.repository;

import com.example.springbootecommerceapi.entity.PasswordTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordTokenRepository extends JpaRepository<PasswordTokenEntity, Long> {

    Optional<PasswordTokenEntity> findByToken(String token);
}
