package nu.eats.gui.plaf.measure;

public record Measure(double value, MeasurementUnit unit) {
    public static final Measure ZERO = Measure.px(0);

    public static Measure px(double pixels) {
        return new Measure(pixels, MeasurementUnit.PIXELS);
    }

    public static Measure fraction(double fraction) {
        return new Measure(fraction, MeasurementUnit.FRACTION);
    }

    public double resolve(double referenceValue) {
        var resolvedValue = switch (unit) {
            case PIXELS -> value;
            case FRACTION -> value * referenceValue;
        };

        return Math.min(resolvedValue, Double.MAX_VALUE);
    }

    public Measure withValue(double value) {
        return new Measure(value, this.unit);
    }
}