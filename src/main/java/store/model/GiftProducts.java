package store.model;

import java.util.ArrayList;
import java.util.List;

public class GiftProducts {
    private final List<GiftProduct> giftProducts;

    public GiftProducts() {
        giftProducts = new ArrayList<>();
    }

    public void update(Product promotion, int possibleGiftProducts) {
        giftProducts.add(new GiftProduct(promotion.getName(), promotion.getGiftCount(possibleGiftProducts)));
    }

    public List<GiftProduct> getGiftProducts() {
        return giftProducts;
    }
}
