package nu.eats.gui.plaf.button;

import nu.eats.gui.plaf.Theme;
import nu.eats.gui.plaf.border.framed.FramedBorder;
import nu.eats.gui.plaf.component.ComponentState;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;

public class ButtonUI extends BasicButtonUI {
    @SuppressWarnings("UnusedDeclaration")
    public static ComponentUI createUI(JComponent component) {
        return InstanceHolder.INSTANCE;
    }

    @Override
    protected void installDefaults(AbstractButton button) {
        button.setContentAreaFilled(true); // also calls setOpaque(false);
        button.setOpaque(false);

        super.installDefaults(button);

        button.setRolloverEnabled(true);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFont(Theme.FONT_MEDIUM_MD);

        ButtonPreset.MD.apply(button);
        ButtonVariant.PRIMARY.install(button);
    }

    @Override
    protected void installListeners(AbstractButton button) {
        super.installListeners(button);

        var buttonModel = button.getModel();

        buttonModel.addChangeListener(ignored -> {
            ComponentState.set(button, ButtonState.of(buttonModel));
        });
    }

    @Override
    public void paint(Graphics graphics, JComponent component) {
        if (component.getBorder() instanceof FramedBorder border) {
            border.paintClientRegionWith(super::paint, graphics, component);

            return;
        }

        super.paint(graphics, component);
    }

    private static final class InstanceHolder {
        private static final ButtonUI INSTANCE = new ButtonUI();
    }
}
