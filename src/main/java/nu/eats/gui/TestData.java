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

        items.add(createItem("Chicken Egg (Small)", "89", vendor, hustlersCup));
        items.add(createItem("Chicken Egg (Medium)", "109", vendor, hustlersCup));
        items.add(createItem("Chicken Egg (Large)", "89", vendor, hustlersCup));
        items.add(createItem("Chicken Egg (Extra Large)", "99", vendor, hustlersCup));

        var classics = new InventoryItemCategory("Chicken Egg Tray", false);

        items.add(createItem("Chicken Egg (Small) Tray", "59", vendor, classics));
        items.add(createItem("Chicken Egg (Medium) Tray", "69", vendor, classics));
        items.add(createItem("Chicken Egg (Large) Tray", "79", vendor, classics));
        items.add(createItem("Chicken Egg (Extra Large) Tray", "89", vendor, classics));

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
