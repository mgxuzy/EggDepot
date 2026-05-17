package nu.eats.authentication.gui;

import nu.eats.common.messaging.EventBus;
import nu.eats.domain.User;
import nu.eats.authentication.domain.Credentials;
import nu.eats.authentication.gui.components.ContinueAsCard;
import nu.eats.gui.components.Section;
import nu.eats.gui.plaf.Theme;
import nu.eats.gui.plaf.border.FramedBorder;
import nu.eats.ui.auth.AuthState;
import nu.eats.ui.auth.AuthViewModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AuthDialog extends JDialog {

    private boolean isSignedIn = false;

    public AuthDialog(Frame owner) {
        super(owner, "Sign In", true);

        setUndecorated(true);

        var rootPane = getRootPane();

        rootPane.setWindowDecorationStyle(JRootPane.NONE);
        rootPane.setOpaque(false);
        rootPane.setBackground(Theme.COLOR_BG);
        rootPane.setBorder(new FramedBorder.Builder()
                .corners((corner) -> corner.radius(12))
                .build());

        var cardsPanel = new JPanel(new CardLayout());
        var continueAsCard = new ContinueAsCard(new AuthViewModel(new Credentials()));

        cardsPanel.setOpaque(false);
        cardsPanel.add(continueAsCard);

        var mainContent = new Section();

        mainContent.setLayout(new BorderLayout());
        mainContent.setBorder(new EmptyBorder(Theme.SPACING_MD, Theme.SPACING_MD, Theme.SPACING_MD, Theme.SPACING_MD));
        mainContent.add(cardsPanel, BorderLayout.CENTER);

        setBackground(Theme.COLOR_TRANSPARENT);
        setSize(420, 580);
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
