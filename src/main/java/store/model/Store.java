package store.model;

import store.constant.ProductState;
import store.exception.InvalidNonExistOrder;
import store.strategy.*;

import java.util.LinkedHashMap;

public class Store {
    private final LinkedHashMap<String, Products> storeProducts;
    private final Receipt receipt;

    public Store() {
        storeProducts = new LinkedHashMap<>();
        receipt = new Receipt();
    }

    public void calculateOrder(Order order, StockManager stockManager, boolean answer) {
        Products products = getProducts(order.getName());
        Product normal = products.getNormalProduct();
        Product promotion = products.getPromotionProduct();

        int reduceNormal = stockManager.calculateNormalReduction(order, products, answer);
        int reducePromotion = stockManager.calculatePromotionReduction(order, products, answer);

        reduceStock(normal, reduceNormal);
        reduceStock(promotion, reducePromotion);
        receipt.updateTotalAndDiscount(order, normal, stockManager.getCanGetDiscount());
        receipt.updateGiftProducts(promotion, reducePromotion);
    }

    public boolean executeWhenNotPromotionPeriod(Order order) {
        Products products = getProducts(order.getName());
        Product promotion = products.getPromotionProduct();

        if (promotion.isPromotionPeriod()) {
            return false;
        }
        calculateOrder(order, new NotPromotionPeriod(), true);
        return true;
    }

    public ProductState getProductState(Order order) {
        if (executeWhenNotPromotionPeriod(order)) {
            return ProductState.NO_PROMOTION_PERIOD;
        }
        if (checkGetOneFree(order)) {
            return ProductState.GET_ONE_FREE;
        }
        if (checkBuyOriginalPrice(order)) {
            return ProductState.BUY_ORIGINAL_PRICE;
        }
        return ProductState.NOTHING_TO_ASK;
    }

    public boolean checkGetOneFree(Order order) {
        Products products = getProducts(order.getName());
        return products.isGetOneFree(order);
    }

    public void calculateWhenGetOneFreeCase(Order order, boolean isGetFree) {
        calculateOrder(order, new GetOneFree(), isGetFree);
    }

    public boolean checkBuyOriginalPrice(Order order) {
        Products products = getProducts(order.getName());
        return products.isBuyOriginalPrice(order);
    }

    public void calculateWhenBuyOriginalPrice(Order order, boolean getOriginalPrice) {
        Products products = getProducts(order.getName());
        calculateOrder(order, new BuyOriginalPrice(order, products), getOriginalPrice);
    }

    public void calculateWhenNothingToAsk(Order order) {
        calculateOrder(order, new NothingToAsk(), true);
    }

    public Receipt getReceipt(boolean isGetDiscount) {
        if (!isGetDiscount) {
            receipt.getNoMembershipDiscount();
        }
        return receipt;
    }

    public int countBuyOriginalPrice(Order order) {
        Products products = getProducts(order.getName());
        return products.countBuyOriginalPrice(order);
    }

    private void reduceStock(Product product, int countReduce) {
        product.reduceStock(countReduce);
    }

    public void addProduct(String name, Products products) {
        storeProducts.put(name, products);
    }

    public LinkedHashMap<String, Products> getStoreProducts() {
        return storeProducts;
    }

    public Products getParticularStoreProducts(String name) {
        if (storeProducts.get(name) == null) {
            throw new InvalidNonExistOrder();
        }
        return storeProducts.get(name);
    }

    public Products getProducts(String name) {
        return storeProducts.get(name);
    }
}
