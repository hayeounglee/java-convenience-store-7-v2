package store.controller;

import store.constant.ProductState;
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


    public Screen(InputView inputView, OutputView outputView) {
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void run() {
        do {
            Store store = bringProducts();
            outputView.printStoreMenu(store);

            Orders orders = askProductAndPrice(store);
            checkProducts(orders, store);

            Receipt receipt = store.getReceipt(askGetMembership());
            outputView.printReceipt(orders, store, receipt);
            updateStockResult(store);
        } while (askAdditionalPurchase());
    }

    public void checkProducts(Orders orders, Store store) {
        for (Order order : orders.getOrders()) {
            ProductState productState = store.getProductState(order);
            if (productState == ProductState.NO_PROMOTION_PERIOD) {
                continue;
            }
            if (productState == ProductState.GET_ONE_FREE) {
                store.calculateWhenGetOneFreeCase(order, askGetOneFree(order));
                continue;
            }
            if (productState == ProductState.BUY_ORIGINAL_PRICE) {
                store.calculateWhenBuyOriginalPrice(order, askBuyOriginalPrice(order, store.countBuyOriginalPrice(order)));
                continue;
            }
            if (productState == ProductState.NOTHING_TO_ASK) {
                store.calculateWhenNothingToAsk(order);
            }
        }
    }

    private Orders askProductAndPrice(Store store) {
        return Task.repeatUntilValid(() -> {
            String input = inputView.getProductAndPrice();
            return makeValidateOrder(input, store);
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

    private Orders makeValidateOrder(String input, Store store) {
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
            return makeValidStore(promotionProducts, normalProducts);
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage()); //이게 뭐지?
        }
    }

    private Store makeValidStore(List<Product> promotionProducts, List<Product> normalProducts) {
        Store store = new Store();
        List<Product> products = new ArrayList<>();
        for (Product promotion : promotionProducts) {
            Product normal = getNormalProduct(normalProducts, promotion);
            normalProducts.remove(normal);

            products.add(promotion);
            products.add(normal);
            store.addProduct(promotion.getName(), new Products(products));
            products = new ArrayList<>();
        }
        if (normalProducts.size() != 0) {
            for (Product normalProduct : normalProducts) {
                store.addProduct(normalProduct.getName(), new Products(List.of(normalProduct)));
            }
        }
        return store;
    }

    private void updateStockResult(Store store) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/products.md", false));
            writer.write("name,price,quantity,promotion\n");
            for (Map.Entry<String, Products> mapElement : store.getStoreProducts().entrySet()) {
                Products productList = mapElement.getValue();
                for (Product product : productList.getProducts()) {
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
                promotionProduct.getName() + "," + promotionProduct.getPrice() + ",0,null"
        );
    }
}

