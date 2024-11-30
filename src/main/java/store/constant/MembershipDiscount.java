package store.constant;

public enum MembershipDiscount {
    MAX_DISCOUNT(8000),
    DISCOUNT_RATE(0.3);

    private final double value;

    MembershipDiscount(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}
