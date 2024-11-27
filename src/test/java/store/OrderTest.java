package store;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import store.model.Order;
import store.model.Product;
import store.model.Store;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTest {
    private Store store;

    @BeforeEach
    void setUp() {
        store = new Store();
        List<Product> products = new ArrayList<>();
        products.add(new Product("밤티라미수,4500,5,반짝할인"));
        products.add(new Product("밤티라미수,4500,2,null"));
        store.addProduct("밤티라미수", products);
    }

    @DisplayName("올바르지 않은 상품 형식을 입력하면 예외가 발생한다.")
    @EmptySource
    @ParameterizedTest
    @ValueSource(strings = {"[", "]", "[-", "-]", "[-]", "[밤티라미수-]", "[-3]", "", " "})
    void throwWhenInvalidProductForm(String input) {
        assertThatThrownBy(() -> new Order(input, store))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("숫자가 아니거나 0 이하의 수량을 입력하면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"[밤티라미수-감]", "[밤티라미수-0]"})
    void throwWhenInvalidProductQuantity(String input) {
        assertThatThrownBy(() -> new Order(input, store))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("초과된 수량을 입력하면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"[밤티라미수-8]"})
    void throwWhenInvalidOrderStock(String input) {
        assertThatThrownBy(() -> new Order(input, store))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 상름 이름을 입력하면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"[포르쉐-8]"})
    void throwWhenInvalidProductName(String input) {
        assertThatThrownBy(() -> new Order(input, store))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
