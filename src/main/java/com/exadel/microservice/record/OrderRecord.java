package com.exadel.microservice.record;

import com.exadel.microservice.util.OrderStatus;

public record OrderRecord(Long id, String customerName, String product, Integer quantity, OrderStatus status) {
}
