package nu.eats.gui.plaf.field;

import nu.eats.gui.plaf.border.FramedBorder;
import nu.eats.gui.plaf.component.StatefulComponentVariant;

import javax.swing.text.JTextComponent;

import static nu.eats.gui.plaf.Theme.*;

public enum TextFieldVariant implements StatefulComponentVariant<JTextComponent, TextFieldState> {
    SECONDARY {
        @Override
        public void apply(JTextComponent component, TextFieldState state) {
            switch (state) {
                case NEUTRAL -> {
                    component.setBackground(COLOR_BG);
                    component.setForeground(COLOR_FG_PRIMARY);
                    component.setCaretColor(COLOR_PRIMARY);
                    component.setFont(FONT_REGULAR_MD);
                }

                case FOCUSED -> {
                    component.setBackground(COLOR_BG);
                    component.setForeground(COLOR_FG_PRIMARY);
                }

                case DISABLED -> {
                    component.setBackground(COLOR_BG_HOVER);
                    component.setForeground(COLOR_FG_SECONDARY);
                }
            }
        }
    },

    TERTIARY {
        public void apply(JTextComponent component, TextFieldState state) {
            switch (state) {
                case NEUTRAL -> {
                    component.setBackground(COLOR_BG);
                    component.setForeground(COLOR_FG_PRIMARY);
                    component.setCaretColor(COLOR_PRIMARY);
                    component.setFont(FONT_MEDIUM_MD);

                    component.setBorder(new FramedBorder.Builder()
                            .sides(side -> side.padding(SPACING_2XS))
                            .edges.bottom(edge -> edge.color(COLOR_BORDER).thickness(1))
                            .build()
                    );
                }

                case FOCUSED -> {
                    component.setBackground(COLOR_BG);
                    component.setForeground(COLOR_FG_PRIMARY);
                }
            }
        }
    };

    public abstract void apply(JTextComponent component, TextFieldState state);
}
