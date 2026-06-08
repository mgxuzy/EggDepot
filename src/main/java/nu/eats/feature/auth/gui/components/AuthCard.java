package nu.eats.feature.auth.gui.components;

import nu.eats.feature.auth.state.AuthViewModel;
import nu.eats.gui.components.Card;
import nu.eats.gui.components.ToolBar;
import nu.eats.gui.plaf.Theme;
import nu.eats.gui.plaf.button.ButtonPreset;
import nu.eats.gui.plaf.button.ButtonVariant;
import nu.eats.gui.plaf.icons.CloseIcon;
import nu.eats.model.Cashier;

import javax.swing.*;
import java.awt.*;

public class AuthCard extends Card {
    public AuthCard(AuthViewModel model) {
        super(new BorderLayout());

        var toolBar = createToolBar();

        var contentPanel = new JPanel();

        contentPanel.setOpaque(false);

        var tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Sign In", new SignInPanel(model));
        tabbedPane.addTab("Sign Up", new SignUpPanel(_ -> model.signIn(new Cashier("c-1", "Guest Customer"))));

        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(tabbedPane, BorderLayout.CENTER);

        this.add(toolBar, BorderLayout.NORTH);
        this.add(contentPanel, BorderLayout.CENTER);
    }

    protected ToolBar createToolBar() {
        var toolBar = new ToolBar();

        toolBar.add(createExitButton(), ToolBar.RIGHT);

        return toolBar;
    }

    protected JButton createExitButton() {
        var closeButton = new JButton(new CloseIcon(Math.round(Theme.FONT_SIZE_SM)));

        ButtonPreset.MD.apply(closeButton);
        ButtonVariant.FLAT.install(closeButton);

        setBorder(null);

        closeButton.setFont(Theme.FONT_MONOSPACE_SM);
        closeButton.addActionListener(_ -> showExitDialog());

        return closeButton;
    }

    private void showExitDialog() {
        Object[] options = {"Yes", "No"};

        var result = JOptionPane.showOptionDialog(
                null,
                "Are you sure you want to exit?",
                "Exit Confirmation",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]
        );

        if (result == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
