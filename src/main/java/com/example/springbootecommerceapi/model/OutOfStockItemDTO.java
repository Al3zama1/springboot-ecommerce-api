package com.example.springbootecommerceapi.model;

import java.util.Objects;

public class OutOfStockItemDTO {
    private String productName;
    private int stockQuantity;
    private int quantityWanted;

    public OutOfStockItemDTO() {
    }

    public OutOfStockItemDTO(String productName, int stockQuantity, int quantityWanted) {
        this.productName = productName;
        this.stockQuantity = stockQuantity;
        this.quantityWanted = quantityWanted;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public int getQuantityWanted() {
        return quantityWanted;
    }

    public void setQuantityWanted(int quantityWanted) {
        this.quantityWanted = quantityWanted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OutOfStockItemDTO that = (OutOfStockItemDTO) o;
        return stockQuantity == that.stockQuantity && quantityWanted == that.quantityWanted && Objects.equals(productName, that.productName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productName, stockQuantity, quantityWanted);
    }

    @Override
    public String toString() {
        return "OutOfStockItemDTO{" +
                "productName='" + productName + '\'' +
                ", stockQuantity=" + stockQuantity +
                ", quantityWanted=" + quantityWanted +
                '}';
    }
}
