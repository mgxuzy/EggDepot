package nu.eats.inventory.model;

import java.math.BigDecimal;

public record Product(String id, String imageUri, String name, BigDecimal price) {
}
