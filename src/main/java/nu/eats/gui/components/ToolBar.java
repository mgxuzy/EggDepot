package nu.eats.gui.components;

import nu.eats.gui.plaf.Theme;
import nu.eats.gui.plaf.border.framed.FramedBorder;
import nu.eats.gui.plaf.button.ButtonPreset;
import nu.eats.gui.plaf.button.ButtonState;
import nu.eats.gui.plaf.button.ButtonVariant;
import nu.eats.gui.plaf.icons.ChevronLeftIcon;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ToolBar extends JPanel {

    public static final String LEFT = "Left";
    public static final String CENTER = "Center";
    public static final String RIGHT = "Right";

    public ToolBar() {
        super(new ActionBarLayout(Theme.SPACING_SM));

        this.setBorder(new FramedBorder.Builder()
                .sides(side -> side.padding(Theme.SPACING_SM))
                .build()
        );
    }

    public void setTitle(String title) {
        if (title != null) {
            var label = new JLabel(title);

            label.setFont(Theme.FONT_BOLD_LG);
            label.setForeground(Theme.COLOR_FG_INVERSE);
            add(label, CENTER);
        }
    }

    public void addAction(Runnable handler) {
        var button = new JButton(new ChevronLeftIcon(12));

        ButtonPreset.SM.apply(button);
        ButtonVariant.SECONDARY.install(button, ButtonState.NEUTRAL);
        button.addActionListener(event -> handler.run());

        add(button, LEFT);
    }

    public static class ActionBarLayout implements LayoutManager {
        public static final String LEFT = ToolBar.LEFT;
        public static final String CENTER = ToolBar.CENTER;
        public static final String RIGHT = ToolBar.RIGHT;

        private final double gap;
        private final List<Component> leftComponents = new ArrayList<>();
        private final List<Component> rightComponents = new ArrayList<>();
        private Component centerComponent;

        public ActionBarLayout(double gap) {
            this.gap = gap;
        }

        @Override
        public void addLayoutComponent(String name, Component component) {
            if (name == null) {
                return;
            }

            switch (name) {
                case LEFT -> leftComponents.add(component);
                case CENTER -> centerComponent = component;
                case RIGHT -> rightComponents.add(component);
            }
        }

        @Override
        public void removeLayoutComponent(Component component) {
            leftComponents.remove(component);
            rightComponents.remove(component);

            if (component == centerComponent) {
                centerComponent = null;
            }
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            var leftDim = measureGroup(leftComponents);
            var rightDim = measureGroup(rightComponents);

            var centerDim = centerComponent != null
                    ? centerComponent.getPreferredSize()
                    : new Dimension(0, 0);

            var insets = parent.getInsets();
            var totalWidth = leftDim.width + centerDim.width + rightDim.width
                    + insets.left + insets.right;
            var maxHeight = Math.max(leftDim.height, Math.max(centerDim.height, rightDim.height))
                    + insets.top + insets.bottom;

            return new Dimension(totalWidth, maxHeight);
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            return preferredLayoutSize(parent);
        }

        @Override
        public void layoutContainer(Container parent) {
            var insets = parent.getInsets();
            var width = parent.getWidth();
            var height = parent.getHeight();

            var top = insets.top;
            var availableHeight = height - top - insets.bottom;

            // 1. Layout Left Components
            var currentX = insets.left;

            currentX = layoutGroup(leftComponents, currentX, top, availableHeight);

            var leftEdgeLimit = currentX;

            // 2. Layout Right Components
            var rightGroupWidth = measureGroup(rightComponents).width;
            var rightStartX = width - insets.right - rightGroupWidth;

            layoutGroup(rightComponents, rightStartX, top, availableHeight);

            // 3. Layout Center Component
            if (centerComponent != null) {
                var size = centerComponent.getPreferredSize();
                var idealCenterX = (width - size.width) / 2;
                var actualCenterX = Math.max(leftEdgeLimit, idealCenterX);
                var actualCenterY = top + (availableHeight - size.height) / 2;

                centerComponent.setBounds(actualCenterX, actualCenterY, size.width, size.height);
            }
        }

        private Dimension measureGroup(List<Component> components) {
            var totalWidth = 0;
            var maxHeight = 0;

            if (!components.isEmpty()) {
                for (var component : components) {
                    var size = component.getPreferredSize();
                    totalWidth += size.width;
                    maxHeight = Math.max(maxHeight, size.height);
                }

                totalWidth += (components.size() - 1) * gap;
            }

            return new Dimension(totalWidth, maxHeight);
        }

        private int layoutGroup(List<Component> components, int startX, int topY, int availableHeight) {
            var currentX = startX;

            for (var component : components) {
                var size = component.getPreferredSize();
                var y = topY + (availableHeight - size.height) / 2;

                component.setBounds(currentX, y, size.width, size.height);
                currentX += size.width + gap;
            }

            return currentX;
        }
    }

}