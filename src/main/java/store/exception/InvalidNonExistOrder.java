package store.exception;

public class InvalidNonExistOrder extends IllegalArgumentException {
    public InvalidNonExistOrder() {
        super("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.");
    }
}
