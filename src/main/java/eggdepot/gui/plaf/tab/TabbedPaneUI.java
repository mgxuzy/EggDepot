package eggdepot.gui.plaf.tab;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;

import static eggdepot.gui.plaf.Constants.DEFAULT_RENDERING_HINTS;
import static eggdepot.gui.plaf.Theme.*;

public class TabbedPaneUI extends BasicTabbedPaneUI {

    private static final int INDICATOR_HEIGHT = 2;
    private static final Insets ZERO = new Insets(0, 0, 0, 0);

    @SuppressWarnings("UnusedDeclaration")
    public static ComponentUI createUI(JComponent component) {
        return new TabbedPaneUI();
    }

    @Override
    protected void installDefaults() {
        super.installDefaults();

        tabPane.setOpaque(false);
        tabPane.setBorder(null);
        tabPane.setFont(FONT_MEDIUM_MD);
        tabPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        tabInsets = new Insets(SPACING_2XL, SPACING_4XL, SPACING_2XL, SPACING_4XL);
        selectedTabPadInsets = ZERO;
        tabAreaInsets = ZERO;
        contentBorderInsets = ZERO;
    }

    // --- Layout ---

    @Override
    protected Insets getTabAreaInsets(int p) {
        return ZERO;
    }

    @Override
    protected Insets getContentBorderInsets(int p) {
        return ZERO;
    }

    @Override
    protected Insets getSelectedTabPadInsets(int p) {
        return ZERO;
    }

    @Override
    protected int getTabRunIndent(int p, int run) {
        return 0;
    }

    @Override
    protected int getTabRunOverlay(int p) {
        return 0;
    }

    @Override
    protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
        int natural = super.calculateTabWidth(tabPlacement, tabIndex, metrics);
        int count = tabPane.getTabCount();
        int total = tabPane.getWidth();

        if (count == 0 || total <= 0) return natural;

        int even = total / count + (tabIndex == count - 1 ? total % count : 0);

        return Math.max(natural, even);
    }

    // --- Paint ---

    @Override
    public void paint(Graphics g, JComponent c) {
        if (rects != null && rects.length > 0) {
            var g2 = (Graphics2D) g.create();
            g2.setRenderingHints(DEFAULT_RENDERING_HINTS);
            g2.setColor(COLOR_BORDER);
            g2.fillRect(0, rects[0].y + rects[0].height - INDICATOR_HEIGHT, c.getWidth(), INDICATOR_HEIGHT);
            g2.dispose();
        }

        super.paint(g, c);
    }

    @Override
    protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex,
                                  int x, int y, int w, int h, boolean isSelected) {
        if (!isSelected) return;

        var g2 = (Graphics2D) g;
        g2.setRenderingHints(DEFAULT_RENDERING_HINTS);
        g2.setColor(COLOR_PRIMARY);
        g2.fillRect(x, y + h - INDICATOR_HEIGHT, w, INDICATOR_HEIGHT);
    }

    @Override
    protected void paintText(Graphics g, int tabPlacement, Font font, FontMetrics metrics,
                             int tabIndex, String title, Rectangle textRect, boolean isSelected) {
        var g2 = (Graphics2D) g;
        g2.setRenderingHints(DEFAULT_RENDERING_HINTS);
        g2.setFont(isSelected ? FONT_MEDIUM_MD : FONT_REGULAR_MD);
        g2.setColor(isSelected ? COLOR_FG_INVERSE : COLOR_FG_SECONDARY);

        var fm = g2.getFontMetrics();
        g2.drawString(title,
                textRect.x + (textRect.width - fm.stringWidth(title)) / 2,
                textRect.y + fm.getAscent());
    }

    @Override
    protected void paintTabBackground(Graphics g, int p, int i, int x, int y, int w, int h, boolean s) {
    }

    @Override
    protected void paintContentBorder(Graphics g, int p, int i) {
    }

    @Override
    protected void paintFocusIndicator(Graphics g, int p, Rectangle[] r, int i, Rectangle ir, Rectangle tr, boolean s) {
    }
}
