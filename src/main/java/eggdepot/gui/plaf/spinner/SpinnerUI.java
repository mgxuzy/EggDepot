package eggdepot.gui.plaf.spinner;

import eggdepot.gui.plaf.field.TextFieldState;
import eggdepot.gui.plaf.field.TextFieldUI;
import eggdepot.gui.plaf.field.TextFieldVariant;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSpinnerUI;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class SpinnerUI extends BasicSpinnerUI {

    @SuppressWarnings("UnusedDeclaration")
    public static ComponentUI createUI(JComponent component) {
        return new SpinnerUI();
    }

    @Override
    protected void installDefaults() {
        super.installDefaults();

        spinner.setOpaque(false);
        spinner.setBorder(null);

        configureEditor(spinner.getEditor());

        UIManager.put("Spinner.disableOnBoundaryValues", true);
        UIManager.put("Spinner.editorAlignment", JTextField.LEFT);
    }

    private void configureEditor(JComponent editor) {
        editor.setOpaque(false);

        if (editor instanceof JSpinner.DefaultEditor defaultEditor) {
            var textField = defaultEditor.getTextField();

            // Force the text field to fill the editor panel
            defaultEditor.setLayout(new BorderLayout());
            defaultEditor.add(textField, BorderLayout.CENTER);

            textField.setUI(new TextFieldUI());
            TextFieldVariant.TERTIARY.install(textField, TextFieldState.NEUTRAL);
            textField.setColumns(2);

            textField.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent event) {
                    var mousePoint = MouseInfo.getPointerInfo().getLocation();

                    if (mousePoint == null) return;

                    SwingUtilities.invokeLater(() -> textField.setCaretPosition(textField.viewToModel2D(mousePoint)));
                }
            });

            SwingUtilities.invokeLater(() -> textField.setHorizontalAlignment(JTextField.CENTER));
        }
    }

    @Override
    protected Component createNextButton() {
        var button = new SpinButton(SwingConstants.NORTH);

        spinner.getModel().getValue();

        button.setName("Spinner.nextButton");
        installNextButtonListeners(button);

        if (spinner.getEditor() instanceof JSpinner.DefaultEditor defaultEditor) {
            var textField = defaultEditor.getTextField();

            button.addActionListener(_ -> {
                SwingUtilities.invokeLater(() -> textField.setCaretPosition(textField.getText().length()));
            });
        }

        return button;
    }

    @Override
    protected Component createPreviousButton() {
        var button = new SpinButton(SwingConstants.SOUTH);

        button.setName("Spinner.previousButton");
        installPreviousButtonListeners(button);

        return button;
    }

    @Override
    protected void replaceEditor(JComponent oldEditor, JComponent newEditor) {
        spinner.remove(oldEditor);
        configureEditor(newEditor);

        var gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 0, 0);

        spinner.add(newEditor, gbc);
    }

    @Override
    public void installUI(JComponent component) {
        super.installUI(component);

        component.removeAll();

        // GridBagLayout is used to prevent stretching.
        component.setLayout(new GridBagLayout());

        var constraints = new GridBagConstraints();

        constraints.weighty = 0.0;
        constraints.anchor = GridBagConstraints.CENTER;

        // 1. Previous Button
        constraints.gridx = 0;
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0.0;
        constraints.insets = new Insets(0, 0, 0, 4); // Tiny gap

        component.add(createPreviousButton(), constraints);

        // 2. Editor (Text Field)
        constraints.gridx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL; // Fill available space
        constraints.weightx = 1.0;
        constraints.insets = new Insets(0, 0, 0, 0);

        component.add(createEditor(), constraints);

        // 3. Next Button
        constraints.gridx = 2;
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0.0;
        constraints.insets = new Insets(0, 4, 0, 0); // Tiny gap

        component.add(createNextButton(), constraints);
    }
}
