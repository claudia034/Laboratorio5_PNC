package com.example.laboratorio500037221.catalog;

import jakarta.validation.constraints.Min;

public record ReserveStockRequest(@Min(1) int units) {
}
