package store.exception;

public class InvalidStockCount extends IllegalArgumentException {
    public InvalidStockCount(){
        super("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
    }
}
