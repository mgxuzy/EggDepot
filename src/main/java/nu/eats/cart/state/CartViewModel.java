package nu.eats.cart.state;

import nu.eats.cart.model.Cart;
import nu.eats.cart.model.CartItem;
import nu.eats.common.messaging.EventBus;
import nu.eats.inventory.model.InventoryItem;

import java.math.BigDecimal;

public class CartViewModel {
    private final EventBus eventBus = EventBus.mainBus();
    private final Cart cart;

    public CartViewModel(Cart cart) {
        this.cart = cart;
    }

    public CartItem[] cartItems() {
        return cart.items();
    }

    public int quantityOf(InventoryItem item) {
        return cart.quantity(item);
    }

    public void addToCart(CartItem item) {
        try {
            CartItem actualItem = cart.add(item);

            eventBus.publish(CartState.ITEM_ADDED, actualItem);
            eventBus.publish(CartState.SUBTOTAL_CHANGED, cart.subtotalPrice());
        } catch (IllegalArgumentException cause) {
            eventBus.publish(CartState.ERROR, cause.getMessage());
        }
    }

    public void removeFromCart(CartItem item) {
        try {
            cart.remove(item);

            eventBus.publish(CartState.ITEM_REMOVED, item);
            eventBus.publish(CartState.SUBTOTAL_CHANGED, cart.subtotalPrice());
        } catch (IllegalArgumentException cause) {
            eventBus.publish(CartState.ERROR, cause.getMessage());
        }
    }

    public void updateCartItem(CartItem item) {
        try {
            eventBus.publish(CartState.ITEM_UPDATED, item);
            eventBus.publish(CartState.SUBTOTAL_CHANGED, cart.subtotalPrice());
        } catch (IllegalArgumentException cause) {
            eventBus.publish(CartState.ERROR, cause.getMessage());
        }
    }

    public void checkOutCart() {
        var items = cart.checkOut();

        eventBus.publish(CartState.CHECKED_OUT, items);
        eventBus.publish(CartState.SUBTOTAL_CHANGED, BigDecimal.ZERO);
    }
}
