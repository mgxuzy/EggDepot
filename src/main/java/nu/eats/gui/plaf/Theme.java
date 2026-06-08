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
    public static final Color COLOR_PRIMARY = new Color(0xFF48672E);

    public static final Color COLOR_SURFACE_ELEVATION_HIGHEST = new Color(0xFFFFFFFF);
    public static final Color COLOR_SURFACE_ELEVATION_LOW = new Color(0xFFF3F5EA);

    public static final Color COLOR_BG = new Color(0xFFF9FAEF);
    public static final Color COLOR_BG_PRESSED = new Color(0xFF314E19);
    public static final Color COLOR_BG_HOVER = new Color(0xFFC9EEA7);
    public static final Color COLOR_BG_SELECTED = ZINC_900;

    public static final Color COLOR_BORDER = new Color(0xFFC4C8BA);
    public static final Color COLOR_BORDER_HOVER = ZINC_400;
    public static final Color COLOR_BORDER_SELECTED = ZINC_900;

    public static final Color COLOR_FG_PRIMARY = new Color(0xFFFFFF);
    public static final Color COLOR_FG_INVERSE = new Color(0xFF1A1D16);
    public static final Color COLOR_FG_SECONDARY = new Color(0xFF44483E);

    public static final Color COLOR_RING = ZINC_900;
    public static final Color COLOR_THUMB = new Color(0x33000000, true);

    public static final Color COLOR_PLACEHOLDER_BG = ZINC_200;
    public static final Color COLOR_PLACEHOLDER_FG = ZINC_400;
    public static final Color COLOR_PLACEHOLDER_BG_INVERSE = ZINC_700;
    public static final Color COLOR_PLACEHOLDER_FG_INVERSE = ZINC_500;

    public static final Color COLOR_ACCENT = new Color(0xFF386664);

    public static final int FONT_SIZE_BASE = 16;
    public static final int TITLE_BAR_BUTTON_GAP = 0;
    // Font Size
    public static final float FONT_SIZE_2XS = scaleFontSize(-2f);
    public static final float FONT_SIZE_XS = scaleFontSize(-1.0f);
    public static final float FONT_SIZE_SM = scaleFontSize(-2.0f / 3.0f);
    public static final Font FONT_ITALIC_SM = Fonts.load("Inter Italic", FONT_SIZE_SM);
    public static final float FONT_SIZE_MD = FONT_SIZE_BASE;
    public static final float FONT_SIZE_LG = scaleFontSize(1.0f);
    public static final float FONT_SIZE_XL = scaleFontSize(2.0f);
    public static final float FONT_SIZE_2XL = scaleFontSize(3.0f);
    public static final Font FONT_REGULAR_BASE = new Font("Inter", Font.PLAIN, FONT_SIZE_BASE);
    public static final Font FONT_REGULAR_SM = FONT_REGULAR_BASE.deriveFont(FONT_SIZE_SM);
    public static final Font FONT_REGULAR_MD = FONT_REGULAR_BASE.deriveFont(FONT_SIZE_MD);
    public static final Font FONT_REGULAR_LG = FONT_REGULAR_BASE.deriveFont(FONT_SIZE_LG);
    public static final Font FONT_BOLD_BASE = new Font("Inter Bold", Font.BOLD, FONT_SIZE_BASE);
    public static final Font FONT_BOLD_SM = FONT_BOLD_BASE.deriveFont(FONT_SIZE_SM);
    public static final Font FONT_BOLD_MD = FONT_BOLD_BASE.deriveFont(FONT_SIZE_MD);
    public static final Font FONT_BOLD_LG = FONT_BOLD_BASE.deriveFont(FONT_SIZE_LG);
    public static final Font FONT_BOLD_XL = FONT_BOLD_BASE.deriveFont(FONT_SIZE_XL);
    public static final Font FONT_BOLD_2XL = FONT_BOLD_BASE.deriveFont(FONT_SIZE_2XL);
    public static final Font FONT_MEDIUM_BASE = new Font("Inter Medium", Font.PLAIN, FONT_SIZE_BASE);
    public static final Font FONT_MEDIUM_SM = FONT_MEDIUM_BASE.deriveFont(FONT_SIZE_SM);
    public static final Font FONT_MEDIUM_MD = FONT_MEDIUM_BASE.deriveFont(FONT_SIZE_MD);
    public static final Font FONT_MONOSPACED_BASE = new Font("JetBrains Mono", Font.PLAIN, FONT_SIZE_BASE);
    public static final Font FONT_MONOSPACE_SM = FONT_MONOSPACED_BASE.deriveFont(FONT_SIZE_SM);
    private static final double RADIUS_BASE_SIZE = 8.0;
    // Radii
    public static final int RADIUS_2XS = scaleRadiusSize(-2.0f);
    public static final int RADIUS_XS = scaleRadiusSize(-2.0f / 2.0f);
    public static final int RADIUS_SM = scaleRadiusSize(-2.0f / 3.0f);
    public static final int RADIUS_MD = scaleRadiusSize(0.0f);
    public static final int RADIUS_LG = scaleRadiusSize(2.0f / 3.0f);
    public static final int RADIUS_XL = scaleRadiusSize(1.0f);
    public static final int RADIUS_2XL = scaleRadiusSize(2.0f);
    private static final double SPACING_BASE_SIZE = 4.0;
    // ── Spacing ──────────────────────────────────────────────────────
    public static final int SPACING_2XS = scaleSpacingSize(-2f);
    public static final int SPACING_XS = scaleSpacingSize(-1.0f);
    public static final int SPACING_SM = scaleSpacingSize(-2f / 3f);
    public static final int SPACING_MD = scaleSpacingSize(0.0);
    public static final int SPACING_LG = scaleSpacingSize(2f / 3f);
    public static final int SPACING_XL = scaleSpacingSize(1.0f);
    public static final int SPACING_2XL = scaleSpacingSize(2f);
    public static final int SPACING_3XL = scaleSpacingSize(3f);
    public static final int SPACING_4XL = scaleSpacingSize(4f);
    public static final int SPACING_5XL = scaleSpacingSize(5f);
    public static final int SPACING_6XL = scaleSpacingSize(6f);
    // ── Title Bar ────────────────────────────────────────────────────
    public static final int TITLE_BAR_HEIGHT = SPACING_6XL;
    public static final int SPACING_7XL = scaleSpacingSize(7f);
    public static final int TITLE_BAR_BUTTON_WIDTH = SPACING_7XL; // 45
    public static final int SPACING_8XL = scaleSpacingSize(8f);
    public static final int SPACING_13XL = scaleSpacingSize(13f);
    public static final int SPACING_14XL = scaleSpacingSize(14f);
    private Theme() {
    }

    private static int scaleRadiusSize(double scale) {
        IO.println("Radius scale %f: %f".formatted(scale, ModularScale.of(scale, RADIUS_BASE_SIZE)));

        return (int) Math.round(ModularScale.of(scale, RADIUS_BASE_SIZE));
    }

    private static int scaleSpacingSize(double scale) {
        IO.println("Spacing scale %f: %f".formatted(scale, ModularScale.of(scale, SPACING_BASE_SIZE)));

        return (int) Math.round(ModularScale.of(scale, SPACING_BASE_SIZE));
    }

    private static int scaleFontSize(float step) {
        IO.println("Font scale %f: %f".formatted(step, ModularScale.of(step, FONT_SIZE_BASE)));

        return (int) Math.round(ModularScale.of(step, FONT_SIZE_BASE));
    }

    public static class ThemeBgColor extends ColorUIResource {
        public ThemeBgColor() {
            super(COLOR_BG);
        }
    }
}
