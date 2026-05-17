package nu.eats.inventory.domain;

import nu.eats.domain.Vendor;

import java.util.LinkedHashSet;
import java.util.Set;

public class Store {
    private final Set<StoreItem> items = new LinkedHashSet<>();

    public boolean add(StoreItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }

        return items.add(item);
    }

    public boolean remove(StoreItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }

        return items.remove(item);
    }

    public StoreItem[] items() {
        return items.toArray(StoreItem[]::new);
    }

    public StoreItem[] itemsBy(Vendor vendor) {
        if (vendor == null) {
            throw new IllegalArgumentException("Vendor cannot be null");
        }

        int count = 0;

        for (StoreItem item : items) {
            if (item.vendor().equals(vendor)) {
                count++;
            }
        }

        StoreItem[] result = new StoreItem[count];

        int index = 0;

        for (StoreItem item : items) {
            if (item.vendor().equals(vendor)) {
                result[index++] = item;
            }
        }

        return result;
    }
}