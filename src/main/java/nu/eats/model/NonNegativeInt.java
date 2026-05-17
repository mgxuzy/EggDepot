package nu.eats.model;

public class NonNegativeInt {
    private int value;

    public NonNegativeInt(int value) {
        setValue(value);
    }

    public int value() {
        return value;
    }

    public void setValue(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        this.value = value;
    }
}
