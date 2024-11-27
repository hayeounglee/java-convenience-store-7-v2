package store.model;

import store.constant.MembershipDiscount;

import java.util.ArrayList;
import java.util.List;

public class Receipt {
    private int totalPurchasePrice = 0;
    private int totalPurchaseCount = 0;
    private int membershipDiscount = 0;
    private int promotionDiscount = 0;

    private final List<GiftProduct> giftProducts;

    public Receipt() {
        giftProducts = new ArrayList<>();
    }

    public void updateTotalAndDiscount(Order order, Product product, boolean canGetDiscount) {
        int price = order.getQuantity() * product.getPrice();
        totalPurchasePrice += price;
        totalPurchaseCount += order.getQuantity();

        if (canGetDiscount) {
            membershipDiscount += price * MembershipDiscount.DISCOUNT_RATE.getValue();
        }
    }

    public void updateGiftProducts(Product promotion, int possibleGiftProducts) {
        giftProducts.add(new GiftProduct(promotion.getName(), promotion.getGiftCount(possibleGiftProducts)));
        promotionDiscount += promotion.getGiftCount(possibleGiftProducts) * promotion.getPrice();
    }

    public int getPayment() {
        return getTotalPurchasePrice() - getMembershipDiscount() - getPromotionDiscount();
    }

    public int getTotalPurchasePrice() {
        return totalPurchasePrice;
    }

    public void getNoMembershipDiscount() {
        membershipDiscount = 0;
    }

    public int getMembershipDiscount() {
        return membershipDiscount;
    }

    public List<GiftProduct> getGiftProducts() {
        return giftProducts;
    }

    public int getTotalPurchaseCount() {
        return totalPurchaseCount;
    }

    public int getPromotionDiscount() {
        return promotionDiscount;
    }
}
