package nu.eats.gui.plaf.field;

import nu.eats.gui.plaf.Theme;
import nu.eats.gui.plaf.border.FramedBorder;
import nu.eats.gui.plaf.component.ComponentState;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import static nu.eats.gui.plaf.Constants.*;

public class TextFieldUI extends BasicTextFieldUI {

    private FocusListener focusListener;

    @SuppressWarnings("UnusedDeclaration")
    public static ComponentUI createUI(JComponent component) {
        return new TextFieldUI();
    }

    @Override
    protected void installDefaults() {
        super.installDefaults();

        JTextComponent editor = getComponent();

        editor.setOpaque(false);

        TextFieldPreset.MD.apply(editor);
        TextFieldVariant.SECONDARY.install(editor, TextFieldState.NEUTRAL);
    }

    @Override
    protected void installListeners() {
        super.installListeners();

        JTextComponent component = getComponent();

        focusListener = new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent event) {
                var component = (JTextComponent) event.getComponent();

                ComponentState.set(component, TextFieldState.FOCUSED);
            }

            @Override
            public void focusLost(FocusEvent event) {
                var component = (JTextComponent) event.getComponent();

                ComponentState.set(component, TextFieldState.NEUTRAL);
            }
        };

        component.addFocusListener(focusListener);
    }

    @Override
    protected void uninstallListeners() {
        JTextComponent component = getComponent();

        component.removeFocusListener(focusListener);

        super.uninstallListeners();
    }

    @Override
    protected void paintSafely(Graphics graphics) {
        JTextComponent component = getComponent();

        var graphics2D = (Graphics2D) graphics.create();

        graphics2D.setRenderingHints(DEFAULT_RENDERING_HINTS);

        try {
            if (component.getBorder() instanceof FramedBorder border) {
                border.paintClientRegion(graphics2D, component);
            }

            super.paintSafely(graphics2D);
        } finally {
            graphics2D.dispose();
        }
    }
}
