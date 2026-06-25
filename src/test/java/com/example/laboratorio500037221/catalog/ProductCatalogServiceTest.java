package com.example.laboratorio500037221.catalog;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductCatalogServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductCatalogService productCatalogService;

    @Test
    void registerNormalizesSkuAndPersistsWhenSkuIsAvailable() {
        ProductRequest request = new ProductRequest(" sku-001 ", " Premium Coffee ", 12, new BigDecimal("8.50"));
        when(productRepository.existsBySku("SKU-001")).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProductResponse response = productCatalogService.register(request);

        assertEquals("SKU-001", response.sku());
        assertEquals("Premium Coffee", response.name());
        assertEquals(12, response.stock());
        assertEquals(new BigDecimal("8.50"), response.unitPrice());
        verify(productRepository).existsBySku("SKU-001");
        verify(productRepository).save(argThat(product ->
                "SKU-001".equals(product.getSku())
                        && "Premium Coffee".equals(product.getName())
                        && product.getStock() == 12
                        && new BigDecimal("8.50").equals(product.getUnitPrice())
        ));
    }

    @Test
    void registerRejectsDuplicateSkuWithoutSaving() {
        ProductRequest request = new ProductRequest("sku-001", "Premium Coffee", 12, new BigDecimal("8.50"));
        when(productRepository.existsBySku("SKU-001")).thenReturn(true);

        assertThrows(ProductAlreadyExistsException.class, () -> productCatalogService.register(request));

        verify(productRepository).existsBySku("SKU-001");
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void reserveStockDiscountsUnitsAndSavesProduct() {
        Product product = new Product("SKU-001", "Premium Coffee", 10, new BigDecimal("8.50"));
        when(productRepository.findBySku("SKU-001")).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);

        ProductResponse response = productCatalogService.reserveStock(" sku-001 ", 3);

        assertEquals(7, response.stock());
        verify(productRepository).findBySku("SKU-001");
        verify(productRepository).save(product);
    }

    @Test
    void reserveStockRejectsWhenStockIsInsufficient() {
        Product product = new Product("SKU-001", "Premium Coffee", 2, new BigDecimal("8.50"));
        when(productRepository.findBySku("SKU-001")).thenReturn(Optional.of(product));

        assertThrows(InsufficientStockException.class, () -> productCatalogService.reserveStock("sku-001", 3));

        verify(productRepository).findBySku("SKU-001");
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void listAvailableMapsRepositoryProducts() {
        Product product = new Product("SKU-002", "Dark Chocolate", 5, new BigDecimal("4.25"));
        when(productRepository.findByStockGreaterThan(0)).thenReturn(List.of(product));

        List<ProductResponse> response = productCatalogService.listAvailable();

        assertEquals(1, response.size());
        assertEquals("SKU-002", response.get(0).sku());
        assertEquals(5, response.get(0).stock());
        verify(productRepository).findByStockGreaterThan(0);
    }
}
