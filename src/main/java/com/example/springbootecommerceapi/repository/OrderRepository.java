package com.example.springbootecommerceapi.repository;

import com.example.springbootecommerceapi.entity.OrderEntity;
import com.example.springbootecommerceapi.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findByCustomer(UserEntity customer);

    @Query("select o from OrderEntity o where o.orderNumber = ?1 and " +
            "o.customer.email = ?2")
    Optional<OrderEntity> findByOrderNumberAndCustomerEmail(long orderNumber, String customerEmail);
}
