package eggdepot.gui.components.chip;

import eggdepot.gui.plaf.Theme;
import eggdepot.gui.plaf.button.ButtonState;
import eggdepot.gui.plaf.button.ButtonVariant;

import javax.swing.*;

public class Chip extends JToggleButton {
    public Chip(String text) {
        super(text);

        setFont(Theme.FONT_MEDIUM_SM);
        ButtonVariant.SECONDARY.install(this, ButtonState.NEUTRAL);
    }
}
