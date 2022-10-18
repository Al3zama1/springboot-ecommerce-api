package com.example.springbootecommerceapi.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(
        name = "orderItem"
)
public class OrderItemEntity {

    @EmbeddedId
    private OrderItemPK orderItemPK;
    private double priceEach;
    private  int quantity;

    public OrderItemEntity() {
    }

    public OrderItemEntity(OrderItemPK orderItemPK, double priceEach, int quantity) {
        this.orderItemPK = orderItemPK;
        this.priceEach = priceEach;
        this.quantity = quantity;
    }

    public OrderItemPK getOrderItemPK() {
        return orderItemPK;
    }

    public OrderItemEntity setOrderItemPK(OrderItemPK orderItemPK) {
        this.orderItemPK = orderItemPK;
        return this;
    }

    public double getPriceEach() {
        return priceEach;
    }

    public OrderItemEntity setPriceEach(double priceEach) {
        this.priceEach = priceEach;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public OrderItemEntity setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItemEntity orderItemEntity = (OrderItemEntity) o;
        return Double.compare(orderItemEntity.priceEach, priceEach) == 0 && quantity == orderItemEntity.quantity && Objects.equals(orderItemPK, orderItemEntity.orderItemPK);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderItemPK, priceEach, quantity);
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "orderItemPK=" + orderItemPK +
                ", priceEach=" + priceEach +
                ", quantity=" + quantity +
                '}';
    }
}
