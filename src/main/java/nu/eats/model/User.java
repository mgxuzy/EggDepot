package nu.eats.model;

import nu.eats.auth.model.UserType;

public interface User {
    String id();

    String username();

    UserType type();
}
