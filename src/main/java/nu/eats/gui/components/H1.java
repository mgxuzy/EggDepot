package nu.eats.gui.components;

import nu.eats.gui.plaf.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class H1 extends JLabel {
    public H1(String text) {
        super(text);

        setOpaque(false);
        setBorder(new EmptyBorder(Theme.SPACING_XL, Theme.SPACING_XL, 20, Theme.SPACING_XL));

        setFont(Theme.FONT_BOLD_24);
        setForeground(Theme.COLOR_FG);
    }
}
