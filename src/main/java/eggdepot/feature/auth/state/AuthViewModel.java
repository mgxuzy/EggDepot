package eggdepot.feature.auth.state;

import eggdepot.common.messaging.EventBus;
import eggdepot.feature.auth.model.Credentials;
import eggdepot.model.User;

public class AuthViewModel {
    private final EventBus eventBus = EventBus.mainBus();

    public AuthViewModel(Credentials credentials) {

    }

    public void signIn(User user) {
        eventBus.publish(AuthState.SIGNED_IN, user);
    }
}
