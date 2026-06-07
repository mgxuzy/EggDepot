package nu.eats.gui.plaf.border;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.util.function.Consumer;

import javax.swing.*;
import javax.swing.border.AbstractBorder;

import static nu.eats.gui.plaf.Constants.DEFAULT_RENDERING_HINTS;

import nu.eats.gui.plaf.UIPainter;
import nu.eats.gui.plaf.measure.Measure;

/**
 * A CSS Border replica using double-precision geometry.
 * Supports per-side colors, thickness, margin, and padding with diagonal miters.
 */
public class FramedBorder extends AbstractBorder {
    public static final FramedBorder NONE = new Builder().build();

    public final double topSideMargin, rightSideMargin, bottomSideMargin, leftSideMargin;
    public final double topSidePadding, rightSidePadding, bottomSidePadding, leftSidePadding;

    public final double topEdgeThickness, rightEdgeThickness, bottomEdgeThickness, leftEdgeThickness;
    public final Color topEdgeColor, rightEdgeColor, bottomEdgeColor, leftEdgeColor;
    public final boolean topEdgeIsVisible, rightEdgeIsVisible, bottomEdgeIsVisible, leftEdgeIsVisible;

    public final Measure topLeftCornerRadiusX, topLeftCornerRadiusY,
            topRightCornerRadiusX, topRightCornerRadiusY,
            bottomRightCornerRadiusX, bottomRightCornerRadiusY,
            bottomLeftCornerRadiusX, bottomLeftCornerRadiusY;

    public final FrameSides sides;
    public final FrameEdges edges;
    public final FrameCorners corners;

    private final transient Path2D.Double frameRegion = new Path2D.Double(Path2D.WIND_EVEN_ODD);
    private final transient Path2D.Double clientRegion = new Path2D.Double();
    private final transient Arc2D.Double topRightRegion = new Arc2D.Double(),
            bottomRightRegion = new Arc2D.Double(),
            bottomLeftRegion = new Arc2D.Double(),
            topLeftRegion = new Arc2D.Double();

    // Per-side miter shaped region
    private final transient Path2D.Double topRegion = new Path2D.Double(),
            rightRegion = new Path2D.Double(),
            bottomRegion = new Path2D.Double(),
            leftRegion = new Path2D.Double();

    private transient int width = -1;
    private transient int height = -1;

    private FramedBorder(Builder builder) {
        var sides = builder.sides.build();

        this.sides = sides;

        FrameSide topSide = sides.top,
                rightSide = sides.right,
                bottomSide = sides.bottom,
                leftSide = sides.left;

        this.topSideMargin = topSide.margin;
        this.rightSideMargin = rightSide.margin;
        this.bottomSideMargin = bottomSide.margin;
        this.leftSideMargin = leftSide.margin;

        this.topSidePadding = topSide.padding;
        this.rightSidePadding = rightSide.padding;
        this.bottomSidePadding = bottomSide.padding;
        this.leftSidePadding = leftSide.padding;

        var edges = builder.edges.build();

        this.edges = edges;

        FrameEdge topEdge = edges.top,
                rightEdge = edges.right,
                bottomEdge = edges.bottom,
                leftEdge = edges.left;

        this.topEdgeColor = topEdge.color;
        this.rightEdgeColor = rightEdge.color;
        this.bottomEdgeColor = bottomEdge.color;
        this.leftEdgeColor = leftEdge.color;

        this.topEdgeThickness = topEdge.thickness;
        this.rightEdgeThickness = rightEdge.thickness;
        this.bottomEdgeThickness = bottomEdge.thickness;
        this.leftEdgeThickness = leftEdge.thickness;

        this.topEdgeIsVisible = topEdge.isVisible;
        this.rightEdgeIsVisible = rightEdge.isVisible;
        this.bottomEdgeIsVisible = bottomEdge.isVisible;
        this.leftEdgeIsVisible = leftEdge.isVisible;

        var corners = builder.corners.build();

        this.corners = corners;

        FrameCorner topLeftCorner = corners.topLeft,
                topRightCorner = corners.topRight,
                bottomRightCorner = corners.bottomRight,
                bottomLeftCorner = corners.bottomLeft;

        this.topLeftCornerRadiusX = topLeftCorner.radiusX;
        this.topLeftCornerRadiusY = topLeftCorner.radiusY;
        this.topRightCornerRadiusX = topRightCorner.radiusX;
        this.topRightCornerRadiusY = topRightCorner.radiusY;
        this.bottomRightCornerRadiusX = bottomRightCorner.radiusX;
        this.bottomRightCornerRadiusY = bottomRightCorner.radiusY;
        this.bottomLeftCornerRadiusX = bottomLeftCorner.radiusX;
        this.bottomLeftCornerRadiusY = bottomLeftCorner.radiusY;
    }

