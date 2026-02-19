package nu.eats.gui.components;

import javax.swing.*;
import java.awt.*;

/**
 * A {@link FlowLayout} subclass where the preferred width is determined by
 * the widest row of child components rather than the parent container.
 * <p>
 * Components are laid out left-to-right. A new row is started when the
 * <em>parent's</em> available width is narrower than the current row width,
 * allowing the container to shrink and force wrapping without ever
 * stretching to fill the parent.
 */
public class FitLayout extends FlowLayout {

    public FitLayout() {
        super();
    }

    public FitLayout(int align) {
        super(align);
    }

    public FitLayout(int align, int horizontalGap, int verticalGap) {
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
            int hGap = getHgap();
            int vGap = getVgap();
            Insets insets = target.getInsets();

            int horizontalInsetsAndGap = insets.left + insets.right + (hGap * 2);

            // The wrap boundary is the parent's width, not our own preferred width.
            // This lets the parent squeeze us and force wrapping.
            int parentWidth = resolveParentWidth(target);
            int maxWidth = parentWidth - horizontalInsetsAndGap;

            Dimension dimension = measureRows(target, maxWidth, hGap, preferred);

            // Width is driven by children (widest row), not the parent.
            dimension.width += horizontalInsetsAndGap;
            dimension.height += insets.top + insets.bottom + vGap * 2;

            return dimension;
        }
    }

    /**
     * Returns the available width of the nearest <em>parent</em> container,
     * preferring a {@link JViewport} when inside a scroll pane.
     * Falls back to {@link Integer#MAX_VALUE} if no sized parent exists yet.
     */
    private int resolveParentWidth(Container target) {
        JViewport viewport = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, target);
        if (viewport != null) {
            return viewport.getWidth();
        }

        Container parent = target.getParent();
        while (parent != null) {
            int width = parent.getSize().width;
            if (width > 0) return width;
            parent = parent.getParent();
        }

        return Integer.MAX_VALUE;
    }

    /**
     * Lays out components into rows, wrapping when the accumulated row width
     * would exceed {@code maxWidth} (i.e. the parent's available width).
     * The returned {@link Dimension} width reflects the widest actual row of
     * children — not the parent width — so the container only grows as wide
     * as its content demands.
     */
    private Dimension measureRows(Container target, int maxWidth, int hGap, boolean preferred) {
        Dimension dimension = new Dimension(0, 0);

        int rowWidth = 0;
        int rowHeight = 0;

        for (int i = 0, count = target.getComponentCount(); i < count; i++) {
            Component component = target.getComponent(i);

            if (!component.isVisible()) continue;

            Dimension componentSize = preferred
                    ? component.getPreferredSize()
                    : component.getMinimumSize();

            // Wrap when adding this component would exceed the *parent* boundary.
            if (rowWidth > 0 && rowWidth + hGap + componentSize.width > maxWidth) {
                addRow(dimension, rowWidth, rowHeight);
                rowWidth = 0;
                rowHeight = 0;
            }

            if (rowWidth != 0) rowWidth += hGap;

            rowWidth += componentSize.width;
            rowHeight = Math.max(rowHeight, componentSize.height);
        }

        // Flush the last row.
        addRow(dimension, rowWidth, rowHeight);

        return dimension;
    }

    /**
     * Accumulates a completed row into the running totals.
     * Width tracks the widest child row; height stacks rows with gaps.
     */
    private void addRow(Dimension size, int rowWidth, int rowHeight) {
        // Width = widest row of children, never inflated to parent width.
        size.width = Math.max(size.width, rowWidth);

        if (size.height > 0) size.height += getVgap();

        size.height += rowHeight;
    }
}
