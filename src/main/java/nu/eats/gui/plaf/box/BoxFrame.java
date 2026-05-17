package nu.eats.gui.plaf.box;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;

public class BoxBorder extends AbstractBorder {
    private BoxBorderSide top;
    private BoxBorderSide left;
    private BoxBorderSide bottom;
    private BoxBorderSide right;

    public BoxBorder(BoxBorderSide top, BoxBorderSide left, BoxBorderSide bottom, BoxBorderSide right) {
        this.top = top;
        this.left = left;
        this.bottom = bottom;
        this.right = right;
    }

    public BoxBorder(Color color, double thickness) {
        var borderSide = new BoxBorderSide(color, thickness);

        this(borderSide, borderSide, borderSide, borderSide);
    }

    public BoxBorderSide top() {
        return this.top;
    }

    public void setTop(BoxBorderSide top) {
        this.top = top;
    }

    public BoxBorderSide left() {
        return this.left;
    }

    public void setLeft(BoxBorderSide left) {
        this.left = left;
    }

    public BoxBorderSide bottom() {
        return this.bottom;
    }

    public void setBottom(BoxBorderSide bottom) {
        this.bottom = bottom;
    }

    public BoxBorderSide right() {
        return this.right;
    }

    public void setRight(BoxBorderSide right) {
        this.right = right;
    }

    public void paint(Graphics2D graphics2D, JComponent component) {
        var width = component.getWidth();
        var height = component.getHeight();
    }
}

