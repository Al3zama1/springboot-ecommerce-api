package com.example.springbootecommerceapi.exception;

import com.example.springbootecommerceapi.model.OutOfStockItemDTO;

import java.util.List;

public class ProductOutOfStockException extends RuntimeException{

    private final List<OutOfStockItemDTO> items;
    public ProductOutOfStockException(List<OutOfStockItemDTO> items) {
        this.items = items;
    }

    public List<OutOfStockItemDTO> getItems() {
        return items;
    }
}
