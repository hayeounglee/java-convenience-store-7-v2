package store.model;

import store.constant.PromotionPeriodState;
import store.exception.InvalidNonExistOrder;

import java.util.LinkedHashMap;
import java.util.Map;

public class Store {
    private final LinkedHashMap<String, Products> storeProducts;
    private final Receipt receipt;

    public Store() {
        storeProducts = new LinkedHashMap<>();
        receipt = new Receipt();
    }

    public boolean executeWhenNotPromotionPeriod(Order order) {
        Products products = getProducts(order.getName());
        int reducePromotion = 0;
        int reduceNormal = 0;

        Product normal = products.getNormalProduct();
        Product promotion = products.getPromotionProduct();

        if (!promotion.isPromotionPeriod()) {
            reduceNormal = order.getQuantity();
            if (normal.getQuantity() < order.getQuantity()) {
                reducePromotion = normal.getQuantity();
                reduceNormal = order.getQuantity() - reducePromotion;
            }
            reduceStock(normal, reduceNormal);
            reduceStock(promotion, reducePromotion);
            receipt.updateTotalAndDiscount(order, normal, true);
            return true;
        }
        return false;
    }

    public PromotionPeriodState executeWhenPromotionPeriod(Order order) {
        if (checkGetOneFree(order)) {
            return PromotionPeriodState.GET_ONE_FREE;
        }
        if (checkBuyOriginalPrice(order)) {
            return PromotionPeriodState.BUY_ORIGINAL_PRICE;
        }
        return PromotionPeriodState.NOTHING;
    }

    public boolean checkGetOneFree(Order order) {
        Products products = getProducts(order.getName());
        return products.isGetOneFree(order);
    }

    public void calculateWhenGetOneFreeCase(Order order, boolean isGetFree) {
        Products products = getProducts(order.getName());
        Product normal = products.getNormalProduct();
        Product promotion = products.getPromotionProduct();

        int reducePromotion = 0;
        int reduceNormal = 0;

        reducePromotion = order.getQuantity();
        if (isGetFree) {
            reducePromotion += 1;
            order.increaseQuantity();
        }
        reduceStock(normal, reduceNormal);
        reduceStock(promotion, reducePromotion);
        receipt.updateTotalAndDiscount(order, normal, false);
        receipt.updateGiftProducts(promotion, reducePromotion);
    }

    public boolean checkBuyOriginalPrice(Order order) {
        Products products = getProducts(order.getName());
        return products.isBuyOriginalPrice(order);
    }

    public void calculateWhenBuyOriginalPrice(Order order, boolean getOriginalPrice) {
        Products products = getProducts(order.getName());
        Product normal = products.getNormalProduct();
        Product promotion = products.getPromotionProduct();

        int reducePromotion = 0;
        int reduceNormal = 0;

        reducePromotion = promotion.getQuantity();
        if (promotion.getQuantity() < order.getQuantity()) {
            reduceNormal = order.getQuantity() - reducePromotion;
        }

        if (!getOriginalPrice) {
            reduceNormal = 0;
            reducePromotion = products.countReducePromotionWhen(order);
            products.notOrderOriginalPrice(order);
        }

        reduceStock(normal, reduceNormal);
        reduceStock(promotion, reducePromotion);

        boolean canGetDiscount = products.isOrderQuantityBuyOnlyPromotionStock(order);

        receipt.updateTotalAndDiscount(order, normal, canGetDiscount);
        receipt.updateGiftProducts(promotion, reducePromotion);
    }

    public void calculateWhenNothingToAsk(Order order) {
        Products products = getProducts(order.getName());
        Product normal = products.getNormalProduct();
        Product promotion = products.getPromotionProduct();

        int reducePromotion = order.getQuantity();
        reduceStock(promotion, reducePromotion);
        receipt.updateTotalAndDiscount(order, normal, false);
        receipt.updateGiftProducts(promotion, reducePromotion);
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
