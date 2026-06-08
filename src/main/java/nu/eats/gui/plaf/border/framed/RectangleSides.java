package nu.eats.gui.plaf.border.framed;

import java.util.function.Consumer;

public class RectangleSides {
    public final RectangleSide top, right, bottom, left;

    public RectangleSides(SubBuilder builder) {
        this.top = builder.top.build();
        this.right = builder.right.build();
        this.bottom = builder.bottom.build();
        this.left = builder.left.build();
    }

    public SubBuilder toBuilder(FramedBorder.Builder parent) {
        return new SubBuilder(parent, this);
    }

    public static class SubBuilder {
        public final RectangleSide.Builder top, right, bottom, left;

        private final FramedBorder.Builder parent;

        public SubBuilder(FramedBorder.Builder parent) {
            this.parent = parent;
            this.top = new RectangleSide.Builder();
            this.right = new RectangleSide.Builder();
            this.bottom = new RectangleSide.Builder();
            this.left = new RectangleSide.Builder();
        }

        public SubBuilder(FramedBorder.Builder parent, RectangleSides sides) {
            this.parent = parent;
            this.top = sides.top.toBuilder();
            this.right = sides.right.toBuilder();
            this.bottom = sides.bottom.toBuilder();
            this.left = sides.left.toBuilder();
        }

        public FramedBorder.Builder top(Consumer<RectangleSide.Builder> side) {
            side.accept(this.top);

            return this.parent;
        }

        public FramedBorder.Builder right(Consumer<RectangleSide.Builder> side) {
            side.accept(this.right);

            return this.parent;
        }

        public FramedBorder.Builder bottom(Consumer<RectangleSide.Builder> side) {
            side.accept(this.bottom);

            return this.parent;
        }

        public FramedBorder.Builder left(Consumer<RectangleSide.Builder> side) {
            side.accept(this.left);

            return this.parent;
        }

        public FramedBorder.Builder horizontal(Consumer<RectangleSide.Builder> side) {
            side.accept(this.left);
            side.accept(this.right);

            return this.parent;
        }

        public FramedBorder.Builder vertical(Consumer<RectangleSide.Builder> side) {
            side.accept(this.top);
            side.accept(this.bottom);

            return this.parent;
        }

        public RectangleSides build() {
            return new RectangleSides(this);
        }
    }
}
