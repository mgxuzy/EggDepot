package eggdepot.gui.plaf.outline;

import eggdepot.gui.plaf.Theme;
import eggdepot.gui.plaf.border.framed.FramedBorder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeListener;

public class FocusOutline extends JComponent {

    private final BoundsTracker boundsTracker = new BoundsTracker();
    private Rectangle focusRect;       // The full, unclipped dimensions of the component
    private Rectangle paintedBounds;   // The visible intersection (used efficiently for clearing old outlines)
    private Insets paintedInsets = new Insets(0, 0, 0, 0);
    private FramedBorder focusBorder;
    private JComponent trackedComponent;
    private final PropertyChangeListener focusListener = evt ->
            setTrackedComponent(evt.getNewValue() instanceof JComponent next ? next : null);

    public FocusOutline() {
        setOpaque(false);
        setFocusable(false);
    }

    public static void install(JRootPane rootPane) {
        var focusOutline = new FocusOutline();

        focusOutline.setBounds(0, 0, rootPane.getWidth(), rootPane.getHeight());

        rootPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent event) {
                focusOutline.setBounds(0, 0, rootPane.getWidth(), rootPane.getHeight());
            }
        });

        rootPane.getLayeredPane().add(focusOutline, JLayeredPane.DRAG_LAYER);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addPropertyChangeListener("permanentFocusOwner", focusListener);
    }

    @Override
    public void removeNotify() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .removePropertyChangeListener("permanentFocusOwner", focusListener);
        setTrackedComponent(null);
        super.removeNotify();
    }

    private void setTrackedComponent(JComponent next) {
        if (trackedComponent != null) {
            trackedComponent.removeComponentListener(boundsTracker);
            trackedComponent.removeHierarchyBoundsListener(boundsTracker);
            trackedComponent.removeHierarchyListener(boundsTracker);
        }

        trackedComponent = next;

        if (trackedComponent != null) {
            trackedComponent.addComponentListener(boundsTracker);
            trackedComponent.addHierarchyBoundsListener(boundsTracker);
            trackedComponent.addHierarchyListener(boundsTracker);
        }

        updateFocusBounds();
    }

    private void updateFocusBounds() {
        // 1. Erase previously drawn visible bounds using the exact insets it was drawn with
        if (paintedBounds != null) {
            repaintRect(paintedBounds, paintedInsets);
        }

        // 2. Clear if no component or invalid state
        if (trackedComponent == null || !trackedComponent.isShowing()
                || SwingUtilities.getRootPane(trackedComponent) != getRootPane()
                || !(trackedComponent.getBorder() instanceof FramedBorder framedBorder)) {
            clearFocusState();
            return;
        }

        // 3. Determine actual visible bounds accurately tracking any ancestor viewports
        Rectangle visibleRect = trackedComponent.getVisibleRect();
        if (visibleRect.isEmpty()) {
            clearFocusState(); // Completely scrolled out of view
            return;
        }

        // 4. Apply State
        this.focusBorder = framedBorder.toBuilder()
                .edges(edge -> {
                    if (edge.build().isVisible) {
                        edge.thickness(2).color(Theme.COLOR_ACCENT);
                    }
                })
                .build();

        this.focusRect = SwingUtilities.convertRectangle(trackedComponent,
                new Rectangle(0, 0, trackedComponent.getWidth(), trackedComponent.getHeight()), this);
        this.paintedBounds = SwingUtilities.convertRectangle(trackedComponent, visibleRect, this);
        this.paintedInsets = focusBorder.getBorderInsets(trackedComponent);

        // 5. Request paint for the new visible area
        repaintRect(paintedBounds, paintedInsets);
    }

    private void clearFocusState() {
        this.focusRect = null;
        this.paintedBounds = null;
        this.focusBorder = null;
        this.paintedInsets = new Insets(0, 0, 0, 0);
    }

    private void repaintRect(Rectangle rectangle, Insets insets) {
        // Dynamically applies padding using the border's true required clearance
        repaint(rectangle.x - insets.left, rectangle.y - insets.top,
                rectangle.width + insets.left + insets.right, rectangle.height + insets.top + insets.bottom);
    }

    @Override
    public boolean contains(int x, int y) {
        return false;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        if (focusRect == null || focusBorder == null || paintedBounds == null) {
            return;
        }

        Graphics2D graphics2D = (Graphics2D) graphics.create();

        try {
            graphics2D.clip(paintedBounds);

            focusBorder.resize(focusRect.width, focusRect.height);
            focusBorder.paintBorder(this, graphics2D, focusRect.x, focusRect.y, focusRect.width, focusRect.height);
        } finally {
            graphics2D.dispose();
        }
    }

    // Coalesced layout watcher delegating to one simple flat update
    private class BoundsTracker extends ComponentAdapter implements HierarchyBoundsListener, HierarchyListener {
        private void update() {
            updateFocusBounds();
        }

        @Override
        public void componentResized(ComponentEvent e) {
            update();
        }

        @Override
        public void componentMoved(ComponentEvent e) {
            update();
        }

        @Override
        public void componentHidden(ComponentEvent e) {
            update();
        }

        @Override
        public void componentShown(ComponentEvent e) {
            update();
        }

        @Override
        public void ancestorMoved(HierarchyEvent e) {
            update();
        }

        @Override
        public void ancestorResized(HierarchyEvent e) {
            update();
        }

        @Override
        public void hierarchyChanged(HierarchyEvent e) {
            if ((e.getChangeFlags() & (HierarchyEvent.SHOWING_CHANGED | HierarchyEvent.DISPLAYABILITY_CHANGED)) != 0) {
                update();
            }
        }
    }
}