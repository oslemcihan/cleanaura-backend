package com.ecommerce.backend.model;

public enum OrderStatus {
    PREPARING("Hazırlanıyor"),
    SHIPPED("Kargoda"),
    DELIVERED("Teslim Edildi"),
    CANCELLED("İptal Edildi");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
