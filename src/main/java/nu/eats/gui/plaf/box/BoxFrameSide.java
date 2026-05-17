package nu.eats.gui.plaf.box;

import java.awt.*;
import java.awt.geom.Line2D;

public class BoxFrameSide {
    private final Line2D.Double line = new Line2D.Double();

    private boolean isVisible = false;

    private Color color;
    private double thickness;

    public BoxFrameSide(Color color, double thickness) {
        this.color = color;
        this.thickness = thickness;
    }

    public Color color() {
        return color;
    }

    public void setColor(Color color) {
        // TODO: Add transparency visibility check
        this.color = color;
    }

    public double thickness() {
        return thickness;
    }

    public void setThickness(double thickness) {
        if (thickness == this.thickness) {
            return;
        }

        this.isVisible = thickness > 0.0d;

        if (!this.isVisible) {
            return;
        }

        this.thickness = thickness;
    }

    public boolean isVisible() {
        return this.isVisible;
    }
}
