package com.exadel.microservice.service;

import com.exadel.microservice.dto.OrderRequest;
import com.exadel.microservice.entity.Order;
import com.exadel.microservice.repository.OrderRepository;
import com.exadel.microservice.util.OrderStatus;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Order createOrder(OrderRequest orderRequest){
        log.info("Creating new order to customer: {}", orderRequest.getCustomerName());

        Order newOrder = new Order();
        newOrder.setCustomerName(orderRequest.getCustomerName());
        newOrder.setProduct(orderRequest.getProduct());
        newOrder.setQuantity(orderRequest.getQuantity());
        newOrder.setStatus(OrderStatus.PENDING);

        Order savedOrder = orderRepository.save(newOrder);

        log.info("Order created with ID: {}", savedOrder.getId());

        eventProducer.sendOrderEvent(savedOrder);

        log.info("Kafka event sent");

        return savedOrder;
    }

    @Transactional
    public Order updateOrderStatus(UUID orderId, OrderStatus newOrderStatus){
        log.info("Updating order {}", orderId);
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(newOrderStatus);

        Order updatedOrder = orderRepository.saveAndFlush(order);

        log.info("Order {} updated", orderId);

        eventProducer.sendOrderEvent(updatedOrder);

        log.info("Kafka event update sent");

        return updatedOrder;
    }

    public Order getOrderById(UUID orderId){
        log.info("Getting order {}", orderId);
        return orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public Page<Order> getOrders(String customerName, OrderStatus status, Pageable pageable){
        if(customerName != null && status != null){
            return orderRepository.findByCustomerNameAndStatus(customerName, status, pageable);
        } else if (customerName != null){
            return orderRepository.findByCustomerName(customerName, pageable);
        } else if (status != null) {
            return orderRepository.findByStatus(status, pageable);
        } else {
            return orderRepository.findAll(pageable);
        }
    }
}
