package nu.eats.common.resources;

import java.awt.*;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public final class Fonts {

    private static final Map<String, Font> CACHE = new HashMap<>();
    private static final String DIR = "fonts";
    private static final GraphicsEnvironment ENV = GraphicsEnvironment.getLocalGraphicsEnvironment();

    private Fonts() {
    }

    public static void init() {
        if (!CACHE.isEmpty()) return;

        ResourceScanner.scan(DIR, Fonts::register);
    }

    public static Font load(String name, float size) {
        if (CACHE.isEmpty()) init();

        var font = CACHE.get(name);

        if (font == null) throw new IllegalArgumentException("Font not found: " + name);

        return font.deriveFont(size);
    }

    private static void register(Path path) {
        try (InputStream input = Files.newInputStream(path)) {
            var font = Font.createFont(Font.TRUETYPE_FONT, input);

            ENV.registerFont(font);
            CACHE.put(font.getFontName(), font);
        } catch (Exception cause) {
            System.err.println("Failed to register font: " + path + ": " + cause.getMessage());
        }
    }
}