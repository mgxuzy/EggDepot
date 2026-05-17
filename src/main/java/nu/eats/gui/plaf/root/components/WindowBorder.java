package nu.eats.gui.plaf.root.components;

import nu.eats.gui.plaf.Theme;

import javax.swing.border.Border;
import javax.swing.plaf.UIResource;
import java.awt.*;

public class WindowBorder implements Border, UIResource {
    private static final int THICKNESS = 0;

    @Override
    public void paintBorder(Component c, Graphics graphics, int x, int y, int width, int height) {
        graphics.setColor(Theme.COLOR_BORDER);
        graphics.drawRect(x, y, width - 1, height - 1);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(THICKNESS, THICKNESS, THICKNESS, THICKNESS);
    }

    @Override
    public boolean isBorderOpaque() {
        return true;
    }
}