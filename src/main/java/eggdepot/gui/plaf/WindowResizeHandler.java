package eggdepot.gui.plaf;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;

public class WindowResizeHandler implements MouseInputListener {

    private static final int BORDER_DRAG_THICKNESS = 7;
    private static final int CORNER_DRAG_WIDTH = 16;
    private static final int GRID_COLS = 5;

    private static final int POS_OUT_OF_BOUNDS = -1;
    private static final int POS_NEAR_START = 0;
    private static final int POS_CORNER_START = 1;
    private static final int POS_MIDDLE = 2;
    private static final int POS_CORNER_END = 3;
    private static final int POS_NEAR_END = 4;

    private static final int[] CURSOR_MAPPING = {
            Cursor.NW_RESIZE_CURSOR, Cursor.NW_RESIZE_CURSOR, Cursor.N_RESIZE_CURSOR,
            Cursor.NE_RESIZE_CURSOR, Cursor.NE_RESIZE_CURSOR,
            Cursor.NW_RESIZE_CURSOR, 0, 0, 0, Cursor.NE_RESIZE_CURSOR,
            Cursor.W_RESIZE_CURSOR, 0, 0, 0, Cursor.E_RESIZE_CURSOR,
            Cursor.SW_RESIZE_CURSOR, 0, 0, 0, Cursor.SE_RESIZE_CURSOR,
            Cursor.SW_RESIZE_CURSOR, Cursor.SW_RESIZE_CURSOR, Cursor.S_RESIZE_CURSOR,
            Cursor.SE_RESIZE_CURSOR, Cursor.SE_RESIZE_CURSOR
    };

    private final JRootPane rootPane;
    private final Component titlePane;

    private boolean isDraggingWindow;
    private int dragCursorType;
    private Cursor lastCursor = Cursor.getDefaultCursor();

    private Point startDrag;
    private Rectangle startBounds;

    public WindowResizeHandler(JRootPane rootPane, Component titlePane) {
        this.rootPane = rootPane;
        this.titlePane = titlePane;
    }

    private static int calculatePosition(int spot, int width) {
        if (spot < BORDER_DRAG_THICKNESS) return POS_NEAR_START;
        if (spot < CORNER_DRAG_WIDTH) return POS_CORNER_START;
        if (spot >= width - BORDER_DRAG_THICKNESS) return POS_NEAR_END;
        if (spot >= width - CORNER_DRAG_WIDTH) return POS_CORNER_END;

        return POS_MIDDLE;
    }

    private static int getCursorType(int corner) {
        return corner == POS_OUT_OF_BOUNDS ? 0 : CURSOR_MAPPING[corner];
    }

    private static Window getWindow(MouseEvent event) {
        Object source = event.getSource();

        if (source instanceof Window window) return window;
        if (source instanceof Component component) return SwingUtilities.getWindowAncestor(component);

        return null;
    }

    private static Point toWindowPoint(MouseEvent event, Window window) {
        Component source = (Component) event.getSource();

        return source == window
                ? event.getPoint()
                : SwingUtilities.convertPoint(source, event.getPoint(), window);
    }

    private static boolean isResizable(Window window) {
        if (window instanceof Frame frame)
            return frame.isResizable() && (frame.getExtendedState() & Frame.MAXIMIZED_BOTH) == 0;

        if (window instanceof Dialog dialog) return dialog.isResizable();

        return false;
    }

    @Override
    public void mousePressed(MouseEvent event) {
        if (rootPane.getWindowDecorationStyle() == JRootPane.NONE) return;

        Window window = getWindow(event);
        if (window == null) return;

        Point point = toWindowPoint(event, window);

        startDrag = event.getLocationOnScreen();
        startBounds = window.getBounds();

        int cursorType = resolveCursorType(window, point);

        if (cursorType != 0 && isResizable(window)) {
            dragCursorType = cursorType;
            isDraggingWindow = false;

            return;
        }

        dragCursorType = 0;

        if (!isOverTitlePane(window, point)) {
            isDraggingWindow = false;

            return;
        }

        boolean maximized = (window instanceof Frame frame)
                && (frame.getExtendedState() & Frame.MAXIMIZED_BOTH) != 0;

        isDraggingWindow = !maximized;
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        Window window = getWindow(event);

        if (window == null || startDrag == null || startBounds == null) return;

        Point current = event.getLocationOnScreen();
        int dx = current.x - startDrag.x;
        int dy = current.y - startDrag.y;

        if (isDraggingWindow) {
            window.setLocation(startBounds.x + dx, startBounds.y + dy);

            return;
        }

        if (dragCursorType == 0) return;

        window.setBounds(computeResizeBounds(dx, dy, window.getMinimumSize()));
        rootPane.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        isDraggingWindow = false;
        dragCursorType = 0;
    }

