package nu.eats.feature.auth.gui.components;

import nu.eats.feature.auth.model.Vendor;
import nu.eats.feature.auth.state.AuthViewModel;
import nu.eats.gui.components.LabeledField;
import nu.eats.gui.plaf.Theme;
import nu.eats.gui.plaf.border.framed.FramedBorder;
import nu.eats.model.UserHandle;

import javax.swing.*;
import java.awt.*;

public class SignInPanel extends JPanel {

    public SignInPanel(AuthViewModel model) {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.setBorder(new FramedBorder.Builder()
                .sides(side -> side.margin(Theme.SPACING_6XL))
                .build()
        );

        // --- Fields ---
        var emailAddressField = new JTextField();
        var passwordField = new JPasswordField();

        // --- Sign-In Button ---
        var signInButton = new JButton("Continue");

        signInButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        signInButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, signInButton.getPreferredSize().height));
        signInButton.addActionListener(_ -> handleSignIn(model));

        // --- Layout ---
        add(Box.createVerticalGlue());
        add(new LabeledField("Email Address", emailAddressField));
        add(Box.createVerticalStrut(Theme.SPACING_4XL));
        add(new LabeledField("Password", passwordField));
        add(Box.createVerticalStrut(Theme.SPACING_8XL));
        add(signInButton);
        add(Box.createVerticalGlue());
    }

    private void handleSignIn(AuthViewModel model) {
        model.signIn(new Vendor("v-1", UserHandle.create("Hello").value(), "My Store"));
    }
}