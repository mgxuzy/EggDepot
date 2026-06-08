package nu.eats.gui;

import nu.eats.model.Vendor;
import nu.eats.feature.inventory.model.Product;
import nu.eats.feature.inventory.model.InventoryItem;
import nu.eats.feature.inventory.model.InventoryItemCategory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TestData {

    public static List<InventoryItem> getCoffeeShopMenu(Vendor vendor) {
        List<InventoryItem> items = new ArrayList<>();

        var hustlersCup = new InventoryItemCategory("Chicken Egg", true);

        items.add(createItem("Chicken Egg (Small)", "7", vendor, hustlersCup));
        items.add(createItem("Chicken Egg (Medium)", "8", vendor, hustlersCup));
        items.add(createItem("Chicken Egg (Large)", "9", vendor, hustlersCup));
        items.add(createItem("Chicken Egg (Extra Large)", "10", vendor, hustlersCup));

        var classics = new InventoryItemCategory("Chicken Egg Tray", false);

        items.add(createItem("Chicken Egg (Small) Tray", "170", vendor, classics));
        items.add(createItem("Chicken Egg (Medium) Tray", "180", vendor, classics));
        items.add(createItem("Chicken Egg (Large) Tray", "190", vendor, classics));
        items.add(createItem("Chicken Egg (Extra Large) Tray", "200", vendor, classics));

        return items;
    }

    private static InventoryItem createItem(String name, String price, Vendor vendor, InventoryItemCategory category) {
        Product product = new Product(
                name.toLowerCase().replace(" ", "-"),
                null,
                name,
                new BigDecimal(price));

        return new InventoryItem(product, vendor, category, 10);
    }
}
