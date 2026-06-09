package eggdepot.gui.theme;

public enum ModularSize {
    XS(-2),
    SM(-1),
    MD(0),
    LG(1),
    XL(2);

    public final double exponent;

    ModularSize(double exponent) {
        this.exponent = exponent;
    }
}
