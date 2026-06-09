package eggdepot.gui.plaf.root.components;

import eggdepot.gui.plaf.Constants;
import eggdepot.gui.plaf.Theme;
import eggdepot.gui.plaf.border.framed.FramedBorder;
import eggdepot.gui.plaf.button.ButtonVariant;
import eggdepot.gui.plaf.icon.CloseIcon;
import eggdepot.gui.plaf.icon.MaximizeIcon;
import eggdepot.gui.plaf.icon.MinimizeIcon;
import eggdepot.gui.plaf.icon.RestoreIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class TitlePane extends JComponent {
    private static final int ICON_SIZE = 12;

    private final JButton closeButton;
    private final JButton maximizeButton, minimizeButton;
    private final List<JButton> windowButtons;

    // Declared as WindowAdapter to support both WindowListener and WindowStateListener
    private final WindowAdapter windowListener;
    private final PropertyChangeListener propertyChangeListener;

    private String title = "";
    private Window window;

    public TitlePane(JRootPane rootPane) {
        setLayout(null);
        setOpaque(true);

        closeButton = createButton(new CloseIcon(ICON_SIZE));
        maximizeButton = createButton(new MaximizeIcon(ICON_SIZE));
        minimizeButton = createButton(new MinimizeIcon(ICON_SIZE));

        windowButtons = List.of(closeButton, maximizeButton, minimizeButton);
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

        setBackground(Theme.COLOR_BG_PRESSED);
        setForeground(Theme.COLOR_FG_PRIMARY);
    }

    private JButton createButton(Icon icon) {
        JButton button = new JButton(icon);

        ButtonVariant.PRIMARY.install(button);
        button.setBorder(FramedBorder.NONE);

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

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);

        repaint();
    }

    @Override
    public void setForeground(Color fg) {
        super.setForeground(fg);

        if (windowButtons != null) {
            for (JButton button : windowButtons) {
                button.setForeground(fg);
            }
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
        if (window != null) {
            window.removeWindowListener(windowListener);
            window.removeWindowStateListener(windowListener);
            window.removePropertyChangeListener(propertyChangeListener);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(0, Theme.TITLE_BAR_HEIGHT);
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHints(Constants.DEFAULT_RENDERING_HINTS);

        Color bg = getBackground();

        if (bg != null) {
            g2.setColor(bg);
            g2.fillRect(0, 0, getWidth(), getHeight());
        }

        if (title != null && !title.isEmpty()) {
            g2.setFont(Theme.FONT_MEDIUM_SM);
            g2.setColor(getForeground());

            FontMetrics fm = g2.getFontMetrics();
            float y = (getHeight() - fm.getHeight()) / 2f + fm.getAscent();
            int x = Theme.SPACING_4XL;

            g2.drawString(title, x, y);
        }

        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    public void doLayout() {
        int w = getWidth();
        int h = getHeight();


        int x = w - Theme.TITLE_BAR_BUTTON_WIDTH;

        closeButton.setBounds(x, 0, Theme.TITLE_BAR_BUTTON_WIDTH, h);

        boolean showMinMax = isResizable() && !(window instanceof Dialog);

        maximizeButton.setVisible(showMinMax);
        minimizeButton.setVisible(showMinMax);

        if (showMinMax) {
            x -= (Theme.TITLE_BAR_BUTTON_GAP + Theme.TITLE_BAR_BUTTON_WIDTH);
            maximizeButton.setBounds(x, 0, Theme.TITLE_BAR_BUTTON_WIDTH, h);

            x -= (Theme.TITLE_BAR_BUTTON_GAP + Theme.TITLE_BAR_BUTTON_WIDTH);
            minimizeButton.setBounds(x, 0, Theme.TITLE_BAR_BUTTON_WIDTH, h);
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
}