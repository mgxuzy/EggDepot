package eggdepot.model;

import eggdepot.feature.auth.model.UserType;

public interface User {
    String id();

    String username();

    UserType type();
}
