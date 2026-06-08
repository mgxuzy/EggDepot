package nu.eats.gui.plaf.border.framed;

import java.util.function.Consumer;

public class RectangleCorners {
    public final RectangleCorner topLeft, topRight, bottomRight, bottomLeft;

    public RectangleCorners(SubBuilder builder) {
        this.topLeft = builder.topLeft.build();
        this.topRight = builder.topRight.build();
        this.bottomRight = builder.bottomRight.build();
        this.bottomLeft = builder.bottomLeft.build();
    }

    public SubBuilder toBuilder(FramedBorder.Builder parent) {
        return new SubBuilder(parent, this);
    }

    public static class SubBuilder {
        public final RectangleCorner.Builder topLeft, topRight, bottomRight, bottomLeft;

        private final FramedBorder.Builder parent;

        public SubBuilder(FramedBorder.Builder parent) {
            this.parent = parent;
            this.topLeft = new RectangleCorner.Builder();
            this.topRight = new RectangleCorner.Builder();
            this.bottomRight = new RectangleCorner.Builder();
            this.bottomLeft = new RectangleCorner.Builder();
        }

        public SubBuilder(FramedBorder.Builder parent, RectangleCorners corners) {
            this.parent = parent;
            this.topLeft = corners.topLeft.toBuilder();
            this.topRight = corners.topRight.toBuilder();
            this.bottomRight = corners.bottomRight.toBuilder();
            this.bottomLeft = corners.bottomLeft.toBuilder();
        }

        public FramedBorder.Builder topLeft(Consumer<RectangleCorner.Builder> corner) {
            corner.accept(this.topLeft);

            return this.parent;
        }

        public FramedBorder.Builder topRight(Consumer<RectangleCorner.Builder> corner) {
            corner.accept(this.topRight);

            return this.parent;
        }

        public FramedBorder.Builder bottomRight(Consumer<RectangleCorner.Builder> corner) {
            corner.accept(this.bottomRight);

            return this.parent;
        }

        public FramedBorder.Builder bottomLeft(Consumer<RectangleCorner.Builder> corner) {
            corner.accept(this.bottomLeft);

            return this.parent;
        }

        public RectangleCorners build() {
            return new RectangleCorners(this);
        }
    }
}