    public Builder toBuilder() {
        return new Builder(this);
    }

    private static void updateCornerRegion(
            Arc2D.Double arc,
            double x, double y,
            double w, double h,
            double start
    ) {
        arc.setArc(x, y, w, h, start, -90, Arc2D.OPEN);
    }

    @Override
    public Insets getBorderInsets(Component component, Insets insets) {
        insets.set(
                (int) Math.ceil(this.topSideMargin + this.topEdgeThickness + this.topSidePadding),
                (int) Math.ceil(this.leftSideMargin + this.leftEdgeThickness + this.leftSidePadding),
                (int) Math.ceil(this.bottomSideMargin + this.bottomEdgeThickness + this.bottomSidePadding),
                (int) Math.ceil(this.rightSideMargin + this.rightEdgeThickness + this.rightSidePadding)
        );

        return insets;
    }

    public boolean containsPosition(int x, int y) {
        return x >= this.leftSideMargin
                && y >= this.topSideMargin
                && x < (width - this.rightSideMargin)
                && y < (height - this.bottomSideMargin);
    }

    @Override
    public void paintBorder(Component component, Graphics graphics, int positionX, int positionY, int width, int height) {
        var graphics2D = (Graphics2D) graphics.create();

        try {
            graphics2D.setRenderingHints(DEFAULT_RENDERING_HINTS);
            graphics2D.translate(positionX, positionY);

            Shape clip = graphics2D.getClip();

            if (this.topEdgeIsVisible) {
                this.paintEdgeRegion(graphics2D, this.topEdgeColor, this.topRegion, clip);
            }

            if (this.rightEdgeIsVisible) {
                this.paintEdgeRegion(graphics2D, this.rightEdgeColor, this.rightRegion, clip);
            }

            if (this.bottomEdgeIsVisible) {
                this.paintEdgeRegion(graphics2D, this.bottomEdgeColor, this.bottomRegion, clip);
            }

            if (this.leftEdgeIsVisible) {
                this.paintEdgeRegion(graphics2D, this.leftEdgeColor, this.leftRegion, clip);
            }
        } finally {
            graphics2D.dispose();
        }
    }

    // Add this alongside your other transient region declarations
    private final transient Path2D.Double borderAndContentRegion = new Path2D.Double();

    private void paintEdgeRegion(Graphics2D graphics2D, Color color, Path2D.Double region, Shape clip) {
        graphics2D.clip(region);
        graphics2D.setColor(color);
        graphics2D.fill(this.frameRegion);
        graphics2D.setClip(clip);
    }

    public void paintClientRegionWith(UIPainter painter, Graphics graphics, JComponent component) {
        int width = component.getWidth(),
                height = component.getHeight();

        if (this.width != width || this.height != height) {
            this.resize(width, height);
        }

        var graphics2D = (Graphics2D) graphics.create();

        try {
            graphics2D.setRenderingHints(DEFAULT_RENDERING_HINTS);
            graphics2D.setColor(component.getBackground());

            Shape safeShape = getCutoutShape(component);

            // 2. Safely apply the clip boundaries if an ancestor was found
            graphics2D.fill(safeShape);
            graphics2D.clip(safeShape);

            painter.paint(graphics2D, component);
        } finally {
            graphics2D.dispose();
        }
    }

