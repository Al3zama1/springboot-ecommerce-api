package com.example.springbootecommerceapi.controller;

import com.example.springbootecommerceapi.config.SecurityConfiguration;
import com.example.springbootecommerceapi.entity.ProductEntity;
import com.example.springbootecommerceapi.repository.UserRepository;
import com.example.springbootecommerceapi.service.JpaUserDetailsService;
import com.example.springbootecommerceapi.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {ProductController.class})
@Import(value = {SecurityConfiguration.class, JpaUserDetailsService.class})
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ProductService productService;
    @MockBean
    private UserRepository userRepository;


    @Test
    void getAllProducts_returnListOfProductsAndStatus200() throws Exception {
        // GIVEN
        ProductEntity product = new ProductEntity(
                1L, "Soccer Ball", 10,
                "The official World Cup 2022 soccer ball", 40
        );
        given(productService.getAllProducts()).willReturn(List.of(product));

        // WHEN
        MvcResult result = mockMvc.perform(get("/api/ecommerce/v1/products")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();


        // THEN
        then(productService).should().getAllProducts();
        ProductEntity[] response = objectMapper.readValue(
                result.getResponse().getContentAsString(), ProductEntity[].class);

//        List<ProductEntity> list = Arrays.asList(response);
        assertThat(response[0]).isEqualTo(product);
    }

    @Test
    void getProduct_returnProductAndStatus200() throws Exception {
        // GIVEN
        long productNumber = 1L;
        ProductEntity product = new ProductEntity(
                productNumber, "Soccer Ball", 10,
                "The official World Cup 2022 soccer ball", 40
        );
        given(productService.getProduct(productNumber)).willReturn(product);

        // WHEN
        MvcResult result =  mockMvc.perform(get("/api/ecommerce/v1/products/{product}", productNumber))
                .andExpect(status().isOk())
                .andReturn();

        ProductEntity returnedProduct = objectMapper.readValue(
                result.getResponse().getContentAsString(), ProductEntity.class
        );

        // THEN
        then(productService).should().getProduct(productNumber);
        assertThat(returnedProduct).isEqualTo(product);
    }

    @Test
    void getProduct_whenInvalidProductNumber_thenReturn422() throws Exception {
        // GIVE
        long productNumber = -1L;

        // WHEN
        mockMvc.perform(get("/api/ecommerce/v1/products/{product}", productNumber))
                .andExpect(status().isUnprocessableEntity());

        // THEN
        then(productService).should(never()).getProduct(anyLong());
    }




}