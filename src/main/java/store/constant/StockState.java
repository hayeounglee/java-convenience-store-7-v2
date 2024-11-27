package store.constant;

public enum StockState {
    NO_STOCK("0", "재고없음"),
    HAVE_STOCK("1", "");

    private final String message;
    private final String stockState;

    StockState(String message, String stockState) {
        this.message = message;
        this.stockState = stockState;
    }

    public static String getMatchingState(String findAnswer) {
        if (NO_STOCK.message.equals(findAnswer)) {
            return NO_STOCK.stockState;
        }
        findAnswer = String.format("%,d", Integer.parseInt(findAnswer));
        return findAnswer + "개";
    }
}
