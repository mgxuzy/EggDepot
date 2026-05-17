package nu.eats.gui.plaf.border;

import java.util.function.Consumer;

public class FrameEdges {
    public final FrameEdge top, right, bottom, left;

    public FrameEdges(SubBuilder builder) {
        this.top = builder.top.build();
        this.right = builder.right.build();
        this.bottom = builder.bottom.build();
        this.left = builder.left.build();
    }

    public SubBuilder toBuilder(FramedBorder.Builder parent) {
        return new SubBuilder(parent, this);
    }

    public static final class SubBuilder {
        public final FrameEdge.Builder top, right, bottom, left;

        private final FramedBorder.Builder parent;

        public SubBuilder(FramedBorder.Builder parent) {
            this.parent = parent;
            this.top = new FrameEdge.Builder();
            this.right = new FrameEdge.Builder();
            this.bottom = new FrameEdge.Builder();
            this.left = new FrameEdge.Builder();
        }

        public SubBuilder(FramedBorder.Builder parent, FrameEdges edges) {
            this.parent = parent;
            this.top = edges.top.toBuilder();
            this.right = edges.right.toBuilder();
            this.bottom = edges.bottom.toBuilder();
            this.left = edges.left.toBuilder();
        }

        public FramedBorder.Builder top(Consumer<FrameEdge.Builder> edge) {
            edge.accept(this.top);

            return this.parent;
        }

        public FramedBorder.Builder right(Consumer<FrameEdge.Builder> edge) {
            edge.accept(this.right);

            return this.parent;
        }

        public FramedBorder.Builder bottom(Consumer<FrameEdge.Builder> edge) {
            edge.accept(this.bottom);

            return this.parent;
        }

        public FramedBorder.Builder left(Consumer<FrameEdge.Builder> edge) {
            edge.accept(this.left);

            return this.parent;
        }

        public FrameEdges build() {
            return new FrameEdges(this);
        }
    }
}