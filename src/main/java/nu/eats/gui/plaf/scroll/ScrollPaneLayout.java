package nu.eats.gui.plaf.scroll;

import javax.swing.*;
import java.awt.*;

public class ScrollPaneLayout extends javax.swing.ScrollPaneLayout {
    @Override
    public void layoutContainer(Container parent) {
        // 1. Let the original Swing layout handle the header and basic structure
        super.layoutContainer(parent);

        // 2. Dead-simple overlay adjustment: Expand the viewport to cover the
        // scrollbar areas while leaving room for the table header at the top.
        if (viewport != null) {
            var insets = parent.getInsets();
            int headerHeight = (colHead != null && colHead.isVisible()) ? colHead.getHeight() : 0;

            // Force viewport to stretch all the way across and down
            viewport.setBounds(
                    insets.left,
                    insets.top + headerHeight,
                    parent.getWidth() - insets.left - insets.right,
                    parent.getHeight() - insets.top - insets.bottom - headerHeight
            );
        }

        // 3. Ensure scrollbars sit comfortably on top of the viewport canvas
        if (vsb != null && vsb.isVisible()) parent.setComponentZOrder(vsb, 0);
        if (hsb != null && hsb.isVisible()) parent.setComponentZOrder(hsb, 0);
    }
}