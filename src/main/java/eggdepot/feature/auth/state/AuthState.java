package eggdepot.feature.auth.state;

import eggdepot.common.messaging.Topic;
import eggdepot.model.User;

public final class AuthState {
    public static final Topic<User> SIGNED_IN = new Topic<>();
}
