package eggdepot.gui.components.chip;

import eggdepot.gui.components.WrapLayout;
import eggdepot.gui.plaf.Theme;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Manages the collection of chips, their layout (collapsed vs expanded), and
 * scrolling capabilities.
 * Optimized with caching for layout dimensions.
 */
public class ChipList extends JPanel implements Scrollable {
    private static final int GAP = Theme.SPACING_SM;
    private static final int SCROLL_INCREMENT = 16;

    private final Map<String, Chip> chips = new HashMap<>();
    private final ButtonGroup group = new ButtonGroup();
    private final FlowLayout collapsedLayout = new FlowLayout(FlowLayout.LEFT, GAP, GAP);
    private final WrapLayout expandedLayout = new WrapLayout(FlowLayout.LEFT, GAP, GAP);

    private String selectedName;
    private boolean expanded = false;
    private Consumer<String> chipClickedListener;

    // Cache to avoid O(N) calculations on every resize/check
    private int cachedSingleRowWidth = -1;
    private int cachedChipHeight = -1;

    public ChipList() {
        setLayout(collapsedLayout);
        setOpaque(false);
    }

    /**
     * listener triggered when a chip is clicked by the user.
     * The listener receives the name of the clicked chip.
     */
    public void setOnChipClicked(Consumer<String> listener) {
        this.chipClickedListener = listener;
    }

    public void addChip(String name) {
        if (chips.containsKey(name)) {
            return;
        }

        Chip chip = new Chip(name);
        chip.addActionListener(_ -> {
            if (chipClickedListener != null) {
                chipClickedListener.accept(name);
            }
        });

        group.add(chip);
        add(chip);
        chips.put(name, chip);

        invalidateCache();
    }

    public void select(String name) {
        selectedName = name;

        Chip chip = chips.get(name);
        if (chip != null) {
            chip.setSelected(true);
        }
    }

    public String getSelectedName() {
        return selectedName;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        if (this.expanded == expanded) {
            return;
        }
        this.expanded = expanded;
        setLayout(expanded ? expandedLayout : collapsedLayout);
        revalidate();
        repaint();
    }

    public int getChipCount() {
        return chips.size();
    }

    public int getSingleRowWidth() {
        if (cachedSingleRowWidth < 0) {
            cachedSingleRowWidth = collapsedLayout.preferredLayoutSize(this).width;
        }
        return cachedSingleRowWidth;
    }

    public int getChipHeight() {
        if (chips.isEmpty()) {
            return 0;
        }
        if (cachedChipHeight < 0) {
            cachedChipHeight = chips.values().iterator().next().getPreferredSize().height;
        }
        return cachedChipHeight;
    }

    private void invalidateCache() {
        cachedSingleRowWidth = -1;
        cachedChipHeight = -1;
    }

    // -- Scrollable Implementation --

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visible, int orientation, int direction) {
        return SCROLL_INCREMENT;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visible, int orientation, int direction) {
        return visible.width;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return expanded;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
}
