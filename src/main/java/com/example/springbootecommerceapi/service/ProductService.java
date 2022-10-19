package com.example.springbootecommerceapi.service;

import com.example.springbootecommerceapi.entity.ProductEntity;
import com.example.springbootecommerceapi.exception.ProductException;
import com.example.springbootecommerceapi.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }

    public ProductEntity getProduct(long productNumber) {
        Optional<ProductEntity> product = productRepository.findByProductNumber(productNumber);

        if (product.isEmpty()) {
            throw new ProductException("Product does not exist");
        }

        return product.get();
    }

    public void addProduct(ProductEntity product) {
        // make sure product name is unique
        boolean exists = productRepository.existsByProductName(product.getProductName());

        if (exists) {
            throw new ProductException("Product with given name exists");
        }

        productRepository.save(product);
    }
}
