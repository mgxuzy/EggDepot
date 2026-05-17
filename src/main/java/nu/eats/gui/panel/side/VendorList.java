package nu.eats.gui.vendor;

import nu.eats.gui.components.Section;
import nu.eats.gui.plaf.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class VendorList extends Section {
    private final JPanel listContent;
    private final ButtonGroup rowGroup;

    public VendorList() {
        setLayout(new BorderLayout());

        listContent = new JPanel();

        listContent.setLayout(new BoxLayout(listContent, BoxLayout.Y_AXIS));
        listContent.setOpaque(false);

        listContent
                .setBorder(new EmptyBorder(Theme.SPACING_XL, Theme.SPACING_XL, Theme.SPACING_XL, Theme.SPACING_XL));

        var mainContent = new JPanel(new GridBagLayout());

        mainContent.setOpaque(false);
        mainContent.add(listContent, new GridBagConstraints());

        var scroll = new JScrollPane(mainContent);

        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        add(scroll, BorderLayout.NORTH);

        rowGroup = new ButtonGroup();
    }

    public void addRow(VendorRow row) {
        rowGroup.add(row);
        listContent.add(row);
        listContent.add(Box.createVerticalStrut(Theme.SPACING_MD));
    }
}
