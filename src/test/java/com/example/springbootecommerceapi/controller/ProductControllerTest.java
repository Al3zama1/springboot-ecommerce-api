package com.example.springbootecommerceapi.controller;

import com.example.springbootecommerceapi.config.SecurityConfiguration;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    void products_returnListOfProducts() throws Exception {
        // GIVEN

        // WHEN
        mockMvc.perform(get("/api/ecommerce/v1/products")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // THEN
        then(productService).should().getAllProducts();
    }


}