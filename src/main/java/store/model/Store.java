package store.model;

import store.constant.PromotionPeriodState;

import java.util.LinkedHashMap;
import java.util.List;

public class Store {
    private final LinkedHashMap<String, List<Product>> store;
    private final Receipt receipt;

    public Store() {
        store = new LinkedHashMap<>();
        receipt = new Receipt();
    }

    public boolean executeWhenNotPromotionPeriod(Order order) {
        List<Product> products = getProducts(order.getName());
        int reducePromotion = 0;
        int reduceNormal = 0;

        Product normal = products.get(0);
        Product promotion = new Product(new String[]{
                normal.getName(), Integer.toString(normal.getPrice()), "0", "null"
        });
        if ((isPromotionButNotApply(products))) {
            promotion = products.get(0);
            normal = products.get(1);
        }

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
        if (checkBuyOriginalPrice(order) > 0) {
            return PromotionPeriodState.BUY_ORIGINAL_PRICE;
        }
        return PromotionPeriodState.NOTHING;
    }

    public boolean checkGetOneFree(Order order) {
        List<Product> products = getProducts(order.getName());
        Product promotion = products.get(0);

        if (promotion.isPromotionPeriod() & isPromotionMoreThanOrder(order, promotion)) {
            if (isPromotionLeft(order, promotion) & isPromotionStockEnough(order, promotion)) {
                return true;
            }
        }
        return false;
    }

    public void calculateWhenGetOneFreeCase(Order order, boolean isGetFree) {
        List<Product> products = getProducts(order.getName());
        Product promotion = products.get(0);
        Product normal = products.get(1);

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

    public int checkBuyOriginalPrice(Order order) {
        List<Product> products = getProducts(order.getName());
        Product promotion = products.get(0);

        if (promotion.isPromotionPeriod() & !isPromotionMoreThanOrder(order, promotion)) {
            return countItemsAtOriginalPrice(order, promotion);
        }
        return 0;
    }

    public void calculateWhenBuyOriginalPrice(Order order, boolean getOriginalPrice) {
        List<Product> products = getProducts(order.getName());
        Product promotion = products.get(0);
        Product normal = products.get(1);

        int reducePromotion = 0;
        int reduceNormal = 0;

        reducePromotion = promotion.getQuantity();
        if (promotion.getQuantity() < order.getQuantity()) {
            reduceNormal = order.getQuantity() - reducePromotion;
        }

        if (!getOriginalPrice) {
            reduceNormal = 0;
            reducePromotion = order.getQuantity() - countItemsAtOriginalPrice(order, promotion);
            order.decreaseQuantity(countItemsAtOriginalPrice(order, promotion));
        }

        reduceStock(normal, reduceNormal);
        reduceStock(promotion, reducePromotion);
        receipt.updateTotalAndDiscount(order, normal, false);
        receipt.updateGiftProducts(promotion, reducePromotion);
    }

    public void calculateWhenNothing(Order order) {
        List<Product> products = getProducts(order.getName());
        Product promotion = products.get(0);
        Product normal = products.get(1);

        int reducePromotion = order.getQuantity();
        reduceStock(promotion, reducePromotion);
        receipt.updateTotalAndDiscount(order, normal, false);
        receipt.updateGiftProducts(promotion, reducePromotion);
    }

    private int countItemsAtOriginalPrice(Order order, Product promotion) {
        return getRemainingPromotion(promotion.getQuantity(), promotion) + order.getQuantity() - promotion.getQuantity();
    }

    private boolean isPromotionMoreThanOrder(Order order, Product promotion) {
        return promotion.getQuantity() > order.getQuantity();
    }

    private boolean isPromotionLeft(Order order, Product promotion) {
        int promotionRemainingCount = getRemainingPromotion(order.getQuantity(), promotion);
        return promotionRemainingCount == promotion.getPromotionBuyCount();
    }

    private int getRemainingPromotion(int num, Product promotion) {
        return num % promotion.getPromotionCount();
    }

    private boolean isPromotionStockEnough(Order order, Product promotion) {
        return promotion.getQuantity() >= order.getQuantity() + 1;
    }

    private void reduceStock(Product product, int countReduce) {
        product.reduceStock(countReduce);
    }

    private boolean isPromotionButNotApply(List<Product> products) {
        return products.size() == 2;
    }

    public void addProduct(String name, List<Product> products) {
        store.put(name, products);
    }

    public LinkedHashMap<String, List<Product>> getStore() {
        return store;
    }

    public List<Product> getProducts(String name) {
        return store.get(name);
    }

}
