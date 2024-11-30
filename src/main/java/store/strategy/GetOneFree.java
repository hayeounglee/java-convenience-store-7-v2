package store.strategy;

import store.model.Order;
import store.model.Products;

public class GetOneFree implements StockManager {
    private boolean canGetDiscount;

    public GetOneFree() {
        canGetDiscount = false;
    }

    @Override
    public int calculateNormalReduction(Order order, Products products, boolean isGetFree) {
        return 0;
    }

    @Override
    public int calculatePromotionReduction(Order order, Products products, boolean isGetFree) {
        int reducePromotion = order.getQuantity();
        if (isGetFree) {
            reducePromotion += 1;
            order.increaseQuantity();
        }
        return reducePromotion;
    }

    public boolean getCanGetDiscount() {
        return canGetDiscount;
    }
}
