package com.ecommerce.backend.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {

    @NotEmpty(message = "Sipariş en az bir ürün içermelidir.")
    private List<OrderItemRequest> items;
}
