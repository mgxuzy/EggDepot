package eggdepot.feature.inventory.gui.components;

import eggdepot.gui.components.H2;
import eggdepot.gui.components.Section;
import eggdepot.gui.plaf.Theme;

import javax.swing.border.EmptyBorder;
import java.awt.*;

public class InventoryCategorySection extends Section {
    public InventoryCategorySection(String title, InventoryItemList content) {
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(0, 0, Theme.SPACING_XL, 0));

        add(new H2(title), BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);
    }
}
