package nu.eats.common.resources;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public final class Images {

    private static final Map<String, BufferedImage> CACHE = new HashMap<>();
    private static final String DIR = "images";

    private Images() {
    }

    public static void init() {
        if (!CACHE.isEmpty()) return;

        ResourceScanner.scan(DIR, Images::register);
    }

    public static BufferedImage load(String fileName) {
        if (CACHE.isEmpty()) init();

        BufferedImage image = CACHE.get(fileName);

        if (image == null) throw new IllegalArgumentException("Image not found: " + fileName);

        return image;
    }

    public static ImageIcon loadIcon(String fileName) {
        return new ImageIcon(load(fileName));
    }

    public static ImageIcon loadIcon(String fileName, int width, int height) {
        Image scaled = load(fileName).getScaledInstance(width, height, Image.SCALE_SMOOTH);

        return new ImageIcon(scaled);
    }

    public static ImageIcon loadIcon(String fileName, int size) {
        BufferedImage image = load(fileName);

        float scale = Math.min((float) size / image.getWidth(), (float) size / image.getHeight());

        int scaledWidth = Math.round(image.getWidth() * scale);
        int scaledHeight = Math.round(image.getHeight() * scale);

        Image scaled = image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);

        return new ImageIcon(scaled);
    }

    private static void register(Path path) {
        try (InputStream input = Files.newInputStream(path)) {
            BufferedImage image = ImageIO.read(input);

            if (image == null) return;

            String fileName = path.getFileName().toString();

            CACHE.put(fileName, image);
        } catch (Exception cause) {
            System.err.println("Failed to register image: " + path + ": " + cause.getMessage());
        }
    }
}