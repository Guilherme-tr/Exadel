package com.exadel.microservice.service;

import com.exadel.microservice.dto.OrderEvent;
import com.exadel.microservice.entity.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderEventProducer {
    private static final Logger log = LoggerFactory.getLogger(OrderEventProducer.class);
    private static final String TOPIC = "order-events";

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public OrderEventProducer(KafkaTemplate<String, OrderEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOrderEvent(Order order){
        log.info("Sending order event to kafka: {}", order);

        OrderEvent event = new OrderEvent(order.getId(),
                order.getCustomerName(),
                order.getProduct(),
                order.getQuantity(),
                order.getStatus());

        kafkaTemplate.send(TOPIC, event);
    }
}
