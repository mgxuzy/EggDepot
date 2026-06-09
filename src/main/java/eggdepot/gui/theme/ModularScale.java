package eggdepot.gui.theme;

public class ModularScale {
    private final double baseSize, ratio;

    public ModularScale(double baseSize, double ratio) {
        this.baseSize = baseSize;
        this.ratio = ratio;
    }

    public double resolve(ModularSize size) {
        return baseSize * Math.pow(ratio, size.exponent);
    }
}
