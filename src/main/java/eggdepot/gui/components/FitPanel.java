package eggdepot.gui.components;

import javax.swing.*;
import java.awt.*;

/**
 * A panel that fits its width to the parent viewport.
 * <p>
 * Use as the view inside a JScrollPane to constrain content width
 * to the viewport. Content wider than the viewport will be clipped
 * (e.g. JLabels truncate with "..."). Vertical scrolling is allowed.
 */
public class FitPanel extends JPanel implements Scrollable {
    public FitPanel(LayoutManager layout) {
        super(layout);
    }

    public FitPanel() {
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visible, int orientation, int direction) {
        return 16;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visible, int orientation, int direction) {
        return orientation == SwingConstants.VERTICAL ? visible.height : visible.width;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
}
