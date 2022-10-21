package com.example.springbootecommerceapi.repository;

import com.example.springbootecommerceapi.entity.OrderItemEntity;
import com.example.springbootecommerceapi.entity.OrderItemPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, OrderItemPK> {

    // given email and orderNumber
    @Query("select o from OrderItemEntity o where o.orderItemPK.orderEntity.orderNumber = ?1 and " +
            "o.orderItemPK.orderEntity.customer.email = ?2")
    List<OrderItemEntity>  getOrderItemsFromOrder(long orderNumber, String customerEmail);
}
