package store.model;

import java.util.ArrayList;
import java.util.List;

public class Orders {
    private List<Order> orders;

    public Orders() {
        orders = new ArrayList<>();
    }

    public void addProduct(String[] inputProducts, Store store) {
        for (String product : inputProducts) {
            orders.add(new Order(product, store));
        }
    }

    public List<Order> getOrders() {
        return orders;
    }
}
