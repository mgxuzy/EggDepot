package nu.eats.gui.plaf.border.framed;

public final class RectangleSide {
    public final double padding, margin;

    private RectangleSide(Builder builder) {
        this.padding = builder.padding;
        this.margin = builder.margin;
    }

    public RectangleSide.Builder toBuilder() {
        return new RectangleSide.Builder(this);
    }

    public static final class Builder {
        private double padding, margin;

        public Builder() {
        }

        public Builder(RectangleSide side) {
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

        public RectangleSide build() {
            return new RectangleSide(this);
        }
    }
}
