package store.strategy;

import store.model.Order;
import store.model.Product;
import store.model.Products;

public class NotPromotionPeriod implements StockManager {
    private boolean canGetDiscount;

    public NotPromotionPeriod() {
        canGetDiscount = true;
    }

    @Override
    public int calculateNormalReduction(Order order, Products products, boolean fake) {
        Product normal = products.getNormalProduct();
        int reduceNormal = order.getQuantity();
        if (normal.getQuantity() < order.getQuantity()) {
            reduceNormal = normal.getQuantity();
        }
        return reduceNormal;
    }

    @Override
    public int calculatePromotionReduction(Order order, Products products, boolean fake) {
        Product normal = products.getNormalProduct();
        int reducePromotion = 0;
        if (normal.getQuantity() < order.getQuantity()) {
            reducePromotion = order.getQuantity() - normal.getQuantity();
        }
        return reducePromotion;
    }

    public boolean getCanGetDiscount() {
        return canGetDiscount;
    }
}
