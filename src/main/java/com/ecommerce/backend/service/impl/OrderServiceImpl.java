package com.ecommerce.backend.service.impl;

import com.ecommerce.backend.dto.OrderItemRequest;
import com.ecommerce.backend.dto.OrderItemResponse;
import com.ecommerce.backend.dto.OrderRequest;
import com.ecommerce.backend.dto.OrderResponse;
import com.ecommerce.backend.exception.ResourceNotFoundException;
import com.ecommerce.backend.model.*;
import com.ecommerce.backend.repository.OrderItemRepository;
import com.ecommerce.backend.repository.OrderRepository;
import com.ecommerce.backend.repository.ProductRepository;
import com.ecommerce.backend.repository.UserRepository;
import com.ecommerce.backend.service.AppSettingsService;
import com.ecommerce.backend.service.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final AppSettingsService appSettingsService;

    public OrderServiceImpl(OrderRepository orderRepository,
                            OrderItemRepository orderItemRepository,
                            UserRepository userRepository,
                            ProductRepository productRepository,
                            AppSettingsService appSettingsService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.appSettingsService = appSettingsService;
    }

    @Override
    @Transactional
    public Order createOrder(OrderRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı bulunamadı: " + userId));

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PREPARING);
        order.setShippingStatus(ShippingStatus.PENDING);

        List<OrderItem> items = new ArrayList<>();
        int totalQuantity = 0;
        double totalPrice = 0.0;

        for (OrderItemRequest itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Ürün bulunamadı: " + itemReq.getProductId()));

            int quantity = itemReq.getQuantity();
            double itemTotal = product.getPrice() * quantity;

            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setQuantity(quantity);
            item.setPrice(product.getPrice());
            item.setOrder(order);

            totalQuantity += quantity;
            totalPrice += itemTotal;

            items.add(item);
        }

        double discountRate = appSettingsService.getSettings().getDiscountRate();

        if (user.getCustomerType() == CustomerType.KURUMSAL && totalQuantity >= 20) {
            totalPrice = totalPrice * (1 - discountRate);
        }

        order.setItems(items);
        order.setTotalPrice(totalPrice);

        Order savedOrder = orderRepository.save(order);
        orderItemRepository.saveAll(items);

        return savedOrder;
    }

    @Override
    public OrderResponse createOrderWithResponse(OrderRequest request, Long userId) {
        Order order = createOrder(request, userId);
        return mapToResponse(order);
    }

    @Override
    public List<OrderResponse> getOrderResponsesByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sipariş bulunamadı: " + id));
    }

    @Override
    public void cancelOrder(Long id) {
        Order order = getOrderById(id);
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    @Override
    public Order updateShippingStatus(Long orderId, ShippingStatus status) {
        Order order = getOrderById(orderId);
        order.setShippingStatus(status);
        return orderRepository.save(order);
    }

    @Override
    public OrderResponse mapToResponse(Order order) {
        List<OrderItemResponse> itemResponses = order.getItems().stream().map(item -> {
            double total = item.getPrice() * item.getQuantity();
            return new OrderItemResponse(
                    item.getProduct().getName(),
                    item.getPrice(),
                    item.getQuantity(),
                    total
            );
        }).toList();

        double totalAmount = itemResponses.stream()
                .mapToDouble(OrderItemResponse::getTotalPrice)
                .sum();

        boolean isBulkCorporate = order.getUser().getCustomerType() == CustomerType.KURUMSAL &&
                order.getItems().stream().mapToInt(OrderItem::getQuantity).sum() >= 20;

        return OrderResponse.builder()
                .id(order.getId())
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .statusDisplayName(order.getStatus().getDisplayName())
                .shippingStatus(order.getShippingStatus())
                .userEmail(order.getUser().getEmail())
                .totalAmount(totalAmount)
                .items(itemResponses)
                .bulkCorporateOrder(isBulkCorporate)
                .build();
    }
}
