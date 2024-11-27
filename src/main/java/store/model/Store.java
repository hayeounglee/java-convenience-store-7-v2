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
