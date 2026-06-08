package nu.eats.gui.plaf.field;

import nu.eats.gui.plaf.Theme;
import nu.eats.gui.plaf.border.framed.FramedBorder;
import nu.eats.gui.plaf.component.ComponentPreset;

import javax.swing.text.JTextComponent;

import static nu.eats.gui.plaf.Theme.*;
import static nu.eats.gui.plaf.Theme.RADIUS_MD;

public enum TextFieldPreset implements ComponentPreset<JTextComponent> {
    MD {
        @Override
        public void apply(JTextComponent component) {
            component.setBorder(new FramedBorder.Builder()
                    .sides.horizontal(side -> side.padding(SPACING_3XL))
                    .sides.vertical(side -> side.padding(SPACING_XL))
                    .edges(edge -> edge.color(COLOR_BORDER).thickness(1))
                    .corners(corner -> corner.radius(RADIUS_MD))
                    .build());

            component.setFont(Theme.FONT_REGULAR_MD);
        }
    },

    LG {
        @Override
        public void apply(JTextComponent component) {
            component.setBorder(new FramedBorder.Builder()
                    .sides.horizontal(side -> side.padding(SPACING_3XL))
                    .sides.vertical(side -> side.padding(SPACING_3XL))
                    .edges(edge -> edge.color(COLOR_BORDER).thickness(1))
                    .corners(corner -> corner.radius(RADIUS_LG))
                    .build());

            component.setFont(Theme.FONT_REGULAR_LG);
        }
    };
}
