package nu.eats.gui.store.components;

import nu.eats.gui.components.WrapLayout;
import nu.eats.gui.plaf.Theme;

import javax.swing.*;

public class StoreItemList extends JPanel {
    public StoreItemList() {
        super(new WrapLayout(WrapLayout.CENTER, Theme.SPACING_XL, Theme.SPACING_XL));

        setOpaque(false);
    }

    public void addRow(StoreItemRow row) {
        add(row);
    }

    public void clear() {
        removeAll();
    }
}
