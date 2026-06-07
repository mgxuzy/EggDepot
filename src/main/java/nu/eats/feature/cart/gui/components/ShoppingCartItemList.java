package nu.eats.feature.cart.gui.components;

import nu.eats.gui.components.FitPanel;
import nu.eats.gui.plaf.border.FramedBorder;

import javax.swing.*;
import java.awt.*;

import static nu.eats.gui.plaf.Theme.*;


public class ShoppingCartItemList extends JPanel {
    private final JPanel listContent;

    public ShoppingCartItemList() {
        super(new BorderLayout());

        setOpaque(false);

        this.listContent = new JPanel(new GridBagLayout());
        this.listContent.setOpaque(false);
        this.listContent.setBorder(new FramedBorder.Builder()
                .sides.horizontal(side -> side.padding(SPACING_4XL))
                .build()
        );

        var contentPanel = new FitPanel(new BorderLayout());

        contentPanel.setOpaque(false);
        contentPanel.add(this.listContent, BorderLayout.NORTH);

        var scrollPane = new JScrollPane(contentPanel);

        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        this.add(scrollPane, BorderLayout.CENTER);
    }

    public void addRow(ShoppingCartItemRow row) {
        var constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.weightx = 1.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.NORTH;

        this.listContent.add(row, constraints);
    }

    public void removeRow(ShoppingCartItemRow row) {
        this.listContent.remove(row);
    }

    public void clear() {
        this.listContent.removeAll();
    }
}
