package com.example.springbootecommerceapi.service;

import com.example.springbootecommerceapi.entity.OrderEntity;
import com.example.springbootecommerceapi.entity.OrderItemEntity;
import com.example.springbootecommerceapi.entity.OrderItemPK;
import com.example.springbootecommerceapi.entity.ProductEntity;
import com.example.springbootecommerceapi.model.OrderDTO;
import com.example.springbootecommerceapi.repository.OrderItemRepository;
import com.example.springbootecommerceapi.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    @Autowired

    public OrderItemService(
            OrderItemRepository orderItemRepository,
            ProductRepository productRepository
    ) {
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
    }

    public void saveItems(OrderEntity order, List<ProductEntity> products, Map<Long, Integer> productAndQuantity) {
        // create list of OrderItems to be saved
        List<OrderItemEntity> orderItemEntities = new ArrayList<>();

        for (ProductEntity product : products) {
            OrderItemPK orderItemPK = new OrderItemPK(product, order);
            OrderItemEntity orderItem = new OrderItemEntity(
                    orderItemPK, product.getProductPrice(),
                    productAndQuantity.get(product.getProductNumber()));
            orderItemEntities.add(orderItem);
        }

        orderItemRepository.saveAll(orderItemEntities);
    }
}
