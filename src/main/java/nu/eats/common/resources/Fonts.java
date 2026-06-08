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

    static {
        ResourceScanner.scan(DIR, Fonts::register);
    }

    private Fonts() {
    }

    public static Font load(String name, float size) {
        var font = new Font(name, Font.PLAIN, (int) size);

        if (font == null)
            throw new IllegalArgumentException("Font not found: " + name);

        return font.deriveFont(size);
    }

    private static void register(Path path) {
        try (InputStream input = Files.newInputStream(path)) {
            var font = Font.createFont(Font.TRUETYPE_FONT, input);

            ENV.registerFont(font);

            IO.println("Registered font: " + font.getFamily());
        } catch (Exception cause) {
            System.err.println("Failed to register font: " + path + ": " + cause.getMessage());
        }
    }
}