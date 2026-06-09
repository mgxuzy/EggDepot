package eggdepot.gui.plaf.table;

import eggdepot.gui.plaf.Theme;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * A highly modernized, zero-overhead TableHeaderRenderer implementation.
 * Delivers clean typographic padding aligned with the modern TableUI layout guidelines.
 */
public class TableHeaderRenderer extends DefaultTableCellRenderer {

    public TableHeaderRenderer() {
        super(); // Invokes parent initialization safely

        setOpaque(true);
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);

        // Horizontal padding matching the modernized TableUI cell tracks perfectly
        setBorder(BorderFactory.createEmptyBorder(0, Theme.SPACING_LG, 0, Theme.SPACING_LG));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {

        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        setFont(Theme.FONT_BOLD_MD);
        setBackground(Theme.COLOR_PRIMARY);
        setForeground(Theme.COLOR_FG_PRIMARY);

        return this;
    }
}