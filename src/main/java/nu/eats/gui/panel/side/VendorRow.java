package nu.eats.gui.panel.side;

import nu.eats.gui.plaf.Theme;
import nu.eats.gui.plaf.button.ButtonPreset;
import nu.eats.gui.plaf.button.ButtonVariant;
import nu.eats.gui.plaf.icons.PlaceholderIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static nu.eats.gui.plaf.Constants.KEY_COMPONENT_VARIANT;

/**
 * A toggle button for category selection with icon and text.
 * Uses PlaceholderIcon for visual representation with automatic inverted state
 * when selected.
 */
public class VendorRow extends JToggleButton {

    private static final int ICON_SIZE = 48;

    public VendorRow(String text) {
        super(text);

        var normalIcon = new PlaceholderIcon(ICON_SIZE);
        var selectedIcon = normalIcon.inverted();

        setIcon(normalIcon);
        setSelectedIcon(selectedIcon);

        setHorizontalTextPosition(SwingConstants.CENTER);
        setVerticalTextPosition(SwingConstants.BOTTOM);

        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);

        ButtonPreset.XS.apply(this);
        // BoxDecoration.ensure(this).borderRadius(Theme.FONT_SIZE_XS);
        setIconTextGap(16);

        setBorder(new EmptyBorder(Theme.SPACING_LG, Theme.SPACING_2XL, Theme.SPACING_LG, Theme.SPACING_2XL));

        putClientProperty(KEY_COMPONENT_VARIANT, ButtonVariant.SECONDARY);
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
    }
}
