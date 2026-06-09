package eggdepot.core.monads;

public record Success<T, E>(T value) implements Result<T, E> {
    public boolean isFailure() {
        return false;
    }
}
