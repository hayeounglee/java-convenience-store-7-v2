package store.view;

import store.constant.StockState;
import store.model.*;

import java.util.Map;

public class OutputView {
    public void printStoreMenu(Store store) {
        System.out.println("""
                안녕하세요. W편의점입니다.
                현재 보유하고 있는 상품입니다.
                """);

        for (Map.Entry<String, Products> mapElement : store.getStoreProducts().entrySet()) {
            Products productList = mapElement.getValue();
            for (Product product : productList.getProducts()) {
                System.out.printf("- %s %s원 %s %s", product.getName(), String.format("%,d", product.getPrice()), changeWhenNoStock(product.getQuantity()), changeWhenNoPromotion(product.getPromotion()));
                System.out.println();
            }
        }
    }

    public void printReceipt(Orders orders, Store store, Receipt receipt) {
        printPurchaseProduct(orders, store);
        printGiftProducts(receipt);
        printAmountInfo(receipt);
    }

    private void printPurchaseProduct(Orders orders, Store store) {
        System.out.println("==============W 편의점================");
        System.out.println("상품명\t\t\t\t수량\t\t금액");
        for (Order order : orders.getOrders()) {
            int price = store.getProducts(order.getName()).getNormalProduct().getPrice();
            System.out.printf("%-4s\t\t\t\t%s\t\t%s\n", order.getName(), order.getQuantity(), String.format("%,d", order.getQuantity() * price));
        }
    }

    private void printGiftProducts(Receipt receipt) {
        System.out.println("=============증\t\t정===============");
        for (GiftProduct giftProduct : receipt.getGiftProducts()) {
            System.out.printf("%-4s\t\t\t\t%d", giftProduct.getName(), giftProduct.getQuantity());
            System.out.println();
        }
    }

    private void printAmountInfo(Receipt receipt) {
        System.out.println("====================================");
        System.out.printf("총구매액\t\t\t\t%s\t\t%s", receipt.getTotalPurchaseCount(), String.format("%,d", receipt.getTotalPurchasePrice()));
        System.out.println();
        System.out.printf("행사할인\t\t\t\t\t\t%s", String.format("-%,d", receipt.getPromotionDiscount()));
        System.out.println();
        System.out.printf("멤버십할인\t\t\t\t\t\t%s", String.format("-%,d", receipt.getMembershipDiscount()));
        System.out.println();
        System.out.printf("내실돈\t\t\t\t\t\t%s", String.format("%,d", receipt.getPayment()));
        System.out.println();
    }

    private String changeWhenNoPromotion(String input) {
        if (input.equals("null")) {
            return "";
        }
        return input;
    }

    private String changeWhenNoStock(int input) {
        return StockState.getMatchingState(Integer.toString(input));
    }
}
