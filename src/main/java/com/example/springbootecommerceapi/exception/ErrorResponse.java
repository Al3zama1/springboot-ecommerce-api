package com.example.springbootecommerceapi.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ErrorResponse {
    private final int status;
    private final String message;
    private String stackTrace;
    private List<ValidationError> errors;
    private List<ProductOutOfStock> outOfStocks;

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    private record ProductOutOfStock(String productName, int availableQuantity, int wantedQuantity) {}

    private record ValidationError(String field, String message) {
    }

    public void addValidationError(String field, String message) {
        if (Objects.isNull(errors)) {
            this.errors = new ArrayList<>();
        }
        this.errors.add(new ValidationError(field, message));
    }

    public void addOutOfStock(String productName, int stockQuantity, int quantityWanted) {
        if (Objects.isNull(outOfStocks)) {
            this.outOfStocks = new ArrayList<>();
        }
        this.outOfStocks.add(new ProductOutOfStock(productName, stockQuantity, quantityWanted));
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public List<ValidationError> getErrors() {
        return errors;
    }

    public void setErrors(List<ValidationError> errors) {
        this.errors = errors;
    }

    public List<ProductOutOfStock> getOutOfStocks() {
        return outOfStocks;
    }

    public void setOutOfStocks(List<ProductOutOfStock> outOfStocks) {
        this.outOfStocks = outOfStocks;
    }

}
