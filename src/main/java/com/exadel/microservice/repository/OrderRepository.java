package com.exadel.microservice.repository;

import com.exadel.microservice.entity.Order;
import com.exadel.microservice.util.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    Page<Order> findByCustomerNameContainingIgnoreCaseAndStatus(String customerName, OrderStatus status, Pageable pageable);
}
