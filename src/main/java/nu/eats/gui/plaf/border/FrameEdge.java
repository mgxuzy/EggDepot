package nu.eats.gui.plaf.border;

import java.awt.*;

public final class FrameEdge {
    public final Color color;
    public final double thickness;
    public final boolean isVisible;

    private FrameEdge(Builder builder) {
        var color = builder.color;
        var thickness = builder.thickness;

        this.color = color;
        this.thickness = thickness;
        this.isVisible = (color != null && color.getAlpha() > 0)
                && thickness > 0.0;
    }

    public Builder toBuilder() {
        return new Builder(this);
    }

    public static final class Builder {
        private Color color = null;
        private double thickness = 0.0;

        public Builder() {
        }

        public Builder(FrameEdge edge) {
            this.color = edge.color;
            this.thickness = edge.thickness;
        }

        public Builder color(Color color) {
            this.color = color;

            return this;
        }

        public Builder thickness(double thickness) {
            this.thickness = Math.max(thickness, 0.0);

            return this;
        }

        public FrameEdge build() {
            return new FrameEdge(this);
        }
    }
}
