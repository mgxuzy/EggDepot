package eggdepot.feature.auth.gui.components;

import eggdepot.feature.auth.model.Vendor;
import eggdepot.gui.components.LabeledField;
import eggdepot.gui.plaf.Theme;
import eggdepot.gui.plaf.border.framed.FramedBorder;
import eggdepot.gui.plaf.button.ButtonVariant;
import eggdepot.model.Password;
import eggdepot.model.User;
import eggdepot.model.UserHandle;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class SignUpPanel extends JPanel {
    private final JTextField nameField;
    private final JTextField emailAddressField;
    private final JPasswordField passwordField;
    private final JPasswordField reenterPasswordField;

    public SignUpPanel(Consumer<User> signedUpHandler) {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.setBorder(new FramedBorder.Builder()
                .sides(side -> side.margin(Theme.SPACING_6XL))
                .build()
        );

        // --- Fields ---
        nameField = new JTextField();
        emailAddressField = new JTextField();
        passwordField = new JPasswordField();
        reenterPasswordField = new JPasswordField();

        // --- Sign-Up Button ---
        var signUpButton = new JButton("Continue");

        ButtonVariant.PRIMARY.install(signUpButton);
        signUpButton.setAlignmentX(CENTER_ALIGNMENT);
        signUpButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, signUpButton.getPreferredSize().height));
        signUpButton.addActionListener(_ -> handleSignUp(signedUpHandler));

        // --- Layout ---
        add(Box.createVerticalGlue());
        add(new LabeledField("Name", nameField));
        add(Box.createVerticalStrut(Theme.SPACING_4XL));
        add(new LabeledField("Email", emailAddressField));
        add(Box.createVerticalStrut(Theme.SPACING_4XL));
        add(new LabeledField("Password", passwordField));
        add(Box.createVerticalStrut(Theme.SPACING_4XL));
        add(new LabeledField("Re-enter Password", reenterPasswordField));
        add(Box.createVerticalStrut(Theme.SPACING_8XL));
        add(signUpButton);
        add(Box.createVerticalGlue());
    }

    private void handleSignUp(Consumer<User> signedUpHandler) {
        var storeName = nameField.getText();
        var confirmPassword = new String(reenterPasswordField.getPassword());

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

        var usernameResult = UserHandle.create(emailAddressField.getText());

        if (usernameResult.isFailure()) {
            JOptionPane.showMessageDialog(this, String.join("\n", usernameResult.error()), "Error", JOptionPane.ERROR_MESSAGE);
        }

        JOptionPane.showMessageDialog(this, "Vendor " + storeName + " registered successfully!", "Success",
                JOptionPane.INFORMATION_MESSAGE);

        signedUpHandler.accept(new Vendor("vd", usernameResult.value(), storeName));
    }
}
