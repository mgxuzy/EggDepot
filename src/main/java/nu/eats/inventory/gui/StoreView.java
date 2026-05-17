package nu.eats.gui.store;

import nu.eats.common.messaging.EventBus;
import nu.eats.domain.store.StoreItem;
import nu.eats.domain.store.StoreItemCategory;
import nu.eats.gui.components.Section;
import nu.eats.gui.components.chip.ChipBar;
import nu.eats.gui.plaf.Theme;
import nu.eats.gui.store.components.StoreCategorySection;
import nu.eats.gui.store.components.StoreItemList;
import nu.eats.gui.store.components.StoreItemRow;
import nu.eats.cart.CartState;
import nu.eats.ui.store.StoreState;
import nu.eats.ui.store.StoreViewModel;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class StoreView extends Section {
    private final JPanel contentPanel;
    private final JScrollPane scrollPane;
    private final ChipBar chipBar;
    private final StoreViewModel model;

    private final Map<StoreItemCategory, StoreItemList> categoryLists = new LinkedHashMap<>();
    private final Map<StoreItemCategory, StoreCategorySection> categorySections = new LinkedHashMap<>();

    public StoreView(StoreViewModel model) {
        this.model = model;

        // Content
        this.contentPanel = new JPanel();

        this.contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        this.contentPanel.setOpaque(false);

        // Chip Bar
        this.chipBar = new ChipBar();
        this.chipBar.setOnCategorySelected(this::onCategorySelected);

        // Header
        var headerPanel = new JPanel();

        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);
        headerPanel.add(Box.createVerticalStrut(Theme.SPACING_MD));
        headerPanel.add(chipBar);
        headerPanel.add(Box.createVerticalStrut(Theme.SPACING_MD));

        // Scroll
        this.scrollPane = new JScrollPane(contentPanel);

        // Revalidate content when the viewport resizes so WrapLayout gets the correct
        // width

        this.scrollPane.getViewport().addChangeListener(_ -> contentPanel.revalidate());

        // Layout
        var mainContainer = new JPanel(new BorderLayout());

        mainContainer.setOpaque(false);
        mainContainer.add(headerPanel, BorderLayout.NORTH);
        mainContainer.add(this.scrollPane, BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(mainContainer, BorderLayout.CENTER);

        // Populate
        Map<StoreItemCategory, List<StoreItem>> groupedItems = model.getItemsByCategory();

        for (var entry : groupedItems.entrySet()) {
            for (StoreItem item : entry.getValue()) {
                addItem(item);
            }
        }

        EventBus.mainBus().subscribe(StoreState.ITEM_ADDED, this::addItem);
    }

    private void onCategorySelected(String categoryName) {
        if ("All".equals(categoryName)) {
            for (StoreCategorySection section : categorySections.values()) {
                section.setVisible(true);
            }
        } else {
            for (var entry : categorySections.entrySet()) {
                entry.getValue().setVisible(entry.getKey().name().equals(categoryName));
            }
        }

        SwingUtilities.invokeLater(() -> scrollPane.getViewport().setViewPosition(new Point(0, 0)));
    }

    public void addItem(StoreItem item) {
        StoreItemCategory category = item.category();

        StoreItemList list = categoryLists.computeIfAbsent(category, itemCategory -> {
            StoreItemList newList = new StoreItemList();
            StoreCategorySection section = new StoreCategorySection(itemCategory.name(), newList);

            contentPanel.add(section);
            categorySections.put(itemCategory, section);

            chipBar.addChip(itemCategory.name());

            contentPanel.revalidate();

            return newList;
        });

        var row = new StoreItemRow(item.product());

        row.setEnabled(item.availableStock() > 0);
        row.setPreferredSize(new Dimension(160, 210));
        row.addActionListener(_ -> model.selectMenuItem(item));

        EventBus.mainBus().subscribe(CartState.ITEM_UPDATED, cartItem -> {
            if (!Objects.equals(item.product().id(), cartItem.catalogItem().product().id()))
                return;
            row.setEnabled(item.availableStock() - cartItem.quantity() > 0);
        });

        list.addRow(row);
    }

}