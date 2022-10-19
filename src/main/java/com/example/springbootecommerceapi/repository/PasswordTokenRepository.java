package com.example.springbootecommerceapi.repository;

import com.example.springbootecommerceapi.entity.PasswordTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordTokenRepository extends JpaRepository<PasswordTokenEntity, Long> {
}
