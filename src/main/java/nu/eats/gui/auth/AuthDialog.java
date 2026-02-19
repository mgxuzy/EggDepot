package nu.eats.gui.auth;

import nu.eats.common.messaging.EventBus;
import nu.eats.domain.User;
import nu.eats.domain.auth.Credentials;
import nu.eats.gui.auth.components.ContinueAsCard;
import nu.eats.gui.components.Section;
import nu.eats.gui.plaf.Theme;
import nu.eats.gui.plaf.box.BoxDecoration;
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

        getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        getRootPane().setOpaque(false);
        getRootPane().setBackground(Theme.COLOR_BG);

        BoxDecoration.ensure(getRootPane()).borderRadius(Theme.RADIUS_LG);

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
