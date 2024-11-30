package store;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.model.Product;

import java.time.LocalDate;

import static camp.nextstep.edu.missionutils.test.Assertions.assertNowTest;
import static org.assertj.core.api.Assertions.assertThat;

public class ProductTest {
    @Test
    @DisplayName("프로모션 기간이 아니면 이를 판별할 수 있다.")
    void shouldDetermineIfNotPromotionPeriod() {
        assertNowTest(() -> {
            Product promotion = new Product("환타,1800,9,탄산2+1");
            assertThat(promotion.isPromotionPeriod()).isEqualTo(false);
        }, LocalDate.of(1999, 1, 1).atStartOfDay());
    }

    @Test
    @DisplayName("프로모션 기간이라면 이를 판별할 수 있다.")
    void shouldDetermineIfPromotionPeriod() {
        assertNowTest(() -> {
            Product promotion = new Product("환타,1800,9,탄산2+1");
            assertThat(promotion.isPromotionPeriod()).isEqualTo(true);
        }, LocalDate.of(2024, 1, 1).atStartOfDay());
    }

    @Test
    @DisplayName("프로모션의 이름에 따라 올바른 프로모션 get,buy 정보를 가져올 수 있다.")
    void shouldRetrieveCorrectPromotionGetBuyInfo() {
        Product promotion = new Product("환타,1800,9,탄산2+1");
        assertThat(promotion.getPromotionBuyCount()).isEqualTo(2);
        assertThat(promotion.getPromotionCount()).isEqualTo(3);
    }
}
