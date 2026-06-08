package nu.eats.gui.components;

import nu.eats.gui.plaf.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class H2 extends JLabel {
    public H2(String text) {
        super(text);

        setOpaque(false);
        setBorder(new EmptyBorder(Theme.SPACING_4XL, Theme.SPACING_4XL, Theme.SPACING_4XL, Theme.SPACING_4XL));

        setFont(Theme.FONT_BOLD_LG);
        setForeground(Theme.COLOR_FG_PRIMARY);
    }
}
