package com.example.domain.valueobjects;

import com.example.ValidationErrorException;

public class UserName {
    private final String value;

    private UserName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static UserName of(String value) {
        Validator.validate(value);
        return new UserName(value);
    }

    public static class Validator {
        private Validator() {
        }

        public static void validate(String value) {
            if (value == null || value.isBlank()) {
                throw new ValidationErrorException("userName", "ユーザー名を入力してください。");
            }
            ValidationErrorException ex = new ValidationErrorException();
            if (value.length() > 20) {
                ex.addError("userName", "ユーザー名は20文字までにしてください。");
            }
            if (ex.getErrors().size() > 0) {
                throw ex;
            }
        }
    }
}
