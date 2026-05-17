package nu.eats.gui.plaf.button;

import nu.eats.gui.plaf.border.FramedBorder;

import javax.swing.*;

import static java.lang.Double.POSITIVE_INFINITY;

public enum ButtonPreset {
    XS {
        @Override
        public void apply(AbstractButton button) {
            button.setBorder(new FramedBorder.Builder()
                    .sides.horizontal(side -> side.padding(12))
                    .sides.vertical(side -> side.padding(8))
                    .corners(corner -> corner.radius(POSITIVE_INFINITY))
                    .build()
            );

            button.setIconTextGap(4);
            button.setFont(button.getFont().deriveFont(12f));
        }
    },

    SM {
        @Override
        public void apply(AbstractButton button) {
            button.setBorder(new FramedBorder.Builder()
                    .sides.horizontal(side -> side.padding(16))
                    .corners(corner -> corner.radius(POSITIVE_INFINITY))
                    .sides.vertical(side -> side.padding(10))
                    .build()
            );

            button.setIconTextGap(8);
            button.setFont(button.getFont().deriveFont(14f));
        }
    },

    MD {
        @Override
        public void apply(AbstractButton button) {
            button.setBorder(new FramedBorder.Builder()
                    .sides.horizontal(side -> side.padding(24))
                    .sides.vertical(side -> side.padding(16))
                    .corners(corner -> corner.radius(POSITIVE_INFINITY))
                    .build()
            );

            button.setIconTextGap(12);
            button.setFont(button.getFont().deriveFont(16f));
        }
    },

    LG {
        @Override
        public void apply(AbstractButton button) {
            button.setBorder(new FramedBorder.Builder()
                    .sides.horizontal(side -> side.padding(48))
                    .sides.vertical(side -> side.padding(32))
                    .corners(corner -> corner.radius(POSITIVE_INFINITY))
                    .build()
            );

            button.setIconTextGap(16);
            button.setFont(button.getFont().deriveFont(20f));
        }
    },

    XL {
        @Override
        public void apply(AbstractButton button) {
            button.setBorder(new FramedBorder.Builder()
                    .sides.horizontal(side -> side.padding(64))
                    .sides.vertical(side -> side.padding(48))
                    .corners(corner -> corner.radius(POSITIVE_INFINITY))
                    .build()
            );

            button.setIconTextGap(20);
            button.setFont(button.getFont().deriveFont(24f));
        }
    };

    public abstract void apply(AbstractButton button);
}
