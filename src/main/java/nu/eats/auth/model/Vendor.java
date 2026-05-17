package nu.eats.authentication.domain;

import nu.eats.domain.User;
import nu.eats.domain.Username;

public final class Vendor implements User {
    private String id;
    private Username username;
    private String storeName;

    public Vendor(String id, Username username, String storeName) {

    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String username() {
        return username.value();
    }


    public String storeName() {
        return storeName;
    }

    @Override
    public UserType type() {
        return UserType.VENDOR;
    }
}
