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
