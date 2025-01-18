package com.exadel.microservice.service;

import com.exadel.microservice.dto.OrderEvent;
import com.exadel.microservice.entity.Order;
import com.exadel.microservice.repository.OrderRepository;
import com.exadel.microservice.util.OrderStatus;
import jakarta.transaction.TransactionScoped;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderEventProducer eventProducer;


    private final Logger log = LoggerFactory.getLogger(OrderService.class);

    public OrderService(OrderRepository orderRepository, OrderEventProducer eventProducer) {
        this.orderRepository = orderRepository;
        this.eventProducer = eventProducer;
    }

    @Transactional
    public Order createOrder(Order order){
        order.setId(UUID.randomUUID());
        Order savedOrder = orderRepository.saveAndFlush(order);

        OrderEvent event = new OrderEvent(savedOrder.getId(),
                savedOrder.getCustomerName(),
                savedOrder.getProduct(),
                savedOrder.getQuantity(),
                savedOrder.getStatus());

        eventProducer.sendOrderEvent(event);

        return savedOrder;
    }

    @Transactional
    public Order updateOrderStatus(UUID orderId, OrderStatus newOrderStatus){
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(newOrderStatus);
        Order updatedOrder = orderRepository.saveAndFlush(order);

        OrderEvent event = new OrderEvent(updatedOrder.getId(),
                updatedOrder.getCustomerName(),
                updatedOrder.getProduct(),
                updatedOrder.getQuantity(),
                updatedOrder.getStatus());

        eventProducer.sendOrderEvent(event);

        return updatedOrder;
    }
}
