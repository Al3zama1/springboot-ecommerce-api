package com.example.springbootecommerceapi.model;

import javax.validation.constraints.PositiveOrZero;
import java.util.Objects;

public class UpdateProduct {
    private String description;
    @PositiveOrZero
    private int quantity;
    @PositiveOrZero
    private float price;

    public UpdateProduct(String description, int quantity, float price) {
        this.description = description;
        this.quantity = quantity;
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdateProduct that = (UpdateProduct) o;
        return quantity == that.quantity && Float.compare(that.price, price) == 0 && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, quantity, price);
    }
}
