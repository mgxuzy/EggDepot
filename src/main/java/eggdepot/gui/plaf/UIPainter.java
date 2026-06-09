package eggdepot.gui.plaf;

import javax.swing.*;
import java.awt.*;

@FunctionalInterface
public interface UIPainter {
    void paint(Graphics graphics, JComponent component);
}
