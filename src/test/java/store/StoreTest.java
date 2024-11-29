package store;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.constant.PromotionPeriodState;
import store.model.Order;
import store.model.Product;
import store.model.Products;
import store.model.Store;

import java.util.ArrayList;
import java.util.List;

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
        products.add(new Product("고구마,1800,2,null"));
        store.addProduct("고구마", new Products(products));
    }

    @Test
    @DisplayName("프로모션 기간이 아닌 주문인지 판별할 수 있다.")
    void shouldDetermineIfNotPromotionPeriod() {
        Order order = new Order("[고구마-1]", store);
        assertThat(store.executeWhenNotPromotionPeriod(order)).isEqualTo(true);
        assertThat(store.executeWhenPromotionPeriod(order)).isEqualTo(PromotionPeriodState.NOTHING);
        assertThat(store.checkGetOneFree(order)).isEqualTo(false);
        assertThat(store.checkBuyOriginalPrice(order)).isEqualTo(false);
    }

    @Test
    @DisplayName("1개 수량을 추가할 수 있는 주문인지 판별할 수 있다.")
    void shouldDetermineIfProductCanBeAdded() {
        Order order = new Order("[오렌지주스-1]", store);
        assertThat(store.executeWhenNotPromotionPeriod(order)).isEqualTo(false);
        assertThat(store.executeWhenPromotionPeriod(order)).isEqualTo(PromotionPeriodState.GET_ONE_FREE);
        assertThat(store.checkGetOneFree(order)).isEqualTo(true);
        assertThat(store.checkBuyOriginalPrice(order)).isEqualTo(false);
    }

    @Test
    @DisplayName("정가로 결제해야 하는 수량이 있는 주문인지 판별할 수 있다.")
    void shouldDetermineIfOrderHasOriginalPrice() {
        Order order = new Order("[오렌지주스-9]", store);
        assertThat(store.executeWhenNotPromotionPeriod(order)).isEqualTo(false);
        assertThat(store.executeWhenPromotionPeriod(order)).isEqualTo(PromotionPeriodState.BUY_ORIGINAL_PRICE);
        assertThat(store.checkGetOneFree(order)).isEqualTo(false);
        assertThat(store.checkBuyOriginalPrice(order)).isEqualTo(true);
    }

    @Test
    @DisplayName("추가로 물어볼 필요가 없는 주문인지 판별할 수 있다.")
    void shouldDetermineNothingToAskProducts() {
        Order order = new Order("[오렌지주스-2]", store);
        assertThat(store.executeWhenNotPromotionPeriod(order)).isEqualTo(false);
        assertThat(store.executeWhenPromotionPeriod(order)).isEqualTo(PromotionPeriodState.NOTHING);
        assertThat(store.checkGetOneFree(order)).isEqualTo(false);
        assertThat(store.checkBuyOriginalPrice(order)).isEqualTo(false);
    }
}
