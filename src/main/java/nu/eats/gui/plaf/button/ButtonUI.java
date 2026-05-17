package nu.eats.gui.plaf.button;

import nu.eats.gui.plaf.border.FramedBorder;
import nu.eats.gui.plaf.component.ComponentState;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;

import static nu.eats.gui.plaf.Constants.DEFAULT_RENDERING_HINTS;

public class ButtonUI extends BasicButtonUI {
    @SuppressWarnings("UnusedDeclaration")
    public static ComponentUI createUI(JComponent component) {
        return InstanceHolder.INSTANCE;
    }

    @Override
    protected void installDefaults(AbstractButton button) {
        button.setContentAreaFilled(false); // also calls setOpaque(false);

        super.installDefaults(button);

        button.setRolloverEnabled(true);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        ButtonPreset.SM.apply(button);
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
        var graphics2D = (Graphics2D) graphics.create();

        graphics2D.setRenderingHints(DEFAULT_RENDERING_HINTS);

        try {
            if (component.getBorder() instanceof FramedBorder border) {
                border.paintClientRegion(graphics2D, component);
            }

            super.paint(graphics2D, component);
        } finally {
            graphics2D.dispose();
        }
    }

    private static final class InstanceHolder {
        private static final ButtonUI INSTANCE = new ButtonUI();
    }
}
