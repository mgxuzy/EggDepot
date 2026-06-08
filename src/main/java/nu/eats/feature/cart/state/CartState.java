package nu.eats.feature.cart.state;

import nu.eats.common.messaging.Topic;
import nu.eats.feature.cart.model.ShoppingCartItem;

import java.math.BigDecimal;

public final class CartState {
    public static final Topic<ShoppingCartItem> ITEM_ADDED = new Topic<>();
    public static final Topic<ShoppingCartItem> ITEM_REMOVED = new Topic<>();
    public static final Topic<ShoppingCartItem> ITEM_UPDATED = new Topic<>();
    public static final Topic<BigDecimal> SUBTOTAL_CHANGED = new Topic<>();
    public static final Topic<ShoppingCartItem[]> CHECKED_OUT = new Topic<>();
    public static final Topic<String> ERROR = new Topic<>();
    public static final Topic<String> WARNING = new Topic<>();

    private CartState() {
    }
}
