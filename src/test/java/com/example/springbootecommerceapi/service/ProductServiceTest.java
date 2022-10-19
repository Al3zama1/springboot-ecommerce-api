package com.example.springbootecommerceapi.service;

import com.example.springbootecommerceapi.entity.ProductEntity;
import com.example.springbootecommerceapi.exception.ProductException;
import com.example.springbootecommerceapi.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;

    @Test
    void getAllProducts_returnListOfProducts() {
        // GIVEN

        // WHEN
        productService.getAllProducts();

        // THEN
        then(productRepository).should().findAll();
    }

    @Test
    void getProduct_wheProductExists_returnProduct() {
        // GIVEN
        long productNumber = 1L;
        ProductEntity product = new ProductEntity(
                productNumber, "Soccer Ball", 10,
                "The official World Cup 2022 soccer ball", 40
        );

        // assume product with given Id exists
        given(productRepository.findByProductNumber(productNumber)).willReturn(Optional.of(product));

        // WHEN
        productService.getProduct(productNumber);

        // THEN
        then(productRepository).should().findByProductNumber(productNumber);
    }

    @Test
    void getProduct_wheProductDoesNotExists_ProductException() {
        // GIVEN
        long productNumber = 1L;
        given(productRepository.findByProductNumber(productNumber)).willReturn(Optional.empty());

        // WHEN
        assertThatThrownBy(() -> productService.getProduct(productNumber))
                .isInstanceOf(ProductException.class)
                .hasMessage("Product does not exist");

    }

    @Test
    void addProduct_whenProductNameIsUnique_saveProduct() {
        // GIVEN
        ProductEntity product = new ProductEntity(
                "Soccer Ball", 10,
                "The official World Cup 2022 soccer ball", 40
        );

        // assume product name is unique
        given(productRepository.existsByProductName(product.getProductName())).willReturn(false);

        // WHEN
        productService.addProduct(product);

        // THEN
        then(productRepository).should().existsByProductName(product.getProductName());
        then(productRepository).should().save(product);
    }

    @Test
    void addProduct_whenProductNameIsNotUnique_throwProductException() {
        // GIVEN
        ProductEntity product = new ProductEntity(
                "Soccer Ball", 10,
                "The official World Cup 2022 soccer ball", 40
        );

        given(productRepository.existsByProductName(product.getProductName())).willReturn(true);

        // WHEN
        assertThatThrownBy(() -> productService.addProduct(product))
                .isInstanceOf(ProductException.class)
                .hasMessage("Product with given name exists");

        // THEN
        then(productRepository).should(never()).save(any());
    }

}