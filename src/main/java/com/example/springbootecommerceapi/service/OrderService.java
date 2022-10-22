package com.example.springbootecommerceapi.service;

import com.example.springbootecommerceapi.entity.*;
import com.example.springbootecommerceapi.exception.OrderException;
import com.example.springbootecommerceapi.exception.ProductOutOfStockException;
import com.example.springbootecommerceapi.exception.UserException;
import com.example.springbootecommerceapi.model.OrderDTO;
import com.example.springbootecommerceapi.model.OrderStatus;
import com.example.springbootecommerceapi.model.OutOfStockItemDTO;
import com.example.springbootecommerceapi.repository.OrderItemRepository;
import com.example.springbootecommerceapi.repository.OrderRepository;
import com.example.springbootecommerceapi.repository.ProductRepository;
import com.example.springbootecommerceapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderItemService orderItemService;
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public OrderService(
            ProductRepository productRepository,
            UserRepository userRepository,
            OrderRepository orderRepository,
            OrderItemService orderItemService,
            OrderItemRepository orderItemRepository
    ) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.orderItemService = orderItemService;
        this.orderItemRepository = orderItemRepository;
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

    public List<OrderEntity> getAllOrdersFromCustomer(String userEmail) {
        // get customer
        Optional<UserEntity> customer = userRepository.findByEmail(userEmail);

        if (customer.isEmpty()) {
            throw new UserException("User does not exist");
        }

        return orderRepository.findByCustomer(customer.get());
    }

    public List<OrderItemEntity> getOrderItems(long orderNumber, String username) {
        List<OrderItemEntity> orderItems = orderItemRepository.getOrderItemsFromOrder(orderNumber, username);
        return orderItems;
    }

    public void cancelCustomerOrder(long orderNumber, String email) {
        Optional<OrderEntity> order = orderRepository
                .findByOrderNumberAndCustomerEmail(orderNumber, email);

        if (order.isEmpty()) {
            throw new OrderException("Either order does not exist or does not belong to given customer");
        }

        if (!order.get().getStatus().equals(OrderStatus.PROCESSING)) {
            throw new OrderException("Order cannot be cancelled. Is is in transit or has been delivered.");
        }

        order.get().setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order.get());
    }
}
