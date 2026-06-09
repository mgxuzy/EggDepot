package eggdepot.model;

import eggdepot.core.monads.Failure;
import eggdepot.core.monads.Result;
import eggdepot.core.monads.Success;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public final class Password {
    private static final Pattern UPPERCASE_LETTER_PATTERN = Pattern.compile("[A-Z]");
    private static final Pattern DIGIT_PATTERN = Pattern.compile("\\d");
    private static final Pattern SPECIAL_CHARACTER_PATTERN = Pattern.compile("[!@#$%^&*(),.?\":{}|<>]");

    private String value;

    private Password(String value) {
        this.value = value;
    }

    public static Result<Password, List<String>> create(String value) {
        var violations = findViolations(value);

        return violations.isEmpty() ? new Success<>(new Password(value)) : new Failure<>(violations);
    }

    private static List<String> findViolations(String value) {
        List<String> violations = new ArrayList<>();

        if (value.isBlank() || value.length() < 8) {
            violations.add("Must be 8+ characters");
        }

        if (!UPPERCASE_LETTER_PATTERN.matcher(value).find()) {
            violations.add("Must contain an uppercase letter");
        }

        if (!DIGIT_PATTERN.matcher(value).find()) {
            violations.add("Must contain a special character (!@#$%^&*(),.?\":{}|<>)");
        }

        if (!SPECIAL_CHARACTER_PATTERN.matcher(value).find()) {
            violations.add("Must contain special char");
        }

        return Collections.unmodifiableList(violations);
    }

    public String value() {
        return this.value;
    }

    public Result<Void, List<String>> setValue(String value) {
        var violations = findViolations(value);

        if (violations.isEmpty()) {
            return new Failure<>(violations);
        } else {
            this.value = value;

            return new Success<>(null);
        }
    }
}
