package com.example.domain.valueobjects;

import com.example.ValidationUtils;
import jakarta.ws.rs.BadRequestException;

public class UserId {
    private final String value;

    private UserId(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static UserId of(String value) {
        String error = Validator.validate(value);
        if (!error.isBlank()) {
            throw new BadRequestException("UserIdが不正です。");
        }
        return new UserId(value);
    }

    public static class Validator {
        private Validator() {
        }

        public static String validate(String value) {
            if (value == null || value.isBlank()) {
                throw new BadRequestException("UserIdを入力してください。");
            }
            if (!ValidationUtils.isNumeric(value)) {
                throw new BadRequestException("UserIdは数字のみにしてください。");
            }
            return null;
        }
    }
}
