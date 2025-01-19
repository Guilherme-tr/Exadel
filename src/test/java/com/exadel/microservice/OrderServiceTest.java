package com.exadel.microservice;

import com.exadel.microservice.dto.OrderRequest;
import com.exadel.microservice.entity.Order;
import com.exadel.microservice.repository.OrderRepository;
import com.exadel.microservice.service.OrderEventProducer;
import com.exadel.microservice.service.OrderService;
import com.exadel.microservice.util.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderEventProducer orderEventProducer;

    @InjectMocks
    private OrderService orderService;

    private Order order;

    private UUID orderId;

    @BeforeEach
    void setUp() {
        order = new Order( "João Silva", "Laptop", 2, OrderStatus.PENDING);
    }

    @Test
    void shouldCreateOrder() {
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderRequest request = new OrderRequest("João Silva", "Laptop", 2);
        Order createdOrder = orderService.createOrder(request);

        assertNotNull(createdOrder);
        assertEquals(order.getCustomerName(), createdOrder.getCustomerName());

        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderEventProducer, times(1)).sendOrderEvent(any(Order.class));
    }

    @Test
    void shouldUpdateOrderStatus() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.saveAndFlush(any(Order.class))).thenReturn(order);

        Order updatedOrder = orderService.updateOrderStatus(orderId, OrderStatus.CONFIRMED);

        assertEquals(OrderStatus.CONFIRMED, updatedOrder.getStatus());
        verify(orderRepository, times(1)).saveAndFlush(any(Order.class));
        verify(orderEventProducer, times(1)).sendOrderEvent(any(Order.class));
    }

    @Test
    void shouldReturnOrdersWithPagination() {
        Page<Order> ordersPage = new PageImpl<>(List.of(order));
        when(orderRepository.findAll(any(Pageable.class))).thenReturn(ordersPage);

        Page<Order> result = orderService.getOrders(any(), null, Pageable.unpaged());

        assertEquals(1, result.getTotalElements());
        verify(orderRepository, times(1)).findAll(any(Pageable.class));
    }
}
