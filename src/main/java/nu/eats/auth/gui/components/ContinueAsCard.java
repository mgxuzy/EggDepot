package nu.eats.authentication.gui.components;

import nu.eats.domain.Customer;
import nu.eats.gui.components.ToolBar;
import nu.eats.gui.plaf.Theme;
import nu.eats.gui.plaf.button.ButtonPreset;
import nu.eats.gui.plaf.button.ButtonVariant;
import nu.eats.ui.auth.AuthViewModel;

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
        customerButton.addActionListener(_ -> model.signIn(new Customer("c-1", "Guest Customer")));

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

        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.add(subtitle);
        mainContent.add(Box.createVerticalGlue());
        mainContent.add(Box.createVerticalStrut(Theme.SPACING_SM));
        mainContent.add(optionList);
        mainContent.add(Box.createVerticalGlue());
    }

    @Override
    protected ToolBar createToolBar() {
        var toolBar = new ToolBar();

        toolBar.add(createExitButton(), ToolBar.RIGHT);

        return toolBar;
    }
}
