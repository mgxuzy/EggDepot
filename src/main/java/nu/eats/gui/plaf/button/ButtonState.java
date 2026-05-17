package nu.eats.gui.plaf.button;

import nu.eats.gui.plaf.component.ComponentState;

import javax.swing.*;

public enum ButtonState implements ComponentState<AbstractButton> {
    NEUTRAL, HOVERED, ACTIVATED, SELECTED, DISABLED;

    public static ButtonState of(ButtonModel model) {
        if (!model.isEnabled()) {
            return DISABLED;
        }

        if (model.isSelected()) {
            return SELECTED;
        } else if (model.isArmed() && model.isPressed()) {
            return ACTIVATED;
        } else if (model.isRollover()) {
            return HOVERED;
        } else {
            return NEUTRAL;
        }
    }
}
