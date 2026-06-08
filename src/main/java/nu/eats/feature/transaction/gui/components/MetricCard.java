package nu.eats.feature.transaction.gui.components;

import nu.eats.gui.plaf.Theme;
import nu.eats.gui.plaf.border.framed.FramedBorder;

import javax.swing.*;
import java.awt.*;

public class MetricCard extends JPanel {
    private final JLabel valueDisplay = new JLabel();

    public MetricCard(String title, String value) {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(Theme.COLOR_SURFACE_ELEVATION_LOWEST);

        this.setBorder(new FramedBorder.Builder()
                .corners(corner -> corner.radius(Theme.RADIUS_MD))
                .sides.vertical(side -> side.padding(Theme.SPACING_3XL))
                .sides.horizontal(side -> side.padding(Theme.SPACING_4XL))
                .build()
        );

        var titleLabel = new JLabel(title);

        titleLabel.setFont(Theme.FONT_BOLD_SM);
        titleLabel.setForeground(Theme.COLOR_FG_SECONDARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        valueDisplay.setFont(Theme.FONT_BOLD_XL);
        valueDisplay.setForeground(Theme.COLOR_PRIMARY);
        valueDisplay.setAlignmentX(Component.LEFT_ALIGNMENT);

        this.add(titleLabel);
        this.add(Box.createVerticalStrut(Theme.SPACING_XS));
        this.add(valueDisplay);
    }

    public void setValue(String value) {
        this.valueDisplay.setText(value);
    }
}
