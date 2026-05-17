package nu.eats.gui.plaf.border;

import java.util.function.Consumer;

public class FrameCorners {
    public final FrameCorner topLeft, topRight, bottomRight, bottomLeft;

    public FrameCorners(SubBuilder builder) {
        this.topLeft = builder.topLeft.build();
        this.topRight = builder.topRight.build();
        this.bottomRight = builder.bottomRight.build();
        this.bottomLeft = builder.bottomLeft.build();
    }

    public SubBuilder toBuilder(FramedBorder.Builder parent) {
        return new SubBuilder(parent, this);
    }

    public static class SubBuilder {
        public final FrameCorner.Builder topLeft, topRight, bottomRight, bottomLeft;

        private final FramedBorder.Builder parent;

        public SubBuilder(FramedBorder.Builder parent) {
            this.parent = parent;
            this.topLeft = new FrameCorner.Builder();
            this.topRight = new FrameCorner.Builder();
            this.bottomRight = new FrameCorner.Builder();
            this.bottomLeft = new FrameCorner.Builder();
        }

        public SubBuilder(FramedBorder.Builder parent, FrameCorners corners) {
            this.parent = parent;
            this.topLeft = corners.topLeft.toBuilder();
            this.topRight = corners.topRight.toBuilder();
            this.bottomRight = corners.bottomRight.toBuilder();
            this.bottomLeft = corners.bottomLeft.toBuilder();
        }

        public FramedBorder.Builder topLeft(Consumer<FrameCorner.Builder> corner) {
            corner.accept(this.topLeft);

            return this.parent;
        }

        public FramedBorder.Builder topRight(Consumer<FrameCorner.Builder> corner) {
            corner.accept(this.topRight);

            return this.parent;
        }

        public FramedBorder.Builder bottomRight(Consumer<FrameCorner.Builder> corner) {
            corner.accept(this.bottomRight);

            return this.parent;
        }

        public FramedBorder.Builder bottomLeft(Consumer<FrameCorner.Builder> corner) {
            corner.accept(this.bottomLeft);

            return this.parent;
        }

        public FrameCorners build() {
            return new FrameCorners(this);
        }
    }
}