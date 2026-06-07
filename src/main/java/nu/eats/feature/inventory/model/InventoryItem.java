package nu.eats.feature.inventory.model;

import nu.eats.model.Vendor;

public class InventoryItem {
    private final Product product;
    private final Vendor vendor;
    private final InventoryItemCategory category;

    private int availableStock;

    public InventoryItem(Product product, Vendor vendor, InventoryItemCategory category, int availableStock) {
        this.product = product;
        this.vendor = vendor;
        this.category = category;

        setAvailableStock(availableStock);
    }

    public Product product() {
        return product;
    }

    public Vendor vendor() {
        return vendor;
    }

    public int availableStock() {
        return availableStock;
    }

    public InventoryItemCategory category() {
        return category;
    }

    public void setAvailableStock(int availableStock) {
        if (availableStock < 0) {
            throw new IllegalArgumentException("Available stock cannot be negative");
        }

        this.availableStock = availableStock;
    }
}
