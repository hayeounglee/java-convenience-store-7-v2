package store.strategy;

import store.model.Order;
import store.model.Products;

public class NothingToAsk implements StockManager {
    private boolean canGetDiscount;

    public NothingToAsk() {
        canGetDiscount = false;
    }

    @Override
    public int calculateNormalReduction(Order order, Products products, boolean fake) {
        return 0;
    }

    @Override
    public int calculatePromotionReduction(Order order, Products products, boolean fake) {
        return order.getQuantity();
    }

    public boolean getCanGetDiscount() {
        return canGetDiscount;
    }
}
