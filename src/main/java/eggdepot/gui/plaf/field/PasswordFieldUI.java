package eggdepot.gui.plaf.field;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.text.Element;
import javax.swing.text.PasswordView;
import javax.swing.text.View;

public class PasswordFieldUI extends TextFieldUI {

    @SuppressWarnings("UnusedDeclaration")
    public static ComponentUI createUI(JComponent component) {
        return new PasswordFieldUI();
    }

    @Override
    protected void installDefaults() {
        super.installDefaults();

        LookAndFeel.installProperty(getComponent(), "echoChar", '•');
    }

    @Override
    protected String getPropertyPrefix() {
        return "PasswordField";
    }

    @Override
    public View create(Element element) {
        return new PasswordView(element);
    }
}
