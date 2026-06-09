package eggdepot.feature.cart.model;

import eggdepot.feature.inventory.model.InventoryItem;
import eggdepot.model.NonNegativeInt;

public final class ShoppingCartItem {
    private final InventoryItem catalogItem;
    private final NonNegativeInt quantity;

    public ShoppingCartItem(InventoryItem catalogItem, int quantity) {
        if (catalogItem == null) {
            throw new IllegalArgumentException("Catalog item cannot be null");
        }

        this.catalogItem = catalogItem;
        this.quantity = new NonNegativeInt(quantity);
    }

    public ShoppingCartItem(InventoryItem inventoryItem) {
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
