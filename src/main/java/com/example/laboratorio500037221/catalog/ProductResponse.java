package com.example.laboratorio500037221.catalog;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String sku,
        String name,
        int stock,
        BigDecimal unitPrice
) {

    static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getSku(),
                product.getName(),
                product.getStock(),
                product.getUnitPrice()
        );
    }
}
