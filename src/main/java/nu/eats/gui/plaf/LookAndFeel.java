package nu.eats.gui.plaf;

import nu.eats.common.resources.Fonts;
import nu.eats.gui.plaf.button.ButtonUI;
import nu.eats.gui.plaf.field.PasswordFieldUI;
import nu.eats.gui.plaf.field.TextFieldUI;
import nu.eats.gui.plaf.panel.PanelUI;
import nu.eats.gui.plaf.root.RootPaneUI;
import nu.eats.gui.plaf.scroll.ScrollBarUI;
import nu.eats.gui.plaf.scroll.ScrollPaneUI;
import nu.eats.gui.plaf.spinner.SpinnerUI;
import nu.eats.gui.plaf.tab.TabbedPaneUI;
import nu.eats.gui.plaf.table.TableUI;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.basic.BasicLookAndFeel;
import java.awt.*;
import java.util.Map;

public class LookAndFeel extends BasicLookAndFeel {

    @Override
    protected void initComponentDefaults(UIDefaults defaults) {
        super.initComponentDefaults(defaults);

        var toolkit = Toolkit.getDefaultToolkit();
        var desktopHintsPropertyName = "awt.font.desktophints";

        var desktopHints = (Map<?, ?>) toolkit
                .getDesktopProperty(desktopHintsPropertyName);

        toolkit.addPropertyChangeListener(desktopHintsPropertyName, event -> {
            if (event.getNewValue() instanceof Map<?, ?> newDesktopHints) {
                defaults.putAll(newDesktopHints);
            }
        });

        defaults.putAll(desktopHints);

        Object themeBg = new Theme.ThemeBgColor();

        for (Object key : defaults.keySet().toArray()) {
            if (key instanceof String keyText && keyText.endsWith("UI")) {
                String componentPrefixLength = keyText.substring(0, keyText.length() - 2);

                defaults.put(componentPrefixLength + ".background", themeBg);
            }
        }

        UIDefaults.LazyValue fontRegular = _ -> new FontUIResource(Fonts.load("Inter Regular", 12));
        UIDefaults.LazyValue fontBold = _ -> new FontUIResource(Fonts.load("Inter Bold", 12));
        UIDefaults.LazyValue fontItalic = _ -> new FontUIResource(Fonts.load("Inter Italic", 12));
        UIDefaults.LazyValue fontBoldItalic = _ -> new FontUIResource(Fonts.load("Inter Bold Italic", 12));

        for (Object key : defaults.keySet().toArray()) {
            if (!(key instanceof String keyText) || !keyText.endsWith(".font")) {
                continue;
            }

            var font = defaults.getFont(keyText);

            if (font == null) {
                continue;
            }

            var fontName = font.getName();

            boolean isLogical = fontName.equalsIgnoreCase(Font.DIALOG) ||
                    fontName.equalsIgnoreCase(Font.SERIF) ||
                    fontName.equalsIgnoreCase(Font.SANS_SERIF) ||
                    fontName.equalsIgnoreCase(Font.MONOSPACED);

            if (isLogical) {
                UIDefaults.LazyValue value = switch (font.getStyle()) {
                    case Font.BOLD -> fontBold;
                    case Font.ITALIC -> fontItalic;
                    case Font.BOLD | Font.ITALIC -> fontBoldItalic;
                    default -> fontRegular;
                };

                defaults.put(keyText, value);
            }
        }

        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
    }

    @Override
    protected void initClassDefaults(UIDefaults defaults) {
        super.initClassDefaults(defaults);

        defaults.put("ButtonUI", ButtonUI.class.getName());
        defaults.put("ToggleButtonUI", ButtonUI.class.getName());

        defaults.put("TextFieldUI", TextFieldUI.class.getName());
        defaults.put("PasswordFieldUI", PasswordFieldUI.class.getName());

        defaults.put("ScrollBarUI", ScrollBarUI.class.getName());
        defaults.put("ScrollPaneUI", ScrollPaneUI.class.getName());

        defaults.put("SpinnerUI", SpinnerUI.class.getName());

        defaults.put("TabbedPaneUI", TabbedPaneUI.class.getName());

        defaults.put("RootPaneUI", RootPaneUI.class.getName());
        defaults.put("PanelUI", PanelUI.class.getName());

        defaults.put("TableUI", TableUI.class.getName());
    }

    @Override
    public String getName() {
        return "Basic";
    }

    @Override
    public String getID() {
        return "Basic";
    }

    @Override
    public String getDescription() {
        return "The Basic Look and Feel";
    }

    @Override
    public boolean isNativeLookAndFeel() {
        return false;
    }

    @Override
    public boolean isSupportedLookAndFeel() {
        return true;
    }

    @Override
    public boolean getSupportsWindowDecorations() {
        return true;
    }
}