package eggdepot.feature.inventory.state;

import eggdepot.common.messaging.Topic;
import eggdepot.feature.inventory.model.InventoryItem;

public final class StoreState {
    public static final Topic<InventoryItem> ITEM_ADDED = new Topic<>();
    public static final Topic<InventoryItem> ITEM_SELECTED = new Topic<>();

    private StoreState() {
    }
}
