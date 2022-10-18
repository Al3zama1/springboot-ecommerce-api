package com.example.springbootecommerceapi.entity;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class OrderItemPK implements Serializable {
    private static final long serialVersionUID = 4094698434812722400L;
    @OneToOne
    @JoinColumn(
            name = "productNumber",
            referencedColumnName = "productNumber"
    )
    private ProductEntity productEntity;
    @OneToOne
    @JoinColumn(
            name = "orderNumber",
            referencedColumnName = "orderNumber"
    )
    private OrderEntity orderEntity;

    public OrderItemPK() {
    }

    public OrderItemPK(ProductEntity productEntity, OrderEntity orderEntity) {
        this.productEntity = productEntity;
        this.orderEntity = orderEntity;
    }

    public ProductEntity getProduct() {
        return productEntity;
    }

    public OrderItemPK setProduct(ProductEntity productEntity) {
        this.productEntity = productEntity;
        return this;
    }

    public OrderEntity getOrder() {
        return orderEntity;
    }

    public OrderItemPK setOrder(OrderEntity orderEntity) {
        this.orderEntity = orderEntity;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItemPK that = (OrderItemPK) o;
        return Objects.equals(productEntity, that.productEntity) && Objects.equals(orderEntity, that.orderEntity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productEntity, orderEntity);
    }

    @Override
    public String toString() {
        return "OrderItemPK{" +
                "product=" + productEntity +
                ", order=" + orderEntity +
                '}';
    }
}
