package store.model;

public class GiftProduct {
    private final String name;
    private final int quantity;

    public GiftProduct(Product promotion, int possibleGiftProducts) {
        this.name = promotion.getName();
        this.quantity = possibleGiftProducts / promotion.getPromotionCount();
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }
}
