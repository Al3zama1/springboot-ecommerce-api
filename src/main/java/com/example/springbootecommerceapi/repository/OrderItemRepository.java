package com.example.springbootecommerceapi.repository;

import com.example.springbootecommerceapi.entity.OrderItemEntity;
import com.example.springbootecommerceapi.entity.OrderItemPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, OrderItemPK> {
}
