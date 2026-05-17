package nu.eats.cart;

import nu.eats.common.messaging.Topic;

import java.math.BigDecimal;

public final class CartState {
    public static final Topic<CartItem> ITEM_ADDED = new Topic<>();
    public static final Topic<CartItem> ITEM_REMOVED = new Topic<>();
    public static final Topic<CartItem> ITEM_UPDATED = new Topic<>();
    public static final Topic<BigDecimal> SUBTOTAL_CHANGED = new Topic<>();
    public static final Topic<CartItem[]> CHECKED_OUT = new Topic<>();
    public static final Topic<String> ERROR = new Topic<>();
    public static final Topic<String> WARNING = new Topic<>();

    private CartState() {
    }
}
