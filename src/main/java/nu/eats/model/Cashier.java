package nu.eats.model;

import nu.eats.feature.auth.model.UserType;

public record Cashier(String id, String username) implements User {
    @Override
    public String id() {
        return id;
    }

    @Override
    public String username() {
        return username;
    }

    @Override
    public UserType type() {
        return UserType.CUSTOMER;
    }
}
