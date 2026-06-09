package eggdepot.gui.plaf.icon;

import eggdepot.gui.plaf.Constants;
import eggdepot.gui.plaf.Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;

public class ChevronRightIcon implements Icon {
    private final int width;
    private final int height;

    public ChevronRightIcon() {
        this(12);
    }

    public ChevronRightIcon(int size) {
        this(size, size);
    }

    public ChevronRightIcon(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHints(Constants.DEFAULT_RENDERING_HINTS);
        g2.setColor(c != null ? c.getForeground() : Theme.COLOR_FG_INVERSE);
        g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        g2.translate(x, y);

        Path2D p = new Path2D.Float();
        float pad = width * 0.3f;

        // > shape
        p.moveTo(pad, height * 0.15f);
        p.lineTo(width - pad, height * 0.5f);
        p.lineTo(pad, height * 0.85f);

        g2.draw(p);
        g2.dispose();
    }

    @Override
    public int getIconWidth() {
        return width;
    }

    @Override
    public int getIconHeight() {
        return height;
    }
}
