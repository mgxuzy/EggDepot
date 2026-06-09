package eggdepot.gui.plaf.icon;

import java.awt.*;

public class MinimizeIcon extends TitleBarIcon {
    public MinimizeIcon() {
        super();
    }

    public MinimizeIcon(int size) {
        super(size);
    }

    public MinimizeIcon(int width, int height) {
        super(width, height);
    }

    @Override
    protected void paintShape(Graphics2D g2, int w, int h) {
        g2.setStroke(new BasicStroke(1.5f));

        int pad = w / 5;

        g2.drawLine(pad, h - pad, w - pad, h - pad);
    }
}
