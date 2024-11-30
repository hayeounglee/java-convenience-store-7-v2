package store.strategy;

import store.model.Order;
import store.model.Products;

public interface StockManager {
    int calculateNormalReduction(Order order, Products products, boolean answer);

    int calculatePromotionReduction(Order order, Products products, boolean answer);

    boolean getCanGetDiscount();
}
