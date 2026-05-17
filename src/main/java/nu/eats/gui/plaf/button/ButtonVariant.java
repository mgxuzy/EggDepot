package nu.eats.gui.plaf.button;

import nu.eats.gui.plaf.border.BoxDecoration;
import nu.eats.gui.plaf.border.FramedBorder;

import javax.swing.*;
import java.awt.font.TextAttribute;
import java.util.Map;

import static nu.eats.gui.plaf.Theme.*;

public enum ButtonVariant {
    PRIMARY {
        @Override
        void apply(AbstractButton button, ButtonState state) {
            switch (state) {
                case DEFAULT -> {
                    button.setBackground(COLOR_PRIMARY);
                    button.setForeground(COLOR_FG_INVERSE);
                }

                case HOVERING -> {
                    button.setBackground(COLOR_BG_PRIMARY_HOVER);
                }

                case ACTIVE -> {
                    button.setBackground(COLOR_BG_PRIMARY_PRESSED);
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
        void apply(AbstractButton button, ButtonState state) {
            switch (state) {
                case DEFAULT -> {
                    button.setBackground(COLOR_BG);
                    button.setForeground(COLOR_FG);
                }

                case HOVERING -> {
                    button.setBackground(COLOR_BG_HOVER);
                    button.setForeground(COLOR_FG);

                    // BoxDecoration.ensure(button)
                    //        .borderColor(COLOR_BORDER)
                    //        .borderWidth(BORDER_WIDTH_THIN);
                }

                case ACTIVE -> {
                    button.setBackground(COLOR_BG_PRESSED);
                    button.setForeground(COLOR_FG);

                    // BoxDecoration.ensure(button)
                    //    .borderColor(COLOR_BORDER)
                    //    .borderWidth(BORDER_WIDTH_THIN);
                }

                case SELECTING -> {
                    // BoxDecoration.ensure(button).borderWidth(BORDER_WIDTH_NONE);

                    PRIMARY.apply(button, ButtonState.DEFAULT);
                }

                case DISABLED -> {
                    button.setBackground(COLOR_BG);
                    button.setForeground(ZINC_300);

                    // BoxDecoration.ensure(button)
                    //     .borderColor(COLOR_BORDER)
                    //   .borderWidth(BORDER_WIDTH_THIN);
                }
            }
        }
    },

    TERTIARY {
        @Override
        void apply(AbstractButton button, ButtonState state) {
            switch (state) {
                case DEFAULT -> {
                    button.setBackground(COLOR_TRANSPARENT);
                    button.setForeground(COLOR_PRIMARY);

                    button.setFont(button.getFont().deriveFont(
                            Map.of(TextAttribute.UNDERLINE, -1)));
                }

                case HOVERING -> {
                    button.setForeground(COLOR_PRIMARY);

                    button.setFont(button.getFont().deriveFont(
                            Map.of(TextAttribute.UNDERLINE,
                                    TextAttribute.UNDERLINE_ON)));
                }

                case ACTIVE -> {
                    button.setForeground(COLOR_BG_PRIMARY_PRESSED);
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
        void apply(AbstractButton button, ButtonState state) {
            button.setBorder(null);
            // BoxDecoration.ensure(button).borderWidth(BORDER_WIDTH_NONE).borderRadius(0);

            switch (state) {
                case DEFAULT -> {
                    button.setBackground(COLOR_TRANSPARENT);
                    button.setForeground(COLOR_FG);
                }

                case HOVERING -> {
                    button.setBackground(COLOR_BG_HOVER);
                }

                case ACTIVE -> {
                    button.setBackground(COLOR_BG_PRESSED);
                }

                case SELECTING -> {
                    PRIMARY.apply(button, ButtonState.DEFAULT);
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
        void apply(AbstractButton button, ButtonState state) {
            switch (state) {
                case DEFAULT -> {
                    button.setBackground(COLOR_TRANSPARENT);
                    button.setForeground(COLOR_FG_INVERSE);
                    button.setFont(FONT_MEDIUM_BASE.deriveFont(button.getFont().getSize2D()));
                }

                case HOVERING -> {
                    button.setBackground(COLOR_BG_PRIMARY_HOVER);
                    button.setForeground(COLOR_FG_INVERSE);
                }

                case ACTIVE -> {
                    button.setBackground(COLOR_BG_PRIMARY_PRESSED);
                    button.setForeground(COLOR_FG_INVERSE);
                }

                case SELECTING -> {
                    PRIMARY.apply(button, ButtonState.DEFAULT);
                }

                case DISABLED -> {
                    button.setForeground(COLOR_PLACEHOLDER_FG_INVERSE);
                }
            }
        }
    };

    abstract void apply(AbstractButton button, ButtonState state);
}
