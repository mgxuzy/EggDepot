package nu.eats.feature.inventory.model;

import nu.eats.model.Vendor;

import java.util.LinkedHashSet;
import java.util.Set;

public class Inventory {
    private final Set<InventoryItem> items = new LinkedHashSet<>();

    public boolean add(InventoryItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }

        return items.add(item);
    }

    public boolean remove(InventoryItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }

        return items.remove(item);
    }

    public InventoryItem[] items() {
        return items.toArray(InventoryItem[]::new);
    }

    public InventoryItem[] itemsBy(Vendor vendor) {
        if (vendor == null) {
            throw new IllegalArgumentException("Vendor cannot be null");
        }

        int count = 0;

        for (InventoryItem item : items) {
            if (item.vendor().equals(vendor)) {
                count++;
            }
        }

        InventoryItem[] result = new InventoryItem[count];

        int index = 0;

        for (InventoryItem item : items) {
            if (item.vendor().equals(vendor)) {
                result[index++] = item;
            }
        }

        return result;
    }
}