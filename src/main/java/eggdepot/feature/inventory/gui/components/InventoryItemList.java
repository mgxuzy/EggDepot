package eggdepot.feature.inventory.gui.components;

import eggdepot.gui.components.WrapLayout;
import eggdepot.gui.plaf.Theme;

import javax.swing.*;

public class InventoryItemList extends JPanel {
    public InventoryItemList() {
        super(new WrapLayout(WrapLayout.CENTER, Theme.SPACING_4XL, Theme.SPACING_4XL));

        setOpaque(false);
    }

    public void addRow(InventoryItemRow row) {
        add(row);
    }

    public void clear() {
        removeAll();
    }
}
