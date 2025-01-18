package com.exadel.microservice.dto;

import com.exadel.microservice.util.OrderStatus;

import java.util.UUID;

public class OrderEvent {
    private UUID id;
    private String customerName;
    private String product;
    private int quantity;
    private OrderStatus status;

    public OrderEvent(UUID id, String customerName, String product, int quantity, OrderStatus status) {
        this.id = id;
        this.customerName = customerName;
        this.product = product;
        this.quantity = quantity;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public OrderStatus getStatus() {
        return status;
    }
}
