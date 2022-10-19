package com.example.springbootecommerceapi.controller;

import com.example.springbootecommerceapi.entity.ProductEntity;
import com.example.springbootecommerceapi.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("api/ecommerce/v1/products")
@Validated
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("")
    public ResponseEntity<List<ProductEntity>> getAllProducts() {
        List<ProductEntity> products = productService.getAllProducts();
        return ResponseEntity.status(200).body(products);
    }

    @GetMapping("/{productNumber}")
    public ResponseEntity<ProductEntity> getProduct(@Positive @PathVariable long productNumber) {
        ProductEntity product = productService.getProduct(productNumber);

        return ResponseEntity.status(HttpStatus.OK).body(product);
    }
}
