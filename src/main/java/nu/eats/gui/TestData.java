package nu.eats.gui;

import nu.eats.domain.Vendor;
import nu.eats.domain.store.Product;
import nu.eats.domain.store.StoreItem;
import nu.eats.domain.store.StoreItemCategory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TestData {

    public static List<StoreItem> getCoffeeShopMenu(Vendor vendor) {
        List<StoreItem> items = new ArrayList<>();

        // Hustler's Cup (Coffee)
        StoreItemCategory hustlersCup = new StoreItemCategory("Hustler's Cup (Coffee)", true);
        items.add(createItem("Barista's Drink", "89", vendor, hustlersCup));
        items.add(createItem("Scotch Crumble", "89", vendor, hustlersCup));
        items.add(createItem("Shaken Cacao Espresso", "89", vendor, hustlersCup));
        items.add(createItem("Tiramisu Latte", "99", vendor, hustlersCup));
        items.add(createItem("Einspanner Vienna", "89", vendor, hustlersCup));

        // Classics
        StoreItemCategory classics = new StoreItemCategory("Classics", false);
        items.add(createItem("Iced or Hot Americano", "59", vendor, classics));
        items.add(createItem("Iced or Hot Cafe Latte", "69", vendor, classics));
        items.add(createItem("Iced or Hot Caramel Macchiato", "89", vendor, classics));
        items.add(createItem("Spanish Latte", "79", vendor, classics));
        items.add(createItem("Summer Sea Salt", "79", vendor, classics));
        items.add(createItem("White Mocha", "89", vendor, classics));

        // Ceremonial Matcha
        StoreItemCategory ceremonialMatcha = new StoreItemCategory("Ceremonial Matcha", false);
        items.add(createItem("Matcha Latte", "79", vendor, ceremonialMatcha));
        items.add(createItem("Matcha Tiramisu", "99", vendor, ceremonialMatcha));
        items.add(createItem("Dirty Matcha", "89", vendor, ceremonialMatcha));
        items.add(createItem("White Chocolate Matcha", "89", vendor, ceremonialMatcha));
        items.add(createItem("Salted Cream Matcha", "89", vendor, ceremonialMatcha));
        items.add(createItem("Matcha Strawberry", "89", vendor, ceremonialMatcha));

        // Others
        StoreItemCategory others = new StoreItemCategory("Others", false);
        items.add(createItem("Icy Cacao", "79", vendor, others));
        items.add(createItem("Summer Berries", "79", vendor, others));
        items.add(createItem("Milo Krunch", "89", vendor, others));

        // Wellness & Refreshment
        StoreItemCategory wellness = new StoreItemCategory("Wellness & Refreshment", false);
        items.add(createItem("Mixed Berries", "79", vendor, wellness));

        // Add-Ons
        StoreItemCategory addOns = new StoreItemCategory("Add-Ons", false);
        items.add(createItem("Espresso", "20", vendor, addOns));
        items.add(createItem("Sea Salt Cream", "15", vendor, addOns));
        items.add(createItem("Sub Oatmilk", "15", vendor, addOns));
        items.add(createItem("Sub Breve", "20", vendor, addOns));

        // Promo
        StoreItemCategory promo = new StoreItemCategory("Promo", false);
        items.add(createItem("Biscoff Latte", "99", vendor, promo));

        return items;
    }

    public static List<StoreItem> getQuickBitesMenu(Vendor vendor) {
        List<StoreItem> items = new ArrayList<>();
        StoreItemCategory mainMenu = new StoreItemCategory("Main Menu", true);

        items.add(createItem("Shawarma Rice", "80.00", vendor, mainMenu));
        items.add(createItem("Siomai Rice", "65.00", vendor, mainMenu));
        items.add(createItem("Hotdog Rice", "50.00", vendor, mainMenu));
        items.add(createItem("Hotdog Sandwich", "40.00", vendor, mainMenu));
        items.add(createItem("Hotdog on Stick", "30.00", vendor, mainMenu));
        items.add(createItem("Pancit Canton", "30.00", vendor, mainMenu));
        items.add(createItem("Pizza", "60.00", vendor, mainMenu));
        items.add(createItem("Siopao", "25.00", vendor, mainMenu));
        items.add(createItem("Siomai", "50.00", vendor, mainMenu));
        items.add(createItem("Tonkatsu Rice", "80.00", vendor, mainMenu));
        items.add(createItem("Shawarma Nachos", "25.00", vendor, mainMenu));

        return items;
    }

    public static List<StoreItem> getCiansDinerMenu(Vendor vendor) {
        List<StoreItem> items = new ArrayList<>();

        // Grilled Sandwich Menu
        StoreItemCategory grilledSandwich = new StoreItemCategory("Grilled Sandwich Menu", true);
        items.add(createItem("Grilled Cheese Sandwich", "35", vendor, grilledSandwich));
        items.add(createItem("Grilled Nutella Sandwich", "35", vendor, grilledSandwich));
        items.add(createItem("Grilled Skippy Sandwich", "35", vendor, grilledSandwich));
        items.add(createItem("Grilled Peanut Butter Sandwich", "35", vendor, grilledSandwich));
        items.add(createItem("Grilled Biscoff Sandwich", "45", vendor, grilledSandwich));
        items.add(createItem("Grilled Ham and Cheese Sandwich", "45", vendor, grilledSandwich));
        items.add(createItem("Grilled Bacon and Cheese Sandwich", "50", vendor, grilledSandwich));
        items.add(createItem("Grilled Spam and Cheese Sandwich", "55", vendor, grilledSandwich));

        // Grilled Fruity Jam
        StoreItemCategory grilledFruityJam = new StoreItemCategory("Grilled Fruity Jam", false);
        items.add(createItem("Strawberry Jam Sandwich", "35", vendor, grilledFruityJam));
        items.add(createItem("Blueberry Jam Sandwich", "35", vendor, grilledFruityJam));
        items.add(createItem("Mango Jam Sandwich", "35", vendor, grilledFruityJam));
        items.add(createItem("Peach Jam Sandwich", "35", vendor, grilledFruityJam));
        items.add(createItem("Green Apple Jam", "35", vendor, grilledFruityJam));

        // Mixed Jam Sandwich
        StoreItemCategory mixedJam = new StoreItemCategory("Mixed Jam Sandwich", false);
        items.add(createItem("Grilled Nutella + Strawberry Jam Sandwich", "50", vendor, mixedJam));
        items.add(createItem("Grilled Ham + Blueberry Jam Sandwich", "55", vendor, mixedJam));
        items.add(createItem("Grilled Ham + Mango Jam Sandwich", "55", vendor, mixedJam));
        items.add(createItem("Grilled Peanut Butter + Strawberry Jam", "50", vendor, mixedJam));

        return items;
    }

    public static List<StoreItem> getNUBaliwagComboMenu(Vendor vendor) {
        List<StoreItem> items = new ArrayList<>();

        // Snacks
        StoreItemCategory snacks = new StoreItemCategory("Snacks", true);
        items.add(createItem("Hot Chix (Fries)", "100", vendor, snacks));
        items.add(createItem("Hot Chix (Mac & Cheese)", "120", vendor, snacks));

        // Meals
        StoreItemCategory meals = new StoreItemCategory("Meals", false);
        items.add(createItem("Beef Rice", "100", vendor, meals));
        items.add(createItem("Pork Tapa", "100", vendor, meals));

        // Drinks
        StoreItemCategory drinks = new StoreItemCategory("Drinks", false);
        items.add(createItem("Milo", "70", vendor, drinks));
        items.add(createItem("Calamansi", "70", vendor, drinks));
        items.add(createItem("Honey Blend", "50", vendor, drinks));

        // Dessert
        StoreItemCategory dessert = new StoreItemCategory("Dessert", false);
        items.add(createItem("S'mores", "60", vendor, dessert));

        return items;
    }

    private static StoreItem createItem(String name, String price, Vendor vendor, StoreItemCategory category) {
        Product product = new Product(
                name.toLowerCase().replace(" ", "-"),
                null,
                name,
                new BigDecimal(price));

        return new StoreItem(product, vendor, category, 10);
    }
}
