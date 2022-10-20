package com.example.springbootecommerceapi.model;

import com.example.springbootecommerceapi.entity.OrderItemEntity;

import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Objects;

public class OrderDTO {
    @Positive
    private long productNumber;
    @Positive
    private int quantity;

    public OrderDTO() {
    }

    public OrderDTO(long productNumber, int quantity) {
        this.productNumber = productNumber;
        this.quantity = quantity;
    }

    public long getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(long productNumber) {
        this.productNumber = productNumber;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDTO orderDTO = (OrderDTO) o;
        return productNumber == orderDTO.productNumber && quantity == orderDTO.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(productNumber, quantity);
    }
}
