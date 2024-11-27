package store.model;

import store.util.Parser;
import store.validator.OrderFormValidator;
import store.validator.StockValidator;

public class Order {
    private String name;
    private int quantity;

    public Order(String product, Store store) {
        validateForm(product);
        putInfo(product);
        validateStock(store);
    }

    private void validateForm(String product) {
        OrderFormValidator orderFormValidator = new OrderFormValidator();
        orderFormValidator.validate(product);
    }

    private void putInfo(String product) {
        String productInfo = product.substring(1, product.length() - 1);
        this.name = productInfo.split("-")[0];
        this.quantity = Parser.parseToInt(productInfo.split("-")[1]);
    }

    private void validateStock(Store store) {
        StockValidator stockValidator = new StockValidator();
        stockValidator.checkStock(name, quantity, store);
    }

    public void increaseQuantity() {
        quantity += 1;
    }

    public void decreaseQuantity(int num) {
        quantity -= num;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }
}
