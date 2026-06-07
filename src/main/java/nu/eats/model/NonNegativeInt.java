package nu.eats.model;

public class NonNegativeInt {
    private int value;

    public NonNegativeInt(int value) {
        this.setValue(value);
    }

    public int value() {
        return this.value;
    }

    public void setValue(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Value cannot be negative");
        }

        this.value = value;
    }
}
