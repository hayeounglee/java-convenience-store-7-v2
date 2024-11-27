package store.validator;

import store.exception.InvalidFormException;
import store.exception.InvalidQuantityException;
import store.util.Parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderFormValidator {
    public static final Pattern ORDER_PATTERN = Pattern.compile("\\[(.+)-(\\d+)]");
    public static final int PRODUCT_NAME_GROUP = 1;
    public static final int PRODUCT_QUANTITY_GROUP = 2;

    Matcher matcher;

    public void validate(String product) {
        matcher = ORDER_PATTERN.matcher(product);
        validatePattern();
        validateQuantity(matcher.group(PRODUCT_QUANTITY_GROUP));
    }


    public void validatePattern() {
        if (!matcher.matches()) {
            throw new InvalidFormException();
        }
    }

    public void validateQuantity(String input) {
        try {
            int quantity = Parser.parseToInt(input);
            if (quantity <= 0) {
                throw new InvalidQuantityException();
            }
        } catch (IllegalArgumentException e) {
            throw new InvalidFormException();
        }
    }
}
