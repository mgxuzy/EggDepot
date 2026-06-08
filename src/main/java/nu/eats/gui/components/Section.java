package nu.eats.gui.components;

import nu.eats.gui.plaf.Theme;
import nu.eats.gui.plaf.border.framed.FramedBorder;

import javax.swing.*;

public class Section extends JPanel {
    public Section() {
        this.setBackground(Theme.COLOR_BG);

        this.setBorder(new FramedBorder.Builder()
                .corners((corner) -> corner.radius(Theme.RADIUS_MD))
                .build()
        );
    }
}
