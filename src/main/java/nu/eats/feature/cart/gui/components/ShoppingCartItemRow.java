package nu.eats.feature.cart.gui.components;

import nu.eats.common.messaging.Subscription;
import nu.eats.feature.cart.model.ShoppingCartItem;
import nu.eats.gui.plaf.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class ShoppingCartItemRow extends JPanel {
    private final SpinnerNumberModel spinnerNumberModel;
    private final ShoppingCartItem item;
    private final List<Consumer<ShoppingCartItem>> subscribers = new CopyOnWriteArrayList<>();

    public ShoppingCartItemRow(ShoppingCartItem item) {
        this.item = item;

        setLayout(new BorderLayout(Theme.SPACING_SM, 0));
        setOpaque(false);
        setBorder(new EmptyBorder(0, 0, Theme.SPACING_2XL, 0));

        var catalogItem = item.catalogItem();

        spinnerNumberModel = new SpinnerNumberModel(item.quantity(), 0, catalogItem.availableStock(), 1);

        spinnerNumberModel.addChangeListener(_ -> {
            item.setQuantity(spinnerNumberModel.getNumber().intValue());

            for (var subscriber : subscribers) {
                subscriber.accept(item);
            }
        });

        add(new ShoppingCartItemSummary(catalogItem.product()), BorderLayout.CENTER);
        add(new JSpinner(spinnerNumberModel), BorderLayout.EAST);
    }

    public Subscription subscribeToItemChange(Consumer<ShoppingCartItem> subscriber) {
        subscribers.add(subscriber);

        return () -> subscribers.remove(subscriber);
    }

    public void sync() {
        spinnerNumberModel.setValue(item.quantity());
    }
}
