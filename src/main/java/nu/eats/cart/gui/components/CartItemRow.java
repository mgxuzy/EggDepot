package nu.eats.cart.gui.components;

import nu.eats.common.messaging.Subscription;
import nu.eats.cart.model.CartItem;
import nu.eats.gui.plaf.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class CartItemRow extends JPanel {
    private final SpinnerNumberModel spinnerNumberModel;
    private final CartItem item;
    private final List<Consumer<CartItem>> subscribers = new CopyOnWriteArrayList<>();

    public CartItemRow(CartItem item) {
        this.item = item;

        setLayout(new BorderLayout(Theme.SPACING_SM, 0));
        setOpaque(false);
        setBorder(new EmptyBorder(0, 0, Theme.SPACING_MD, 0));

        var catalogItem = item.catalogItem();

        spinnerNumberModel = new SpinnerNumberModel(item.quantity(), 0, catalogItem.availableStock(), 1);

        spinnerNumberModel.addChangeListener(_ -> {
            item.setQuantity(spinnerNumberModel.getNumber().intValue());

            for (var subscriber : subscribers) {
                subscriber.accept(item);
            }
        });

        add(new ProductSummary(catalogItem.product()), BorderLayout.CENTER);
        add(new JSpinner(spinnerNumberModel), BorderLayout.EAST);
    }

    public Subscription subscribeToItemChange(Consumer<CartItem> subscriber) {
        subscribers.add(subscriber);

        return () -> subscribers.remove(subscriber);
    }

    public void sync() {
        spinnerNumberModel.setValue(item.quantity());
    }
}
