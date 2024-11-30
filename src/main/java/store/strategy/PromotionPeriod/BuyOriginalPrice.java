package store.strategy.PromotionPeriod;

import store.model.Order;
import store.model.Product;
import store.model.Products;
import store.strategy.StockManager;

public class BuyOriginalPrice implements StockManager {
    private final boolean canGetDiscount;

    public BuyOriginalPrice(Order order, Products products) {
        this.canGetDiscount = products.isOrderQuantityBuyOnlyPromotionStock(order);
    }

    @Override
    public int calculateNormalReduction(Order order, Products products, boolean getOriginalPrice) {
        Product promotion = products.getPromotionProduct();

        int reduceNormal = 0;
        if (promotion.getQuantity() < order.getQuantity()) {
            reduceNormal = order.getQuantity() - promotion.getQuantity();
        }
        if (!getOriginalPrice) {
            reduceNormal = 0;
        }
        return reduceNormal;
    }

    @Override
    public int calculatePromotionReduction(Order order, Products products, boolean getOriginalPrice) {
        Product promotion = products.getPromotionProduct();

        int reducePromotion = promotion.getQuantity();
        if (!getOriginalPrice) {
            reducePromotion = products.countReducePromotionWhen(order);
            products.doNotOrderOriginalPrice(order);
        }
        return reducePromotion;
    }

    @Override
    public boolean getCanGetDiscount() {
        return canGetDiscount;
    }
}
