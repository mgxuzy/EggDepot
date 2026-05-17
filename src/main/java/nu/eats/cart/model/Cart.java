package nu.eats.cart.model;

import nu.eats.inventory.model.InventoryItem;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Cart {
    private final Map<InventoryItem, CartItem> items = new HashMap<>();

    public CartItem add(CartItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }

        var catalogItem = item.catalogItem();
        var mappedItem = items.get(catalogItem);

        if (mappedItem != null) {
            int newQuantity = mappedItem.quantity() + item.quantity();

            if (catalogItem.availableStock() < newQuantity) {
                throw new IllegalArgumentException("Insufficient stock available");
            }

            mappedItem.setQuantity(newQuantity);

            return mappedItem;
        }

        if (catalogItem.availableStock() < item.quantity()) {
            throw new IllegalArgumentException("Insufficient stock available");
        }

        items.put(catalogItem, item);

        return item;
    }

    public int quantity(InventoryItem item) {
        var cartItem = items.get(item);

        if (cartItem != null) {
            return cartItem.quantity();
        }

        return 0;
    }

    public CartItem[] items() {
        return items.values().toArray(CartItem[]::new);
    }

    public void remove(CartItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }

        items.remove(item.catalogItem());
    }

    public BigDecimal subtotalPrice() {
        var value = BigDecimal.ZERO;

        for (CartItem item : items.values()) {
            value = value.add(item.catalogItem().product().price()
                    .multiply(BigDecimal.valueOf(item.quantity())));
        }

        return value;
    }

    public CartItem[] checkOut() {
        var result = items.values().toArray(CartItem[]::new);

        for (CartItem item : result) {
            var catalogItem = item.catalogItem();

            catalogItem.setAvailableStock(catalogItem.availableStock() - item.quantity());
        }

        items.clear();

        return result;
    }
}
