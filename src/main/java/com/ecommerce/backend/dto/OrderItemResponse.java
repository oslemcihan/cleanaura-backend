package com.ecommerce.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderItemResponse {
    private String productName;
    private double price;
    private int quantity;
    private double totalPrice;
}
