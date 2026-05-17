package nu.eats.cart;

import nu.eats.domain.Quantity;
import nu.eats.inventory.domain.StoreItem;

public final class CartItem {
    private final StoreItem catalogItem;
    private final Quantity quantity;

    public CartItem(StoreItem catalogItem, int quantity) {
        if (catalogItem == null) {
            throw new IllegalArgumentException("Catalog item cannot be null");
        }

        this.catalogItem = catalogItem;
        this.quantity = new Quantity(quantity);
    }

    public CartItem(StoreItem storeItem) {
        this(storeItem, 1);
    }

    public StoreItem catalogItem() {
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
