package eggdepot.gui.plaf.scroll;

import java.awt.*;

public class ScrollPaneLayout extends javax.swing.ScrollPaneLayout {
    @Override
    public void layoutContainer(Container parent) {
        super.layoutContainer(parent);

        if (viewport != null) {
            var insets = parent.getInsets();
            int headerHeight = (colHead != null && colHead.isVisible()) ? colHead.getHeight() : 0;

            viewport.setBounds(
                    insets.left,
                    insets.top + headerHeight,
                    parent.getWidth() - insets.left - insets.right,
                    parent.getHeight() - insets.top - insets.bottom - headerHeight
            );
        }

        if (vsb != null && vsb.isVisible()) {
            parent.setComponentZOrder(vsb, 0);
        }

        if (hsb != null && hsb.isVisible()) {
            parent.setComponentZOrder(hsb, 0);
        }
    }
}