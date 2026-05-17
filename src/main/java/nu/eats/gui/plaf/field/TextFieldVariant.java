package nu.eats.gui.plaf.field;

import nu.eats.gui.plaf.Theme;
import nu.eats.gui.plaf.border.FramedBorder;
import nu.eats.gui.plaf.component.StatefulComponentVariant;

import javax.swing.text.JTextComponent;

public enum TextFieldVariant implements StatefulComponentVariant<JTextComponent, TextFieldState> {
    SECONDARY {
        @Override
        public void apply(JTextComponent component, TextFieldState state) {
            switch (state) {
                case NEUTRAL -> {
                    component.setBackground(Theme.COLOR_BG);
                    component.setForeground(Theme.COLOR_FG);
                    component.setCaretColor(Theme.COLOR_PRIMARY);
                    component.setFont(Theme.FONT_REGULAR_BASE);

                    component.setBorder(new FramedBorder.Builder()
                            .sides.horizontal(side -> side.padding(10))
                            .sides.vertical(side -> side.padding(8))
                            .edges(edge -> edge.color(Theme.COLOR_BORDER).thickness(1))
                            .corners(corner -> corner.radius(Theme.RADIUS_MD))
                            .build());
                }

                case FOCUSED -> {
                    component.setBackground(Theme.COLOR_BG);
                    component.setForeground(Theme.COLOR_FG);

                    // boxDecoration.borderColor(Theme.COLOR_RING)
                    //  .borderWidth(BORDER_WIDTH_THICK);
                }

                case DISABLED -> {
                    component.setBackground(Theme.COLOR_BG_HOVER);
                    component.setForeground(Theme.COLOR_FG_MUTED);

                    // boxDecoration.borderColor(Theme.COLOR_BORDER)
                    //       .borderWidth(BORDER_WIDTH_THIN);
                }
            }
        }
    },

    TERTIARY {
        public void apply(JTextComponent component, TextFieldState state) {
            switch (state) {
                case NEUTRAL -> {
                    component.setBackground(Theme.COLOR_BG);
                    component.setForeground(Theme.COLOR_FG);
                    component.setCaretColor(Theme.COLOR_PRIMARY);
                    component.setFont(Theme.FONT_MEDIUM_BASE);

                    component.setBorder(new FramedBorder.Builder()
                            .sides(side -> side.padding(2))
                            .edges.bottom(edge -> edge.color(Theme.COLOR_BORDER).thickness(1))
                            .build()
                    );
                }

                case FOCUSED -> {
                    component.setBackground(Theme.COLOR_BG);
                    component.setForeground(Theme.COLOR_FG);
                }
            }
        }
    };

    public abstract void apply(JTextComponent component, TextFieldState state);
}
