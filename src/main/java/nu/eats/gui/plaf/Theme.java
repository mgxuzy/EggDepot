package nu.eats.gui.plaf;

import nu.eats.common.resources.Fonts;

import javax.swing.plaf.ColorUIResource;
import java.awt.*;

public final class Theme {
    // Slate Palette (Cooler Gray)
    public static final Color ZINC_50 = new Color(244, 246, 248);
    public static final Color ZINC_100 = new Color(235, 238, 241);
    public static final Color ZINC_200 = new Color(218, 222, 226);
    public static final Color ZINC_300 = new Color(199, 206, 214);
    public static final Color ZINC_400 = new Color(148, 163, 184);
    public static final Color ZINC_500 = new Color(100, 116, 139);
    public static final Color ZINC_600 = new Color(71, 85, 105);
    public static final Color ZINC_700 = new Color(51, 65, 85);
    public static final Color ZINC_800 = new Color(30, 41, 59);
    public static final Color ZINC_900 = new Color(15, 23, 42); // Almost black navy
    public static final Color ZINC_950 = new Color(2, 6, 23);

    public static final Color COLOR_TRANSPARENT = new Color(0, 0, 0, 0);
    public static final Color COLOR_PRIMARY = ZINC_900;
    public static final Color COLOR_BG_PRIMARY_HOVER = ZINC_800;
    public static final Color COLOR_BG_PRIMARY_PRESSED = ZINC_600;

    public static final Color COLOR_BG = ZINC_50;
    public static final Color COLOR_BG_HOVER = ZINC_100;
    public static final Color COLOR_BG_PRESSED = ZINC_200;
    public static final Color COLOR_BG_SELECTED = ZINC_900;

    public static final Color COLOR_BORDER = ZINC_200;
    public static final Color COLOR_BORDER_HOVER = ZINC_400;
    public static final Color COLOR_BORDER_SELECTED = ZINC_900;

    public static final Color COLOR_FG = ZINC_950;
    public static final Color COLOR_FG_MUTED = ZINC_500;
    public static final Color COLOR_FG_INVERSE = ZINC_50;

    public static final Color COLOR_RING = ZINC_900;
    public static final Color COLOR_THUMB = ZINC_300;

    public static final Color COLOR_PLACEHOLDER_BG = ZINC_200;
    public static final Color COLOR_PLACEHOLDER_FG = ZINC_400;
    public static final Color COLOR_PLACEHOLDER_BG_INVERSE = ZINC_700;
    public static final Color COLOR_PLACEHOLDER_FG_INVERSE = ZINC_500;

    public static final Color COLOR_GREEN = new Color(0, 128, 0);

    public static final float RADIUS_SM = 8f;
    public static final float RADIUS_MD = 12f;
    public static final float RADIUS_LG = 16f;

    public static final int SPACING_2XS = 2;
    public static final int SPACING_XS = 4;
    public static final int SPACING_SM = 8;
    public static final int SPACING_MD = 12;
    public static final int SPACING_LG = 16;
    public static final int SPACING_XL = 20;
    public static final int SPACING_2XL = 24;
    public static final int SPACING_3XL = 30;
    public static final int SPACING_9XL = 72;

    public static final int TITLE_BAR_HEIGHT = 32;
    public static final int TITLE_BAR_BUTTON_WIDTH = 46;
    public static final int TITLE_BAR_BUTTON_GAP = 0;

    public static final int FONT_SIZE_XS = 12;
    public static final int FONT_SIZE_SM = 14;
    public static final int FONT_SIZE_BASE = 16;
    public static final int FONT_SIZE_2XL = 24;

    public static final String FONT_MONOSPACE = "JetBrains Mono";

    public static final Font FONT_REGULAR_12 = Fonts.load("Inter Regular", FONT_SIZE_XS);
    public static final Font FONT_REGULAR_14 = Fonts.load("Inter Regular", FONT_SIZE_SM);
    public static final Font FONT_REGULAR_BASE = Fonts.load("Inter Regular", FONT_SIZE_BASE);

    public static final Font FONT_BOLD_12 = Fonts.load("Inter Bold", FONT_SIZE_XS);
    public static final Font FONT_BOLD_14 = Fonts.load("Inter Bold", FONT_SIZE_SM);
    public static final Font FONT_BOLD_BASE = Fonts.load("Inter Bold", FONT_SIZE_BASE);
    public static final Font FONT_BOLD_24 = Fonts.load("Inter Bold", FONT_SIZE_2XL);

    public static final Font FONT_ITALIC_14 = Fonts.load("Inter Italic", FONT_SIZE_SM);

    public static final Font FONT_MEDIUM_12 = Fonts.load("Inter SemiBold", FONT_SIZE_XS);
    public static final Font FONT_MEDIUM_14 = Fonts.load("Inter SemiBold", FONT_SIZE_SM);
    public static final Font FONT_MEDIUM_BASE = Fonts.load("Inter SemiBold", FONT_SIZE_BASE);

    public static final Font FONT_MONO_12 = new Font(FONT_MONOSPACE, Font.PLAIN, FONT_SIZE_XS);
    public static final Font FONT_MONO_13 = new Font(FONT_MONOSPACE, Font.PLAIN, 13);
    public static final Font FONT_MONO_14 = new Font(FONT_MONOSPACE, Font.PLAIN, FONT_SIZE_SM);

    private Theme() {
    }

    public static class ThemeBgColor extends ColorUIResource {
        public ThemeBgColor() {
            super(COLOR_BG);
        }
    }
}
