package nu.eats.feature.inventory.state;

import nu.eats.common.messaging.Topic;
import nu.eats.feature.inventory.model.InventoryItem;

public final class StoreState {
    public static final Topic<InventoryItem> ITEM_ADDED = new Topic<>();
    public static final Topic<InventoryItem> ITEM_SELECTED = new Topic<>();

    private StoreState() {
    }
}
