package com.example.laboratorio500037221.catalog;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Service
@Transactional
public class ProductCatalogService {

    private final ProductRepository productRepository;

    public ProductCatalogService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse register(ProductRequest request) {
        Objects.requireNonNull(request, "Product request is required");
        String sku = normalizeSku(request.sku());
        String name = normalizeName(request.name());
        validateNewProduct(request, sku, name);

        if (productRepository.existsBySku(sku)) {
            throw new ProductAlreadyExistsException(sku);
        }

        Product product = new Product(sku, name, request.stock(), request.unitPrice());
        return ProductResponse.from(productRepository.save(product));
    }

    public ProductResponse reserveStock(String sku, int units) {
        String normalizedSku = normalizeSku(sku);
        if (units <= 0) {
            throw new IllegalArgumentException("Units must be greater than zero");
        }

        Product product = productRepository.findBySku(normalizedSku)
                .orElseThrow(() -> new ProductNotFoundException(normalizedSku));

        product.reserve(units);
        return ProductResponse.from(productRepository.save(product));
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> listAvailable() {
        return productRepository.findByStockGreaterThan(0)
                .stream()
                .map(ProductResponse::from)
                .toList();
    }

    private void validateNewProduct(ProductRequest request, String sku, String name) {
        if (sku.isBlank()) {
            throw new IllegalArgumentException("SKU is required");
        }
        if (name.isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (request.stock() < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }
        BigDecimal unitPrice = request.unitPrice();
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Unit price must be greater than zero");
        }
    }

    private String normalizeSku(String sku) {
        return Objects.requireNonNull(sku, "SKU is required")
                .trim()
                .toUpperCase(Locale.ROOT);
    }

    private String normalizeName(String name) {
        return Objects.requireNonNull(name, "Name is required").trim();
    }
}
