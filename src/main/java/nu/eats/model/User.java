package nu.eats.domain;

import nu.eats.authentication.domain.UserType;

public interface User {
    String id();

    String username();

    UserType type();
}
