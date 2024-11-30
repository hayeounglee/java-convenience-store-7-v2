package store.exception;

public class InvalidQuantityException extends IllegalArgumentException{
    public InvalidQuantityException() {
        super("[ERROR] 수량은 1개이상 이상 입력해야 합니다.");
    }
}
