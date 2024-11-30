package store.util;

import store.exception.InvalidFormException;

public class Parser {
    public static int parseToInt(String input) {
        try {
            return Integer.parseInt(input);
        } catch (IllegalArgumentException e) {
            throw new InvalidFormException();
        }
    }
}
