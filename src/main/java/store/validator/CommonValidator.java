package store.validator;

import store.exception.InvalidFormException;

public class CommonValidator {
    public void validateEmpty(String input) {
        if (input.isBlank() || input.isEmpty()) {
            throw new InvalidFormException();
        }
    }
}
