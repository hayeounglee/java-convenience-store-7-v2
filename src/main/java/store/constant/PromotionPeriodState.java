package store.constant;

public enum PromotionPeriodState {
    GET_ONE_FREE("Free"),
    BUY_ORIGINAL_PRICE("Original"),
    NOTHING("NoAdditionalAsk");

    private final String message;

    PromotionPeriodState(String message) {
        this.message = message;
    }
}
