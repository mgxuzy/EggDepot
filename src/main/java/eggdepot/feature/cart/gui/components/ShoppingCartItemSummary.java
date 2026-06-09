package eggdepot.feature.cart.gui.components;

import eggdepot.feature.inventory.model.Product;
import eggdepot.gui.plaf.Theme;

import javax.swing.*;

/**
 * Displays a product's name and price in a vertical stack.
 */
public class ShoppingCartItemSummary extends JPanel {
    public ShoppingCartItemSummary(Product product) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        var nameLabel = new JLabel(product.name());

        nameLabel.setFont(Theme.FONT_MEDIUM_MD);
        nameLabel.setAlignmentX(LEFT_ALIGNMENT);

        var priceLabel = new JLabel(String.format("₱%.2f", product.price()));

        priceLabel.setFont(Theme.FONT_REGULAR_SM);
        priceLabel.setForeground(Theme.COLOR_FG_SECONDARY);
        priceLabel.setAlignmentX(LEFT_ALIGNMENT);

        add(nameLabel);
        add(Box.createVerticalStrut(4));
        add(priceLabel);
    }
}
