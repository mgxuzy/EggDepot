package eggdepot.gui.plaf.icon;

import java.awt.geom.Path2D;

public class CheckIcon {
    private static final int CHECK_SIZE = 18;

    private static Path2D getCheck(int x, int y) {
        var checkPath = new Path2D.Float();

        // Start (Left wing)
        var x1 = x + (CHECK_SIZE * 0.28f);
        var y1 = y + (CHECK_SIZE * 0.52f);

        // Pivot (Bottom point)
        var x2 = x + (CHECK_SIZE * 0.46f);
        var y2 = y + (CHECK_SIZE * 0.72f);

        // End (Right wing)
        var x3 = x + (CHECK_SIZE * 0.74f);
        var y3 = y + (CHECK_SIZE * 0.32f);

        checkPath.moveTo(x1, y1);
        checkPath.lineTo(x2, y2);
        checkPath.lineTo(x3, y3);

        return checkPath;
    }
}
