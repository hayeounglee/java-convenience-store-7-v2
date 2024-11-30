package store;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import store.model.Orders;
import store.model.Product;
import store.model.Products;
import store.model.Store;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrdersTest {
    private Store store;

    @BeforeEach
    void setUp() {
        store = new Store();
        List<Product> products = new ArrayList<>();
        products.add(new Product("밤티라미수,4500,5,반짝할인"));
        products.add(new Product("밤티라미수,4500,2,null"));
        store.addProduct("밤티라미수", new Products(products));
    }

    @DisplayName("중복되는 상품 이름을 입력하면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"[밤티라미수-1],[밤티라미수-1]"})
    void throwW(String input) {
        String[] inputs = input.split(",");
        Orders orders = new Orders();
        assertThatThrownBy(() -> orders.addProduct(inputs, store))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
