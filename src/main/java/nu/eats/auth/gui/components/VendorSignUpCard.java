package nu.eats.authentication.gui.components;

import nu.eats.domain.Password;
import nu.eats.domain.User;
import nu.eats.domain.Username;
import nu.eats.authentication.domain.Vendor;
import nu.eats.gui.components.LabeledField;
import nu.eats.gui.components.Section;
import nu.eats.gui.plaf.Theme;
import nu.eats.gui.plaf.button.ButtonPreset;
import nu.eats.gui.plaf.button.ButtonVariant;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.function.Consumer;

import static nu.eats.gui.plaf.Constants.KEY_COMPONENT_VARIANT;

public class VendorSignUpCard extends AuthCard {
    private final JTextField storeNameField;
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JPasswordField confirmPasswordField;

    public VendorSignUpCard(Consumer<User> signedUpHandler) {
        super();

        toolBar().setTitle("Vendor Sign-Up");

        // -- Fields ---
        storeNameField = new JTextField();
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        confirmPasswordField = new JPasswordField();

        // --- Sign-Up ---
        var signUpButton = new JButton("Sign Up");

        ButtonPreset.MD.apply(signUpButton);
        signUpButton.putClientProperty(KEY_COMPONENT_VARIANT, ButtonVariant.PRIMARY);
        signUpButton.setAlignmentX(CENTER_ALIGNMENT);
        signUpButton.setMaximumSize(new Dimension(280, signUpButton.getPreferredSize().height));
        signUpButton.addActionListener(_ -> handleSignUp(signedUpHandler));

        var signInLabel = new JLabel("Already have an account? ");
        var signInButton = new JButton("Sign In");

        ButtonPreset.XS.apply(signInButton);
        signInButton.setBorder(null);
        signInButton.setFont(Theme.FONT_MEDIUM_12);
        signInButton.putClientProperty(KEY_COMPONENT_VARIANT, ButtonVariant.TERTIARY);
        signInButton.setAlignmentX(CENTER_ALIGNMENT);
        signInButton.addActionListener(_ -> unstack());

        var signInRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));

        signInRow.setOpaque(false);
        signInRow.add(signInLabel);
        signInRow.add(signInButton);

        // -- Main Content --
        var mainContent = new Section();

        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));

        mainContent.setBorder(new EmptyBorder(
                Theme.SPACING_2XL, Theme.SPACING_2XL, Theme.SPACING_2XL, Theme.SPACING_2XL));
        mainContent.add(Box.createVerticalGlue());
        mainContent.add(new LabeledField("Store Name", storeNameField));
        mainContent.add(Box.createVerticalStrut(Theme.SPACING_MD));
        mainContent.add(new LabeledField("Username", usernameField));
        mainContent.add(Box.createVerticalStrut(Theme.SPACING_MD));
        mainContent.add(new LabeledField("Password", passwordField));
        mainContent.add(Box.createVerticalStrut(Theme.SPACING_MD));
        mainContent.add(new LabeledField("Re-enter Password", confirmPasswordField));
        mainContent.add(Box.createVerticalStrut(Theme.SPACING_3XL));
        mainContent.add(signUpButton);
        mainContent.add(Box.createVerticalStrut(Theme.SPACING_LG));
        mainContent.add(signInRow);
        mainContent.add(Box.createVerticalGlue());

        this.mainContent.setLayout(new BorderLayout());
        this.mainContent.add(mainContent, BorderLayout.CENTER);
    }

    private void handleSignUp(Consumer<User> signedUpHandler) {
        var storeName = storeNameField.getText();
        var confirmPassword = new String(confirmPasswordField.getPassword());

        if (storeName.isBlank()) {
            JOptionPane.showMessageDialog(this, "Store Name is required", "Error", JOptionPane.ERROR_MESSAGE);

            return;
        }

        var passwordResult = Password.create(new String(passwordField.getPassword()));

        if (passwordResult.isFailure()) {
            JOptionPane.showMessageDialog(this, String.join("\n", passwordResult.error()), "Error",
                    JOptionPane.ERROR_MESSAGE);

            return;
        }

        if (!passwordResult.value().value().equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);

            return;
        }

        var usernameResult = Username.create(usernameField.getText());

        if (usernameResult.isFailure()) {
            JOptionPane.showMessageDialog(this, String.join("\n", usernameResult.error()), "Error", JOptionPane.ERROR_MESSAGE);
        }

        JOptionPane.showMessageDialog(this, "Vendor " + storeName + " registered successfully!", "Success",
                JOptionPane.INFORMATION_MESSAGE);

        signedUpHandler.accept(new Vendor("vd", usernameResult.value(), storeName));
    }
}
