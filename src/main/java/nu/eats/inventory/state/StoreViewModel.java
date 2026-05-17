package nu.eats.ui.store;

import nu.eats.common.messaging.EventBus;
import nu.eats.inventory.domain.Store;
import nu.eats.inventory.domain.StoreItem;
import nu.eats.inventory.domain.StoreItemCategory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class StoreViewModel {
    private final EventBus eventBus = EventBus.mainBus();
    private final Store store;

    public StoreViewModel(Store store) {
        this.store = store;
    }

    public StoreItem[] storeItems() {
        return store.items();
    }

    public void addItem(StoreItem item) {
        store.add(item);

        eventBus.publish(StoreState.ITEM_ADDED, item);
    }

    public void selectMenuItem(StoreItem item) {
        eventBus.publish(StoreState.ITEM_SELECTED, item);
    }

    public Map<StoreItemCategory, List<StoreItem>> getItemsByCategory() {
        return Arrays.stream(store.items())
                .collect(java.util.stream.Collectors.groupingBy(
                        StoreItem::category,
                        java.util.LinkedHashMap::new,
                        java.util.stream.Collectors.toList()));
    }
}
