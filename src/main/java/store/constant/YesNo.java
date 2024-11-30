package store.constant;

import store.exception.InvalidFormException;

public enum YesNo {
    YES("Y", true),
    NO("N", false);

    private final String message;
    private final boolean answer;

    YesNo(String message, Boolean answer) {
        this.message = message;
        this.answer = answer;
    }

    public static boolean getMatchingAnswer(String findAnswer) {
        for (YesNo yesNo : YesNo.values()) {
            if (yesNo.message.equals(findAnswer)) {
                return yesNo.answer;
            }
        }
        throw new InvalidFormException();
    }
}
