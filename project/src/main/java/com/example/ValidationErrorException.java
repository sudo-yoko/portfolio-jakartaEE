package com.example;

import java.util.ArrayList;
import java.util.List;

public class ValidationErrorException extends RuntimeException {
    static final long serialVersionUID = 1L;

    private final List<Detail> details;

    public ValidationErrorException() {
        this.details = new ArrayList<>();
    }

    public ValidationErrorException(String field, String message) {
        this();
        addDetail(field, message);
    }

    public ValidationErrorException addDetail(String field, String message) {
        details.add(new Detail(field, message));
        return this;
    }

    public List<Detail> getDetails() {
        return List.copyOf(details);
    }

    
    @Override
    public String getMessage() {
        return "バリデーションエラー: details=" + getDetails();
    }

    public static class Detail {
        private String field;
        private String message;

        public Detail(String field, String message) {
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
