package nu.eats.gui.plaf.border.framed;

import java.util.function.Consumer;

public class RectangleEdges {
    public final RectangleEdge top, right, bottom, left;

    public RectangleEdges(SubBuilder builder) {
        this.top = builder.top.build();
        this.right = builder.right.build();
        this.bottom = builder.bottom.build();
        this.left = builder.left.build();
    }

    public SubBuilder toBuilder(FramedBorder.Builder parent) {
        return new SubBuilder(parent, this);
    }

    public static final class SubBuilder {
        public final RectangleEdge.Builder top, right, bottom, left;

        private final FramedBorder.Builder parent;

        public SubBuilder(FramedBorder.Builder parent) {
            this.parent = parent;
            this.top = new RectangleEdge.Builder();
            this.right = new RectangleEdge.Builder();
            this.bottom = new RectangleEdge.Builder();
            this.left = new RectangleEdge.Builder();
        }

        public SubBuilder(FramedBorder.Builder parent, RectangleEdges edges) {
            this.parent = parent;
            this.top = edges.top.toBuilder();
            this.right = edges.right.toBuilder();
            this.bottom = edges.bottom.toBuilder();
            this.left = edges.left.toBuilder();
        }

        public FramedBorder.Builder top(Consumer<RectangleEdge.Builder> edge) {
            edge.accept(this.top);

            return this.parent;
        }

        public FramedBorder.Builder right(Consumer<RectangleEdge.Builder> edge) {
            edge.accept(this.right);

            return this.parent;
        }

        public FramedBorder.Builder bottom(Consumer<RectangleEdge.Builder> edge) {
            edge.accept(this.bottom);

            return this.parent;
        }

        public FramedBorder.Builder left(Consumer<RectangleEdge.Builder> edge) {
            edge.accept(this.left);

            return this.parent;
        }

        public FramedBorder.Builder horizontal(Consumer<RectangleEdge.Builder> edge) {
            edge.accept(this.top);
            edge.accept(this.bottom);

            return this.parent;
        }

        public FramedBorder.Builder vertical(Consumer<RectangleEdge.Builder> edge) {
            edge.accept(this.left);
            edge.accept(this.right);

            return this.parent;
        }

        public RectangleEdges build() {
            return new RectangleEdges(this);
        }
    }
}