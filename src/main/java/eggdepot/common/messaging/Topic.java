package eggdepot.common.messaging;

public final class Topic<T> {
    public static <T> Topic<T> of(String name) {
        return new Topic<>();
    }
}
