package com.example.springbootecommerceapi.service;

import com.example.springbootecommerceapi.entity.*;
import com.example.springbootecommerceapi.exception.ProductException;
import com.example.springbootecommerceapi.exception.ProductOutOfStockException;
import com.example.springbootecommerceapi.exception.UserException;
import com.example.springbootecommerceapi.model.OrderDTO;
import com.example.springbootecommerceapi.model.OutOfStockItemDTO;
import com.example.springbootecommerceapi.repository.OrderRepository;
import com.example.springbootecommerceapi.repository.ProductRepository;
import com.example.springbootecommerceapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderItemService orderItemService;

    @Autowired
    public OrderService(
            ProductRepository productRepository,
            UserRepository userRepository,
            OrderRepository orderRepository,
            OrderItemService orderItemService
    ) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.orderItemService = orderItemService;
    }



    public void createOrder(List<OrderDTO> orderItems, String userEmail) {
        // retrieve user
        Optional<UserEntity> user = userRepository.findByEmail(userEmail);

        if (user.isEmpty()) {
            throw new UserException("User does not exist");
        }

        // generate list of object to retrieve from db
        Map<Long, Integer> productAndQuantity = new HashMap<>();

        for (OrderDTO item : orderItems) {
            productAndQuantity.put(item.getProductNumber(), item.getQuantity());
        }

        // fetch all products from db;
        List<ProductEntity> products = productRepository.findAllById(productAndQuantity.keySet());

        // get products for which there is not enough inventory to satisfy purchase
        List<OutOfStockItemDTO> outOfStock = new ArrayList<>();

        for (ProductEntity product : products) {
            if (productAndQuantity.get(product.getProductNumber()) > product.getProductStock()) {
                OutOfStockItemDTO item = new OutOfStockItemDTO(
                        product.getProductName(),
                        product.getProductStock(),
                        productAndQuantity.get(product.getProductNumber()));
                outOfStock.add(item);
            }
        }

        if (outOfStock.size() > 0) {
            throw new ProductOutOfStockException(outOfStock);
        }


        // create and save order
        OrderEntity orderEntity = new OrderEntity(user.get());
        OrderEntity savedOrder = orderRepository.save(orderEntity);


        // update product stock quantity of products purchased
        for (ProductEntity product: products) {
            product.setProductStock(
                    product.getProductStock() -
                            productAndQuantity.get(product.getProductNumber()));
        }

        productRepository.saveAll(products);

        orderItemService.saveItems(savedOrder, products, productAndQuantity);
    }
}
