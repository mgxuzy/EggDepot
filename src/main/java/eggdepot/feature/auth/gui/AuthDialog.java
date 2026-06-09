package eggdepot.feature.auth.gui;

import eggdepot.common.messaging.EventBus;
import eggdepot.feature.auth.gui.components.AuthCard;
import eggdepot.feature.auth.model.Credentials;
import eggdepot.feature.auth.state.AuthState;
import eggdepot.feature.auth.state.AuthViewModel;
import eggdepot.gui.components.Section;
import eggdepot.gui.plaf.Theme;
import eggdepot.gui.plaf.border.framed.FramedBorder;
import eggdepot.model.User;

import javax.swing.*;
import java.awt.*;

public class AuthDialog extends JDialog {

    private boolean isSignedIn = false;

    public AuthDialog(Frame owner) {
        super(owner, "Sign In", true);

        setUndecorated(true);

        var rootPane = getRootPane();

        rootPane.setWindowDecorationStyle(JRootPane.NONE);

        rootPane.setBorder(new FramedBorder.Builder()
                .corners((corner) -> corner.radius(12))
                .build());

        var cardsPanel = new JPanel(new CardLayout());
        var authCard = new AuthCard(new AuthViewModel(new Credentials()));

        cardsPanel.add(authCard);

        var mainContent = new Section();

        mainContent.setLayout(new BorderLayout());
        mainContent.add(cardsPanel, BorderLayout.CENTER);

        setBackground(Theme.COLOR_TRANSPARENT);
        setSize(Theme.SPACING_13XL, Theme.SPACING_14XL);
        setLocationRelativeTo(null);
        add(mainContent);

        EventBus.mainBus().subscribe(AuthState.SIGNED_IN, this::handleSignedIn);
    }

    private void handleSignedIn(User user) {
        this.isSignedIn = true;

        dispose();
    }

    public boolean isSignedIn() {
        return isSignedIn;
    }
}
