package store.controller;

import store.constant.PromotionPeriodState;
import store.model.*;
import store.util.Task;
import store.view.InputView;
import store.view.OutputView;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Screen {
    private final InputView inputView;
    private final OutputView outputView;

    private Orders orders;
    private Store store;

    public Screen(InputView inputView, OutputView outputView) {
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void run() {
        do {
            store = bringProducts();
            outputView.printStoreMenu(store);

            orders = askProductAndPrice();
            checkProducts();

            Receipt receipt = store.getReceipt(askGetMembership());
            outputView.printReceipt(orders, store, receipt);
            updateStockResult(store);
        } while (askAdditionalPurchase());

    }

    public void checkProducts() {
        for (Order order : orders.getOrders()) {
            if (store.executeWhenNotPromotionPeriod(order)) {
                continue;
            }

            PromotionPeriodState promotionPeriodState = store.executeWhenPromotionPeriod(order);
            if (promotionPeriodState == PromotionPeriodState.GET_ONE_FREE) {
                store.calculateWhenGetOneFreeCase(order, askGetOneFree(order));
                continue;
            }
            if (promotionPeriodState == PromotionPeriodState.BUY_ORIGINAL_PRICE) {
                store.calculateWhenBuyOriginalPrice(order, askBuyOriginalPrice(order, store.checkBuyOriginalPrice(order)));
                continue;
            }
            if (promotionPeriodState == PromotionPeriodState.NOTHING) {
                store.calculateWhenNothing(order);
            }
        }
    }

    private Orders askProductAndPrice() {
        return Task.repeatUntilValid(() -> {
            String input = inputView.getProductAndPrice();
            return makeValidateOrder(input);
        });
    }

    private boolean askGetOneFree(Order order) {
        return Task.repeatUntilValid(() -> inputView.getOneMoreFree(order));
    }

    private boolean askBuyOriginalPrice(Order order, int itemsAtOriginalPriceCount) {
        return Task.repeatUntilValid(() -> inputView.getPurchaseOrNot(order.getName(), itemsAtOriginalPriceCount));
    }

    private boolean askGetMembership() {
        return Task.repeatUntilValid(inputView::getMembershipDiscountOrNot);
    }

    private boolean askAdditionalPurchase() {
        return Task.repeatUntilValid(inputView::getAdditionalPurchase);
    }

    private Orders makeValidateOrder(String input) {
        String[] inputProducts = input.split(",", -1);
        Orders orders = new Orders();
        orders.addProduct(inputProducts, store);
        return orders;
    }

    private Store bringProducts() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/products.md"));
            String line;
            List<Product> promotionProducts = new ArrayList<>();
            List<Product> normalProducts = new ArrayList<>();

            reader.readLine();
            while ((line = reader.readLine()) != null) {
                Product product = new Product(line);
                if (product.isPromotion()) {
                    promotionProducts.add(product);
                    continue;
                }
                normalProducts.add(product);
            }
            reader.close();

            Store store = new Store();
            List<Product> products = new ArrayList<>();
            for (Product promotion : promotionProducts) {
                Product normal = getNormalProduct(normalProducts, promotion);

                normalProducts.remove(normal);
                products.add(normal);

                products.add(0, promotion);
                store.addProduct(promotion.getName(), products);
                products = new ArrayList<>();
            }
            if (normalProducts.size() != 0) {
                for (Product normalProduct : normalProducts) {
                    store.addProduct(normalProduct.getName(), new ArrayList<>(List.of(normalProduct)));
                }
            }
            return store;
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage()); //이게 뭐지?
        }
    }

    private void updateStockResult(Store store) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/products.md", false));
            writer.write("name,price,quantity,promotion\n");
            for (Map.Entry<String, List<Product>> mapElement : store.getStoreProducts().entrySet()) {
                List<Product> productList = mapElement.getValue();
                for (Product product : productList) {
                    writer.write(product.getName() + "," + product.getPrice() + "," + product.getQuantity() + "," + product.getPromotion() + "\n");
                }
            }
            writer.close();
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage()); //이게 뭐지?
        }
    }

    private Product getNormalProduct(List<Product> normalProducts, Product promotionProduct) {
        for (Product normal : normalProducts) {
            if (normal.getName().equals(promotionProduct.getName())) {
                return normal;
            }
        }
        return new Product(
                promotionProduct.getName() +","+ promotionProduct.getPrice() + ",0,null"
        );
    }
}

