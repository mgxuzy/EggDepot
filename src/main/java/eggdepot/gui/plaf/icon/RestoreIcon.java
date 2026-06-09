package eggdepot.gui.plaf.icon;

import javax.swing.*;
import java.awt.*;

public class RestoreIcon extends TitleBarIcon {
    public RestoreIcon() {
        super();
    }

    public RestoreIcon(int size) {
        super(size);
    }

    public RestoreIcon(int width, int height) {
        super(width, height);
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);

        AbstractButton b = (AbstractButton) c;

        // Use smaller padding for RestoreIcon to allow 2 squares to fit comfortably
        int pad = Math.max(1, width / 8);
        g2.translate(x + pad, y + pad);
        g2.setStroke(new BasicStroke(1f));

        int availW = width - 2 * pad;
        int availH = height - 2 * pad;

        int offset = Math.max(2, (int) (availW * 0.25)); // Offset relative to available space
        int rectW = availW - offset;
        int rectH = availH - offset;

        // Background rect (top right)
        g2.setColor(getColor(b));
        g2.drawRect(offset, 0, rectW, rectH);

        g2.setColor(getColor(b));
        g2.drawRect(0, offset, rectW, rectH);
        g2.dispose();
    }

    @Override
    protected void paintShape(Graphics2D g2, int w, int h) {
        // No-op
    }
}
