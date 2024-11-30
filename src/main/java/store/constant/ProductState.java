package store.constant;

public enum ProductState {
    NO_PROMOTION_PERIOD("No promotion period"),
    GET_ONE_FREE("Promotion Period - Can get one more Free"),
    BUY_ORIGINAL_PRICE("Promotion Period - Buy in Original Price"),
    NOTHING_TO_ASK("Promotion Period - No Additional Ask");

    private final String message;

    ProductState(String message) {
        this.message = message;
    }
}
