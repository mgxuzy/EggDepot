package nu.eats.gui.plaf.scroll;

import nu.eats.gui.plaf.border.framed.FramedBorder;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicScrollPaneUI;

import java.awt.*;

import static nu.eats.gui.plaf.Theme.FONT_SIZE_BASE;

public class ScrollPaneUI extends BasicScrollPaneUI {
    @SuppressWarnings("UnusedDeclaration")
    public static ComponentUI createUI(JComponent component) {
        return new ScrollPaneUI();
    }

    @Override
    protected void installDefaults(JScrollPane scrollPane) {
        super.installDefaults(scrollPane);

        scrollPane.setOpaque(false);
        scrollPane.setBorder(FramedBorder.NONE);
        scrollPane.setLayout(new ScrollPaneLayout());
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setViewportBorder(FramedBorder.NONE);

        scrollPane.getVerticalScrollBar().setUnitIncrement(FONT_SIZE_BASE);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(FONT_SIZE_BASE);
    }
}
