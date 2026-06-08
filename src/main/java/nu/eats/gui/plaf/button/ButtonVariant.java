package nu.eats.gui.plaf.button;

import nu.eats.gui.plaf.Theme;
import nu.eats.gui.plaf.component.StatefulComponentVariant;

import javax.swing.*;
import java.awt.font.TextAttribute;
import java.util.Map;

import static nu.eats.gui.plaf.Theme.*;

public enum ButtonVariant implements StatefulComponentVariant<AbstractButton, ButtonState> {
    PRIMARY {
        @Override
        public void apply(AbstractButton button, ButtonState state) {
            switch (state) {
                case NEUTRAL -> {
                    button.setBackground(COLOR_PRIMARY);
                    button.setForeground(COLOR_FG_PRIMARY);
                }

                case HOVERED -> {
                    button.setBackground(Theme.COLOR_BG_HOVER);
                    button.setForeground(COLOR_FG_INVERSE);
                }

                case ACTIVATED -> {
                    button.setBackground(Theme.COLOR_BG_PRESSED);
                    button.setForeground(COLOR_FG_PRIMARY);
                }

                case DISABLED -> {
                    button.setBackground(COLOR_PLACEHOLDER_BG);
                    button.setForeground(COLOR_PLACEHOLDER_FG);
                }
            }
        }
    },

    SECONDARY {
        @Override
        public void apply(AbstractButton button, ButtonState state) {
            switch (state) {
                case NEUTRAL -> {
                    button.setBackground(COLOR_SURFACE_ELEVATION_HIGHEST);
                    button.setForeground(COLOR_FG_INVERSE);
                }

                case HOVERED -> {
                    button.setBackground(Theme.COLOR_BG_HOVER);
                    button.setForeground(COLOR_FG_SECONDARY);
                }

                case ACTIVATED -> {
                    button.setBackground(Theme.COLOR_BG_PRESSED);
                    button.setForeground(COLOR_FG_PRIMARY);
                }

                case SELECTED -> {
                    PRIMARY.apply(button, ButtonState.NEUTRAL);
                }

                case DISABLED -> {
                    button.setBackground(COLOR_SURFACE_ELEVATION_HIGHEST);
                    button.setForeground(COLOR_FG_SECONDARY);
                }
            }
        }
    },

    TERTIARY {
        @Override
        public void apply(AbstractButton button, ButtonState state) {
            switch (state) {
                case NEUTRAL -> {
                    button.setBackground(COLOR_TRANSPARENT);
                    button.setForeground(COLOR_PRIMARY);

                    button.setFont(button.getFont().deriveFont(
                            Map.of(TextAttribute.UNDERLINE, -1)));
                }

                case HOVERED -> {
                    button.setForeground(COLOR_PRIMARY);

                    button.setFont(button.getFont().deriveFont(
                            Map.of(TextAttribute.UNDERLINE,
                                    TextAttribute.UNDERLINE_ON)));
                }

                case ACTIVATED -> {
                    button.setForeground(Theme.COLOR_BG_PRESSED);
                }

                case DISABLED -> {
                    button.setForeground(COLOR_PLACEHOLDER_FG);
                }
            }
        }
    },

    /**
     * Borderless, zero-radius button with subtle hover/active feedback.
     */
    FLAT {
        @Override
        public void apply(AbstractButton button, ButtonState state) {
            switch (state) {
                case NEUTRAL -> {
                    button.setBackground(COLOR_TRANSPARENT);
                    button.setForeground(COLOR_FG_INVERSE);
                }

                case HOVERED -> {
                    button.setBackground(Theme.COLOR_BG_HOVER);
                    button.setForeground(COLOR_FG_INVERSE);
                }

                case ACTIVATED -> {
                    button.setBackground(COLOR_BG_PRESSED);
                    button.setForeground(COLOR_FG_PRIMARY);
                }

                case SELECTED -> {
                    PRIMARY.apply(button, ButtonState.NEUTRAL);
                }

                case DISABLED -> {
                    button.setForeground(COLOR_PLACEHOLDER_FG);
                }
            }
        }
    },

    /**
     * White-text variant of FLAT, designed for use on Primary/Dark backgrounds.
     */
    GHOST {
        @Override
        public void apply(AbstractButton button, ButtonState state) {
            switch (state) {
                case NEUTRAL -> {
                    button.setBackground(COLOR_TRANSPARENT);
                    button.setForeground(COLOR_FG_PRIMARY);
                }

                case HOVERED -> {
                    button.setBackground(Theme.COLOR_BG_HOVER);
                    button.setForeground(COLOR_FG_PRIMARY);
                }

                case ACTIVATED -> {
                    button.setBackground(Theme.COLOR_BG_PRESSED);
                    button.setForeground(COLOR_FG_PRIMARY);
                }

                case SELECTED -> {
                    PRIMARY.apply(button, ButtonState.NEUTRAL);
                }

                case DISABLED -> {
                    button.setForeground(COLOR_PLACEHOLDER_FG_INVERSE);
                }
            }
        }
    };

    public void install(AbstractButton button) {
        this.install(button, ButtonState.NEUTRAL);
    }
}
