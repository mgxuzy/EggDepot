package nu.eats.gui;

import nu.eats.common.messaging.EventBus;
import nu.eats.common.resources.Images;
import nu.eats.domain.Vendor;
import nu.eats.domain.cart.Cart;
import nu.eats.domain.cart.CartItem;
import nu.eats.domain.store.Store;
import nu.eats.domain.store.StoreItem;
import nu.eats.gui.cart.CartView;
import nu.eats.gui.components.Section;
import nu.eats.gui.plaf.Theme;
import nu.eats.gui.store.StoreView;
import nu.eats.gui.vendor.VendorList;
import nu.eats.gui.vendor.VendorRow;
import nu.eats.ui.cart.CartViewModel;
import nu.eats.ui.store.StoreState;
import nu.eats.ui.store.StoreViewModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainView extends JPanel {
    private final Map<Vendor, StoreView> storeViewByVendor = new HashMap<>();
    private final JPanel mainContent;
    private final CartViewModel cartViewModel;
    private final VendorList vendorList;

    public MainView() {
        setLayout(new BorderLayout(Theme.SPACING_XS, 0));
        setBackground(Theme.COLOR_PRIMARY);
        setBorder(new EmptyBorder(0, Theme.SPACING_XS, Theme.SPACING_XS, Theme.SPACING_XS));
        setMinimumSize(new Dimension(600, 400));

        this.vendorList = new VendorList();
        this.mainContent = new JPanel(new CardLayout());
        this.mainContent.setOpaque(false);

        this.cartViewModel = new CartViewModel(new Cart());

        CartView cartView = new CartView(cartViewModel);

        var leftPanel = new Section();

        leftPanel.setLayout(new BorderLayout());

        var logoDisplay = new JLabel(Images.loadIcon("NU-Logo.png", 96));

        logoDisplay.setBorder(new EmptyBorder(Theme.SPACING_LG, Theme.SPACING_LG, Theme.SPACING_MD, Theme.SPACING_LG));

        leftPanel.add(logoDisplay, BorderLayout.NORTH);
        leftPanel.add(vendorList, BorderLayout.CENTER);

        add(leftPanel, BorderLayout.WEST);
        add(mainContent, BorderLayout.CENTER);
        add(cartView, BorderLayout.EAST);

        initTestData();

        var eventBus = EventBus.mainBus();

        eventBus.subscribe(StoreState.ITEM_SELECTED, item -> {
            if (item instanceof StoreItem storeItem) {
                cartViewModel.addToCart(new CartItem(storeItem));
            }
        });
    }

    private void initTestData() {
        Vendor[] vendors = {
                new Vendor("hustlers", null, "Hustler's Cup"),
                new Vendor("quickbites", null, "Quick Bites Corner"),
                new Vendor("cians", null, "Cian's Diner"),
                new Vendor("nubaliwag", null, "NU Baliwag Combo")
        };

        for (int index = 0; index < vendors.length; index++) {
            Vendor vendor = vendors[index];
            StoreView storeView = createStoreViewForVendor(vendor);

            addVendor(vendor, storeView);
            mainContent.add(storeView, vendor.id());

            if (index == 0) {
                setVendor(vendor);
            }
        }
    }

    private StoreView createStoreViewForVendor(Vendor vendor) {
        Store store = new Store();
        List<StoreItem> items = switch (vendor.id()) {
            case "hustlers" -> TestData.getCoffeeShopMenu(vendor);
            case "quickbites" -> TestData.getQuickBitesMenu(vendor);
            case "cians" -> TestData.getCiansDinerMenu(vendor);
            case "nubaliwag" -> TestData.getNUBaliwagComboMenu(vendor);
            default -> Collections.emptyList();
        };

        for (StoreItem item : items) {
            store.add(item);
        }

        return new StoreView(new StoreViewModel(store));
    }

    public void setVendor(Vendor vendor) {
        CardLayout layout = (CardLayout) mainContent.getLayout();

        layout.show(mainContent, vendor.id());
    }

    private void addVendor(Vendor vendor, StoreView storeView) {
        storeViewByVendor.put(vendor, storeView);

        VendorRow row = new VendorRow(vendor.name());

        row.setActionCommand(vendor.name());

        row.addItemListener(event -> {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                setVendor(vendor);
            }
        });

        if (storeViewByVendor.size() == 1) {
            row.setSelected(true);
        }

        vendorList.addRow(row);
    }
}
