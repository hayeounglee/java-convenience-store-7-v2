package store.model;

import store.util.Parser;

public class Product {
    private final String name;
    private final int price;
    private int quantity;
    private final String promotion;
    private final PromotionPolicy promotionPolicy;

    public Product(String line) {
        String[] product = line.split(",");
        this.name = product[0];
        this.price = Parser.parseToInt(product[1]);
        this.quantity = Parser.parseToInt(product[2]);
        this.promotion = product[3];
        promotionPolicy = new PromotionPolicy(promotion);
    }

    public boolean isPromotionBenefitPossibleLeft(Order order) {
        int promotionRemainingCount = getNoPromotionBenefit(order.getQuantity());
        return promotionRemainingCount == getPromotionBuyCount();
    }

    public void reduceStock(int num) {
        quantity -= num;
    }

    public boolean isPromotion() {
        return !promotion.equals("null");
    }

    public boolean isPromotionPeriod() {
        return promotionPolicy.isValidPeriod();
    }

    public int getNoPromotionBenefit(int num) {
        return num % getPromotionCount();
    }

    public int getPromotionCount() {
        return promotionPolicy.getPromotionCount();
    }

    public int getPromotionBuyCount() {
        return promotionPolicy.getPromotionBuyCount();
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getPromotion() {
        return promotion;
    }
}
