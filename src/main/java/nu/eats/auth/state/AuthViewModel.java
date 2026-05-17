package nu.eats.ui.auth;

import nu.eats.common.messaging.EventBus;
import nu.eats.domain.User;
import nu.eats.authentication.domain.Credentials;

public class AuthViewModel {
    private final EventBus eventBus = EventBus.mainBus();

    public AuthViewModel(Credentials credentials) {

    }

    public void signIn(User user) {
        eventBus.publish(AuthState.SIGNED_IN, user);
    }
}
