package store.validator;

import store.exception.InvalidStockCount;
import store.model.Product;
import store.model.Store;

import java.util.List;

public class OrderStockValidator {
    public void validate(String name, int orderCount, Store store) {
        if (isStockLack(name, orderCount, store)) {
            throw new InvalidStockCount();
        }
    }

    private boolean isStockLack(String name, int orderCount, Store store) {
        List<Product> productList = store.getParticularStoreProducts(name);
        int count = 0;

        for (Product product : productList) {
            count += product.getQuantity();
        }

        return orderCount > count;
    }
}
