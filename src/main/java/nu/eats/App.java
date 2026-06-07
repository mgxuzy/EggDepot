package nu.eats;

import nu.eats.gui.MainView;
import nu.eats.feature.auth.gui.AuthDialog;
import nu.eats.gui.plaf.LookAndFeel;

import javax.swing.*;

public class App {
    private static final String NAME = "Mang Boy";

    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 800;

    static void main() {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new LookAndFeel());
            } catch (UnsupportedLookAndFeelException cause) {
                throw new RuntimeException(cause);
            }

            // --- Auth ---
            var authDialog = new AuthDialog(null);

            authDialog.setVisible(true);

            if (!authDialog.isSignedIn()) {
                System.exit(0);
            }

            // --- Store ---
            var frame = new JFrame(NAME);

            // BoxDecoration.ensure(frame.getRootPane()).borderRadius(BoxMeasure.pixels(16));

            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setContentPane(new MainView());

            frame.pack();
            frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
