package nu.eats.gui.plaf.button;

import nu.eats.gui.plaf.border.FramedBorder;

import javax.swing.*;

public enum ButtonSizePreset {
    XS {
        @Override
        public void apply(AbstractButton button) {
            button.setBorder(new FramedBorder.Builder()
                    .sides.horizontal(side -> side.padding(12))
                    .sides.vertical(side -> side.padding(8))
                    .build()
            );

            button.setIconTextGap(4);
            button.setFont(button.getFont().deriveFont(12f));
        }

        // borderRadius(Theme.PILL_SHAPE_RADIUS)
    },

    SM {
        @Override
        public void apply(AbstractButton button) {
            button.setBorder(new FramedBorder.Builder()
                    .sides.horizontal(side -> side.padding(16))
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
                    .build()
            );

            button.setIconTextGap(20);
            button.setFont(button.getFont().deriveFont(24f));
        }
    };

    public abstract void apply(AbstractButton button);
}
