package eggdepot.gui.components;

import javax.swing.*;
import java.awt.*;

public class WrapPanel extends JPanel {
    public WrapPanel(FlowLayout layout) {
        super(layout);

        getAccessibleContext().setAccessibleDescription(
                "A panel that automatically wraps its child components based on available width.");
    }

    @Override
    public Dimension getPreferredSize() {
        var base = super.getPreferredSize();
        var parent = getParent();

        if (parent == null) {
            return base;
        }

        var availableWidth = parent.getWidth();

        if (availableWidth <= 0) {
            return base;
        }

        var insets = getInsets();

        availableWidth -= insets.left + insets.right;

        var y = getY(availableWidth);

        return new Dimension(parent.getWidth(), y + insets.top + insets.bottom);
    }

    private int getY(int availableWidth) {
        var hgap = 0;
        var vgap = 0;

        if (getLayout() instanceof FlowLayout flowLayout) {
            hgap = flowLayout.getHgap();
            vgap = flowLayout.getVgap();
        }

        var x = 0;
        var y = vgap;
        var rowHeight = 0;

        for (var component : getComponents()) {
            if (!component.isVisible())
                continue;

            var componentSize = component.getPreferredSize();

            if (x > 0 && x + componentSize.width > availableWidth) {
                x = 0;
                y += rowHeight + vgap;
                rowHeight = 0;
            }

            x += componentSize.width + hgap;
            rowHeight = Math.max(rowHeight, componentSize.height);
        }

        y += rowHeight + vgap;

        return y;
    }
}
