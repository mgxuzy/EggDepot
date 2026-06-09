package eggdepot.gui.plaf.spinner;

import eggdepot.gui.plaf.button.ButtonState;
import eggdepot.gui.plaf.button.ButtonVariant;

import javax.swing.plaf.basic.BasicArrowButton;
import java.awt.*;
import java.awt.geom.Line2D;

import static eggdepot.gui.plaf.Constants.DEFAULT_RENDERING_HINTS;

public class SpinButton extends BasicArrowButton {
    private static final int SIZE = 24;

    public SpinButton(int direction) {
        super(direction, null, null, null, null);

        setPreferredSize(new Dimension(SIZE, SIZE));
        setMinimumSize(new Dimension(SIZE, SIZE));
        setMaximumSize(new Dimension(SIZE, SIZE));

        setFocusable(false);

        ButtonVariant.TERTIARY.install(this, ButtonState.NEUTRAL);

        String name = (direction == NORTH) ? "Increase Quantity" : "Decrease Quantity";
        getAccessibleContext().setAccessibleName(name);
    }

    @Override
    public void paint(Graphics graphics) {
        var graphics2D = (Graphics2D) graphics.create();

        graphics2D.setRenderingHints(DEFAULT_RENDERING_HINTS);

        try {
            int width = getWidth();
            int height = getHeight();

            // graphics2D.fillRect(0, 0, getWidth(), getHeight());

            // Icon Drawing
            graphics2D.setColor(getForeground()); // Or Color.BLACK

            double centerX = width / 2.0;
            double centerY = height / 2.0;
            double radius = 4.0; // Half-length of the line (Total length 8px)

            graphics2D.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            // Minus
            graphics2D.draw(new Line2D.Double(centerX - radius, centerY, centerX + radius, centerY));

            // Plus
            if (direction == NORTH) {
                graphics2D.draw(new Line2D.Double(centerX, centerY - radius, centerX, centerY + radius));
            }
        } finally {
            graphics2D.dispose();
        }
    }
}
