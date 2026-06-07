package nu.eats.gui.plaf.table;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTableUI;

import nu.eats.gui.plaf.Theme;

/**
 * A minimal, high-contrast, overhead-free TableUI implementation.
 * Focuses on readability, keyboard accessibility, and a flat modern aesthetic.
 */
public class TableUI extends BasicTableUI {

    public static ComponentUI createUI(JComponent component) {
        return new TableUI();
    }

    @Override
    protected void installDefaults() {
        super.installDefaults();

        // 1. Structural Adjustments
        table.setOpaque(true);
        table.setFillsViewportHeight(true);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false); // Modern tables typically drop vertical separators

        // Ensure standard modern row spacing (breathing room for accessibility)
        int minRowHeight = 28;

        if (table.getRowHeight() < minRowHeight) {
            table.setRowHeight(minRowHeight);
        }

        table.setIntercellSpacing(new Dimension(0, Theme.SPACING_SM));

        table.setFont(Theme.FONT_REGULAR_MD);

        // 2. Standard Flat Colors
        Color bg = Theme.COLOR_BG;
        Color fg = Theme.COLOR_FG_PRIMARY;
        Color gridColor = Theme.COLOR_BORDER;
        Color selBg = Theme.COLOR_BG_PRIMARY_HOVER;
        Color selFg = Theme.COLOR_FG_PRIMARY;

        table.setBackground(bg);
        table.setForeground(fg);
        table.setGridColor(gridColor);
        table.setSelectionBackground(selBg);
        table.setSelectionForeground(selFg);

        // Set custom header renderer and ensure header is visible
        table.getTableHeader().setDefaultRenderer(new TableHeaderRenderer());
        table.getTableHeader().setOpaque(true);
        table.getTableHeader().setBackground(Theme.COLOR_PRIMARY);
        table.getTableHeader().setForeground(Theme.COLOR_FG_INVERSE);
        table.getTableHeader().setReorderingAllowed(true);
        table.getTableHeader().setResizingAllowed(true);
        table.getTableHeader().setPreferredSize(new java.awt.Dimension(
                table.getTableHeader().getPreferredSize().width,
                Theme.SPACING_6XL
        ));

        // 3. Clear focus indicator setup
        // Uses a high-contrast focus indicator border rather than legacy dotted lines
        Border focusBorder = UIManager.getBorder("Table.focusCellHighlightBorder");
        if (focusBorder == null) {
            UIManager.put("Table.focusCellHighlightBorder",
                    BorderFactory.createLineBorder(Color.RED, 2));
        }
    }

    @Override
    protected void installListeners() {
        super.installListeners();
        // Uses standard keyboard navigation and accessibility listeners
        // inherited from BasicTableUI to ensure screen reader support.
    }
}