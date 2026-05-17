package nu.eats.inventory.domain;

import nu.eats.domain.Vendor;

public class StoreItem {
    private final Product product;
    private final Vendor vendor;
    private final StoreItemCategory category;

    private int availableStock;

    public StoreItem(Product product, Vendor vendor, StoreItemCategory category, int availableStock) {
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

    public StoreItemCategory category() {
        return category;
    }

    public void setAvailableStock(int availableStock) {
        if (availableStock < 0) {
            throw new IllegalArgumentException("Available stock cannot be negative");
        }

        this.availableStock = availableStock;
    }
}
