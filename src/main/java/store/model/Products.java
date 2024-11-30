package store.model;

import java.util.List;

public class Products {
    public static final int HAVE_PROMOTION_BENEFIT_SIZE = 2;
    public static final int HAVE_NO_PROMOTION_BENEFIT_SIZE = 1;
    List<Product> products;

    public Products(List<Product> products) {
        this.products = products;
    }

    public boolean isGetOneFree(Order order) {
        Product promotion = getPromotionProduct();
        return isPromotionPeriod() &&
                isPromotionMoreThanOrder(order, promotion) &&
                isPromotionBenefitPossibleLeft(order, promotion) &&
                isPromotionStockEnough(order, promotion);
    }

    public boolean isBuyOriginalPrice(Order order) {
        return countBuyOriginalPrice(order) > 0;
    }

    public boolean isPromotionPeriod() {
        Product promotion = getPromotionProduct();
        return promotion.isPromotionPeriod();
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

    public int countBuyOriginalPrice(Order order) {
        Product promotion = getPromotionProduct();
        if (promotion.isPromotionPeriod() && !isPromotionMoreThanOrder(order, promotion)) {
            return promotion.getNoPromotionBenefit(promotion.getQuantity()) + order.getQuantity() - promotion.getQuantity();
        }
        return 0;
    }

    public int countReducePromotionWhen(Order order) {
        return order.getQuantity() - countBuyOriginalPrice(order);
    }

    public void doNotOrderOriginalPrice(Order order) {
        order.decreaseQuantity(countBuyOriginalPrice(order));
    }

    public boolean isOrderQuantityBuyOnlyPromotionStock(Order order) {
        return countBuyOriginalPrice(order) == order.getQuantity();
    }

    public Product getPromotionProduct() {
        if (products.size() == HAVE_PROMOTION_BENEFIT_SIZE) {
            return products.get(0);
        }
        if (products.size() == HAVE_NO_PROMOTION_BENEFIT_SIZE) {
            return new Product(getNormalProduct().getName() + "," + getNormalProduct().getPrice() + ",0,null");
        }
        throw new IllegalArgumentException();
    }

    public Product getNormalProduct() {
        if (products.size() == HAVE_PROMOTION_BENEFIT_SIZE) {
            return products.get(1);
        }
        if (products.size() == HAVE_NO_PROMOTION_BENEFIT_SIZE) {
            return products.get(0);
        }
        throw new IllegalArgumentException();
    }

    public List<Product> getProducts() {
        return products;
    }
}
