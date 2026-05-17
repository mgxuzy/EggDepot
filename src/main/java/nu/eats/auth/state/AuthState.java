package nu.eats.auth.state;

import nu.eats.common.messaging.Topic;
import nu.eats.model.User;

public final class AuthState {
    public static final Topic<User> SIGNED_IN = new Topic<>();
}
