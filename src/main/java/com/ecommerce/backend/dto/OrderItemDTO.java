package com.ecommerce.backend.dto;

import lombok.Data;

@Data
public class OrderItemDTO {
    private String productName;
    private int quantity;
}
