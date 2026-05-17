package nu.eats.domain;

import nu.eats.core.monads.Failure;
import nu.eats.core.monads.Result;
import nu.eats.core.monads.Success;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class Username {
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]{3,20}$");

    private String value;

    private Username(String value) {
        this.value = value;
    }

    public static Result<Username, List<String>> create(String value) {
        var violations = findViolations(value);

        return violations == null ? new Success<>(new Username(value)) : new Failure<>(violations);
    }

    private static List<String> findViolations(String value) {
        List<String> violations = null;

        if (!USERNAME_PATTERN.matcher(value).find()) {
            violations = new ArrayList<>();

            violations.add("Invalid username");
        }

        return violations;
    }

    public String value() {
        return value;
    }

    public Result<Username, List<String>> setValue(String value) {
        var violations = findViolations(value);

        if (violations == null) {
            this.value = value;

            return new Success<>(null);
        } else {
            return new Failure<>(violations);
        }
    }
}
