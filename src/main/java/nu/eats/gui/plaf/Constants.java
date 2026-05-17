package nu.eats.gui.plaf;

import java.awt.*;
import java.util.Map;

public final class Constants {
    public static final RenderingHints DEFAULT_RENDERING_HINTS = new RenderingHints(Map.of(
            RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON,
            RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE,
            RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));

    public static final String KEY_BOX_DECORATION = "backgroundShape";
    public static final String KEY_COMPONENT_VARIANT = "component.variant";
    public static final String KEY_COMPONENT_STATE = "component.state";
    public static final String KEY_PLACEHOLDER = "placeholder";
}
