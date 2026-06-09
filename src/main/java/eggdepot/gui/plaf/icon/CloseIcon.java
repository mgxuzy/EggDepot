package eggdepot.gui.plaf.icon;

import javax.swing.*;
import java.awt.*;

public class CloseIcon extends TitleBarIcon {
    public CloseIcon() {
        super();
    }

    public CloseIcon(int size) {
        super(size);
    }

    public CloseIcon(int width, int height) {
        super(width, height);
    }

    @Override
    protected Color getColor(AbstractButton b) {
        if (b.getModel().isRollover())
            return Color.RED;
        return super.getColor(b);
    }

    @Override
    protected void paintShape(Graphics2D g2, int w, int h) {
        g2.setStroke(new BasicStroke(1.5f));
        int pad = w / 5;
        g2.drawLine(pad, pad, w - pad, h - pad);
        g2.drawLine(w - pad, pad, pad, h - pad);
    }
}
