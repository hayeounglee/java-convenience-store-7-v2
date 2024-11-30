package store;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.constant.PromotionPeriodState;
import store.model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static camp.nextstep.edu.missionutils.test.Assertions.assertNowTest;
import static org.assertj.core.api.Assertions.assertThat;

public class StoreTest {
    private Store store;

    @BeforeEach
    void setUp() {
        store = new Store();
        List<Product> products = new ArrayList<>();
        products.add(new Product("오렌지주스,1800,9,MD추천상품"));
        products.add(new Product("오렌지주스,1800,2,null"));
        store.addProduct("오렌지주스", new Products(products));
        products = new ArrayList<>();
        products.add(new Product("물,500,7,null"));
        store.addProduct("물", new Products(products));
        products = new ArrayList<>();
        products.add(new Product("컵라면,1700,0,MD추천상품"));
        products.add(new Product("컵라면,1700,10,null"));
        store.addProduct("컵라면", new Products(products));
    }

    @Test
    @DisplayName("프로모션 기간 아닐 때 - 프로모션 헤택이 없는 주문인지 판별할 수 있다.")
    void shouldDetermineIfNotPromotionBenefit() {
        Order order = new Order("[물-1]", store);
        assertThat(store.executeWhenNotPromotionPeriod(order)).isEqualTo(true);
        assertThat(store.executeWhenPromotionPeriod(order)).isEqualTo(PromotionPeriodState.NOTHING);
        assertThat(store.checkGetOneFree(order)).isEqualTo(false);
        assertThat(store.checkBuyOriginalPrice(order)).isEqualTo(false);
    }

    @Test
    @DisplayName("프로모션 기간 아닐 때 - 프로모션 혜택이 가능한 상품이지만 기간이 아닌 주문인지 판별할 수 있다.")
    void shouldDetermineIfHasPromotionBenefitButNotPromotionPeriod() {
        assertNowTest(() -> {
            Order order = new Order("[오렌지주스-3]", store);
            assertThat(store.executeWhenNotPromotionPeriod(order)).isEqualTo(true);
            assertThat(store.executeWhenPromotionPeriod(order)).isEqualTo(PromotionPeriodState.NOTHING);
            assertThat(store.checkGetOneFree(order)).isEqualTo(false);
            assertThat(store.checkBuyOriginalPrice(order)).isEqualTo(false);
        }, LocalDate.of(1999, 1, 1).atStartOfDay());
    }

    @Test
    @DisplayName("프로모션 기간일 때 - 1개 수량을 추가할 수 있는 주문인지 판별할 수 있다.")
    void shouldDetermineIfProductCanBeAdded() {
        Order order = new Order("[오렌지주스-1]", store);
        assertThat(store.executeWhenNotPromotionPeriod(order)).isEqualTo(false);
        assertThat(store.executeWhenPromotionPeriod(order)).isEqualTo(PromotionPeriodState.GET_ONE_FREE);
        assertThat(store.checkGetOneFree(order)).isEqualTo(true);
        assertThat(store.checkBuyOriginalPrice(order)).isEqualTo(false);
    }

    @Test
    @DisplayName("프로모션 기간일 때 - 정가로 결제해야 하는 수량이 있는 주문인지 판별할 수 있다.")
    void shouldDetermineIfOrderHasOriginalPrice() {
        Order order = new Order("[오렌지주스-9]", store);
        assertThat(store.executeWhenNotPromotionPeriod(order)).isEqualTo(false);
        assertThat(store.executeWhenPromotionPeriod(order)).isEqualTo(PromotionPeriodState.BUY_ORIGINAL_PRICE);
        assertThat(store.checkGetOneFree(order)).isEqualTo(false);
        assertThat(store.checkBuyOriginalPrice(order)).isEqualTo(true);
    }

