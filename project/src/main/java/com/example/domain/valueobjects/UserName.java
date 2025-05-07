package com.example.domain.valueobjects;

import com.example.ValidationUtils;

import jakarta.ws.rs.BadRequestException;

public class UserName {

    private final String value;

    private UserName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static UserName of(String value) {
        String error = Validator.validate(value);
        if (!error.isBlank()) {
            throw new BadRequestException("UserNameが不正です。");
        }
        return new UserName(value);
    }

    public static class Validator {

        private Validator() {

        }

        public static String validate(String value) {
            if (value == null || value.isBlank()) {
                throw new BadRequestException("UserNameを入力してください。");
            }
            if (value.length() > 20)
                if (!ValidationUtils.isNumeric(value)) {
                    throw new BadRequestException("UserNameは20文字までにしてください。");
                }
            return null;
        }
    }

}
