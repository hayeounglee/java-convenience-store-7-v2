package store.model;

import store.service.PromotionPolicy;
import store.util.Parser;

public class Product {
    private final String name;
    private final int price;
    private int quantity;
    private final String promotion;
    private final PromotionPolicy promotionPolicy;

    public Product(String[] product) {
        this.name = product[0];
        this.price = Parser.parseToInt(product[1]);
        this.quantity = Parser.parseToInt(product[2]);
        this.promotion = product[3];
        promotionPolicy = new PromotionPolicy(promotion);
    }

    public boolean isPromotion() {
        return !promotion.equals("null");
    }

    public boolean isPromotionPeriod() {
        return promotionPolicy.isValidPeriod();
    }

    public void reduceStock(int num) {
        quantity -= num;
    }

    public int getPromotionCount() {
        return promotionPolicy.getPromotionCount();
    }

    public int getPromotionBuyCount() {
        return promotionPolicy.getPromotionBuyCount();
    }

    public int getGiftCount(int possibleGiftProducts) {
        return promotionPolicy.getGiftCount(possibleGiftProducts);
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
