package nu.eats.auth.gui.components;

import nu.eats.gui.components.Card;
import nu.eats.gui.components.ToolBar;
import nu.eats.gui.plaf.Theme;
import nu.eats.gui.plaf.button.ButtonPreset;
import nu.eats.gui.plaf.button.ButtonState;
import nu.eats.gui.plaf.button.ButtonVariant;
import nu.eats.gui.plaf.icons.CloseIcon;

import javax.swing.*;
import java.awt.*;

import static nu.eats.gui.plaf.Constants.KEY_COMPONENT_VARIANT;

public class AuthCard extends Card {
    protected final JPanel contentPanel;

    private final ToolBar toolBar;

    public AuthCard() {
        super(new BorderLayout());

        this.toolBar = createToolBar();

        add(toolBar, BorderLayout.NORTH);

        this.contentPanel = new JPanel();
        this.contentPanel.setOpaque(false);

        add(this.contentPanel, BorderLayout.CENTER);

        setOpaque(false);
        setBackground(Theme.COLOR_BG);
    }

    protected ToolBar createToolBar() {
        var toolBar = new ToolBar();

        toolBar.addBackAction(this::unstack);
        toolBar.add(createExitButton(), ToolBar.RIGHT);

        return toolBar;
    }

    protected JButton createExitButton() {
        var closeButton = new JButton(new CloseIcon(12));

        ButtonPreset.XS.apply(closeButton);

        setBorder(null);

        closeButton.setFont(Theme.FONT_MONO_14);
        ButtonVariant.SECONDARY.install(closeButton);
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

    public ToolBar toolBar() {
        return toolBar;
    }
}
