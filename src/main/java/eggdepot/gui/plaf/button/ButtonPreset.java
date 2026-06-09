package eggdepot.gui.plaf.button;

import eggdepot.gui.plaf.border.framed.FramedBorder;
import eggdepot.gui.plaf.component.ComponentPreset;

import javax.swing.*;

import static java.lang.Double.POSITIVE_INFINITY;
import static eggdepot.gui.plaf.Theme.*;

public enum ButtonPreset implements ComponentPreset<AbstractButton> {
    SM {
        @Override
        public void apply(AbstractButton button) {
            button.setBorder(new FramedBorder.Builder()
                    .sides.horizontal(side -> side.padding(SPACING_LG))
                    .sides.vertical(side -> side.padding(SPACING_MD))
                    .corners(corner -> corner.radius(POSITIVE_INFINITY))
                    .build()
            );

            button.setIconTextGap(SPACING_SM);
            button.setFont(button.getFont().deriveFont(FONT_SIZE_SM));
        }
    },

    MD {
        @Override
        public void apply(AbstractButton button) {
            // NOTE: 4:1 Spacing Ratio

            button.setBorder(new FramedBorder.Builder()
                    .sides.horizontal(side -> side.padding(SPACING_4XL))
                    .sides.vertical(side -> side.padding(SPACING_2XL))
                    .corners(corner -> corner.radius(POSITIVE_INFINITY))
                    .build()
            );

            button.setIconTextGap(SPACING_MD);
            button.setFont(button.getFont().deriveFont(FONT_SIZE_MD));
        }
    },

    LG {
        @Override
        public void apply(AbstractButton button) {
            button.setBorder(new FramedBorder.Builder()
                    .sides.horizontal(side -> side.padding(SPACING_5XL))
                    .sides.vertical(side -> side.padding(SPACING_3XL))
                    .corners(corner -> corner.radius(POSITIVE_INFINITY))
                    .build()
            );

            button.setIconTextGap(SPACING_LG);
            button.setFont(button.getFont().deriveFont(FONT_SIZE_LG));
        }
    }
}
