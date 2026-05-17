package nu.eats.inventory.domain;

import java.math.BigDecimal;

public record Product(String id, String imageUri, String name, BigDecimal price) {
}
