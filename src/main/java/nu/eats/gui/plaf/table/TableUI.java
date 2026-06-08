package nu.eats.gui.plaf.table;

import nu.eats.gui.plaf.Theme;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTableUI;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * A flat, modern TableUI implementation.
 * Uses core Swing inheritance mapping safely with zero runtime allocations.
 */
public class TableUI extends BasicTableUI {

    private static final DefaultTableCellRenderer SYSTEM_RENDERER = new DefaultTableCellRenderer() {
        private final Border normalPadding = BorderFactory.createEmptyBorder(0, Theme.SPACING_LG, 0, Theme.SPACING_LG);

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {

            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (!isSelected) {
                setBackground(row % 2 == 1 ? Theme.COLOR_SURFACE_ELEVATION_LOW : Theme.COLOR_SURFACE_ELEVATION_HIGHEST);
                setForeground(Theme.COLOR_FG_INVERSE);
            } else {
                setBackground(Theme.COLOR_BG_HOVER);
                setForeground(Theme.COLOR_FG_INVERSE);
            }

            setBorder(normalPadding);

            return this;
        }
    };

    public static ComponentUI createUI(JComponent component) {
        return new TableUI();
    }

    @Override
    protected void installDefaults() {
        super.installDefaults();

        table.setOpaque(true);
        table.setFillsViewportHeight(true);
        table.setShowHorizontalLines(false);
        table.setRowHeight(44);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFont(Theme.FONT_REGULAR_MD);
        table.setBorder(null);

        // 2. Color Application
        table.setBackground(Theme.COLOR_BG);
        table.setForeground(Theme.COLOR_FG_INVERSE);

        // 3. Header Hook Setup
        var header = table.getTableHeader();

        if (header != null) {
            header.setDefaultRenderer(new TableHeaderRenderer());
            header.setBackground(Theme.COLOR_SURFACE_ELEVATION_HIGHEST);
            header.setForeground(Theme.COLOR_FG_SECONDARY);
            header.setReorderingAllowed(false);
            header.setResizingAllowed(true);
            header.setPreferredSize(new Dimension(header.getWidth(), 46));
        }

        // 4. Fallback Default Mapping
        table.setDefaultRenderer(Object.class, SYSTEM_RENDERER);
        table.setDefaultRenderer(Number.class, SYSTEM_RENDERER);
        table.setDefaultRenderer(Integer.class, SYSTEM_RENDERER);
        table.setDefaultRenderer(Double.class, SYSTEM_RENDERER);
        table.setDefaultRenderer(String.class, SYSTEM_RENDERER);
    }
}