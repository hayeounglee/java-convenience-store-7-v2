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
        if (checkBuyOriginalPrice(order) > 0) {
            return PromotionPeriodState.BUY_ORIGINAL_PRICE;
        }
        return PromotionPeriodState.NOTHING;
    }

    public boolean checkGetOneFree(Order order) {
        Products products = getProducts(order.getName());
        Product promotion = products.getPromotionProduct();

        if (promotion.isPromotionPeriod() & isPromotionMoreThanOrder(order, promotion)) {
            if (isPromotionBenefitPossibleLeft(order, promotion) & isPromotionStockEnough(order, promotion)) {
                return true;
            }
        }
        return false;
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

    public int checkBuyOriginalPrice(Order order) {
        Products products = getProducts(order.getName());
        Product promotion = products.getPromotionProduct();

        if (promotion.isPromotionPeriod() & !isPromotionMoreThanOrder(order, promotion)) {
            return countItemsAtOriginalPrice(order, promotion);
        }
        return 0;
    }

    public void calculateWhenBuyOriginalPrice(Order order, boolean getOriginalPrice) {
        Products products = getProducts(order.getName());
        Product normal = products.getNormalProduct();
        Product promotion = products.getPromotionProduct();

        int reducePromotion = 0;
        int reduceNormal = 0;
        boolean canGetDiscount = false;

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

        if (countItemsAtOriginalPrice(order, promotion) == order.getQuantity()) {
            canGetDiscount = true;
        }

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

    private int countItemsAtOriginalPrice(Order order, Product promotion) {
        return promotion.getNoPromotionBenefit(promotion.getQuantity()) + order.getQuantity() - promotion.getQuantity();
    }

    private boolean isPromotionMoreThanOrder(Order order, Product promotion) {
        return promotion.getQuantity() > order.getQuantity();
    }

    private boolean isPromotionBenefitPossibleLeft(Order order, Product promotion) {
        return promotion.isPromotionBenefitPossibleLeft(order);
    }

    private boolean isPromotionStockEnough(Order order, Product promotion) {
        return promotion.getQuantity() >= order.getQuantity() + 1;
    }

    public boolean isOrderProductExist(Order order) {
        for (Map.Entry<String, Products> mapElement : storeProducts.entrySet()) {
            Products productList = mapElement.getValue();
            for (Product product : productList.getProducts()) {
                if (product.getName().equals(order.getName())) {
                    return true;
                }
            }
        }
        return false;
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
