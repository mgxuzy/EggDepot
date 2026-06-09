package eggdepot.gui.plaf.icon;

import java.awt.*;

public class MaximizeIcon extends TitleBarIcon {
    public MaximizeIcon() {
        super();
    }

    public MaximizeIcon(int size) {
        super(size);
    }

    public MaximizeIcon(int width, int height) {
        super(width, height);
    }

    @Override
    protected void paintShape(Graphics2D g2, int w, int h) {
        g2.setStroke(new BasicStroke(1.5f));

        int pad = w / 5;

        g2.drawRect(pad, pad, w - 2 * pad, h - 2 * pad);
    }
}
