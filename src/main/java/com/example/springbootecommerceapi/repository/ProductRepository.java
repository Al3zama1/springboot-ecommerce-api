package com.example.springbootecommerceapi.repository;

import com.example.springbootecommerceapi.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    Optional<ProductEntity> findByProductNumber(long productNumber);
    boolean existsByProductName(String productName);
}