    public static Shape getCutoutShape(JComponent component) {
        if (component == null) return null;

        Shape accumulatedClip = null;
        Component current = component;
        int w = component.getWidth();
        int h = component.getHeight();
        int dx = 0, dy = 0;

        // 1. Walk up parent hierarchy accumulating translations and clipping paths
        while (current != null) {
            if (current instanceof JComponent jc && jc.getBorder() instanceof FramedBorder fb) {
                int parentW = current.getWidth();
                int parentH = current.getHeight();

                // Sync border dimensions only when size actually changes
                if (fb.width != parentW || fb.height != parentH) {
                    fb.resize(parentW, parentH);
                }

                // Resolve maximum corner radii for this ancestor
                double maxRx = Math.max(
                        Math.max(fb.topLeftCornerRadiusX.resolve(parentW), fb.topRightCornerRadiusX.resolve(parentW)),
                        Math.max(fb.bottomLeftCornerRadiusX.resolve(parentW), fb.bottomRightCornerRadiusX.resolve(parentW))
                );
                double maxRy = Math.max(
                        Math.max(fb.topLeftCornerRadiusY.resolve(parentH), fb.topRightCornerRadiusY.resolve(parentH)),
                        Math.max(fb.bottomLeftCornerRadiusY.resolve(parentH), fb.bottomRightCornerRadiusY.resolve(parentH))
                );

                // Calculate safe flat rectangular area bounds
                double safeMinX = fb.leftEdgeThickness + maxRx;
                double safeMinY = fb.topEdgeThickness + maxRy;
                double safeMaxX = parentW - (fb.rightEdgeThickness + maxRx);
                double safeMaxY = parentH - (fb.bottomEdgeThickness + maxRy);

                // If overlapping the rounded border or margin, accumulate clip
                if (!(dx >= safeMinX && dx + w <= safeMaxX && dy >= safeMinY && dy + h <= safeMaxY)) {
                    Path2D.Double localizedParent = new Path2D.Double(fb.clientRegion);
                    localizedParent.transform(AffineTransform.getTranslateInstance(-dx, -dy));

                    if (accumulatedClip == null) {
                        accumulatedClip = localizedParent; // Keep it as a lightweight Path2D
                    } else {
                        // Fallback to heavy Area intersection only if nested outer borders also overlap
                        Area baseArea = (accumulatedClip instanceof Area a) ? a : new Area(accumulatedClip);
                        baseArea.intersect(new Area(localizedParent));
                        accumulatedClip = baseArea;
                    }
                }
            }

            // Accumulate position coordinate offsets relative to the next parent
            dx += current.getX();
            dy += current.getY();
            current = current.getParent();
        }

        if (accumulatedClip == null && component.getBorder() instanceof FramedBorder border) {
            accumulatedClip = border.clientRegion;
        }

        return accumulatedClip;
    }

    public void paintClientRegion(Graphics graphics, JComponent component) {
        int width = component.getWidth(),
                height = component.getHeight();

        if (this.width != width || this.height != height) {
            this.resize(width, height);
        }

        var graphics2D = (Graphics2D) graphics.create();

        try {
            graphics2D.setRenderingHints(DEFAULT_RENDERING_HINTS);
            graphics2D.setColor(component.getBackground());
            graphics2D.fill(this.clientRegion);
            graphics2D.setClip(this.clientRegion);
        } finally {
            graphics2D.dispose();
        }
    }

    private double scaleFactor(double currentScale, double r1, double r2, double limit) {
        double sum = r1 + r2;

        if (sum <= 0.0) return currentScale;

        return Math.min(currentScale, limit / sum);
    }