    @Override
    public void mouseMoved(MouseEvent event) {
        if (rootPane.getWindowDecorationStyle() == JRootPane.NONE) return;

        Window window = getWindow(event);

        if (window == null) return;

        Point point = toWindowPoint(event, window);
        int cursorType = resolveCursorType(window, point);

        window.setCursor(cursorType != 0 && isResizable(window)
                ? Cursor.getPredefinedCursor(cursorType)
                : lastCursor);
    }

    @Override
    public void mouseEntered(MouseEvent event) {
        Window window = getWindow(event);

        if (window == null) return;

        lastCursor = window.getCursor();

        mouseMoved(event);
    }

    @Override
    public void mouseExited(MouseEvent event) {
        Window window = getWindow(event);

        if (window == null) return;

        window.setCursor(lastCursor);
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        Window window = getWindow(event);

        if (!(window instanceof Frame frame) || titlePane == null) return;

        Point point = toWindowPoint(event, window);

        if (!titlePane.contains(SwingUtilities.convertPoint(window, point, titlePane))) return;

        boolean isDoubleLeftClick = SwingUtilities.isLeftMouseButton(event)
                && (event.getClickCount() % 2) == 0;

        if (!isDoubleLeftClick || !frame.isResizable()) return;

        int state = frame.getExtendedState();

        frame.setExtendedState((state & Frame.MAXIMIZED_BOTH) != 0
                ? state & ~Frame.MAXIMIZED_BOTH
                : state | Frame.MAXIMIZED_BOTH);
    }

    private Rectangle computeResizeBounds(int dx, int dy, Dimension min) {
        int left = startBounds.x;
        int top = startBounds.y;
        int right = startBounds.x + startBounds.width;
        int bottom = startBounds.y + startBounds.height;

        switch (dragCursorType) {
            case Cursor.N_RESIZE_CURSOR -> top = Math.min(top + dy, bottom - min.height);
            case Cursor.S_RESIZE_CURSOR -> bottom = Math.max(bottom + dy, top + min.height);
            case Cursor.W_RESIZE_CURSOR -> left = Math.min(left + dx, right - min.width);
            case Cursor.E_RESIZE_CURSOR -> right = Math.max(right + dx, left + min.width);
            case Cursor.NW_RESIZE_CURSOR -> {
                top = Math.min(top + dy, bottom - min.height);
                left = Math.min(left + dx, right - min.width);
            }
            case Cursor.NE_RESIZE_CURSOR -> {
                top = Math.min(top + dy, bottom - min.height);
                right = Math.max(right + dx, left + min.width);
            }
            case Cursor.SW_RESIZE_CURSOR -> {
                bottom = Math.max(bottom + dy, top + min.height);
                left = Math.min(left + dx, right - min.width);
            }
            case Cursor.SE_RESIZE_CURSOR -> {
                bottom = Math.max(bottom + dy, top + min.height);
                right = Math.max(right + dx, left + min.width);
            }
        }

        return new Rectangle(left, top, right - left, bottom - top);
    }

    private int resolveCursorType(Window window, Point point) {
        return getCursorType(calculateCorner(window, point.x, point.y));
    }

    private int calculateCorner(Window window, int x, int y) {
        Insets insets = window.getInsets();

        var leftInset = insets.left;
        var topInset = insets.top;

        int positionX = calculatePosition(x - leftInset, window.getWidth() - leftInset - insets.right);
        int positionY = calculatePosition(y - topInset, window.getHeight() - topInset - insets.bottom);

        if (positionX == POS_OUT_OF_BOUNDS || positionY == POS_OUT_OF_BOUNDS) return POS_OUT_OF_BOUNDS;

        return positionY * GRID_COLS + positionX;
    }

    private boolean isOverTitlePane(Window window, Point windowPoint) {
        if (titlePane == null || !titlePane.isVisible()) return false;

        return titlePane.contains(SwingUtilities.convertPoint(window, windowPoint, titlePane));
    }
}