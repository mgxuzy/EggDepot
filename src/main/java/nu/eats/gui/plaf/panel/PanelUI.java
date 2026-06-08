package nu.eats.gui.plaf.panel;

import nu.eats.gui.plaf.Theme;
import nu.eats.gui.plaf.border.FramedBorder;

import javax.swing.*;
import javax.swing.plaf.basic.BasicPanelUI;
import java.awt.*;

public class PanelUI extends BasicPanelUI {
    @SuppressWarnings("UnusedDeclaration")
    public static PanelUI createUI(JComponent component) {
        return InstanceHolder.INSTANCE;
    }

    @Override
    protected void installDefaults(JPanel panel) {
        panel.setOpaque(false);
        panel.setBackground(Theme.COLOR_TRANSPARENT);
        panel.setBorder(FramedBorder.NONE);
    }

    @Override
    public void update(Graphics graphics, JComponent component) {
        if (component.getBorder() instanceof FramedBorder border) {
            border.paintClientRegionWith(super::paint, graphics, component);

            return;
        }

        super.update(graphics, component);
    }

    private static final class InstanceHolder {
        private static final PanelUI INSTANCE = new PanelUI();
    }
}
