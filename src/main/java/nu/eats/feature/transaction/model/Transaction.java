package nu.eats.feature.transaction.model;

import java.time.LocalDateTime;

public record Transaction(
        int id,
        String product,
        int quantity,
        double price,
        double total,
        LocalDateTime date
) {
}
