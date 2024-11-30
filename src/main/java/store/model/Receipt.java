package store.model;

import store.constant.MembershipDiscount;

import java.util.List;

public class Receipt {
    private final GiftProducts giftProducts;

    private int totalPurchasePrice;
    private int totalPurchaseCount;
    private int membershipDiscount;
    private int promotionDiscount;

    public Receipt() {
        giftProducts = new GiftProducts();
        totalPurchasePrice = 0;
        totalPurchaseCount = 0;
        membershipDiscount = 0;
        promotionDiscount = 0;
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
        if (possibleGiftProducts > 0 & promotion.isPromotionPeriod()) {
            GiftProduct giftProduct = giftProducts.update(promotion, possibleGiftProducts);
            promotionDiscount += giftProduct.getQuantity() * promotion.getPrice();
        }
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
        return giftProducts.getGiftProducts();
    }

    public int getTotalPurchaseCount() {
        return totalPurchaseCount;
    }

    public int getPromotionDiscount() {
        return promotionDiscount;
    }
}