    public void resize(int width, int height) {
        this.width = width;
        this.height = height;

        // Math.max guarantees bounds don't flip backwards if component shrinks smaller than margins
        double borderMinX = this.leftSideMargin,
                borderMaxX = Math.max(width - this.rightSideMargin, borderMinX);

        double borderMinY = this.topSideMargin,
                borderMaxY = Math.max(height - this.bottomSideMargin, borderMinY);

        double borderWidth = borderMaxX - borderMinX;
        double borderHeight = borderMaxY - borderMinY;

        double borderMax = Math.max(borderWidth, borderHeight);

        double topLeftCornerRadiusX = Math.min(this.topLeftCornerRadiusX.resolve(width), borderMax),
                topLeftCornerRadiusY = Math.min(this.topLeftCornerRadiusY.resolve(height), borderMax),
                topRightCornerRadiusX = Math.min(this.topRightCornerRadiusX.resolve(width), borderMax),
                topRightCornerRadiusY = Math.min(this.topRightCornerRadiusY.resolve(height), borderMax);

        double bottomRightCornerRadiusX = Math.min(this.bottomRightCornerRadiusX.resolve(width), borderMax),
                bottomRightCornerRadiusY = Math.min(this.bottomRightCornerRadiusY.resolve(height), borderMax),
                bottomLeftCornerRadiusX = Math.min(this.bottomLeftCornerRadiusX.resolve(width), borderMax),
                bottomLeftCornerRadiusY = Math.min(this.bottomLeftCornerRadiusY.resolve(height), borderMax);

        // Overlapping Curves (Proportional Reduction)
        // Causes 1.0 fraction units to become ellipses and px to become pilled shapes
        var scale = 1.0;

        scale = scaleFactor(scale, topLeftCornerRadiusX, topRightCornerRadiusX, borderWidth);
        scale = scaleFactor(scale, bottomLeftCornerRadiusX, bottomRightCornerRadiusX, borderWidth);
        scale = scaleFactor(scale, topLeftCornerRadiusY, bottomLeftCornerRadiusY, borderHeight);
        scale = scaleFactor(scale, topRightCornerRadiusY, bottomRightCornerRadiusY, borderHeight);

        if (scale < 1.0) {
            topLeftCornerRadiusX *= scale;
            topLeftCornerRadiusY *= scale;
            topRightCornerRadiusX *= scale;
            topRightCornerRadiusY *= scale;
            bottomLeftCornerRadiusX *= scale;
            bottomLeftCornerRadiusY *= scale;
            bottomRightCornerRadiusX *= scale;
            bottomRightCornerRadiusY *= scale;
        }

        this.frameRegion.reset();

        appendRoundedRectangleUnsafe(this.frameRegion, borderMinX, borderMinY, borderWidth, borderHeight,
                topLeftCornerRadiusX, topLeftCornerRadiusY, topRightCornerRadiusX, topRightCornerRadiusY, bottomRightCornerRadiusX, bottomRightCornerRadiusY, bottomLeftCornerRadiusX, bottomLeftCornerRadiusY);

        this.borderAndContentRegion.reset();
        this.borderAndContentRegion.append(this.frameRegion, false);

        // 2. Draw Client Shape
        double clientMinX = borderMinX + this.leftEdgeThickness,
                clientMaxX = Math.max(borderMaxX - this.rightEdgeThickness, clientMinX);

        double clientMinY = borderMinY + this.topEdgeThickness,
                clientMaxY = Math.max(borderMaxY - this.bottomEdgeThickness, clientMinY);

        double clientWidth = clientMaxX - clientMinX,
                clientHeight = clientMaxY - clientMinY;

        double clientTopLeftCornerRadiusX = Math.max(topLeftCornerRadiusX - this.leftEdgeThickness, 0),
                clientTopLeftCornerRadiusY = Math.max(topLeftCornerRadiusY - this.topEdgeThickness, 0),
                clientTopRightCornerRadiusX = Math.max(topRightCornerRadiusX - this.rightEdgeThickness, 0),
                clientTopRightCornerRadiusY = Math.max(topRightCornerRadiusY - this.topEdgeThickness, 0);

        double clientBottomRightCornerRadiusX = Math.max(bottomRightCornerRadiusX - this.rightEdgeThickness, 0),
                clientBottomRightCornerRadiusY = Math.max(bottomRightCornerRadiusY - this.bottomEdgeThickness, 0),
                clientBottomLeftCornerRadiusX = Math.max(bottomLeftCornerRadiusX - this.leftEdgeThickness, 0),
                clientBottomLeftCornerRadiusY = Math.max(bottomLeftCornerRadiusY - this.bottomEdgeThickness, 0);

        this.clientRegion.reset();

        appendRoundedRectangleUnsafe(this.clientRegion, clientMinX, clientMinY, clientWidth, clientHeight,
                clientTopLeftCornerRadiusX, clientTopLeftCornerRadiusY, clientTopRightCornerRadiusX, clientTopRightCornerRadiusY, clientBottomRightCornerRadiusX, clientBottomRightCornerRadiusY, clientBottomLeftCornerRadiusX, clientBottomLeftCornerRadiusY);

        this.frameRegion.append(this.clientRegion, false);

        // 3. Update Quadrant Zones (Miters route safely to transparent inner boundaries naturally)
        topRegion.reset();
        topRegion.moveTo(0, 0);
        topRegion.lineTo(width, 0);
        topRegion.lineTo(borderMaxX, borderMinY);
        topRegion.lineTo(clientMaxX, clientMinY);
        topRegion.lineTo(clientMaxX - clientTopRightCornerRadiusX, clientMinY + clientTopRightCornerRadiusY);
        topRegion.lineTo(clientMinX + clientTopLeftCornerRadiusX, clientMinY + clientTopLeftCornerRadiusY);
        topRegion.lineTo(clientMinX, clientMinY);
        topRegion.lineTo(borderMinX, borderMinY);
        topRegion.closePath();

        rightRegion.reset();
        rightRegion.moveTo(width, 0);
        rightRegion.lineTo(width, height);
        rightRegion.lineTo(borderMaxX, borderMaxY);
        rightRegion.lineTo(clientMaxX, clientMaxY);
        rightRegion.lineTo(clientMaxX - clientBottomRightCornerRadiusX, clientMaxY - clientBottomRightCornerRadiusY);
        rightRegion.lineTo(clientMaxX - clientTopRightCornerRadiusX, clientMinY + clientTopRightCornerRadiusY);
        rightRegion.lineTo(clientMaxX, clientMinY);
        rightRegion.lineTo(borderMaxX, borderMinY);
        rightRegion.closePath();

        bottomRegion.reset();
        bottomRegion.moveTo(width, height);
        bottomRegion.lineTo(0, height);
        bottomRegion.lineTo(borderMinX, borderMaxY);
        bottomRegion.lineTo(clientMinX, clientMaxY);
        bottomRegion.lineTo(clientMinX + clientBottomLeftCornerRadiusX, clientMaxY - clientBottomLeftCornerRadiusY);
        bottomRegion.lineTo(clientMaxX - clientBottomRightCornerRadiusX, clientMaxY - clientBottomRightCornerRadiusY);
        bottomRegion.lineTo(clientMaxX, clientMaxY);
        bottomRegion.lineTo(borderMaxX, borderMaxY);
        bottomRegion.closePath();

        leftRegion.reset();
        leftRegion.moveTo(0, height);
        leftRegion.lineTo(0, 0);
        leftRegion.lineTo(borderMinX, borderMinY);
        leftRegion.lineTo(clientMinX, clientMinY);
        leftRegion.lineTo(clientMinX + clientTopLeftCornerRadiusX, clientMinY + clientTopLeftCornerRadiusY);
        leftRegion.lineTo(clientMinX + clientBottomLeftCornerRadiusX, clientMaxY - clientBottomLeftCornerRadiusY);
        leftRegion.lineTo(clientMinX, clientMaxY);
        leftRegion.lineTo(borderMinX, borderMaxY);
        leftRegion.closePath();
    }

