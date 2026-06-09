package eggdepot.common.resources;

import java.net.URI;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

public final class ResourceScanner {

    private ResourceScanner() {
    }

    /**
     * Walks all files under the given classpath directory, invoking the consumer for each regular file.
     */
    public static void scan(String directory, Consumer<Path> fileConsumer) {
        try {
            var resources = ResourceScanner.class.getClassLoader().getResources(directory);

            while (resources.hasMoreElements()) {
                walk(resources.nextElement(), directory, fileConsumer);
            }
        } catch (Exception cause) {
            throw new RuntimeException("Resource scan failed for: " + directory, cause);
        }
    }

    private static void walk(URL url, String directory, Consumer<Path> fileConsumer) throws Exception {
        var protocol = url.getProtocol();

        if ("file".equals(protocol)) {
            try (Stream<Path> walker = Files.walk(Path.of(url.toURI()))) {
                walker.filter(Files::isRegularFile).forEach(fileConsumer);
            }

            return;
        }

        if ("jar".equals(protocol)) {
            var uri = URI.create("jar:" + url.toURI());

            try (var fs = FileSystems.newFileSystem(uri, Map.of());
                 Stream<Path> walker = Files.walk(fs.getPath(directory))) {
                walker.filter(Files::isRegularFile).forEach(fileConsumer);
            }
        }
    }
}
