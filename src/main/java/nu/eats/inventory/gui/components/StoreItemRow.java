package nu.eats.gui.store.components;

import nu.eats.domain.store.Product;
import nu.eats.gui.plaf.Theme;
import nu.eats.gui.plaf.button.ButtonState;
import nu.eats.gui.plaf.button.ButtonVariant;
import nu.eats.gui.plaf.icons.PlaceholderIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Map;

import static nu.eats.gui.plaf.Constants.KEY_COMPONENT_VARIANT;

public class StoreItemRow extends JButton {
    private static final int CARD_WIDTH = 160;
    private static final int CARD_HEIGHT = 210;

    public StoreItemRow(Product product) {
        setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
        setLayout(new BorderLayout(0, 0));
        setBorder(new EmptyBorder(16, 16, 16, 16));

        var priceText = String.format("₱%.2f", product.price());
        var imageUri = product.imageUri();

        Icon image;

        try {
            image = scaleIcon(new ImageIcon(URI.create(imageUri).toURL()), 110, 110);
        } catch (MalformedURLException | NullPointerException | IllegalArgumentException _) {
            image = new PlaceholderIcon(110, 110);
        }

        var itemImageDisplay = new JLabel();

        itemImageDisplay.setHorizontalAlignment(SwingConstants.CENTER);
        itemImageDisplay.setIcon(image);

        add(itemImageDisplay, BorderLayout.CENTER);

        // --- Item Summary ---
        var itemSummary = new JPanel();

        itemSummary.setLayout(new BoxLayout(itemSummary, BoxLayout.Y_AXIS));
        itemSummary.setOpaque(false);

        var itemNameLabel = new JLabel(product.name());

        itemNameLabel.setForeground(Theme.COLOR_FG);
        itemNameLabel.setFont(Theme.FONT_MEDIUM_14);
        itemNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        var itemPriceLabel = new JLabel(priceText);

        itemPriceLabel.setForeground(Theme.COLOR_FG_MUTED);
        itemPriceLabel.setFont(Theme.FONT_REGULAR_12);
        itemPriceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        itemSummary.add(Box.createVerticalStrut(12));
        itemSummary.add(itemNameLabel);
        itemSummary.add(Box.createVerticalStrut(4));
        itemSummary.add(itemPriceLabel);

        add(itemSummary, BorderLayout.SOUTH);

        // BoxDecoration.ensure(this).borderRadius(Theme.FONT_SIZE_XS);

        putClientProperty(KEY_COMPONENT_VARIANT, ButtonVariant.SECONDARY);

        model.addChangeListener(_ -> {
            switch (ButtonState.of(model)) {
                case DISABLED -> {
                    itemNameLabel.setForeground(Theme.COLOR_PLACEHOLDER_FG);

                    var strikeFont = getFont()
                            .deriveFont(Map.of(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON));

                    itemNameLabel.setFont(strikeFont);
                }

                case NEUTRAL -> {
                    itemNameLabel.setFont(getFont());
                    itemNameLabel.setForeground(Theme.COLOR_FG);
                }
            }
        });
    }

    private ImageIcon scaleIcon(ImageIcon src, int width, int height) {
        var sourceImage = src.getImage();
        var resizedBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        var g2 = resizedBufferedImage.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(sourceImage, 0, 0, width, height, null);
        g2.dispose();

        return new ImageIcon(resizedBufferedImage);
    }
}
