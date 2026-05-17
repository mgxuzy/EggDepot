package nu.eats.gui.store.components;

import nu.eats.gui.components.Section;
import nu.eats.gui.plaf.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class StoreCategorySection extends Section {
    public StoreCategorySection(String title, StoreItemList content) {
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(0, 0, Theme.SPACING_XL, 0));

        var label = new JLabel(title);

        label.setFont(Theme.FONT_BOLD_24);
        label.setForeground(Theme.COLOR_FG);
        label.setBorder(new EmptyBorder(Theme.SPACING_SM, Theme.SPACING_XL, Theme.SPACING_SM, Theme.SPACING_XL));

        add(label, BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);
    }
}