    private void appendRoundedRectangleUnsafe(
            Path2D.Double path,
            double positionX, double positionY,
            double width, double height,
            double topLeftCornerRadiusX, double topLeftCornerRadiusY,
            double topRightCornerRadiusX, double topRightCornerRadiusY,
            double bottomRightCornerRadiusX, double bottomRightCornerRadiusY,
            double bottomLeftCornerRadiusX, double bottomLeftCornerRadiusY
    ) {
        double maxX = positionX + width;
        double maxY = positionY + height;

        path.moveTo(positionX + topLeftCornerRadiusX, positionY);

        // Top edge
        path.lineTo(maxX - topRightCornerRadiusX, positionY);

        updateCornerRegion(
                topRightRegion,
                maxX - 2 * topRightCornerRadiusX,
                positionY,
                2 * topRightCornerRadiusX,
                2 * topRightCornerRadiusY,
                90
        );

        path.append(topRightRegion, true);

        // Right edge
        path.lineTo(maxX, maxY - bottomRightCornerRadiusY);

        updateCornerRegion(
                bottomRightRegion,
                maxX - 2 * bottomRightCornerRadiusX,
                maxY - 2 * bottomRightCornerRadiusY,
                2 * bottomRightCornerRadiusX,
                2 * bottomRightCornerRadiusY,
                0
        );

        path.append(bottomRightRegion, true);

        // Bottom edge
        path.lineTo(positionX + bottomLeftCornerRadiusX, maxY);

        updateCornerRegion(
                bottomLeftRegion,
                positionX,
                maxY - 2 * bottomLeftCornerRadiusY,
                2 * bottomLeftCornerRadiusX,
                2 * bottomLeftCornerRadiusY,
                270
        );

        path.append(bottomLeftRegion, true);

        // Left edge
        path.lineTo(positionX, positionY + topLeftCornerRadiusY);

        updateCornerRegion(
                topLeftRegion,
                positionX,
                positionY,
                2 * topLeftCornerRadiusX,
                2 * topLeftCornerRadiusY,
                180
        );

        path.append(topLeftRegion, true);
        path.closePath();
    }

