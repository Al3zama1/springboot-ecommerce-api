package com.example.springbootecommerceapi.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.util.Objects;

@Entity
@Table(
        name = "product"
)
public class ProductEntity {

    @Id
    @Column(
            name = "productNumber"
    )
    @SequenceGenerator(
            name = "productSequence",
            sequenceName = "productSequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "productSequence"
    )
    private Long productNumber;
    @Column(
            name = "productName",
            nullable = false,
            unique = true
    )
    @NotBlank
    private String productName;
    @Column(
            name = "productStock"
    )
    @PositiveOrZero
    private int productStock;
    @Column(
            name = "productDescription"
    )
    private String productDescription;
    @Column(
            name = "productPrice",
            nullable = false
    )
    @PositiveOrZero
    private double productPrice;

    public ProductEntity() {
    }

    public ProductEntity(String productName, int productStock, String productDescription, double productPrice) {
        this.productName = productName;
        this.productStock = productStock;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
    }

    public ProductEntity(Long productNumber, String productName, int productStock, String productDescription, double productPrice) {
        this.productNumber = productNumber;
        this.productName = productName;
        this.productStock = productStock;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
    }

    public Long getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(Long productNumber) {
        this.productNumber = productNumber;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductStock() {
        return productStock;
    }

    public void setProductStock(int productStock) {
        this.productStock = productStock;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductEntity productEntity = (ProductEntity) o;
        return productStock == productEntity.productStock && Double.compare(productEntity.productPrice, productPrice) == 0 && Objects.equals(productNumber, productEntity.productNumber) && Objects.equals(productName, productEntity.productName) && Objects.equals(productDescription, productEntity.productDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productNumber, productName, productStock, productDescription, productPrice);
    }

    @Override
    public String toString() {
        return "Product{" +
                "productNumber=" + productNumber +
                ", productName='" + productName + '\'' +
                ", productStock=" + productStock +
                ", productDescription='" + productDescription + '\'' +
                ", productPrice=" + productPrice +
                '}';
    }
}
