package eggdepot.gui.components;

import javax.swing.*;
import java.awt.*;

/**
 * A {@link FlowLayout} subclass that fully supports wrapping of components.
 * <p>
 * Unlike {@code FlowLayout}, the preferred height grows as components wrap
 * onto new rows, making it suitable for use inside scroll panes and
 * resizable containers.
 * <p>
 * See reference implementation <a href="https://github.com/tips4java/tips4java/blob/main/source/WrapLayout.java">WrapLayout.java</a>
 */
public class WrapLayout extends FlowLayout {

    public WrapLayout() {
        super();
    }

    public WrapLayout(int align) {
        super(align);
    }

    public WrapLayout(int align, int horizontalGap, int verticalGap) {
        super(align, horizontalGap, verticalGap);
    }

    @Override
    public Dimension preferredLayoutSize(Container target) {
        return layoutSize(target, true);
    }

    @Override
    public Dimension minimumLayoutSize(Container target) {
        Dimension minimum = layoutSize(target, false);

        minimum.width -= (getHgap() + 1);

        return minimum;
    }

    private Dimension layoutSize(Container target, boolean preferred) {
        synchronized (target.getTreeLock()) {
            int targetWidth = resolveTargetWidth(target);
            int horizontalGap = getHgap();
            int verticalGap = getVgap();

            Insets insets = target.getInsets();

            int horizontalInsetsAndGap = insets.left + insets.right + (horizontalGap * 2);
            int maxWidth = targetWidth - horizontalInsetsAndGap;

            Dimension dimension = measureRows(target, maxWidth, horizontalGap, preferred);

            dimension.width += horizontalInsetsAndGap;
            dimension.height += insets.top + insets.bottom + verticalGap * 2;

            // Match the container's width so the layout doesn't force horizontal expansion
            if (preferred && targetWidth != Integer.MAX_VALUE) {
                // dimension.width -= horizontalInsetsAndGap;
            }

            // Shrink slightly inside a scroll pane so the container can still shrink
            if (SwingUtilities.getAncestorOfClass(JScrollPane.class, target) != null) {
                dimension.width -= (horizontalGap + 1);
            }

            return dimension;
        }
    }

    /**
     * Determines the available width by walking up the container hierarchy.
     * Prefers a {@link JViewport} ancestor width when present (scroll pane),
     * otherwise finds the nearest parent with a non-zero width.
     */
    private int resolveTargetWidth(Container target) {
        JViewport viewport = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, target);

        if (viewport != null) {
            return viewport.getWidth();
        }

        Container container = target;

        while (container.getSize().width == 0) {
            Container parent = container.getParent();

            if (parent == null) {
                break;
            }

            container = parent;
        }

        int width = container.getSize().width;

        return width > 0 ? width : Integer.MAX_VALUE;
    }

    /**
     * Measures all visible components, wrapping onto new rows when a component
     * would exceed {@code maxWidth}.
     */
    private Dimension measureRows(Container target, int maxWidth, int hGap, boolean preferred) {
        Dimension dimension = new Dimension(0, 0);

        int rowWidth = 0;
        int rowHeight = 0;

        for (int index = 0, count = target.getComponentCount(); index < count; index++) {
            Component component = target.getComponent(index);

            if (!component.isVisible()) {
                continue;
            }

            Dimension componentSize = preferred ? component.getPreferredSize() : component.getMinimumSize();

            if (rowWidth + componentSize.width > maxWidth) {
                addRow(dimension, rowWidth, rowHeight);

                rowWidth = 0;
                rowHeight = 0;
            }

            if (rowWidth != 0) {
                rowWidth += hGap;
            }

            rowWidth += componentSize.width;
            rowHeight = Math.max(rowHeight, componentSize.height);
        }

        addRow(dimension, rowWidth, rowHeight);

        return dimension;
    }

    /**
     * Accumulates a completed row's dimensions into the running total.
     */
    private void addRow(Dimension size, int rowWidth, int rowHeight) {
        size.width = Math.max(size.width, rowWidth);

        if (size.height > 0) {
            size.height += getVgap();
        }

        size.height += rowHeight;
    }
}