package nu.eats.ui.auth;

import nu.eats.common.messaging.Topic;
import nu.eats.domain.User;

public final class AuthState {
    public static final Topic<User> SIGNED_IN = new Topic<>();
}
