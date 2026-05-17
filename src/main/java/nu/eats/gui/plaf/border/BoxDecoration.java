package nu.eats.gui.plaf.border;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;

import static nu.eats.gui.plaf.Constants.KEY_BOX_DECORATION;

public class BoxDecoration {
    private final RoundRectangle2D.Double contentBox = new RoundRectangle2D.Double();

    private final JComponent component;

    private FramedBorder border;

    private Shape contentBoxShape;

    private double width = -1;
    private double height = -1;
    private boolean isDirty = true;

    private BoxDecoration(JComponent component) {
        this.component = component;
    }

    public static BoxDecoration ensure(JComponent component) {
        if (component.getClientProperty(KEY_BOX_DECORATION) instanceof BoxDecoration boxDecoration) {
            return boxDecoration;
        }

        var boxDecoration = new BoxDecoration(component);

        component.putClientProperty(KEY_BOX_DECORATION, boxDecoration);

        component.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent event) {
                boxDecoration.setSize(component.getWidth(), component.getHeight());
            }
        });

        return boxDecoration;
    }

    public void markDirty() {
        if (!isDirty) {
            isDirty = true;

            SwingUtilities.invokeLater(component::repaint);
        }
    }

    public void setBorder(FramedBorder border) {
        if (this.border == border) {
            return;
        }

        this.component.setBorder(border);
        this.border = border;

        markDirty();
    }

    private void setSize(double width, double height) {
        if (this.width == width && this.height == height) {
            return;
        }

        this.width = width;
        this.height = height;

        markDirty();
    }

    public void paint(Graphics2D graphics2D) {
        Color backgroundColor = component.getBackground();

        // TODO: Use the area inside the border as the background area

        if (backgroundColor != null) {
            graphics2D.setColor(backgroundColor);
            graphics2D.fill(this.contentBox);
        }
    }
}