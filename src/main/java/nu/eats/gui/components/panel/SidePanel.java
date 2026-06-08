package nu.eats.gui.components.panel;

import nu.eats.gui.plaf.Theme;

import javax.swing.*;
import java.awt.*;

public class SidePanel extends JPanel {
    private final JPanel rowList;
    private final ButtonGroup rowGroup;

    public SidePanel() {
        setLayout(new BorderLayout());

        this.rowList = new JPanel();

        this.rowList.setLayout(new BoxLayout(this.rowList, BoxLayout.Y_AXIS));

        var mainContent = new JPanel(new GridBagLayout());

        mainContent.add(this.rowList, new GridBagConstraints());

        var scroll = new JScrollPane(mainContent);

        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        this.add(scroll, BorderLayout.NORTH);

        this.rowGroup = new ButtonGroup();
    }

    public void addRow(SidePanelRow row) {
        this.rowGroup.add(row);
        this.rowList.add(row);
        this.rowList.add(Box.createVerticalStrut(Theme.SPACING_MD));
    }
}
