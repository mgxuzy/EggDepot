package nu.eats.authentication.gui.components;

import nu.eats.domain.Username;
import nu.eats.authentication.domain.Vendor;
import nu.eats.gui.components.LabeledField;
import nu.eats.gui.plaf.Theme;
import nu.eats.gui.plaf.button.ButtonPreset;
import nu.eats.gui.plaf.button.ButtonVariant;
import nu.eats.ui.auth.AuthViewModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static nu.eats.gui.plaf.Constants.KEY_COMPONENT_VARIANT;

public class VendorSignInCard extends AuthCard {
    private final JTextField usernameField;
    private final JPasswordField passwordField;

    public VendorSignInCard(AuthViewModel model) {
        super();

        toolBar().setTitle("Vendor Sign-In");

        // --- Fields ---
        usernameField = new JTextField();
        passwordField = new JPasswordField();

        // --- Sign-In ---
        JButton signInButton = new JButton("Sign In");

        ButtonPreset.MD.apply(signInButton);
        signInButton.putClientProperty(KEY_COMPONENT_VARIANT, ButtonVariant.PRIMARY);
        signInButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        signInButton.setMaximumSize(new Dimension(280, signInButton.getPreferredSize().height));
        signInButton.addActionListener(_ -> handleSignIn(model));

        // --- Sign-Up ---
        var signUpLabel = new JLabel("Don't have an account? ");
        var signUpButton = new JButton("Sign Up");

        ButtonPreset.XS.apply(signUpButton);
        signUpButton.setBorder(null);
        signUpButton.setFont(Theme.FONT_MEDIUM_12);
        signUpButton.putClientProperty(KEY_COMPONENT_VARIANT, ButtonVariant.TERTIARY);
        signUpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        signUpButton.addActionListener(_ -> stack(new VendorSignUpCard(model::signIn)));

        var signUpRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));

        signUpRow.setOpaque(false);
        signUpRow.add(signUpLabel);
        signUpRow.add(signUpButton);

        var mainContent = new JPanel();

        mainContent.setBorder(new EmptyBorder(
                Theme.SPACING_2XL, Theme.SPACING_2XL, Theme.SPACING_2XL, Theme.SPACING_2XL));
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setOpaque(false);
        mainContent.add(Box.createVerticalGlue());
        mainContent.add(new LabeledField("Username", usernameField));
        mainContent.add(Box.createVerticalStrut(Theme.SPACING_MD));
        mainContent.add(new LabeledField("Password", passwordField));
        mainContent.add(Box.createVerticalStrut(Theme.SPACING_3XL));
        mainContent.add(signInButton);
        mainContent.add(Box.createVerticalStrut(Theme.SPACING_LG));
        mainContent.add(signUpRow);
        mainContent.add(Box.createVerticalGlue());

        this.mainContent.setLayout(new BorderLayout());
        this.mainContent.add(mainContent, BorderLayout.CENTER);
    }

    private void handleSignIn(AuthViewModel model) {
        model.signIn(new Vendor("v-1", Username.create("Hello").value(), "My Store"));
    }
}