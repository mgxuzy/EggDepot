package nu.eats.feature.inventory.gui.components;

import nu.eats.feature.inventory.model.Product;
import nu.eats.gui.plaf.Theme;
import nu.eats.gui.plaf.border.framed.FramedBorder;
import nu.eats.gui.plaf.button.ButtonState;
import nu.eats.gui.plaf.button.ButtonVariant;
import nu.eats.gui.plaf.icons.PlaceholderIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Map;

import static nu.eats.gui.plaf.Theme.*;

public class InventoryItemRow extends JButton {
    private static final int CARD_WIDTH = 180;
    private static final int CARD_HEIGHT = 230;

    public InventoryItemRow(Product product) {
        setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
        setLayout(new BorderLayout(0, 0));

        ButtonVariant.SECONDARY.install(this, ButtonState.NEUTRAL);

        setBorder(new FramedBorder.Builder()
                .corners(corner -> corner.radius(Theme.RADIUS_LG))
                .edges(edge -> edge.color(Theme.COLOR_BORDER))
                .build()
        );

        var priceText = String.format("₱%.2f", product.price());
        var imageUri = product.imageUri();

        Icon image;

        try {
            image = scaleIcon(new ImageIcon(URI.create(imageUri).toURL()), 130, 130);
        } catch (MalformedURLException | NullPointerException | IllegalArgumentException _) {
            image = new PlaceholderIcon(130, 130);
        }

        var itemImageDisplay = new JLabel();

        itemImageDisplay.setHorizontalAlignment(SwingConstants.CENTER);
        itemImageDisplay.setIcon(image);

        add(itemImageDisplay, BorderLayout.CENTER);

        var itemSummary = new JPanel(new GridLayout(2, 1, 0, Theme.SPACING_3XL));

        itemSummary.setBackground(getBackground());

        itemSummary.setBorder(new FramedBorder.Builder()
                .sides.vertical(side -> side.padding(SPACING_XL))
                .sides.horizontal(side -> side.padding(SPACING_4XL))
                .build()
        );

        var itemNameLabel = new JLabel(product.name());

        itemNameLabel.setForeground(Theme.COLOR_FG_INVERSE);
        itemNameLabel.setHorizontalAlignment(SwingConstants.LEFT);

        var itemPriceLabel = new JLabel(priceText);

        itemPriceLabel.setForeground(Theme.COLOR_FG_SECONDARY);
        itemPriceLabel.setFont(Theme.FONT_REGULAR_MD);
       // itemPriceLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        itemSummary.add(itemNameLabel);
        itemSummary.add(itemPriceLabel);

        add(itemSummary, BorderLayout.SOUTH);

        var itemNameLabelFont = itemNameLabel.getFont();
        var itemNameLabelStrikedFont = itemNameLabelFont
                .deriveFont(Map.of(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON));

        model.addChangeListener(_ -> {
            switch (ButtonState.of(model)) {
                case DISABLED -> {
                    itemNameLabel.setForeground(Theme.COLOR_PLACEHOLDER_FG);
                    itemNameLabel.setFont(itemNameLabelStrikedFont);
                }

                case NEUTRAL -> {
                    itemNameLabel.setFont(itemNameLabelFont);
                    itemNameLabel.setForeground(Theme.COLOR_FG_INVERSE);
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
