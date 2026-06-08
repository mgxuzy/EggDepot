package nu.eats.feature.auth.state;

import nu.eats.common.messaging.EventBus;
import nu.eats.feature.auth.model.Credentials;
import nu.eats.model.User;

public class AuthViewModel {
    private final EventBus eventBus = EventBus.mainBus();

    public AuthViewModel(Credentials credentials) {

    }

    public void signIn(User user) {
        eventBus.publish(AuthState.SIGNED_IN, user);
    }
}
