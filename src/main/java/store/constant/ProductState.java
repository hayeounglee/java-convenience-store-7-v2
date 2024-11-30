package store.constant;

public enum ProductState {
    NO_PROMOTION_PERIOD("No promotion period"),
    GET_ONE_FREE("Can get one more Free"),
    BUY_ORIGINAL_PRICE("Original"),
    NOTHING_TO_ASK("No Additional Ask");

    private final String message;

    ProductState(String message) {
        this.message = message;
    }
}
