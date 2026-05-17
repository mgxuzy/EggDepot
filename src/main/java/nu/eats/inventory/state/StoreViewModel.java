package nu.eats.inventory.state;

import nu.eats.common.messaging.EventBus;
import nu.eats.inventory.model.Inventory;
import nu.eats.inventory.model.InventoryItem;
import nu.eats.inventory.model.InventoryItemCategory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class StoreViewModel {
    private final EventBus eventBus = EventBus.mainBus();
    private final Inventory inventory;

    public StoreViewModel(Inventory inventory) {
        this.inventory = inventory;
    }

    public InventoryItem[] storeItems() {
        return inventory.items();
    }

    public void addItem(InventoryItem item) {
        inventory.add(item);

        eventBus.publish(StoreState.ITEM_ADDED, item);
    }

    public void selectMenuItem(InventoryItem item) {
        eventBus.publish(StoreState.ITEM_SELECTED, item);
    }

    public Map<InventoryItemCategory, List<InventoryItem>> getItemsByCategory() {
        return Arrays.stream(inventory.items())
                .collect(java.util.stream.Collectors.groupingBy(
                        InventoryItem::category,
                        java.util.LinkedHashMap::new,
                        java.util.stream.Collectors.toList()));
    }
}
