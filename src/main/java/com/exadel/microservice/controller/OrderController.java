package com.exadel.microservice.controller;

import com.exadel.microservice.dto.OrderRequest;
import com.exadel.microservice.entity.Order;
import com.exadel.microservice.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

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
}
