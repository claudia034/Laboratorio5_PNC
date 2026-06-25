package com.example.laboratorio500037221.catalog;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.math.BigDecimal;

@Entity
@Table(
        name = "products",
        uniqueConstraints = @UniqueConstraint(name = "uk_products_sku", columnNames = "sku")
)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 40)
    private String sku;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false)
    private int stock;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;

    protected Product() {
    }

    public Product(String sku, String name, int stock, BigDecimal unitPrice) {
        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Unit price must be greater than zero");
        }
        this.sku = sku;
        this.name = name;
        this.stock = stock;
        this.unitPrice = unitPrice;
    }

    public void reserve(int units) {
        if (units <= 0) {
            throw new IllegalArgumentException("Units must be greater than zero");
        }
        if (stock < units) {
            throw new InsufficientStockException(sku, units, stock);
        }
        stock -= units;
    }

    public Long getId() {
        return id;
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public int getStock() {
        return stock;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
}
