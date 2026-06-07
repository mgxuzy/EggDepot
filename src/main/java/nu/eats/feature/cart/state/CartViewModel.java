package nu.eats.feature.cart.state;

import nu.eats.feature.cart.model.ShoppingCart;
import nu.eats.feature.cart.model.ShoppingCartItem;
import nu.eats.common.messaging.EventBus;
import nu.eats.feature.inventory.model.InventoryItem;

import java.math.BigDecimal;

public class CartViewModel {
    private final EventBus eventBus = EventBus.mainBus();
    private final ShoppingCart shoppingCart;

    public CartViewModel(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public ShoppingCartItem[] cartItems() {
        return shoppingCart.items();
    }

    public int quantityOf(InventoryItem item) {
        return shoppingCart.quantity(item);
    }

    public void addToCart(ShoppingCartItem item) {
        try {
            ShoppingCartItem actualItem = shoppingCart.add(item);

            eventBus.publish(CartState.ITEM_ADDED, actualItem);
            eventBus.publish(CartState.SUBTOTAL_CHANGED, shoppingCart.subtotalPrice());
        } catch (IllegalArgumentException cause) {
            eventBus.publish(CartState.ERROR, cause.getMessage());
        }
    }

    public void removeFromCart(ShoppingCartItem item) {
        try {
            shoppingCart.remove(item);

            eventBus.publish(CartState.ITEM_REMOVED, item);
            eventBus.publish(CartState.SUBTOTAL_CHANGED, shoppingCart.subtotalPrice());
        } catch (IllegalArgumentException cause) {
            eventBus.publish(CartState.ERROR, cause.getMessage());
        }
    }

    public void updateCartItem(ShoppingCartItem item) {
        try {
            eventBus.publish(CartState.ITEM_UPDATED, item);
            eventBus.publish(CartState.SUBTOTAL_CHANGED, shoppingCart.subtotalPrice());
        } catch (IllegalArgumentException cause) {
            eventBus.publish(CartState.ERROR, cause.getMessage());
        }
    }

    public void checkOutCart() {
        var items = shoppingCart.checkOut();

        eventBus.publish(CartState.CHECKED_OUT, items);
        eventBus.publish(CartState.SUBTOTAL_CHANGED, BigDecimal.ZERO);
    }
}
