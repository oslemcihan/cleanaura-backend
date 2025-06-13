package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.OrderRequest;
import com.ecommerce.backend.dto.OrderResponse;
import com.ecommerce.backend.model.Order;
import com.ecommerce.backend.model.OrderItem;
import com.ecommerce.backend.model.ShippingStatus;

import java.util.List;

public interface OrderService {
    Order createOrder(OrderRequest request, Long userId);
    List<Order> getOrdersByUserId(Long userId);
    Order getOrderById(Long id);
    void cancelOrder(Long id);
    OrderResponse createOrderWithResponse(OrderRequest request, Long userId);
    List<OrderResponse> getOrderResponsesByUserId(Long userId);
    List<Order> getAllOrders();
    OrderResponse mapToResponse(Order order);
    Order updateShippingStatus(Long orderId, ShippingStatus status);


}
