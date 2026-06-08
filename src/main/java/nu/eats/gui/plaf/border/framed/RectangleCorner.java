package nu.eats.gui.plaf.border.framed;

import nu.eats.gui.plaf.measure.Measure;

public final class RectangleCorner {
    public final Measure radiusX, radiusY;

    private RectangleCorner(Builder builder) {
        this.radiusX = builder.radiusX;
        this.radiusY = builder.radiusY;
    }

    public Builder toBuilder() {
        return new Builder(this);
    }

    public static final class Builder {
        private Measure radiusX = Measure.ZERO,
                radiusY = Measure.ZERO;

        public Builder() {
        }

        public Builder(RectangleCorner corner) {
            this.radiusX = corner.radiusX;
            this.radiusY = corner.radiusY;
        }

        public Builder radiusX(double radiusX) {
            this.radiusX = Measure.px(Math.max(radiusX, 0.0));

            return this;
        }

        public Builder radiusY(double radiusY) {
            this.radiusY = Measure.px(Math.max(radiusY, 0.0));

            return this;
        }

        public Builder radius(double radius) {
            return this.radiusX(radius)
                    .radiusY(radius);
        }

        public Builder radiusX(Measure radiusX) {
            this.radiusX = radiusX.withValue(Math.max(radiusX.value, 0.0));

            return this;
        }

        public Builder radiusY(Measure radiusY) {
            this.radiusY = radiusY.withValue(Math.max(radiusY.value, 0.0));

            return this;
        }

        public Builder radius(Measure radius) {
            return this.radiusX(radius)
                    .radiusY(radius);
        }

        public RectangleCorner build() {
            return new RectangleCorner(this);
        }
    }
}
