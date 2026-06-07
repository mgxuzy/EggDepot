package nu.eats.model;

import nu.eats.feature.auth.model.UserType;

public interface User {
    String id();

    String username();

    UserType type();
}
