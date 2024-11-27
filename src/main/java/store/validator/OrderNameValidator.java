package store.validator;

import store.exception.InvalidDuplicateOrder;
import store.exception.InvalidNonExistOrder;
import store.model.Order;
import store.model.Store;

import java.util.HashSet;
import java.util.List;
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
            if (!store.isOrderProductExist(order)) {
                throw new InvalidNonExistOrder();
            }
        }
    }
}
