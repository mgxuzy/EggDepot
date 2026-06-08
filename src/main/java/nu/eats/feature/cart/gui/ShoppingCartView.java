package nu.eats.feature.cart.gui;

import nu.eats.common.messaging.EventBus;
import nu.eats.feature.cart.gui.components.ShoppingCartItemList;
import nu.eats.feature.cart.gui.components.ShoppingCartItemRow;
import nu.eats.feature.cart.gui.components.ShoppingCartSummary;
import nu.eats.feature.cart.model.ShoppingCartItem;
import nu.eats.feature.cart.state.CartState;
import nu.eats.feature.cart.state.CartViewModel;
import nu.eats.gui.components.H2;
import nu.eats.gui.components.Section;
import nu.eats.gui.plaf.Theme;
import nu.eats.gui.plaf.border.framed.FramedBorder;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import static nu.eats.gui.plaf.Theme.SPACING_4XL;
import static nu.eats.gui.plaf.Theme.SPACING_XL;

public class ShoppingCartView extends Section {
    private final CartViewModel model;
    private final ShoppingCartItemList itemList;

    private final Map<ShoppingCartItem, ShoppingCartItemRow> itemRowByItem = new LinkedHashMap<>();

    public ShoppingCartView(CartViewModel model) {
        this.model = model;
        this.itemList = new ShoppingCartItemList();

        var cartItems = model.cartItems();

        for (var cartItem : cartItems) {
            addItem(cartItem);
        }

        var cartSummary = new ShoppingCartSummary();
        var checkoutActionButton = new JButton("Check Out");

        checkoutActionButton.setPreferredSize(new Dimension(0, 45));
        checkoutActionButton.getAccessibleContext().setAccessibleName("Proceed to Checkout");
        checkoutActionButton.addActionListener(_ -> model.checkOutCart());
        checkoutActionButton.setEnabled(false);

        var cartBottomBar = new JPanel(new BorderLayout(SPACING_4XL, SPACING_XL));

        cartBottomBar.setOpaque(false);
        cartBottomBar.add(cartSummary, BorderLayout.NORTH);
        cartBottomBar.add(checkoutActionButton, BorderLayout.SOUTH);
        cartBottomBar.setBorder(new FramedBorder.Builder()
                .sides(side -> side.padding(SPACING_4XL))
                .edges.top(edge -> edge
                        .thickness(1)
                        .color(Theme.COLOR_BORDER)
                )
                .build()
        );

        var contentPanel = new JPanel(new BorderLayout());

        contentPanel.setOpaque(false);
        contentPanel.add(new H2("Order"), BorderLayout.NORTH);
        contentPanel.add(itemList, BorderLayout.CENTER);
        contentPanel.add(cartBottomBar, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(360, 20));
        add(contentPanel, BorderLayout.CENTER);

        var eventBus = EventBus.mainBus();

        // --- Cart ---
        eventBus.subscribe(CartState.ITEM_ADDED, this::addItem);
        eventBus.subscribe(CartState.ITEM_REMOVED, this::removeItem);
        eventBus.subscribe(CartState.CHECKED_OUT, this::clearItems);
        eventBus.subscribe(CartState.ITEM_UPDATED, this::updateItem);

        eventBus.subscribe(CartState.SUBTOTAL_CHANGED, total -> checkoutActionButton.setEnabled(!total.equals(BigDecimal.ZERO)));

        eventBus.subscribe(CartState.ERROR,
                message -> JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE));
    }

    public void addItem(ShoppingCartItem item) {
        if (itemRowByItem.containsKey(item)) {
            updateItem(item);

            return;
        }

        var row = new ShoppingCartItemRow(item);

        row.subscribeToItemChange(model::updateCartItem);

        itemRowByItem.put(item, row);
        itemList.addRow(row);
    }

    public void removeItem(ShoppingCartItem item) {
        itemList.removeRow(itemRowByItem.remove(item));
    }

    public void clearItems(ShoppingCartItem[] cartItems) {
        itemRowByItem.clear();
        itemList.clear();
    }

    private void updateItem(ShoppingCartItem item) {
        var row = itemRowByItem.get(item);

        if (row == null) return;

        row.sync();

        if (item.quantity() <= 0) {
            model.removeFromCart(item);
        }
    }
}
