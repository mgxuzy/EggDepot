package eggdepot.gui.components;

import eggdepot.gui.plaf.Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import static eggdepot.gui.plaf.Constants.DEFAULT_RENDERING_HINTS;


/**
 * A placeholder icon displaying a simple avatar silhouette.
 * Supports normal and inverted color variants.
 */
public class PlaceholderIcon implements Icon {
    protected final int width;
    protected final int height;

    protected Color bgColor;
    protected Color fgColor;

    public PlaceholderIcon(int size) {
        this(size, size);
    }

    public PlaceholderIcon(int width, int height) {
        this.width = width;
        this.height = height;
        this.bgColor = Theme.COLOR_PLACEHOLDER_BG;
        this.fgColor = Theme.COLOR_PLACEHOLDER_FG;
    }

    /**
     * Creates an inverted version with swapped foreground/background for selected
     * states.
     */
    public PlaceholderIcon inverted() {
        var inverted = new PlaceholderIcon(width, height);

        inverted.bgColor = Theme.COLOR_PLACEHOLDER_BG_INVERSE;
        inverted.fgColor = Theme.COLOR_PLACEHOLDER_FG_INVERSE;

        return inverted;
    }

    @Override
    public void paintIcon(Component component, Graphics graphics, int x, int y) {
        var graphics2D = (Graphics2D) graphics.create();

        graphics2D.setRenderingHints(DEFAULT_RENDERING_HINTS);

        graphics2D.setColor(bgColor);
        graphics2D.fill(new RoundRectangle2D.Float(x, y, width, height, Theme.RADIUS_SM, Theme.RADIUS_SM));

        graphics2D.setColor(fgColor);

        var avatarSize = Math.min(width, height) / 3;
        var centerX = x + width / 2;
        var centerY = y + height / 2;

        graphics2D.fillOval(centerX - avatarSize / 3, centerY - avatarSize / 2, avatarSize / 2, avatarSize / 2);

        var avatarBodyHeight = avatarSize / 2;

        graphics2D.fill(new RoundRectangle2D.Float(centerX - avatarSize / 2f, centerY, avatarSize, avatarBodyHeight,
                Theme.RADIUS_SM / 2f, Theme.RADIUS_SM / 2f));

        graphics2D.dispose();
    }

    @Override
    public int getIconWidth() {
        return width;
    }

    @Override
    public int getIconHeight() {
        return height;
    }

    public ImageIcon toImageIcon() {
        var bufferedImage = new BufferedImage(
                width, height, BufferedImage.TYPE_INT_ARGB);

        var graphics2D = bufferedImage.createGraphics();

        paintIcon(null, graphics2D, 0, 0);

        graphics2D.dispose();

        return new ImageIcon(bufferedImage);
    }
}