    @Test
    @DisplayName("프로모션 기간일 때 - 추가로 물어볼 필요가 없는 주문인지 판별할 수 있다.")
    void shouldDetermineNothingToAskProducts() {
        Order order = new Order("[오렌지주스-2]", store);
        assertThat(store.executeWhenNotPromotionPeriod(order)).isEqualTo(false);
        assertThat(store.executeWhenPromotionPeriod(order)).isEqualTo(PromotionPeriodState.NOTHING);
        assertThat(store.checkGetOneFree(order)).isEqualTo(false);
        assertThat(store.checkBuyOriginalPrice(order)).isEqualTo(false);
    }

    @Test
    @DisplayName("프로모션 기간 아닐 때 재고관리 - 일반재고밖에 없을 때 일반재고를 올바르게 감소시킬 수 있다. 또한 증정품의 개수와, 멤버십 할인을 올바르게 계산한다.")
    void shouldDecreaseNormalWhenOnlyNormal() {
        Order order = new Order("[물-1]", store);
        store.executeWhenNotPromotionPeriod(order);
        Receipt receipt = store.getReceipt(true);

        Products products = store.getProducts(order.getName());
        Product promotion = products.getPromotionProduct();
        Product normal = products.getNormalProduct();

        assertThat(receipt.getTotalPurchaseCount()).isEqualTo(1);
        assertThat(promotion.getQuantity()).isEqualTo(0);
        assertThat(normal.getQuantity()).isEqualTo(6);

        assertThat(receipt.getGiftProducts()).size().isEqualTo(0);
        assertThat(receipt.getMembershipDiscount()).isEqualTo(150);
    }

    @Test
    @DisplayName("프로모션 기간 아닐 때 재고관리 - 프로모션 혜택이 가능한 상품이지만 기간이 아닐 때 일반재고를 우선 감소시킬 수 있다. 또한 증정품의 개수와, 멤버십 할인을 올바르게 계산한다.")
    void shouldDecreaseNormalWhenNotPromotionPeriodAndHasPromotionBenefit() {
        assertNowTest(() -> {
            Order order = new Order("[오렌지주스-3]", store);
            store.executeWhenNotPromotionPeriod(order);
            Receipt receipt = store.getReceipt(true);

            Products products = store.getProducts(order.getName());
            Product promotion = products.getPromotionProduct();
            Product normal = products.getNormalProduct();

            assertThat(receipt.getTotalPurchaseCount()).isEqualTo(3);
            assertThat(promotion.getQuantity()).isEqualTo(8);
            assertThat(normal.getQuantity()).isEqualTo(0);

            assertThat(receipt.getGiftProducts()).size().isEqualTo(0);
            assertThat(receipt.getMembershipDiscount()).isEqualTo(1620);
        }, LocalDate.of(1999, 1, 1).atStartOfDay());
    }

    @Test
    @DisplayName("프로모션 기간일 때 재고관리 - 1개 수량을 추가 결제할 때 프로모션 재고를 올바르게 감소시킬 수 있다. 또한 증정품의 개수와, 멤버십 할인을 올바르게 계산한다.")
    void shouldDecreasePromotionWhenPurchaseOneProduct() {
        Order order = new Order("[오렌지주스-1]", store);
        store.calculateWhenGetOneFreeCase(order, true);
        Receipt receipt = store.getReceipt(true);

        Products products = store.getProducts(order.getName());
        Product promotion = products.getPromotionProduct();
        Product normal = products.getNormalProduct();

        assertThat(receipt.getTotalPurchaseCount()).isEqualTo(2);
        assertThat(promotion.getQuantity()).isEqualTo(7);
        assertThat(normal.getQuantity()).isEqualTo(2);

        assertThat(receipt.getGiftProducts()).size().isEqualTo(1);
        assertThat(receipt.getMembershipDiscount()).isEqualTo(0);
    }

