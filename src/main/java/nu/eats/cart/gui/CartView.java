package nu.eats.cart;

import nu.eats.common.messaging.EventBus;
import nu.eats.cart.components.ui.CartItemList;
import nu.eats.cart.components.ui.CartItemRow;
import nu.eats.cart.components.ui.CartSummary;
import nu.eats.gui.components.H1;
import nu.eats.gui.components.Section;
import nu.eats.gui.plaf.Theme;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

public class CartView extends Section {
    private final CartViewModel model;
    private final CartItemList cartItemList;

    private final Map<CartItem, CartItemRow> itemRowByItem = new LinkedHashMap<>();

    public CartView(CartViewModel model) {
        this.model = model;
        this.cartItemList = new CartItemList();

        var cartItems = model.cartItems();

        for (var cartItem : cartItems) {
            addItem(cartItem);
        }

        var cartSummary = new CartSummary();
        var checkoutActionButton = new JButton("Check Out");

        checkoutActionButton.setPreferredSize(new Dimension(0, 45));
        checkoutActionButton.getAccessibleContext().setAccessibleName("Proceed to Checkout");
        checkoutActionButton.addActionListener(_ -> model.checkOutCart());
        checkoutActionButton.setEnabled(false);

        var cartBottomBar = new JPanel(new BorderLayout());

        cartBottomBar.setOpaque(false);
        cartBottomBar.add(cartSummary, BorderLayout.NORTH);
        cartBottomBar.add(checkoutActionButton, BorderLayout.SOUTH);
        cartBottomBar.setBorder(new CompoundBorder(
                new MatteBorder(1, 0, 0, 0, Theme.COLOR_BORDER),
                new EmptyBorder(0, Theme.SPACING_XL, Theme.SPACING_XL, Theme.SPACING_XL)));

        var mainContent = new JPanel(new BorderLayout());

        mainContent.setOpaque(false);
        mainContent.add(new H1("Cart"), BorderLayout.NORTH);
        mainContent.add(cartItemList, BorderLayout.CENTER);
        mainContent.add(cartBottomBar, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(340, 0));
        add(mainContent, BorderLayout.CENTER);

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

    public void addItem(CartItem item) {
        if (itemRowByItem.containsKey(item)) {
            updateItem(item);

            return;
        }

        var row = new CartItemRow(item);

        row.subscribeToItemChange(model::updateCartItem);

        itemRowByItem.put(item, row);
        cartItemList.addRow(row);
    }

    public void removeItem(CartItem item) {
        var row = itemRowByItem.get(item);

        cartItemList.removeRow(itemRowByItem.remove(item));
    }

    public void clearItems(CartItem[] cartItems) {
        itemRowByItem.clear();
        cartItemList.clear();
    }

    private void updateItem(CartItem item) {
        var row = itemRowByItem.get(item);

        if (row == null) return;

        row.sync();

        if (item.quantity() <= 0) {
            model.removeFromCart(item);
        }
    }
}
