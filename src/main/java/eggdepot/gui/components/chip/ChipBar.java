package eggdepot.gui.components.chip;

import eggdepot.gui.plaf.Theme;
import eggdepot.gui.plaf.button.ButtonVariant;
import eggdepot.gui.plaf.icon.ChevronDownIcon;
import eggdepot.gui.plaf.icon.ChevronUpIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.function.Consumer;

import static eggdepot.gui.plaf.Constants.KEY_COMPONENT_VARIANT;

/**
 * A horizontal chip navigation bar with an expand/collapse toggle.
 * <p>
 * Decoupled implementation:
 * - {@link ChipList} handles the chips, layout switching, and scrolling
 * mechanics.
 * - {@link ChipBar} coordinates the scrolling view and the expansion toggle.
 */
public class ChipBar extends JPanel {
    private static final String ALL = "All";
    private static final int GAP = Theme.SPACING_SM;

    private final ChipList list;
    private final JScrollPane scroll;
    private final JButton expandButton;

    private Consumer<String> onCategorySelected = _ -> {
    };

    public ChipBar() {
        setLayout(new BorderLayout());
        setOpaque(false);

        // List
        list = new ChipList();
        list.setOnChipClicked(this::handleChipClick);

        // Scroll
        scroll = new JScrollPane(list);

        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scroll.getHorizontalScrollBar().setVisible(false);
        scroll.setWheelScrollingEnabled(false);
        scroll.addMouseWheelListener(event -> {
            if (list.isExpanded()) {
                // Bubble up when expanded (vertical scroll usually handled by parent)
                getParent().dispatchEvent(SwingUtilities.convertMouseEvent(scroll, event, getParent()));

                return;
            }

            var bar = scroll.getHorizontalScrollBar();

            bar.setValue(bar.getValue() + event.getWheelRotation() * bar.getUnitIncrement());
        });

        add(scroll, BorderLayout.CENTER);

        // Expand Button
        expandButton = new JButton(new ChevronDownIcon(12));
        expandButton.putClientProperty(KEY_COMPONENT_VARIANT, ButtonVariant.FLAT);
        expandButton.setVisible(false);
        expandButton.addActionListener(_ -> toggleExpansion());

        var buttonWrapper = new JPanel(new BorderLayout());

        buttonWrapper.setOpaque(false);
        buttonWrapper.setBorder(new EmptyBorder(GAP, 0, 0, 0)); // Align with chip top gap
        buttonWrapper.add(expandButton, BorderLayout.NORTH);

        add(buttonWrapper, BorderLayout.EAST);

        // Init
        addChip(ALL);
        select(ALL);

        // Listeners
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                checkOverflow();
            }
        });
    }

    public void setOnCategorySelected(Consumer<String> listener) {
        this.onCategorySelected = listener;
    }

    public void addChip(String name) {
        list.addChip(name);
        syncButtonHeight();
        SwingUtilities.invokeLater(this::checkOverflow);
    }

    public void select(String categoryName) {
        String current = list.getSelectedName();
        if (categoryName.equals(current))
            return;

        list.select(categoryName);
        onCategorySelected.accept(categoryName);
    }

    private void handleChipClick(String name) {
        String current = list.getSelectedName();
        // Toggle logic: tapping selected reverts to ALL
        String target = name.equals(current) ? ALL : name;
        select(target);
    }

    private void toggleExpansion() {
        boolean expanding = !list.isExpanded();
        list.setExpanded(expanding);
        expandButton.setIcon(expanding ? new ChevronUpIcon(12) : new ChevronDownIcon(12));

        if (!expanding) {
            scroll.getViewport().setViewPosition(new Point(0, 0));
        }

        revalidate();
        repaint();
    }

    private void checkOverflow() {
        if (list.getChipCount() <= 1) {
            expandButton.setVisible(false);
            return;
        }

        int contentWidth = list.getSingleRowWidth();
        int viewportWidth = scroll.getViewport().getWidth();
        // If viewport is not yet laid out, use container width
        if (viewportWidth == 0)
            viewportWidth = getWidth();

        boolean overflow = contentWidth > viewportWidth && viewportWidth > 0;
        expandButton.setVisible(overflow);

        // Auto-collapse if it fits
        if (!overflow && list.isExpanded()) {
            toggleExpansion();
        }
    }

    private void syncButtonHeight() {
        // Match button height to chip height effectively
        int h = list.getChipHeight();
        if (h > 0) {
            expandButton.setPreferredSize(new Dimension(expandButton.getPreferredSize().width, h));
        }
    }
}