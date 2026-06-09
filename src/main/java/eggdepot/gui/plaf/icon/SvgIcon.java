package eggdepot.gui.plaf.icon;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Document;

import javax.swing.*;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * A lightweight, reusable Swing Icon that renders SVG content using Apache Batik.
 * It parses the document once and caches the GVT structure for fast rendering.
 */
public class SvgIcon implements Icon {

    private final GraphicsNode rootNode;
    private final int width;
    private final int height;

    public SvgIcon(URL url, int width, int height) throws IOException {
        this.width = width;
        this.height = height;
        this.rootNode = loadSvg(url);
    }

    public SvgIcon(InputStream inputStream, int width, int height) throws IOException {
        this.width = width;
        this.height = height;
        this.rootNode = loadSvg(inputStream);
    }

    private GraphicsNode loadSvg(URL url) throws IOException {
        var parser = XMLResourceDescriptor.getXMLParserClassName();
        var factory = new SAXSVGDocumentFactory(parser);
        var doc = factory.createDocument(url.toString());
        return buildGraphicsNode(doc);
    }

    private GraphicsNode loadSvg(InputStream is) throws IOException {
        var parser = XMLResourceDescriptor.getXMLParserClassName();
        var factory = new SAXSVGDocumentFactory(parser);
        // Using null for the URI parameter since we are reading directly from a stream
        var doc = factory.createDocument(null, is);
        return buildGraphicsNode(doc);
    }

    private GraphicsNode buildGraphicsNode(Document doc) {
        var userAgent = new UserAgentAdapter();
        var loader = new DocumentLoader(userAgent);
        var context = new BridgeContext(userAgent, loader);

        // STATIC state disables scripting and dynamic updates for faster initial processing
        context.setDynamicState(BridgeContext.STATIC);

        var builder = new GVTBuilder();
        return builder.build(context, doc);
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        var g2d = (Graphics2D) g.create();
        try {
            // Enable high-quality rendering hints
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            // Translate paint position
            g2d.translate(x, y);

            var bounds = rootNode.getPrimitiveBounds();
            if (bounds == null) {
                bounds = rootNode.getBounds();
            }

            if (bounds != null) {
                double svgWidth = bounds.getWidth();
                double svgHeight = bounds.getHeight();

                if (svgWidth > 0 && svgHeight > 0) {
                    double scaleX = width / svgWidth;
                    double scaleY = height / svgHeight;

                    var transform = new AffineTransform();
                    transform.scale(scaleX, scaleY);

                    // Normalize the coordinates if the SVG viewport starts offset
                    transform.translate(-bounds.getX(), -bounds.getY());
                    g2d.transform(transform);
                }
            }

            rootNode.paint(g2d);
        } finally {
            g2d.dispose();
        }
    }

    @Override
    public int getIconWidth() {
        return this.width;
    }

    @Override
    public int getIconHeight() {
        return this.height;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            var frame = new JFrame("SVG Icon Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 400);

            try {
                // Example of loading an SVG resource from a URL
                var svgUrl = new URL("https://upload.wikimedia.org/wikipedia/commons/d/d5/CSS3_logo_and_wordmark.svg");

                // Instantiate the icon at target dimensions (e.g., 128x128)
                var icon = new SvgIcon(svgUrl, 128, 128);

                var label = new JLabel("Batik SVG Icon", icon, JLabel.CENTER);
                frame.add(label);

            } catch (IOException e) {
                e.printStackTrace();
            }

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}