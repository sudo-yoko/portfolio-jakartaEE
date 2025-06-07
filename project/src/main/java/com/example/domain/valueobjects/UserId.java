package com.example.domain.valueobjects;

import com.example.ValidationErrorException;
import com.example.ValidationUtils;

public class UserId {
    private final String value;

    private UserId(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static UserId of(String value) {
        Validator.validate(value);
        return new UserId(value);
    }

    public static class Validator {
        private Validator() {
        }

        public static void validate(String value) {
            if (value == null || value.isBlank()) {
                throw new ValidationErrorException("userId", "ユーザーIDを入力してください。");
            }
            ValidationErrorException ex = new ValidationErrorException();
            if (!ValidationUtils.isNumeric(value)) {
                ex.addError("userId", "ユーザーIDは数字のみにしてください。");
            }
            if (value.length() > 10) {
                ex.addError("userId", "ユーザーIDは10文字までにしてください。");
            }
            if (ex.getErrors().size() > 0) {
                throw ex;
            }
        }
    }
}
