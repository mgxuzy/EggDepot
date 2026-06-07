package nu.eats.gui.components.chip;

import nu.eats.gui.plaf.Theme;
import nu.eats.gui.plaf.button.ButtonState;
import nu.eats.gui.plaf.button.ButtonVariant;

import javax.swing.*;

public class Chip extends JToggleButton {
    public Chip(String text) {
        super(text);

        setFont(Theme.FONT_MEDIUM_SM);
        ButtonVariant.SECONDARY.install(this, ButtonState.NEUTRAL);
    }
}
