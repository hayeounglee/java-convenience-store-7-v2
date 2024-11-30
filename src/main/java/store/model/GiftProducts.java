package store.model;

import java.util.ArrayList;
import java.util.List;

public class GiftProducts {
    private final List<GiftProduct> giftProducts;

    public GiftProducts() {
        giftProducts = new ArrayList<>();
    }

    public GiftProduct update(Product promotion, int possibleGiftProducts) {
        GiftProduct giftProduct = new GiftProduct(promotion, possibleGiftProducts);
        giftProducts.add(giftProduct);
        return giftProduct;
    }

    public List<GiftProduct> getGiftProducts() {
        return giftProducts;
    }
}
