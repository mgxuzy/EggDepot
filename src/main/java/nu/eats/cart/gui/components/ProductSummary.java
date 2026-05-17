package nu.eats.cart.gui.components;

import nu.eats.inventory.model.Product;
import nu.eats.gui.plaf.Theme;

import javax.swing.*;

/**
 * Displays a product's name and price in a vertical stack.
 */
public class ProductSummary extends JPanel {
    public ProductSummary(Product product) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);

        var nameLabel = new JLabel(product.name());
        nameLabel.setFont(Theme.FONT_MEDIUM_BASE);
        nameLabel.setAlignmentX(LEFT_ALIGNMENT);

        var priceLabel = new JLabel(String.format("₱%.2f", product.price()));
        priceLabel.setFont(Theme.FONT_REGULAR_14);
        priceLabel.setForeground(Theme.COLOR_FG_MUTED);
        priceLabel.setAlignmentX(LEFT_ALIGNMENT);

        add(nameLabel);
        add(Box.createVerticalStrut(4));
        add(priceLabel);
    }
}
