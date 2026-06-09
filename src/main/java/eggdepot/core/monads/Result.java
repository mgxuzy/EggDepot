package eggdepot.core.monads;

public sealed interface Result<T, E> permits Success, Failure {
    boolean isFailure();

    default T value() {
        return switch (this) {
            case Success<T, E> success -> success.value();
            case Failure<T, E> failure -> null;
        };
    }

    default E error() {
        return switch (this) {
            case Success<T, E> success -> null;
            case Failure<T, E> failure -> failure.error();
        };
    }
}