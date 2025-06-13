package com.ecommerce.backend.dto;

import com.ecommerce.backend.model.OrderStatus;
import com.ecommerce.backend.model.ShippingStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private Long id;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private ShippingStatus shippingStatus;
    private String userEmail;
    private double totalAmount;
    private List<OrderItemResponse> items;
    private boolean bulkCorporateOrder;
    private String statusDisplayName;

}
