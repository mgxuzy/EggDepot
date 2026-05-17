package nu.eats.gui.plaf.root;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;

public class FocusOutline extends JComponent {
    private Rectangle focusRect;   // In overlay's coordinate space

    // Configurable drawing properties
    private float arc = 8f;        // Corner rounding
    private float strokeWidth = 2f;// Thickness of the outline
    private float fw = 4f;         // Distance to expand outside the component

    public FocusOutline() {
        setOpaque(false);
        setFocusable(false); // Overlay itself must not consume focus
    }

    void focusChanged(Component prev, Component next) {
        // 1. Dirty the old location to erase the previous outline
        if (focusRect != null) {
            repaintRect(focusRect);
        }

        if (next == null || !isAncestorWindow(next)) {
            focusRect = null;
            return;
        }

        // 2. Translate focused component bounds into overlay space
        Rectangle r = new Rectangle(next.getWidth(), next.getHeight());
        Rectangle newFocusRect = SwingUtilities.convertRectangle(next, r, this);

        // 3. Find if any ancestor is a JViewport and clip accordingly
        Container ancestor = next.getParent();
        while (ancestor != null) {
            if (ancestor instanceof JViewport vp) {
                Rectangle vpRect = SwingUtilities.convertRectangle(
                        vp, new Rectangle(vp.getSize()), this);
                newFocusRect = newFocusRect.intersection(vpRect);
                // Intentionally NOT breaking here to support nested scroll panes
            }
            ancestor = ancestor.getParent();
        }

        // 4. Update and repaint the new location (if visible)
        if (newFocusRect.width <= 0 || newFocusRect.height <= 0) {
            focusRect = null;
        } else {
            focusRect = newFocusRect;
            repaintRect(focusRect);
        }
    }

    /**
     * Repaints a padded region to fully encompass the stroke width and expansion
     */
    private void repaintRect(Rectangle r) {
        int pad = (int) Math.ceil(fw + strokeWidth) + 2;
        repaint(r.x - pad, r.y - pad, r.width + pad * 2, r.height + pad * 2);
    }

    boolean isAncestorWindow(Component c) {
        return SwingUtilities.getRootPane(c) == this.getRootPane();
    }

    @Override
    public boolean contains(int x, int y) {
        // Return false so all mouse events fall right through the overlay layer
        return false;
    }

    public static void install(JRootPane rootPane) {
        FocusOutline overlay = new FocusOutline();

        // Must fill the entire root pane initially
        overlay.setBounds(0, 0, rootPane.getWidth(), rootPane.getHeight());

        // Keep it sized correctly on frame resize
        rootPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Ignore the x/y of getBounds(), strict lock to 0,0
                overlay.setBounds(0, 0, rootPane.getWidth(), rootPane.getHeight());
            }
        });

        // DRAG_LAYER is above popups and the default content pane
        rootPane.getLayeredPane().add(overlay, JLayeredPane.DRAG_LAYER);

        // Listen for global focus changes
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addPropertyChangeListener("permanentFocusOwner", evt -> {
                    Component prev = (Component) evt.getOldValue();
                    Component next = (Component) evt.getNewValue();
                    overlay.focusChanged(prev, next);
                });
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (focusRect == null) return;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.RED);
        g2.setStroke(new BasicStroke(strokeWidth));

        // Paint OUTSIDE the component rect by expanding by the given thickness (fw)
        g2.draw(new RoundRectangle2D.Float(
                focusRect.x - fw,
                focusRect.y - fw,
                focusRect.width  + fw * 2,
                focusRect.height + fw * 2,
                arc, arc
        ));

        g2.dispose();
    }
}