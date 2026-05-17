package nu.eats.cart.model;

import nu.eats.model.NonNegativeInt;
import nu.eats.inventory.model.InventoryItem;

public final class CartItem {
    private final InventoryItem catalogItem;
    private final NonNegativeInt quantity;

    public CartItem(InventoryItem catalogItem, int quantity) {
        if (catalogItem == null) {
            throw new IllegalArgumentException("Catalog item cannot be null");
        }

        this.catalogItem = catalogItem;
        this.quantity = new NonNegativeInt(quantity);
    }

    public CartItem(InventoryItem inventoryItem) {
        this(inventoryItem, 1);
    }

    public InventoryItem catalogItem() {
        return catalogItem;
    }

    public int quantity() {
        return quantity.value();
    }

    public void setQuantity(int quantity) {
        if (catalogItem.availableStock() < quantity) {
            throw new IllegalArgumentException("Insufficient stock available");
        }

        this.quantity.setValue(quantity);
    }
}
