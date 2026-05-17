package nu.eats.domain;

public class Quantity {
    private int value;

    public Quantity(int value) {
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
