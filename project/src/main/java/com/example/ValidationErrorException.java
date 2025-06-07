package com.example;

import java.util.ArrayList;
import java.util.List;

public class ValidationErrorException extends RuntimeException {
    static final long serialVersionUID = 1L;

    private final List<Error> errors;

    public ValidationErrorException() {
        this.errors = new ArrayList<>();
    }

    public ValidationErrorException(String field, String message) {
        this();
        addError(field, message);
    }

    public ValidationErrorException addError(String field, String message) {
        errors.add(new Error(field, message));
        return this;
    }

    public List<Error> getErrors() {
        return List.copyOf(errors);
    }

    @Override
    public String getMessage() {
        return "バリデーションエラー: errors=" + getErrors();
    }

    public static class Error {
        private String field;
        private String message;

        public Error(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public String getField() {
            return field;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            sb.append("field=").append(field).append(", ");
            sb.append("message=").append(message);
            sb.append("}");
            return sb.toString();
        }
    }

}
