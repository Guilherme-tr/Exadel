package com.exadel.microservice.controller;

import com.exadel.microservice.dto.OrderRequest;
import com.exadel.microservice.entity.Order;
import com.exadel.microservice.service.OrderService;
import com.exadel.microservice.util.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    public final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public  ResponseEntity<Order> createOrder(@RequestBody OrderRequest orderRequest){
        Order newOrder = orderService.createOrder(orderRequest);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                        .path("/{id}")
                                .buildAndExpand(newOrder.getId())
                                        .toUri();
        return ResponseEntity.created(location).body(newOrder);
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable UUID orderId, @RequestParam OrderStatus newStatus){
        Order updatedOrder = orderService.updateOrderStatus(orderId, newStatus);
        return ResponseEntity.ok(updatedOrder);
    }

    @GetMapping
    public ResponseEntity<Page<Order>> getOrders(@RequestParam(required = false) String customerName,
                                                 @RequestParam(required = false) OrderStatus status,
                                                 Pageable pageable) {
        Page<Order> orders = orderService.getOrders(customerName, status, pageable);
        return ResponseEntity.ok(orders);
    }

}