    @DisplayName("프로모션 기간일 때 재고관리 - 정가로 결제해야 하는 수량을 결제하지 않을 때 올바르게 재고를 감소시킬 수 있다. 또한 증정품의 개수와, 멤버십 할인을 올바르게 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {"[오렌지주스-9],8,1,2", "[오렌지주스-10],8,1,2"})
    void shouldDecreaseWhenNotPurchaseOriginalPrice(String name, int expectedTotalPurchase, int expectedPromotionQuantity, int expectedNormalPurchase) {
        Order order = new Order(name, store);
        store.calculateWhenBuyOriginalPrice(order, false);
        Receipt receipt = store.getReceipt(true);

        Products products = store.getProducts(order.getName());
        Product promotion = products.getPromotionProduct();
        Product normal = products.getNormalProduct();

        assertThat(receipt.getTotalPurchaseCount()).isEqualTo(expectedTotalPurchase);
        assertThat(promotion.getQuantity()).isEqualTo(expectedPromotionQuantity);
        assertThat(normal.getQuantity()).isEqualTo(expectedNormalPurchase);

        int count = 0;
        for (GiftProduct giftProduct : receipt.getGiftProducts()) {
            count += giftProduct.getQuantity();
        }
        assertThat(count).isEqualTo(4);
        assertThat(receipt.getMembershipDiscount()).isEqualTo(0);
    }

    @DisplayName("프로모션 기간일 때 재고관리 - 정가로 결제해야 하는 수량을 결제할 때 올바르게 재고를 감소시킬 수 있다. 또한 증정품의 개수와, 멤버십 할인을 올바르게 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {"[오렌지주스-9],9,0,2,4,0", "[오렌지주스-10],10,0,1,4,0", "[컵라면-1],1,0,9,0,510"})
    void shouldDecreaseWhenPurchaseOriginalPrice(String name, int expectedTotalPurchase, int expectedPromotionQuantity, int expectedNormalPurchase, int expectedGift, int expectedDiscount) {
        Order order = new Order(name, store);
        store.calculateWhenBuyOriginalPrice(order, true);
        Receipt receipt = store.getReceipt(true);

        Products products = store.getProducts(order.getName());
        Product promotion = products.getPromotionProduct();
        Product normal = products.getNormalProduct();

        assertThat(receipt.getTotalPurchaseCount()).isEqualTo(expectedTotalPurchase);
        assertThat(promotion.getQuantity()).isEqualTo(expectedPromotionQuantity);
        assertThat(normal.getQuantity()).isEqualTo(expectedNormalPurchase);

        int count = 0;
        for (GiftProduct giftProduct : receipt.getGiftProducts()) {
            count += giftProduct.getQuantity();
        }
        assertThat(count).isEqualTo(expectedGift);
        assertThat(receipt.getMembershipDiscount()).isEqualTo(expectedDiscount);
    }

    @Test
    @DisplayName("프로모션 기간일 때 재고관리 - 추가로 물어볼 필요가 없는 주문일 때 재고를 올바르게 감소시킬 수 있다. 또한 증정품의 개수와, 멤버십 할인을 올바르게 계산한다.")
    void shouldDecreaseWhenNothingToAsk() {
        Order order = new Order("[오렌지주스-2]", store);
        store.calculateWhenNothingToAsk(order);
        Receipt receipt = store.getReceipt(true);

        Products products = store.getProducts(order.getName());
        Product promotion = products.getPromotionProduct();
        Product normal = products.getNormalProduct();

        assertThat(receipt.getTotalPurchaseCount()).isEqualTo(2);
        assertThat(promotion.getQuantity()).isEqualTo(7);
        assertThat(normal.getQuantity()).isEqualTo(2);

        int count = 0;
        for (GiftProduct giftProduct : receipt.getGiftProducts()) {
            count += giftProduct.getQuantity();
        }
        assertThat(count).isEqualTo(1);
        assertThat(receipt.getMembershipDiscount()).isEqualTo(0);
    }
}
