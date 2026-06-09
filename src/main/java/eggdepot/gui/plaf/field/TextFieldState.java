package eggdepot.gui.plaf.field;

import eggdepot.gui.plaf.component.ComponentState;

import javax.swing.text.JTextComponent;

public enum TextFieldState implements ComponentState<JTextComponent> {
    NEUTRAL,
    FOCUSED,
    DISABLED;

    public static TextFieldState of(JTextComponent component) {
        if (!component.isEnabled()) {
            return DISABLED;
        }

        if (component.hasFocus()) {
            return FOCUSED;
        }

        return NEUTRAL;
    }
}
