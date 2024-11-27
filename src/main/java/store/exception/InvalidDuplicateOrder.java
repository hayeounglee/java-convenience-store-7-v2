package store.exception;

public class InvalidDuplicateOrder extends IllegalArgumentException{
    public InvalidDuplicateOrder(){
        super("[ERROR] 중복되는 상품을 입력했습니다. 다시 입력해주세요.");
    }
}
