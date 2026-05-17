package nu.eats.cart.gui.components;

import nu.eats.gui.components.FitPanel;
import nu.eats.gui.plaf.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CartItemList extends JPanel {
    private final JPanel listContent;

    public CartItemList() {
        super(new BorderLayout());

        setOpaque(false);

        listContent = new JPanel(new GridBagLayout());
        listContent.setOpaque(false);
        listContent.setBorder(new EmptyBorder(0, Theme.SPACING_XL, 0, Theme.SPACING_XL));

        var mainContent = new FitPanel(new BorderLayout());

        mainContent.setOpaque(false);
        mainContent.add(listContent, BorderLayout.NORTH);

        var scrollPane = new JScrollPane(mainContent);

        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPane, BorderLayout.NORTH);
    }

    public void addRow(CartItemRow row) {
        if (row == null)
            return;

        var gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;

        listContent.add(row, gbc);
    }

    public void removeRow(CartItemRow row) {
        listContent.remove(row);
    }

    public void clear() {
        listContent.removeAll();
    }
}
