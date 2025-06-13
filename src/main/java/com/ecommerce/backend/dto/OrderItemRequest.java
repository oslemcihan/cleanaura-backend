package com.ecommerce.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderItemRequest {

    @NotNull(message = "Ürün ID'si zorunludur.")
    private Long productId;

    @Min(value = 1, message = "Miktar en az 1 olmalıdır.")
    private int quantity;
}
