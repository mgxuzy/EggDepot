package eggdepot.gui.components.panel;

import eggdepot.gui.plaf.Theme;
import eggdepot.gui.plaf.border.framed.FramedBorder;
import eggdepot.gui.plaf.button.ButtonVariant;
import eggdepot.gui.plaf.icon.PlaceholderIcon;

import javax.swing.*;
import java.awt.*;

/**
 * A toggle button for category selection with icon and text.
 * Uses PlaceholderIcon for visual representation with automatic inverted state
 * when selected.
 */
public class SidePanelRow extends JToggleButton {

    private static final int ICON_SIZE = 32;

    public SidePanelRow(String text) {
        super(text);

        var normalIcon = new PlaceholderIcon(ICON_SIZE);
        var selectedIcon = normalIcon.inverted();

        this.setIcon(selectedIcon);
        this.setSelectedIcon(normalIcon);

        this.setHorizontalTextPosition(SwingConstants.RIGHT);
        this.setVerticalTextPosition(SwingConstants.CENTER);

        this.setHorizontalAlignment(SwingConstants.LEFT);
        this.setVerticalAlignment(SwingConstants.CENTER);

        ButtonVariant.FLAT.install(this);

        this.setIconTextGap(Theme.SPACING_3XL);

        if (this.getBorder() instanceof FramedBorder framedBorder) {
            this.setBorder(framedBorder.toBuilder()
                    .sides.vertical(side -> side.padding(Theme.SPACING_2XL))
                    .sides.horizontal(side -> side.padding(Theme.SPACING_4XL))
                    .corners(corner -> corner.radius(0))
                    .corners.topLeft(corner -> corner.radius(Integer.MAX_VALUE))
                    .corners.bottomLeft(corner -> corner.radius(Integer.MAX_VALUE))
                    .build()
            );
        }
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
    }
}
