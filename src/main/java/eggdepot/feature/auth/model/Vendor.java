package eggdepot.feature.auth.model;

import eggdepot.model.User;
import eggdepot.model.UserHandle;

public final class Vendor implements User {
    private String id;
    private UserHandle userHandle;
    private String storeName;

    public Vendor(String id, UserHandle userHandle, String storeName) {

    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String username() {
        return userHandle.value();
    }


    public String storeName() {
        return storeName;
    }

    @Override
    public UserType type() {
        return UserType.VENDOR;
    }
}
