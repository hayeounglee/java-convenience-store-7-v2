package store.validator;

import store.exception.InvalidDuplicateOrder;
import store.exception.InvalidNonExistOrder;
import store.model.Order;
import store.model.Product;
import store.model.Products;
import store.model.Store;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OrderNameValidator {
    public void validate(List<Order> orders, Store store) {
        checkDuplicateOrder(orders);
        checkExistOrder(orders, store);
    }

    private void checkDuplicateOrder(List<Order> orders) {
        Set<String> uniqueOrders = new HashSet<>();
        for (Order order : orders) {
            if (!uniqueOrders.add(order.getName())) {
                throw new InvalidDuplicateOrder();
            }
        }
    }

    private void checkExistOrder(List<Order> orders, Store store) {
        for (Order order : orders) {
            if (!isOrderProductExist(order, store)) {
                throw new InvalidNonExistOrder();
            }
        }
    }

    private boolean isOrderProductExist(Order order, Store store) {
        for (Map.Entry<String, Products> mapElement : store.getStoreProducts().entrySet()) {
            Products productList = mapElement.getValue();
            for (Product product : productList.getProducts()) {
                if (product.getName().equals(order.getName())) {
                    return true;
                }
            }
        }
        return false;
    }
}
