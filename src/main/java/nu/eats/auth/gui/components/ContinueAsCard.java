package nu.eats.auth.gui.components;

import nu.eats.model.Cashier;
import nu.eats.gui.components.ToolBar;
import nu.eats.gui.plaf.Theme;
import nu.eats.gui.plaf.button.ButtonPreset;
import nu.eats.gui.plaf.button.ButtonVariant;
import nu.eats.auth.state.AuthViewModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ContinueAsCard extends AuthCard {
    public ContinueAsCard(AuthViewModel model) {
        super();

        var subtitle = new JLabel("Choose how you'd like to continue as");

        subtitle.setFont(Theme.FONT_REGULAR_BASE);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        var customerButton = new JButton("Customer");

        ButtonPreset.XL.apply(customerButton);
        customerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        customerButton.addActionListener(_ -> model.signIn(new Cashier("c-1", "Guest Customer")));

        var vendorButton = new JButton("Vendor");

        ButtonPreset.XL.apply(vendorButton);
        ButtonVariant.SECONDARY.install(vendorButton);

        vendorButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        vendorButton.addActionListener(_ -> stack(new VendorSignInCard(model)));

        var optionList = new JPanel(new GridLayout(2, 1, 0, Theme.SPACING_SM));

        optionList.setBorder(new EmptyBorder(Theme.SPACING_2XL, Theme.SPACING_2XL, Theme.SPACING_2XL, Theme.SPACING_2XL));
        optionList.add(customerButton);
        optionList.add(vendorButton);

        toolBar().setTitle("Welcome");

        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.add(subtitle);
        contentPanel.add(Box.createVerticalGlue());
        contentPanel.add(Box.createVerticalStrut(Theme.SPACING_SM));
        contentPanel.add(optionList);
        contentPanel.add(Box.createVerticalGlue());
    }

    @Override
    protected ToolBar createToolBar() {
        var toolBar = new ToolBar();

        toolBar.add(createExitButton(), ToolBar.RIGHT);

        return toolBar;
    }
}
