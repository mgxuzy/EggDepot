package nu.eats.feature.auth.state;

import nu.eats.common.messaging.EventBus;
import nu.eats.model.User;
import nu.eats.feature.auth.model.Credentials;

public class AuthViewModel {
    private final EventBus eventBus = EventBus.mainBus();

    public AuthViewModel(Credentials credentials) {

    }

    public void signIn(User user) {
        eventBus.publish(AuthState.SIGNED_IN, user);
    }
}
