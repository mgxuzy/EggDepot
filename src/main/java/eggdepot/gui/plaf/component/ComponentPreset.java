package eggdepot.gui.plaf.component;

import javax.swing.*;

public interface ComponentPreset<T extends JComponent> {
    void apply(T component);
}
