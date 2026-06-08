package nu.eats.gui.plaf.root;

import nu.eats.gui.plaf.WindowResizeHandler;
import nu.eats.gui.plaf.border.framed.FramedBorder;
import nu.eats.gui.plaf.outline.FocusOutline;
import nu.eats.gui.plaf.root.components.TitlePane;
import nu.eats.gui.plaf.root.components.WindowBorder;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicRootPaneUI;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.util.function.Function;

public class RootPaneUI extends BasicRootPaneUI {

    private static final boolean SHOW_WINDOW_BORDER = false;

    private TitlePane titlePane;
    private WindowResizeHandler windowResizeHandler;
    private LayoutManager oldLayoutManager;
    private JRootPane rootPane;

    public static ComponentUI createUI(JComponent component) {
        return new RootPaneUI();
    }

    @Override
    public void installUI(JComponent component) {
        super.installUI(component);

        rootPane = (JRootPane) component;

        rootPane.setOpaque(false);

        // rootPane.setDoubleBuffered(true);
        rootPane.setBorder(FramedBorder.NONE);
        // rootPane.setBackground(Theme.COLOR_BG);

        if (rootPane.getWindowDecorationStyle() != JRootPane.NONE) {
            installClientDecorations(rootPane);
        }

        Container contentPane = rootPane.getContentPane();

        if (contentPane != null) {
            // contentPane.setBackground(Theme.COLOR_BG);
        }

        FocusOutline.install(rootPane);
    }

    @Override
    public void uninstallUI(JComponent component) {
        uninstallClientDecorations(rootPane);

        rootPane = null;

        super.uninstallUI(component);
    }

    @Override
    public void paint(Graphics graphics, JComponent component) {
        if (component.getBorder() instanceof FramedBorder border) {
            border.paintClientRegionWith(super::paint, graphics, component);

            return;
        }

        super.paint(graphics, component);
    }

    private void installClientDecorations(JRootPane root) {
        installTitlePane(root);
        installBorder(root);
    }

    private void uninstallClientDecorations(JRootPane root) {
        uninstallTitlePane(root);
        uninstallBorder(root);
    }

    private void installBorder(JRootPane root) {
        if (root.getWindowDecorationStyle() == JRootPane.NONE || !SHOW_WINDOW_BORDER) {
            return;
        }

        Border border = root.getBorder();

        if (border == null || border instanceof UIResource) {
            root.setBorder(new WindowBorder());
        }
    }

    private void uninstallBorder(JRootPane root) {
        if (root.getBorder() instanceof WindowBorder) {
            root.setBorder(null);
        }
    }

    private void installTitlePane(JRootPane root) {
        if (titlePane != null || root.getWindowDecorationStyle() == JRootPane.NONE) {
            return;
        }

        titlePane = new TitlePane(root);

        root.add(titlePane);

        windowResizeHandler = new WindowResizeHandler(root, titlePane);

        root.addMouseListener(windowResizeHandler);
        root.addMouseMotionListener(windowResizeHandler);

        oldLayoutManager = root.getLayout();

        root.setLayout(new RootLayout());
    }

    private void uninstallTitlePane(JRootPane root) {
        if (titlePane == null) {
            return;
        }

        root.removeMouseListener(windowResizeHandler);
        root.removeMouseMotionListener(windowResizeHandler);
        titlePane.removeMouseListener(windowResizeHandler);
        titlePane.removeMouseMotionListener(windowResizeHandler);

        root.remove(titlePane);

        if (oldLayoutManager != null) {
            root.setLayout(oldLayoutManager);
        }

        titlePane = null;
        windowResizeHandler = null;
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        super.propertyChange(event);

        if ("windowDecorationStyle".equals(event.getPropertyName())) {
            JRootPane root = (JRootPane) event.getSource();

            uninstallClientDecorations(root);

            if (root.getWindowDecorationStyle() != JRootPane.NONE) {
                installClientDecorations(root);
            }
        }
    }

    private class RootLayout implements LayoutManager2 {
        @Override
        public void addLayoutComponent(String name, Component comp) {
        }

        @Override
        public void removeLayoutComponent(Component comp) {
        }

        @Override
        public void addLayoutComponent(Component comp, Object constraints) {
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            return computeLayoutSize(Component::getPreferredSize);
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            return computeLayoutSize(Component::getMinimumSize);
        }

        private Dimension computeLayoutSize(Function<Component, Dimension> sizeFn) {
            Dimension contentPaneDimension = rootPane.getContentPane() != null
                    ? sizeFn.apply(rootPane.getContentPane())
                    : new Dimension(0, 0);

            int titleHeight = 0;
            int titleWidth = 0;

            if (titlePane != null && titlePane.isVisible()) {
                Dimension titleSize = sizeFn.apply(titlePane);
                titleHeight = titleSize.height;
                titleWidth = titleSize.width;
            }

            Insets inset = rootPane.getInsets();

            return new Dimension(
                    Math.max(contentPaneDimension.width, titleWidth) + inset.left + inset.right,
                    contentPaneDimension.height + titleHeight + inset.top + inset.bottom);
        }

        @Override
        public Dimension maximumLayoutSize(Container target) {
            return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
        }

        @Override
        public float getLayoutAlignmentX(Container target) {
            return 0.5f;
        }

        @Override
        public float getLayoutAlignmentY(Container target) {
            return 0.5f;
        }

        @Override
        public void invalidateLayout(Container target) {
        }

        @Override
        public void layoutContainer(Container parent) {
            JRootPane root = (JRootPane) parent;
            Rectangle bounds = root.getBounds();
            Insets insets = root.getInsets();

            int w = bounds.width - insets.left - insets.right;
            int h = bounds.height - insets.top - insets.bottom;
            int x = insets.left;
            int y = insets.top;

            int titleHeight = 0;

            if (titlePane != null && titlePane.isVisible()) {
                titleHeight = titlePane.getPreferredSize().height;
                titlePane.setBounds(x, y, w, titleHeight);
            }

            int contentY = y + titleHeight;
            int contentH = h - titleHeight;

            if (root.getLayeredPane() != null) {
                root.getLayeredPane().setBounds(x, contentY, w, contentH);
                layoutContentWithinLayeredPane(root, w, contentH);
            }

            if (root.getGlassPane() != null) {
                root.getGlassPane().setBounds(x, y, w, h);
            }
        }

        private void layoutContentWithinLayeredPane(JRootPane root, int w, int contentH) {
            Container content = root.getContentPane();
            JMenuBar menuBar = root.getJMenuBar();

            if (menuBar != null && menuBar.getParent() == root.getLayeredPane()) {
                int menuHeight = menuBar.getPreferredSize().height;
                menuBar.setBounds(0, 0, w, menuHeight);

                if (content != null && content.getParent() == root.getLayeredPane()) {
                    content.setBounds(0, menuHeight, w, contentH - menuHeight);
                }
            } else if (content != null && content.getParent() == root.getLayeredPane()) {
                content.setBounds(0, 0, w, contentH);
            }
        }
    }
}