    public static class Builder {
        public final FrameSides.SubBuilder sides;
        public final FrameEdges.SubBuilder edges;
        public final FrameCorners.SubBuilder corners;

        public Builder() {
            this.sides = new FrameSides.SubBuilder(this);
            this.edges = new FrameEdges.SubBuilder(this);
            this.corners = new FrameCorners.SubBuilder(this);
        }

        public Builder(FramedBorder border) {
            this.sides = border.sides.toBuilder(this);
            this.edges = border.edges.toBuilder(this);
            this.corners = border.corners.toBuilder(this);
        }

        public Builder sides(Consumer<FrameSide.Builder> side) {
            var sides = this.sides;

            side.accept(sides.top);
            side.accept(sides.right);
            side.accept(sides.bottom);
            side.accept(sides.left);

            return this;
        }

        public Builder edges(Consumer<FrameEdge.Builder> edge) {
            var edges = this.edges;

            edge.accept(edges.top);
            edge.accept(edges.right);
            edge.accept(edges.bottom);
            edge.accept(edges.left);

            return this;
        }

        public Builder corners(Consumer<FrameCorner.Builder> corner) {
            var corners = this.corners;

            corner.accept(corners.topLeft);
            corner.accept(corners.topRight);
            corner.accept(corners.bottomRight);
            corner.accept(corners.bottomLeft);

            return this;
        }

        public FramedBorder build() {
            return new FramedBorder(this);
        }
    }
}