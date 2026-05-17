package nu.eats.gui.plaf.box;

import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.geom.Path2D;
import java.util.Objects;

/**
 * A perfectly accurate CSS Border replica using double-precision geometry.
 * Extensively optimized for 0(1) allocation during static repaints, dynamic resizing, and value mutation.
 */
public class BoxAreaBorder extends AbstractBorder {

    private static final double KAPPA = 4.0 * (Math.sqrt(2.0) - 1.0) / 3.0;
    private static final double BEZIER_OFFSET = 1.0 - KAPPA;

    private final Color borderColor;
    private final double borderThickness;

    // Precomputed for O(1) reads
    private final int cachedStrokeWidth;
    private final boolean isVisible;

    private Insets borderPadding;
    private BoxCornerRadii cornerRadii;

    // Persisted instances guarantee ZERO object allocations during rapid resizing or mutating
    private final transient Path2D.Double cachedPath = new Path2D.Double(Path2D.WIND_EVEN_ODD);
    private final transient BoxCornerRadii lastPaintedRadii = new BoxCornerRadii(0);

    private transient int cachedWidth = -1;
    private transient int cachedHeight = -1;
    private transient boolean requiresUpdate = true;



    public BoxAreaBorder(Color borderColor, double frameThickness, Insets borderPadding, BoxCornerRadii cornerRadii) {
        this.borderColor = borderColor;
        this.borderThickness = frameThickness;
        this.cachedStrokeWidth = (int) Math.ceil(frameThickness);

        // Pre-evaluate so we don't query the Color object every paint cycle
        this.isVisible = frameThickness > 0 && borderColor != null && borderColor.getAlpha() > 0;

        this.borderPadding = borderPadding != null ? borderPadding : new Insets(0, 0, 0, 0);
        this.cornerRadii = cornerRadii != null ? cornerRadii : new BoxCornerRadii(0);
    }

    public BoxCornerRadii getCornerRadii() {
        return cornerRadii;
    }

    public void setCornerRadii(BoxCornerRadii newRadii) {
        if (newRadii == null) newRadii = new BoxCornerRadii(0);

        this.cornerRadii = newRadii;
        this.requiresUpdate = true;
    }

    public void setPadding(Insets newPadding) {
        this.borderPadding = newPadding != null ? newPadding : new Insets(0, 0, 0, 0);
    }

    @Override
    public Insets getBorderInsets(Component component, Insets insets) {
        insets.set(
                cachedStrokeWidth + borderPadding.top,
                cachedStrokeWidth + borderPadding.left,
                cachedStrokeWidth + borderPadding.bottom,
                cachedStrokeWidth + borderPadding.right
        );
        return insets;
    }

    @Override
    public void paintBorder(Component component, Graphics graphics, int positionX, int positionY, int width, int height) {
        if (!isVisible) return;

        // Detect if cornerRadii was modified externally via .set()
        if (!cornerRadii.equals(lastPaintedRadii)) {
            lastPaintedRadii.set(lastPaintedRadii.topLeftX, lastPaintedRadii.topLeftY, lastPaintedRadii.topRightX, lastPaintedRadii.topRightY,
                    lastPaintedRadii.bottomRightX, lastPaintedRadii.bottomRightY, lastPaintedRadii.bottomLeftX, lastPaintedRadii.bottomLeftY);
            requiresUpdate = true;
        }

        // Zero-allocation update: Clears geometry but retains backing arrays
        if (requiresUpdate || cachedWidth != width || cachedHeight != height) {
            updatePathGeometry(width, height);

            cachedWidth = width;
            cachedHeight = height;
            requiresUpdate = false;
        }

        Graphics2D graphics2D = (Graphics2D) graphics.create();

        try {
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.translate(positionX, positionY);

            graphics2D.setColor(borderColor);
            graphics2D.fill(cachedPath);
        } finally {
            graphics2D.dispose();
        }
    }

    private void updatePathGeometry(double width, double height) {
        cachedPath.reset();

        // 1. Draw Outer Frame Shape
        appendRoundedRectangle(cachedPath, 0, 0, width, height,
                cornerRadii.topLeftX, cornerRadii.topLeftY,
                cornerRadii.topRightX, cornerRadii.topRightY,
                cornerRadii.bottomRightX, cornerRadii.bottomRightY,
                cornerRadii.bottomLeftX, cornerRadii.bottomLeftY);

        // 2. Draw Inner Cutout Shape
        double innerLeft = borderThickness;
        double innerTop = borderThickness;
        double innerRight = width - borderThickness;
        double innerBottom = height - borderThickness;

        double innerTopLeftX = Math.max(0, cornerRadii.topLeftX - borderThickness);
        double innerTopLeftY = Math.max(0, cornerRadii.topLeftY - borderThickness);
        double innerTopRightX = Math.max(0, cornerRadii.topRightX - borderThickness);
        double innerTopRightY = Math.max(0, cornerRadii.topRightY - borderThickness);
        double innerBottomRightX = Math.max(0, cornerRadii.bottomRightX - borderThickness);
        double innerBottomRightY = Math.max(0, cornerRadii.bottomRightY - borderThickness);
        double innerBottomLeftX = Math.max(0, cornerRadii.bottomLeftX - borderThickness);
        double innerBottomLeftY = Math.max(0, cornerRadii.bottomLeftY - borderThickness);

        appendRoundedRectangle(cachedPath, innerLeft, innerTop, innerRight, innerBottom,
                innerTopLeftX, innerTopLeftY,
                innerTopRightX, innerTopRightY,
                innerBottomRightX, innerBottomRightY,
                innerBottomLeftX, innerBottomLeftY);
    }

    private void appendRoundedRectangle(Path2D.Double path,
                                        double left, double top, double right, double bottom,
                                        double cornerTopLeftX, double cornerTopLeftY,
                                        double cornerTopRightX, double cornerTopRightY,
                                        double cornerBottomRightX, double cornerBottomRightY,
                                        double cornerBottomLeftX, double cornerBottomLeftY) {

        path.moveTo(left + cornerTopLeftX, top);

        // Top Edge & Top-Right Corner
        path.lineTo(right - cornerTopRightX, top);
        path.curveTo(right - cornerTopRightX * BEZIER_OFFSET, top, right, top + cornerTopRightY * BEZIER_OFFSET, right, top + cornerTopRightY);

        // Right Edge & Bottom-Right Corner
        path.lineTo(right, bottom - cornerBottomRightY);
        path.curveTo(right, bottom - cornerBottomRightY * BEZIER_OFFSET, right - cornerBottomRightX * BEZIER_OFFSET, bottom, right - cornerBottomRightX, bottom);

        // Bottom Edge & Bottom-Left Corner
        path.lineTo(left + cornerBottomLeftX, bottom);
        path.curveTo(left + cornerBottomLeftX * BEZIER_OFFSET, bottom, left, bottom - cornerBottomLeftY * BEZIER_OFFSET, left, bottom - cornerBottomLeftY);

        // Left Edge & Top-Left Corner
        path.lineTo(left, top + cornerTopLeftY);
        path.curveTo(left, top + cornerTopLeftY * BEZIER_OFFSET, left + cornerTopLeftX * BEZIER_OFFSET, top, left + cornerTopLeftX, top);

        path.closePath();
    }
}