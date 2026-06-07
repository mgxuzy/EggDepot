package nu.eats.gui.components;

import nu.eats.gui.plaf.Theme;
import nu.eats.gui.plaf.border.BoxDecoration;
import nu.eats.gui.plaf.border.FramedBorder;

import javax.swing.*;
import java.awt.*;

import static nu.eats.gui.plaf.Constants.DEFAULT_RENDERING_HINTS;
import static nu.eats.gui.plaf.Constants.KEY_BOX_DECORATION;

public class Section extends JPanel {
    public Section() {
        this.setBackground(Theme.COLOR_BG);

        this.setBorder(new FramedBorder.Builder()
                .corners((corner) -> corner.radius(Theme.RADIUS_MD))
                .build()
        );
    }
}
