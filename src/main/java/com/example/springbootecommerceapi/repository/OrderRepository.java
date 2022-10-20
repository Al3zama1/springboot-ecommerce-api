package com.example.springbootecommerceapi.repository;

import com.example.springbootecommerceapi.entity.OrderEntity;
import com.example.springbootecommerceapi.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findByCustomer(UserEntity customer);
}
