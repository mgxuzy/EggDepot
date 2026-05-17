package nu.eats.gui.components;

import nu.eats.gui.plaf.Theme;
import nu.eats.gui.plaf.border.BoxDecoration;
import nu.eats.gui.plaf.border.FramedBorder;

import javax.swing.*;
import java.awt.*;

import static nu.eats.gui.plaf.Constants.DEFAULT_RENDERING_HINTS;
import static nu.eats.gui.plaf.Constants.KEY_BOX_DECORATION;

public class Section extends JPanel {
    public Section() {
        this.setOpaque(false);
        this.setBackground(Theme.COLOR_BG);
        this.setBorder(new FramedBorder.Builder()
                .corners((corner) -> corner.radius(12))
                .build()
        );
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        var graphics2D = (Graphics2D) graphics.create();

        graphics2D.setRenderingHints(DEFAULT_RENDERING_HINTS);

        try {
            if (this.getBorder() instanceof FramedBorder border) {
                border.paintClientRegion(graphics2D, this);
            }

            super.paintComponent(graphics2D);
        } finally {
            graphics2D.dispose();
        }
    }
}
