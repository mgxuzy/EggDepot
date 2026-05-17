package nu.eats.gui.plaf.border;

import java.util.function.Consumer;

public class FrameSides {
    public final FrameSide top, right, bottom, left;

    public FrameSides(SubBuilder builder) {
        this.top = builder.top.build();
        this.right = builder.right.build();
        this.bottom = builder.bottom.build();
        this.left = builder.left.build();
    }

    public SubBuilder toBuilder(FramedBorder.Builder parent) {
        return new SubBuilder(parent, this);
    }

    public static class SubBuilder {
        public final FrameSide.Builder top, right, bottom, left;

        private final FramedBorder.Builder parent;

        public SubBuilder(FramedBorder.Builder parent) {
            this.parent = parent;
            this.top = new FrameSide.Builder();
            this.right = new FrameSide.Builder();
            this.bottom = new FrameSide.Builder();
            this.left = new FrameSide.Builder();
        }

        public SubBuilder(FramedBorder.Builder parent, FrameSides sides) {
            this.parent = parent;
            this.top = sides.top.toBuilder();
            this.right = sides.right.toBuilder();
            this.bottom = sides.bottom.toBuilder();
            this.left = sides.left.toBuilder();
        }

        public FramedBorder.Builder top(Consumer<FrameSide.Builder> side) {
            side.accept(this.top);

            return this.parent;
        }

        public FramedBorder.Builder right(Consumer<FrameSide.Builder> side) {
            side.accept(this.right);

            return this.parent;
        }

        public FramedBorder.Builder bottom(Consumer<FrameSide.Builder> side) {
            side.accept(this.bottom);

            return this.parent;
        }

        public FramedBorder.Builder left(Consumer<FrameSide.Builder> side) {
            side.accept(this.left);

            return this.parent;
        }

        public FramedBorder.Builder horizontal(Consumer<FrameSide.Builder> side) {
            side.accept(this.left);
            side.accept(this.right);

            return this.parent;
        }

        public FramedBorder.Builder vertical(Consumer<FrameSide.Builder> side) {
            side.accept(this.top);
            side.accept(this.bottom);

            return this.parent;
        }

        public FrameSides build() {
            return new FrameSides(this);
        }
    }
}
