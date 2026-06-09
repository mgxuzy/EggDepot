package eggdepot.core.monads;

public record Failure<T, E>(E error) implements Result<T, E> {
    public boolean isFailure() {
        return true;
    }
}
