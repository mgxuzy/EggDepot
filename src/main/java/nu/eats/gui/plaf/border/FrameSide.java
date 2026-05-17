package nu.eats.gui.plaf.border;

import java.awt.*;

public final class FrameSide {
    public final double padding, margin;

    private FrameSide(Builder builder) {
        this.padding = builder.padding;
        this.margin = builder.margin;
    }

    public FrameSide.Builder toBuilder() {
        return new FrameSide.Builder(this);
    }

    public static final class Builder {
        private double padding, margin;

        public Builder() {
        }

        public Builder(FrameSide side) {
            this.padding = side.padding;
            this.margin = side.margin;
        }

        public Builder padding(double padding) {
            this.padding = Math.max(padding, 0.0);

            return this;
        }

        public Builder margin(double margin) {
            this.margin = margin;

            return this;
        }

        public FrameSide build() {
            return new FrameSide(this);
        }
    }
}
