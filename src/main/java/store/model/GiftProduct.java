package store.model;

public class GiftProduct {
    private final String name;
    private final int quantity;

    public GiftProduct(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }
}
