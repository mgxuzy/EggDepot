package nu.eats.gui.plaf.scroll;

import nu.eats.gui.plaf.Theme;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

import static nu.eats.gui.plaf.Constants.DEFAULT_RENDERING_HINTS;

public class ScrollBarUI extends BasicScrollBarUI {
    private static final Dimension ZERO_DIMENSION = new Dimension(0, 0);

    @SuppressWarnings("UnusedDeclaration")
    public static ComponentUI createUI(JComponent component) {
        return new ScrollBarUI();
    }

    @Override
    protected void configureScrollBarColors() {
        thumbColor = Theme.COLOR_THUMB;
        trackColor = new Color(0, 0, 0, 0);
    }

    @Override
    protected void installDefaults() {
        super.installDefaults();

        scrollbar.setOpaque(false);
        scrollbar.setBorder(null);
    }

    @Override
    public Dimension getPreferredSize(JComponent component) {
        if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
            return new Dimension(SCROLLBAR_WIDTH, super.getPreferredSize(component).height);
        }

        return new Dimension(super.getPreferredSize(component).width, SCROLLBAR_WIDTH);
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return createInvisibleButton();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return createInvisibleButton();
    }

    private JButton createInvisibleButton() {
        var invisibleButton = new JButton();

        invisibleButton.setPreferredSize(ZERO_DIMENSION);
        invisibleButton.setMinimumSize(ZERO_DIMENSION);
        invisibleButton.setMaximumSize(ZERO_DIMENSION);

        return invisibleButton;
    }

    @Override
    public void paint(Graphics graphics, JComponent component) {
        Graphics2D graphics2D = (Graphics2D) graphics;

        graphics2D.setRenderingHints(DEFAULT_RENDERING_HINTS);

        super.paint(graphics2D, component);
    }

    @Override
    protected void paintThumb(Graphics graphics, JComponent component, Rectangle thumbBounds) {
        if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
            return;
        }

        graphics.setColor(thumbColor);

        if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
            int yPosition = thumbBounds.y + THUMB_PADDING;
            int thumbHeight = Math.max(thumbBounds.height - (THUMB_PADDING * 2), MIN_THUMB_SIZE);

            graphics.fillRoundRect(OVERLAY_OFFSET, yPosition, THUMB_WIDTH, thumbHeight, THUMB_WIDTH, THUMB_WIDTH);
        } else {
            int xPosition = thumbBounds.x + THUMB_PADDING;
            int thumbWidth = Math.max(thumbBounds.width - (THUMB_PADDING * 2), MIN_THUMB_SIZE);

            graphics.fillRoundRect(xPosition, OVERLAY_OFFSET, thumbWidth, THUMB_WIDTH, THUMB_WIDTH, THUMB_WIDTH);
        }
    }

    @Override
    protected void paintTrack(Graphics graphics, JComponent component, Rectangle trackBounds) {
        // no paint track
    }

    private static final int THUMB_WIDTH = 4,
            THUMB_PADDING = 2,
            OVERLAY_OFFSET = 2, // Distance from the edge
            SCROLLBAR_WIDTH = THUMB_WIDTH + (OVERLAY_OFFSET * 2), // Total width needed
            MIN_THUMB_SIZE = 5;


}