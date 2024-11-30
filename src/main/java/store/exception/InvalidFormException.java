package store.exception;

public class InvalidFormException extends IllegalArgumentException {
    public InvalidFormException() {
        super("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
    }
}
