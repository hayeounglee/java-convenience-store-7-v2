package store.model;

import java.util.ArrayList;
import java.util.List;

public class Products {
    public static final int HAVE_PROMOTION_BENEFIT_SIZE = 2;
    public static final int HAVE_NO_PROMOTION_BENEFIT_SIZE = 1;
    List<Product> products;

    public Products(List<Product> products) {
        this.products = products;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public boolean isPromotionButNotApply(List<Product> products) {
        Product promotion = getPromotionProduct();
        if (promotion.isPromotion() & !promotion.isPromotionPeriod()) {
            return true;
        }
        if (promotion.isPromotion() & promotion.getQuantity() == 0) {
            return true;
        }
        return false;
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
