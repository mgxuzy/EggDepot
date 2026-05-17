package nu.eats.gui.plaf.root.components;

import nu.eats.gui.plaf.Constants;
import nu.eats.gui.plaf.Theme;
import nu.eats.gui.plaf.button.ButtonVariant;
import nu.eats.gui.plaf.icons.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class TitlePane extends JComponent {
    private static final int ICON_SIZE = 12;

    private final List<JButton> windowButtons;
    private final JButton closeButton;
    private final JButton maximizeButton;
    private final JButton minimizeButton;
    private final JButton backButton;
    private final WindowAdapter windowListener;
    private final PropertyChangeListener propertyChangeListener;
    private String title = "";
    private Color titleBarColor;
    private boolean isPrimaryStyle;
    private Window window;

    public TitlePane(JRootPane rootPane) {
        setLayout(null);
        setBackground(Theme.COLOR_BG);
        setOpaque(false);

        closeButton = createButton(new CloseIcon(ICON_SIZE));
        maximizeButton = createButton(new MaximizeIcon(ICON_SIZE));
        minimizeButton = createButton(new MinimizeIcon(ICON_SIZE));

        backButton = createButton(new ChevronLeftIcon(ICON_SIZE));
        backButton.setVisible(false);

        windowButtons = List.of(closeButton, maximizeButton, minimizeButton, backButton);

        windowButtons.forEach(this::add);

        closeButton.addActionListener(_ -> close());
        maximizeButton.addActionListener(_ -> toggleMaximize());
        minimizeButton.addActionListener(_ -> minimize());

        windowListener = new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                repaint();
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                repaint();
            }

            @Override
            public void windowStateChanged(WindowEvent e) {
                updateMaximizeIcon();
            }
        };

        propertyChangeListener = event -> {
            if ("title".equals(event.getPropertyName())) {
                title = (String) event.getNewValue();

                repaint();
            }
        };

        setTitleBarColor(Theme.COLOR_PRIMARY);
    }

    private JButton createButton(Icon icon) {
        JButton button = new JButton(icon);

        button.putClientProperty(Constants.KEY_COMPONENT_VARIANT, ButtonVariant.FLAT);

        return button;
    }

    private void close() {
        if (window != null) {
            window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
        }
    }

    private void toggleMaximize() {
        if (window instanceof Frame frame) {
            int state = frame.getExtendedState();
            boolean isMaximized = (state & Frame.MAXIMIZED_BOTH) != 0;

            frame.setExtendedState(isMaximized ? state & ~Frame.MAXIMIZED_BOTH : state | Frame.MAXIMIZED_BOTH);
        }
    }

    private void minimize() {
        if (window instanceof Frame frame) {
            frame.setExtendedState(Frame.ICONIFIED);
        }
    }

    private void updateMaximizeIcon() {
        if (window instanceof Frame frame) {
            boolean maximized = (frame.getExtendedState() & Frame.MAXIMIZED_BOTH) != 0;

            maximizeButton.setIcon(maximized ? new RestoreIcon(ICON_SIZE) : new MaximizeIcon(ICON_SIZE));
        }
    }

    public void setBackVisible(boolean visible) {
        backButton.setVisible(visible);

        repaint();
        revalidate();
    }

    public JButton getBackButton() {
        return backButton;
    }

    public void setTitleBarColor(Color color) {
        this.titleBarColor = color;
        this.isPrimaryStyle = Theme.COLOR_PRIMARY.equals(color);

        Color fg = isPrimaryStyle ? Theme.COLOR_FG_INVERSE : Theme.COLOR_FG;
        ButtonVariant variant = isPrimaryStyle ? ButtonVariant.GHOST : ButtonVariant.FLAT;

        for (JButton button : windowButtons) {
            button.setForeground(fg);
            button.putClientProperty(Constants.KEY_COMPONENT_VARIANT, variant);
        }

        repaint();
    }

    @Override
    public void addNotify() {
        super.addNotify();

        uninstallWindowListeners();

        window = SwingUtilities.getWindowAncestor(this);

        if (window != null) {
            window.addWindowListener(windowListener);
            window.addWindowStateListener(windowListener);
            window.addPropertyChangeListener(propertyChangeListener);

            updateMaximizeIcon();

            title = resolveWindowTitle();

            repaint();
        }
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        uninstallWindowListeners();
        window = null;
    }

    private String resolveWindowTitle() {
        if (window instanceof Frame frame) {
            return frame.getTitle() != null ? frame.getTitle() : "";
        } else if (window instanceof Dialog dialog) {
            return dialog.getTitle() != null ? dialog.getTitle() : "";
        }

        return "";
    }

    private void uninstallWindowListeners() {
        if (window == null) return;

        window.removeWindowListener(windowListener);
        window.removeWindowStateListener(windowListener);
        window.removePropertyChangeListener(propertyChangeListener);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(super.getPreferredSize().width, Theme.TITLE_BAR_HEIGHT);
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHints(Constants.DEFAULT_RENDERING_HINTS);

        if (titleBarColor != null) {
            g2.setColor(titleBarColor);
            g2.fillRect(0, 0, getWidth(), getHeight());
        }

        if (title != null && !title.isEmpty()) {
            g2.setFont(Theme.FONT_MEDIUM_14);
            g2.setColor(resolveTitleForeground());

            FontMetrics fm = g2.getFontMetrics();

            float y = (getHeight() - fm.getHeight()) / 2f + fm.getAscent();
            int x = Theme.SPACING_MD;

            if (backButton.isVisible()) {
                x += Theme.TITLE_BAR_BUTTON_WIDTH;
            }

            g2.drawString(title, x, y);
        }

        g2.dispose();
    }

    private Color resolveTitleForeground() {
        boolean active = window != null && window.isActive();

        if (isPrimaryStyle) {
            return active ? Theme.COLOR_FG_INVERSE : Theme.ZINC_400;
        }

        return active ? Theme.COLOR_FG : Theme.COLOR_FG_MUTED;
    }

    @Override
    public void doLayout() {
        int w = getWidth();
        int h = getHeight();
        int x = w;

        x -= Theme.TITLE_BAR_BUTTON_WIDTH;
        closeButton.setBounds(x, 0, Theme.TITLE_BAR_BUTTON_WIDTH, h);

        boolean showMinMax = isResizable() && !(window instanceof Dialog);

        if (showMinMax) {
            x = layoutButtonBefore(maximizeButton, x, h);
            layoutButtonBefore(minimizeButton, x, h);

            maximizeButton.setVisible(true);
            minimizeButton.setVisible(true);
        } else {
            maximizeButton.setVisible(false);
            minimizeButton.setVisible(false);
        }

        if (backButton.isVisible()) {
            backButton.setBounds(0, 0, Theme.TITLE_BAR_BUTTON_WIDTH, h);
        }
    }

    private boolean isResizable() {
        if (window instanceof Frame frame) {
            return frame.isResizable();
        } else if (window instanceof Dialog dialog) {
            return dialog.isResizable();
        }

        return true;
    }

    /**
     * Positions a button to the left of the given x-coordinate and returns the new x-coordinate.
     */
    private int layoutButtonBefore(JButton button, int x, int h) {
        x -= Theme.TITLE_BAR_BUTTON_GAP + Theme.TITLE_BAR_BUTTON_WIDTH;

        button.setBounds(x, 0, Theme.TITLE_BAR_BUTTON_WIDTH, h);

        return x;
    }
}