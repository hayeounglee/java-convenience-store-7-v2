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
        } while ();

    }

    private Store bringProducts() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/products.md"));
            String line;
            List<Product> promotionProducts = new ArrayList<>();
            List<Product> normalProducts = new ArrayList<>();
            String[] oneProduct;

            reader.readLine();
            while ((line = reader.readLine()) != null) {
                oneProduct = line.split(",");
                Product product = new Product(oneProduct);
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

    private Product getNormalProduct(List<Product> normalProducts, Product promotionProduct) {
        for (Product normal : normalProducts) {
            if (normal.getName().equals(promotionProduct.getName())) {
                return normal;
            }
        }
        return new Product(new String[]{
                promotionProduct.getName(), Integer.toString(promotionProduct.getPrice()), "0", "null"
        });
    }
}

