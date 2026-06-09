package eggdepot.gui.components;

import eggdepot.gui.plaf.Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LabeledField extends JPanel {
    private static final int FIELD_WIDTH = 280;

    public LabeledField(String labelText, JComponent field) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setAlignmentX(Component.CENTER_ALIGNMENT);

        var label = new JLabel(labelText);

        label.setFont(Theme.FONT_MEDIUM_MD);
        label.setForeground(Theme.COLOR_FG_INVERSE);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.setAlignmentX(Component.LEFT_ALIGNMENT);

        var mainContent = new JPanel();

        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setOpaque(false);
        mainContent.add(label);
        mainContent.add(Box.createVerticalStrut(Theme.SPACING_LG));
        mainContent.add(field);

        add(mainContent);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                field.requestFocusInWindow();
            }
        });
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }
}
