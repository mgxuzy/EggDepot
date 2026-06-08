package nu.eats.gui.plaf;

public final class ModularScale {
    public static double of(double step, double base) {
        return base * Math.pow(2.0, step / 2.0);
    }

    private ModularScale() {}
}
