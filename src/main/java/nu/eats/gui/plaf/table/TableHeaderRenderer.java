package nu.eats.gui.plaf.table;

import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import nu.eats.gui.plaf.Theme;

public class TableHeaderRenderer extends DefaultTableCellRenderer {

    public TableHeaderRenderer() {
        setOpaque(true);
        setHorizontalAlignment(SwingConstants.LEFT);
        setVerticalAlignment(SwingConstants.CENTER);
        
        // Use theme token spacing with top/bottom padding only
        // Avoid side margins to prevent scroll pane header issues
        setBorder(BorderFactory.createEmptyBorder(
                Theme.SPACING_LG, 
                Theme.SPACING_LG, 
                Theme.SPACING_LG, 
                Theme.SPACING_LG
        ));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {

        // Set text to the column name, defaulting to empty string if null
        setText(value != null ? value.toString() : "");
        
        // Clean, flat, high-contrast styling for the header
        setFont(table.getFont().deriveFont(Font.BOLD));

        setBackground(Theme.COLOR_PRIMARY);
        setForeground(Theme.COLOR_FG_INVERSE);

        return this;
    }
}