package eggdepot.gui.plaf.icon;

import javax.swing.*;
import java.awt.*;

public abstract class TitleBarIcon implements Icon {
    protected final int width;
    protected final int height;

    public TitleBarIcon() {
        this(12, 12);
    }

    public TitleBarIcon(int size) {
        this(size, size);
    }

    public TitleBarIcon(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public int getIconWidth() {
        return width;
    }

    @Override
    public int getIconHeight() {
        return height;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);

        AbstractButton b = (AbstractButton) c;
        g2.setColor(getColor(b));

        g2.translate(x, y);
        paintShape(g2, width, height);
        g2.dispose();
    }

    protected Color getColor(AbstractButton b) {
        return b.getForeground();
    }

    protected abstract void paintShape(Graphics2D g2, int w, int h);
}
