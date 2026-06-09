package eggdepot.model;

import eggdepot.core.monads.Failure;
import eggdepot.core.monads.Result;
import eggdepot.core.monads.Success;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class UserHandle {
    private static final Pattern HANDLE_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]{3,20}$");

    private String value;

    private UserHandle(String value) {
        this.value = value;
    }

    public static Result<UserHandle, List<String>> create(String value) {
        var violations = findViolations(value);

        return violations == null ? new Success<>(new UserHandle(value)) : new Failure<>(violations);
    }

    private static List<String> findViolations(String value) {
        List<String> violations = null;

        if (!HANDLE_PATTERN.matcher(value).find()) {
            violations = new ArrayList<>();

            violations.add("Invalid username");
        }

        return violations;
    }

    public String value() {
        return value;
    }

    public Result<UserHandle, List<String>> setValue(String value) {
        var violations = findViolations(value);

        if (violations == null) {
            this.value = value;

            return new Success<>(null);
        } else {
            return new Failure<>(violations);
        }
    }
}
